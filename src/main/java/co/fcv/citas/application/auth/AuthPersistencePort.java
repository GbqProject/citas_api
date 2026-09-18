package co.fcv.citas.application.auth;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AuthPersistencePort {
    Optional<Account> findAccountByEmail(String email);
    boolean emailExists(String email);
    boolean documentExists(String documentType, String documentNumber);
    long createUser(NewUser user);
    void assignRole(long userId, String role);
    void saveRefreshToken(long userId, String hash, Instant expiresAt);
    Optional<RefreshSession> findActiveRefreshSession(String hash, Instant now);
    void revokeRefreshSession(long id, Instant revokedAt);

    record Account(long id, String email, String passwordHash, boolean active, List<String> roles) { }
    record NewUser(String firstName, String lastName, String documentType, String documentNumber, String email, String phone, String passwordHash) { }
    record RefreshSession(long id, long userId, List<String> roles) { }
}
