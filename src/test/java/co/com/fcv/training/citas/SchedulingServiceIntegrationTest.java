package co.com.fcv.training.citas;

import co.com.fcv.training.citas.application.SchedulingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.*;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
class SchedulingServiceIntegrationTest {
    @Container static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");
    @DynamicPropertySource static void properties(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", MYSQL::getJdbcUrl); r.add("spring.datasource.username", MYSQL::getUsername); r.add("spring.datasource.password", MYSQL::getPassword);
        r.add("app.jwt.access-secret", () -> "a".repeat(40)); r.add("app.jwt.refresh-secret", () -> "b".repeat(40)); r.add("app.cookie.secure", () -> true); r.add("app.cookie.same-site", () -> "None");
    }
    @Autowired SchedulingService scheduling; @Autowired JdbcTemplate jdbc;
    @Test void retainsConsecutiveSlotsAndReleasesThemAfterAdministrativeRejection() {
        String suffix=UUID.randomUUID().toString();
        Long professionalUser=user("prof-"+suffix+"@example.test",suffix); Long professional=scheduling.createProfessional("Pro","Fes","CC","P"+suffix,"pro2-"+suffix+"@example.test","300", "hash", "PC"+suffix,"LIC"+suffix);
        // createProfessional creates a second user; use that owner for the professional block
        Long owner=jdbc.queryForObject("select user_id from professionals where id=?",Long.class,professional);
        Long specialty=scheduling.createSpecialty("ESP"+suffix,"Especialidad "+suffix,60,false).id(); Long location=jdbc.queryForObject("select id from locations where active=true limit 1",Long.class);
        scheduling.setProfessionalSpecialties(professional,List.of(specialty),specialty); scheduling.setProfessionalLocations(professional,List.of(location));
        LocalDate date=LocalDate.now().plusDays(2); scheduling.createBlock(owner,location,date,LocalTime.of(8,0),LocalTime.of(10,0));
        assertThat(scheduling.availability(location,specialty,professional,date)).anyMatch(a -> a.slots().stream().anyMatch(slot -> slot.startAt().equals(LocalDateTime.of(date,LocalTime.of(8,0))) && slot.endAt().equals(LocalDateTime.of(date,LocalTime.of(9,0)))));
        Long patient=user("patient-"+suffix+"@example.test","U"+suffix); Long admin=user("admin-"+suffix+"@example.test","A"+suffix);
        SchedulingService.Appointment appointment=scheduling.reserve(patient,professional,location,specialty,LocalDateTime.of(date,LocalTime.of(8,0)),"Prueba");
        assertThat(appointment.status()).isEqualTo("REQUESTED");
        assertThatThrownBy(() -> scheduling.reserve(patient,professional,location,specialty,LocalDateTime.of(date,LocalTime.of(8,0)),"Duplicada")).hasMessageContaining("Franja");
        assertThat(scheduling.decide(admin,appointment.id(),"REJECT","Sin disponibilidad clínica").status()).isEqualTo("REJECTED");
        assertThat(scheduling.availability(location,specialty,professional,date)).anyMatch(a -> a.slots().stream().anyMatch(slot -> slot.startAt().equals(LocalDateTime.of(date,LocalTime.of(8,0)))));
    }
    @Test void cancelsOwnedFutureAppointmentAndReleasesSlotsWithoutReactivation() {
        String suffix=UUID.randomUUID().toString();
        Long professional=scheduling.createProfessional("Cancel","Professional","CC","P"+suffix,"cancel-pro-"+suffix+"@example.test","300","hash","PC"+suffix,"LIC"+suffix);
        Long owner=jdbc.queryForObject("select user_id from professionals where id=?",Long.class,professional);
        Long specialty=jdbc.queryForObject("select id from specialties where code='MEDICINA_GENERAL'",Long.class); Long location=jdbc.queryForObject("select id from locations where active=true limit 1",Long.class);
        scheduling.setProfessionalSpecialties(professional,List.of(specialty),specialty); scheduling.setProfessionalLocations(professional,List.of(location));
        LocalDate date=LocalDate.now().plusDays(3); scheduling.createBlock(owner,location,date,LocalTime.of(11,0),LocalTime.of(11,30));
        Long patient=user("cancel-patient-"+suffix+"@example.test","C"+suffix);
        SchedulingService.Appointment appointment=scheduling.reserve(patient,professional,location,specialty,LocalDateTime.of(date,LocalTime.of(11,0)),"Cancel test");
        assertThat(scheduling.cancel(patient,appointment.id()).status()).isEqualTo("CANCELLED");
        assertThat(jdbc.queryForObject("select count(*) from professional_slots where appointment_id=?",Integer.class,appointment.id())).isZero();
        assertThat(jdbc.queryForObject("select count(*) from appointment_status_history h join appointment_statuses s on s.id=h.status_id where h.appointment_id=? and s.code='CANCELLED' and h.change_source='USER'",Integer.class,appointment.id())).isEqualTo(1);
        assertThatThrownBy(() -> scheduling.cancel(patient,appointment.id())).hasMessageContaining("no se puede cancelar");
    }
    private Long user(String email,String document) { jdbc.update("insert into users(first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) values ('Test','User','CC',?,?,?,'hash',true,false)",document,email,"300"); return jdbc.queryForObject("select id from users where email=?",Long.class,email); }
}
