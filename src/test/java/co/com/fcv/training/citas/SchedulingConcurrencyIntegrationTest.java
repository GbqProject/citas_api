package co.com.fcv.training.citas;

import co.com.fcv.training.citas.adapter.security.JwtTokens;
import co.com.fcv.training.citas.application.SchedulingService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SchedulingConcurrencyIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(SchedulingConcurrencyIntegrationTest.class);

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("app.jwt.access-secret", () -> "a".repeat(40));
        registry.add("app.jwt.refresh-secret", () -> "b".repeat(40));
        registry.add("app.cookie.secure", () -> true);
        registry.add("app.cookie.same-site", () -> "None");
    }

    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired JwtTokens tokens;
    @Autowired SchedulingService scheduling;

    @Test
    void concurrentGeneralReservationsProduceOneApprovedAndOneHttp409() throws Exception {
        String suffix = UUID.randomUUID().toString();
        Long professional = professional(suffix);
        Long general = jdbc.queryForObject("select id from specialties where code='MEDICINA_GENERAL'", Long.class);
        Long location = location();
        Long professionalUser = jdbc.queryForObject("select user_id from professionals where id=?", Long.class, professional);
        scheduling.setProfessionalSpecialties(professional, List.of(general), general);
        scheduling.setProfessionalLocations(professional, List.of(location));
        LocalDate date = LocalDate.now().plusDays(3);
        scheduling.createBlock(professionalUser, location, date, LocalTime.of(8, 0), LocalTime.of(8, 30));

        Long firstUser = user("first-" + suffix + "@example.test", "F" + suffix);
        Long secondUser = user("second-" + suffix + "@example.test", "S" + suffix);
        String payload = """
                {"professionalId":%d,"locationId":%d,"specialtyId":%d,"date":"%s","startTime":"08:00:00","reason":"Concurrent test"}
                """.formatted(professional, location, general, date);

        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Callable<Integer> attempt = () -> {
                start.await(10, TimeUnit.SECONDS);
                Long user = Thread.currentThread().getName().endsWith("1") ? firstUser : secondUser;
                int response = mvc.perform(post("/api/v1/appointments")
                                .with(jwt().jwt(jwt -> jwt.subject(user.toString()))
                                        .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON).content(payload))
                        .andReturn().getResponse().getStatus();
                log.info("RESERVATION_ATTEMPT user={} status={} slot={}", user, response,
                        LocalDateTime.of(date, LocalTime.of(8, 0)));
                return response;
            };
            Future<Integer> first = pool.submit(attempt);
            Future<Integer> second = pool.submit(attempt);
            start.countDown();
            List<Integer> responses = List.of(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS));

            assertThat(responses).containsExactlyInAnyOrder(201, 409);
            assertThat(jdbc.queryForObject("select count(*) from appointments where professional_id=? and scheduled_start_at=?", Integer.class,
                    professional, LocalDateTime.of(date, LocalTime.of(8, 0)))).isEqualTo(1);
            assertThat(jdbc.queryForObject("select count(*) from appointments a join appointment_statuses s on s.id=a.status_id where a.professional_id=? and s.code='APPROVED'", Integer.class, professional)).isEqualTo(1);
            assertThat(jdbc.queryForObject("select count(*) from appointment_status_history h join appointment_statuses s on s.id=h.status_id where h.appointment_id=(select min(id) from appointments where professional_id=?) and s.code='APPROVED' and h.change_source='SYSTEM'", Integer.class, professional)).isEqualTo(1);
            assertThat(jdbc.queryForObject("select count(*) from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id where b.professional_id=? and ps.start_at=? and ps.appointment_id is not null", Integer.class, professional, LocalDateTime.of(date, LocalTime.of(8, 0)))).isEqualTo(1);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void specializedReservationIsRequestedAndRetainsAllSlotsWithoutDuplicateAppointment() {
        String suffix = UUID.randomUUID().toString();
        Long professional = professional(suffix);
        Long specialty = scheduling.createSpecialty("ESP_" + suffix, "Especialidad " + suffix, 60, false).id();
        Long location = location();
        Long professionalUser = jdbc.queryForObject("select user_id from professionals where id=?", Long.class, professional);
        scheduling.setProfessionalSpecialties(professional, List.of(specialty), specialty);
        scheduling.setProfessionalLocations(professional, List.of(location));
        LocalDate date = LocalDate.now().plusDays(4);
        scheduling.createBlock(professionalUser, location, date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        Long patient = user("specialized-" + suffix + "@example.test", "E" + suffix);

        SchedulingService.Appointment result = scheduling.reserve(patient, professional, location, specialty,
                LocalDateTime.of(date, LocalTime.of(9, 0)), "Specialized retention test");

        assertThat(result.status()).isEqualTo("REQUESTED");
        assertThat(jdbc.queryForObject("select count(*) from appointments where id=?", Integer.class, result.id())).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from professional_slots where appointment_id=?", Integer.class, result.id())).isEqualTo(2);
        assertThat(jdbc.queryForObject("select count(*) from appointment_status_history h join appointment_statuses s on s.id=h.status_id where h.appointment_id=? and s.code='REQUESTED' and h.change_source='USER'", Integer.class, result.id())).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(distinct appointment_id) from professional_slots where availability_block_id in (select id from availability_blocks where professional_id=? and available_date=?) and appointment_id is not null", Integer.class, professional, date)).isEqualTo(1);
    }

    private Long professional(String suffix) {
        Long id = scheduling.createProfessional("Synthetic", "Professional", "CC", "P" + suffix,
                "professional-" + suffix + "@example.test", "3000000000", "hash", "PC" + suffix, "LIC" + suffix);
        return id;
    }

    private Long user(String email, String document) {
        jdbc.update("insert into users(first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) values ('Synthetic','Patient','CC',?,?,?,'hash',true,false)", document, email, "3000000000");
        return jdbc.queryForObject("select id from users where email=?", Long.class, email);
    }

    private Long location() {
        return jdbc.queryForObject("select id from locations where active=true order by id limit 1", Long.class);
    }
}
