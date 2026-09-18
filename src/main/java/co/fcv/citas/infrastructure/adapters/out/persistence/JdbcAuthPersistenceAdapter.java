package co.fcv.citas.infrastructure.adapters.out.persistence;

import co.fcv.citas.application.auth.AuthPersistencePort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAuthPersistenceAdapter implements AuthPersistencePort {
    private final JdbcTemplate jdbc;
    public JdbcAuthPersistenceAdapter(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @Override public Optional<Account> findAccountByEmail(String email) {
        List<Account> accounts = jdbc.query("SELECT id,email,password_hash,is_active FROM users WHERE email=?", (rs, row) -> {
            long id = rs.getLong("id");
            List<String> roles = jdbc.queryForList("SELECT role_code FROM user_roles WHERE user_id=?", String.class, id);
            return new Account(id, rs.getString("email"), rs.getString("password_hash"), rs.getBoolean("is_active"), roles);
        }, email);
        return accounts.stream().findFirst();
    }
    @Override public boolean emailExists(String email) { return Boolean.TRUE.equals(jdbc.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE email=?)", Boolean.class, email)); }
    @Override public boolean documentExists(String type, String number) { return Boolean.TRUE.equals(jdbc.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE document_type=? AND document_number=?)", Boolean.class, type, number)); }
    @Override public long createUser(NewUser user) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> { PreparedStatement ps = connection.prepareStatement("INSERT INTO users(first_name,last_name,document_type,document_number,email,phone,password_hash) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.firstName()); ps.setString(2,user.lastName()); ps.setString(3,user.documentType()); ps.setString(4,user.documentNumber()); ps.setString(5,user.email()); ps.setString(6,user.phone()); ps.setString(7,user.passwordHash()); return ps; }, keys);
        return keys.getKey().longValue();
    }
    @Override public void assignRole(long userId, String role) { jdbc.update("INSERT INTO user_roles(user_id,role_code) VALUES(?,?)", userId, role); }
    @Override public void saveRefreshToken(long userId, String hash, Instant expiresAt) { jdbc.update("INSERT INTO refresh_tokens(user_id,token_hash,expires_at) VALUES(?,?,?)", userId, hash, Timestamp.from(expiresAt)); }
    @Override public Optional<RefreshSession> findActiveRefreshSession(String hash, Instant now) {
        List<RefreshSession> sessions = jdbc.query("SELECT rt.id,rt.user_id FROM refresh_tokens rt JOIN users u ON u.id=rt.user_id WHERE rt.token_hash=? AND rt.revoked_at IS NULL AND rt.expires_at>? AND u.is_active=TRUE", (rs,row) -> {
            long userId=rs.getLong("user_id"); return new RefreshSession(rs.getLong("id"),userId,jdbc.queryForList("SELECT role_code FROM user_roles WHERE user_id=?",String.class,userId)); }, hash, Timestamp.from(now));
        return sessions.stream().findFirst();
    }
    @Override public void revokeRefreshSession(long id, Instant revokedAt) { jdbc.update("UPDATE refresh_tokens SET revoked_at=? WHERE id=? AND revoked_at IS NULL", Timestamp.from(revokedAt), id); }
}
