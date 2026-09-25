package co.com.fcv.training.citas.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.*;
import java.util.*;

@Service
public class SchedulingService {
    public record Specialty(Long id,String code,String name,int durationMinutes,boolean general,boolean active) {}
    public record Block(Long id,Long locationId,LocalDate date,LocalTime startTime,LocalTime endTime) {}
    public record Slot(LocalDateTime startAt,LocalDateTime endAt) {}
    public record Available(Long professionalId,String professionalName,List<Slot> slots) {}
    public record Appointment(Long id,String status,LocalDateTime startAt,LocalDateTime endAt) {}
    public record UserAppointment(Long id,String status,LocalDateTime startAt,LocalDateTime endAt,int durationMinutes,
                                  String professionalName,String specialtyName,String locationName,String rejectionReason) {}
    public record RescheduleRequest(Long id,Long appointmentId,String status,LocalDateTime newStartAt,LocalDateTime newEndAt) {}
    private final JdbcTemplate jdbc; private final Clock clock;
    public SchedulingService(JdbcTemplate jdbc, Clock clock) { this.jdbc=jdbc; this.clock=clock; }

    public List<Map<String,Object>> catalog(String name) {
        String sql = switch(name) {
            case "locations" -> "select id,code,name,address,city,department from locations where active=true order by name";
            case "appointment-statuses" -> "select id,code,name,is_terminal as terminal from appointment_statuses order by id";
            case "roles" -> "select id,code,name,description from roles order by id";
            case "regimes" -> "select id,code,name from insurance_regimes order by name";
            case "plans" -> "select p.id,p.code,p.name,p.eps_id as epsId,p.regime_id as regimeId from eps_plans p where p.active=true order by p.name";
            default -> throw new IllegalArgumentException("Catálogo no soportado");
        }; return jdbc.queryForList(sql);
    }
    public List<Specialty> specialties(boolean activeOnly) { return jdbc.query("select id,code,name,appointment_duration_minutes,is_general,active from specialties " + (activeOnly?"where active=true ":"") + "order by name", (r,n)->new Specialty(r.getLong(1),r.getString(2),r.getString(3),r.getInt(4),r.getBoolean(5),r.getBoolean(6))); }
    public List<Map<String,Object>> professionals() { return jdbc.queryForList("select p.id,p.professional_code as professionalCode,p.license_number as licenseNumber,p.active,concat(u.first_name,' ',u.last_name) as name,u.email from professionals p join users u on u.id=p.user_id order by u.first_name,u.last_name"); }
    @Transactional public Specialty createSpecialty(String code,String name,int duration,boolean general) { validateDuration(duration); Long id=insert("insert into specialties(code,name,appointment_duration_minutes,is_general,requires_admin_approval,active) values (?,?,?,?,?,true)",code,name,duration,general,!general); return specialty(id); }
    @Transactional public Specialty updateSpecialty(Long id,String name,Integer duration,Boolean active) { Specialty old=specialty(id); int d=duration==null?old.durationMinutes():duration; validateDuration(d); jdbc.update("update specialties set name=?,appointment_duration_minutes=?,active=? where id=?", name==null?old.name():name,d,active==null?old.active():active,id); return specialty(id); }
    public Long createProfessional(String first,String last,String docType,String doc,String email,String phone,String passwordHash,String code,String license) {
        Long user=insert("insert into users(first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) values (?,?,?,?,?,?,?,true,false)",first,last,docType,doc,email,phone,passwordHash);
        jdbc.update("insert into user_roles(user_id,role_id) select ?,id from roles where code='PROFESSIONAL'",user);
        return insert("insert into professionals(user_id,professional_code,license_number,active) values (?,?,?,true)",user,code,license);
    }
    @Transactional public void setProfessionalSpecialties(Long professionalId,List<Long> ids,Long primary) { requireProfessional(professionalId); if(ids==null||ids.isEmpty()||primary==null||!ids.contains(primary)) throw new IllegalArgumentException("Asignación primaria requerida"); for(Long id:ids) { if(count("select count(*) from specialties where id=?",id)==0) throw notFound("Especialidad"); } jdbc.update("delete from professional_specialties where professional_id=?",professionalId); for(Long id:ids) jdbc.update("insert into professional_specialties(professional_id,specialty_id,is_primary,active) values (?,?,?,true)",professionalId,id,id.equals(primary)); }
    @Transactional public void setProfessionalLocations(Long professionalId,List<Long> ids) { requireProfessional(professionalId); if(ids==null||ids.isEmpty()||ids.size()>2) throw new IllegalArgumentException("Debe asignar una o dos sedes"); for(Long id:ids) if(count("select count(*) from locations where id=? and active=true",id)==0) throw notFound("Sede"); jdbc.update("delete from professional_locations where professional_id=?",professionalId); for(Long id:ids) jdbc.update("insert into professional_locations(professional_id,location_id,active) values (?,?,true)",professionalId,id); }
    public void setProfessionalActive(Long id,boolean active) { if(jdbc.update("update professionals set active=? where id=?",active,id)==0) throw notFound("Profesional"); }
    @Transactional public Block createBlock(Long userId,Long locationId,LocalDate date,LocalTime start,LocalTime end) { Long professional=professionalForUser(userId); validateBlock(professional,locationId,date,start,end,null); Long id=insert("insert into availability_blocks(professional_id,location_id,available_date,start_time,end_time,active) values (?,?,?,?,?,true)",professional,locationId,date,start,end); createSlots(id,date,start,end); return new Block(id,locationId,date,start,end); }
    public List<Block> blocks(Long userId,LocalDate date,Long location) { Long professional=professionalForUser(userId); String sql="select id,location_id,available_date,start_time,end_time from availability_blocks where professional_id=? and active=true"; List<Object> ps=new ArrayList<>(List.of(professional)); if(date!=null){sql+=" and available_date=?";ps.add(date);}if(location!=null){sql+=" and location_id=?";ps.add(location);}sql+=" order by available_date,start_time";return jdbc.query(sql,(r,n)->new Block(r.getLong(1),r.getLong(2),r.getObject(3,LocalDate.class),r.getObject(4,LocalTime.class),r.getObject(5,LocalTime.class)),ps.toArray()); }
    @Transactional public Block updateBlock(Long userId,Long id,Long location,LocalDate date,LocalTime start,LocalTime end) { Long professional=professionalForUser(userId); Block old=jdbc.query("select id,location_id,available_date,start_time,end_time from availability_blocks where id=? and professional_id=? and active=true for update",(r,n)->new Block(r.getLong(1),r.getLong(2),r.getObject(3,LocalDate.class),r.getObject(4,LocalTime.class),r.getObject(5,LocalTime.class)),id,professional).stream().findFirst().orElseThrow(()->notFound("Bloque")); if(count("select count(*) from professional_slots where availability_block_id=? and appointment_id is not null",id)>0) throw conflict("El bloque tiene citas comprometidas"); Long lid=location==null?old.locationId():location; LocalDate d=date==null?old.date():date; LocalTime s=start==null?old.startTime():start,e=end==null?old.endTime():end; validateBlock(professional,lid,d,s,e,id); jdbc.update("update availability_blocks set location_id=?,available_date=?,start_time=?,end_time=? where id=?",lid,d,s,e,id);jdbc.update("delete from professional_slots where availability_block_id=?",id);createSlots(id,d,s,e);return new Block(id,lid,d,s,e); }
    @Transactional public void deleteBlock(Long userId,Long id) { Long professional=professionalForUser(userId); if(count("select count(*) from availability_blocks where id=? and professional_id=? and active=true",id,professional)==0) throw notFound("Bloque");if(count("select count(*) from professional_slots where availability_block_id=? and appointment_id is not null",id)>0)throw conflict("El bloque tiene citas comprometidas");jdbc.update("delete from professional_slots where availability_block_id=?",id);jdbc.update("update availability_blocks set active=false where id=?",id); }
    public List<Available> availability(Long location,Long specialty,Long professional,LocalDate date) {
        Specialty selected=specialty(specialty);
        String sql="select ps.start_at,ps.end_at,p.id,u.first_name,u.last_name from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id join professionals p on p.id=b.professional_id join users u on u.id=p.user_id join professional_specialties x on x.professional_id=p.id and x.specialty_id=? and x.active=true join professional_locations l on l.professional_id=p.id and l.location_id=b.location_id and l.active=true where b.active=true and p.active=true and ps.appointment_id is null and ps.reschedule_request_id is null and b.location_id=? and b.available_date=?"+(professional==null?"":" and p.id=?")+" order by p.id,ps.start_at";
        List<Object> args=new ArrayList<>(List.of(specialty,location,date)); if(professional!=null) args.add(professional);
        Map<Long,String> names=new LinkedHashMap<>(); Map<Long,List<Slot>> grouped=new LinkedHashMap<>();
        jdbc.query(sql,(r,n)->{ Long id=r.getLong(3); names.putIfAbsent(id,r.getString(4)+" "+r.getString(5)); grouped.computeIfAbsent(id,k->new ArrayList<>()).add(new Slot(r.getObject(1,LocalDateTime.class),r.getObject(2,LocalDateTime.class))); return null; },args.toArray());
        return grouped.entrySet().stream().map(entry -> {
            List<Slot> slots=entry.getValue();
            if(selected.durationMinutes()==60){ List<Slot> consecutive=new ArrayList<>(); for(int i=0;i+1<slots.size();i++) if(slots.get(i).endAt().equals(slots.get(i+1).startAt())) consecutive.add(new Slot(slots.get(i).startAt(),slots.get(i+1).endAt())); slots=consecutive; }
            return new Available(entry.getKey(),names.get(entry.getKey()),slots);
        }).filter(result -> !result.slots().isEmpty()).toList();
    }
    @Transactional public Appointment reserve(Long user,Long professional,Long location,Long specialtyId,LocalDateTime start,String reason) { Specialty specialty=specialty(specialtyId); int minutes=specialty.durationMinutes(); LocalDateTime end=start.plusMinutes(minutes); if(start.isBefore(LocalDateTime.now(clock)))throw new IllegalArgumentException("La cita debe ser futura"); if(count("select count(*) from professionals p join professional_specialties x on x.professional_id=p.id and x.specialty_id=? and x.active=true join professional_locations l on l.professional_id=p.id and l.location_id=? and l.active=true where p.id=? and p.active=true and ?=(select 1)",specialtyId,location,professional,1)==0)throw conflict("Profesional no disponible para la selección"); List<Long> slots=jdbc.queryForList("select ps.id from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id where b.professional_id=? and b.location_id=? and b.active=true and ps.start_at>=? and ps.end_at<=? order by ps.start_at",Long.class,professional,location,start,end); if(slots.size()!=minutes/30)throw conflict("Franja no disponible"); List<LocalDateTime> times=jdbc.query("select ps.start_at from professional_slots ps where ps.id in ("+String.join(",",Collections.nCopies(slots.size(),"?"))+") order by ps.start_at",(r,n)->r.getObject(1,LocalDateTime.class),slots.toArray());for(int i=1;i<times.size();i++)if(!times.get(i-1).plusMinutes(30).equals(times.get(i)))throw conflict("Slots no consecutivos"); String status=specialty.general()?"APPROVED":"REQUESTED"; Long affiliation=jdbc.query("select id from user_insurance_affiliations where user_id=? and is_current=true order by id desc limit 1",(r,n)->r.getLong(1),user).stream().findFirst().orElse(null);Long statusId=statusId(status);Long appointment=insert("insert into appointments(patient_user_id,professional_id,location_id,specialty_id,insurance_affiliation_id,status_id,reason,scheduled_start_at,scheduled_end_at,created_by_user_id,approved_at) values (?,?,?,?,?,?,?,?,?,?,?)",user,professional,location,specialtyId,affiliation,statusId,reason,start,end,user,specialty.general()?LocalDateTime.now(clock):null); int claimed=jdbc.update("update professional_slots set appointment_id=? where appointment_id is null and id in ("+String.join(",",Collections.nCopies(slots.size(),"?"))+")",concat(appointment,slots)); if(claimed!=slots.size())throw conflict("Franja ya reservada"); jdbc.update("insert into appointment_status_history(appointment_id,status_id,changed_by_user_id,change_source,reason) values (?,?,?,?,?)",appointment,statusId,specialty.general()?null:user,specialty.general()?"SYSTEM":"USER",specialty.general()?"Aprobación automática":reason);return new Appointment(appointment,status,start,end); }
    public List<Map<String,Object>> pending() { return jdbc.queryForList("select a.id,st.code as status,a.scheduled_start_at as startAt,a.scheduled_end_at as endAt,concat(pu.first_name,' ',pu.last_name) as patientName,concat(pro.first_name,' ',pro.last_name) as professionalName,s.name as specialtyName,l.name as locationName,s.appointment_duration_minutes as durationMinutes from appointments a join appointment_statuses st on st.id=a.status_id join users pu on pu.id=a.patient_user_id join professionals pr on pr.id=a.professional_id join users pro on pro.id=pr.user_id join specialties s on s.id=a.specialty_id join locations l on l.id=a.location_id where st.code='REQUESTED' and s.is_general=false order by a.scheduled_start_at"); }
    public List<UserAppointment> myAppointments(Long user, String status, LocalDate from, LocalDate to) {
        if (status != null && !Set.of("APPROVED", "REQUESTED", "REJECTED", "CANCELLED", "COMPLETED", "NO_SHOW").contains(status))
            throw new IllegalArgumentException("Estado de cita inválido");
        String sql = "select a.id,st.code,a.scheduled_start_at,a.scheduled_end_at,s.appointment_duration_minutes, " +
                "concat(pro.first_name,' ',pro.last_name),s.name,l.name, " +
                "case when st.code='REJECTED' then h.reason else null end " +
                "from appointments a join appointment_statuses st on st.id=a.status_id " +
                "join professionals p on p.id=a.professional_id join users pro on pro.id=p.user_id " +
                "join specialties s on s.id=a.specialty_id join locations l on l.id=a.location_id " +
                "left join appointment_status_history h on h.id=(select max(h2.id) from appointment_status_history h2 where h2.appointment_id=a.id and h2.status_id=a.status_id) " +
                "where a.patient_user_id=?";
        List<Object> args = new ArrayList<>(List.of(user));
        if (status != null) { sql += " and st.code=?"; args.add(status); }
        if (from != null) { sql += " and a.scheduled_start_at >= ?"; args.add(from.atStartOfDay()); }
        if (to != null) { sql += " and a.scheduled_start_at < ?"; args.add(to.plusDays(1).atStartOfDay()); }
        sql += " order by a.scheduled_start_at desc";
        return jdbc.query(sql, (r,n) -> new UserAppointment(r.getLong(1), r.getString(2), r.getObject(3,LocalDateTime.class),
                r.getObject(4,LocalDateTime.class), r.getInt(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)), args.toArray());
    }
    @Transactional public Appointment cancel(Long user, Long appointment) {
        record Current(LocalDateTime start, LocalDateTime end, boolean terminal) {}
        Current current = jdbc.query("select a.scheduled_start_at,a.scheduled_end_at,st.is_terminal from appointments a join appointment_statuses st on st.id=a.status_id where a.id=? and a.patient_user_id=? for update",
                (r,n) -> new Current(r.getObject(1,LocalDateTime.class), r.getObject(2,LocalDateTime.class), r.getBoolean(3)), appointment, user)
                .stream().findFirst().orElseThrow(() -> notFound("Cita"));
        if (current.terminal() || !current.start().isAfter(LocalDateTime.now(clock)))
            throw conflict("La cita no se puede cancelar");
        Long statusId = statusId("CANCELLED");
        jdbc.update("update appointments set status_id=? where id=?", statusId, appointment);
        jdbc.update("update professional_slots set appointment_id=null where appointment_id=?", appointment);
        jdbc.update("insert into appointment_status_history(appointment_id,status_id,changed_by_user_id,change_source,reason) values (?,?,?,?,?)",
                appointment, statusId, user, "USER", "Cancelación solicitada por el usuario");
        return new Appointment(appointment, "CANCELLED", current.start(), current.end());
    }
    @Transactional public RescheduleRequest requestReschedule(Long user, Long appointment, LocalDateTime newStart, String reason) {
        Map<String,Object> current = jdbc.queryForMap("select a.professional_id,a.location_id,a.specialty_id,a.scheduled_start_at,st.code,sp.appointment_duration_minutes from appointments a join appointment_statuses st on st.id=a.status_id join specialties sp on sp.id=a.specialty_id where a.id=? and a.patient_user_id=? for update", appointment, user);
        if (!"APPROVED".equals(current.get("code")) || !((LocalDateTime) current.get("scheduled_start_at")).isAfter(LocalDateTime.now(clock))) throw conflict("La cita no puede reprogramarse");
        if (count("select count(*) from appointment_reschedule_requests r join rescheduling_statuses rs on rs.id=r.status_id where r.appointment_id=? and rs.code='PENDING'", appointment) > 0) throw conflict("La cita ya tiene una reprogramación pendiente");
        int minutes = ((Number) current.get("appointment_duration_minutes")).intValue();
        LocalDateTime newEnd = newStart.plusMinutes(minutes);
        if (!newStart.isAfter(LocalDateTime.now(clock))) throw conflict("La nueva franja debe ser futura");
        Long professional = ((Number) current.get("professional_id")).longValue();
        Long location = ((Number) current.get("location_id")).longValue();
        Long specialty = ((Number) current.get("specialty_id")).longValue();
        List<Long> slots = jdbc.queryForList("select ps.id from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id join professional_specialties x on x.professional_id=b.professional_id and x.specialty_id=? and x.active=true where b.professional_id=? and b.location_id=? and b.active=true and ps.appointment_id is null and ps.reschedule_request_id is null and ps.start_at>=? and ps.end_at<=? order by ps.start_at", Long.class, specialty, professional, location, newStart, newEnd);
        if (slots.size() != minutes / 30) throw conflict("Nueva franja no disponible");
        List<LocalDateTime> times = jdbc.query("select ps.start_at from professional_slots ps where ps.id in (" + String.join(",", Collections.nCopies(slots.size(), "?")) + ") order by ps.start_at", (r,n) -> r.getObject(1, LocalDateTime.class), slots.toArray());
        for (int i=1;i<times.size();i++) if (!times.get(i-1).plusMinutes(30).equals(times.get(i))) throw conflict("Slots no consecutivos");
        Long request = insert("insert into appointment_reschedule_requests(appointment_id,requested_by_user_id,status_id,new_scheduled_start_at,new_scheduled_end_at,reason) values (?,?,?,?,?,?)", appointment, user, statusId("PENDING", true), newStart, newEnd, reason);
        int claimed = jdbc.update("update professional_slots set reschedule_request_id=? where reschedule_request_id is null and appointment_id is null and id in (" + String.join(",", Collections.nCopies(slots.size(), "?")) + ")", concat(request, slots));
        if (claimed != slots.size()) throw conflict("Nueva franja ya reservada");
        return new RescheduleRequest(request, appointment, "PENDING", newStart, newEnd);
    }
    @Transactional public Appointment decide(Long admin,Long appointment,String decision,String reason) { if(!Set.of("APPROVE","REJECT").contains(decision))throw new IllegalArgumentException("Decisión inválida"); if("REJECT".equals(decision)&&(reason==null||reason.isBlank()))throw new IllegalArgumentException("Motivo de rechazo obligatorio"); Map<String,Object> a=jdbc.queryForMap("select a.id,a.scheduled_start_at,a.scheduled_end_at from appointments a join appointment_statuses s on s.id=a.status_id join specialties sp on sp.id=a.specialty_id where a.id=? and s.code='REQUESTED' and sp.is_general=false for update",appointment);String status="APPROVE".equals(decision)?"APPROVED":"REJECTED";Long sid=statusId(status);jdbc.update("update appointments set status_id=?,approved_by_user_id=?,approved_at=? where id=?",sid,admin,LocalDateTime.now(clock),appointment);if("REJECTED".equals(status))jdbc.update("update professional_slots set appointment_id=null where appointment_id=?",appointment);jdbc.update("insert into appointment_status_history(appointment_id,status_id,changed_by_user_id,change_source,reason) values (?,?,?,?,?)",appointment,sid,admin,"ADMIN",reason);return new Appointment(appointment,status,(LocalDateTime)a.get("scheduled_start_at"),(LocalDateTime)a.get("scheduled_end_at")); }
    private void validateBlock(Long professional,Long location,LocalDate date,LocalTime start,LocalTime end,Long except) { if(!date.isAfter(LocalDate.now(clock))||!end.isAfter(start)||start.getMinute()%30!=0||end.getMinute()%30!=0)throw new IllegalArgumentException("Bloque futuro y alineado a 30 minutos requerido");if(count("select count(*) from professionals p join professional_locations l on l.professional_id=p.id and l.location_id=? and l.active=true where p.id=? and p.active=true",location,professional)==0)throw conflict("Profesional no habilitado en sede");String q="select count(*) from availability_blocks where professional_id=? and active=true and available_date=? and start_time < ? and end_time > ?"+(except==null?"":" and id<>?");List<Object> x=new ArrayList<>(List.of(professional,date,end,start));if(except!=null)x.add(except);if(count(q,x.toArray())>0)throw conflict("Bloque solapado"); }
    private void createSlots(Long block,LocalDate date,LocalTime start,LocalTime end){for(LocalTime t=start;t.isBefore(end);t=t.plusMinutes(30))jdbc.update("insert into professional_slots(availability_block_id,start_at,end_at) values (?,?,?)",block,LocalDateTime.of(date,t),LocalDateTime.of(date,t.plusMinutes(30)));}
    private Long professionalForUser(Long user){return jdbc.query("select id from professionals where user_id=? and active=true",(r,n)->r.getLong(1),user).stream().findFirst().orElseThrow(()->notFound("Profesional"));}
    private void requireProfessional(Long id){if(count("select count(*) from professionals where id=?",id)==0)throw notFound("Profesional");}
    private Specialty specialty(Long id){return jdbc.query("select id,code,name,appointment_duration_minutes,is_general,active from specialties where id=?",(r,n)->new Specialty(r.getLong(1),r.getString(2),r.getString(3),r.getInt(4),r.getBoolean(5),r.getBoolean(6)),id).stream().findFirst().filter(Specialty::active).orElseThrow(()->notFound("Especialidad activa"));}
    private Long statusId(String code){return statusId(code,false);}
    private Long statusId(String code, boolean reschedule){return jdbc.query("select id from " + (reschedule ? "rescheduling_statuses" : "appointment_statuses") + " where code=?",(r,n)->r.getLong(1),code).stream().findFirst().orElseThrow(()->new IllegalStateException("Estado faltante"));}
    private Long insert(String sql,Object... args){KeyHolder keys=new GeneratedKeyHolder();jdbc.update(c->{PreparedStatement p=c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);return p;},keys);return Objects.requireNonNull(keys.getKey()).longValue();}
    private int count(String sql,Object... args){Integer i=jdbc.queryForObject(sql,Integer.class,args);return i==null?0:i;}
    private void validateDuration(int d){if(d!=30&&d!=60)throw new IllegalArgumentException("Duración debe ser 30 o 60 minutos");}
    private SchedulingFailure notFound(String s){return new SchedulingFailure(SchedulingFailure.Kind.NOT_FOUND,s+" no encontrado");} private SchedulingFailure conflict(String s){return new SchedulingFailure(SchedulingFailure.Kind.CONFLICT,s);} private Object[] concat(Object first,List<Long> rest){Object[] r=new Object[rest.size()+1];r[0]=first;for(int i=0;i<rest.size();i++)r[i+1]=rest.get(i);return r;}
}
