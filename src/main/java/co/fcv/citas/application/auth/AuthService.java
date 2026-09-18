package co.fcv.citas.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
public class AuthService {
    private static final String USER_ROLE = "USER";
    private final AuthPersistencePort persistence;
    private final AccessTokenPort accessTokens;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    private final long refreshDays;

    public AuthService(AuthPersistencePort persistence, AccessTokenPort accessTokens, PasswordEncoder passwordEncoder,
                       @org.springframework.beans.factory.annotation.Value("${app.security.jwt.refresh-days}") long refreshDays) {
        this.persistence = persistence; this.accessTokens = accessTokens; this.passwordEncoder = passwordEncoder; this.refreshDays = refreshDays;
    }

    @Transactional
    public void register(RegisterCommand command) {
        String email = command.email().trim().toLowerCase();
        if (persistence.emailExists(email)) throw new AuthException("EMAIL_ALREADY_REGISTERED", "Email already registered");
        if (persistence.documentExists(command.documentType().trim(), command.documentNumber().trim()))
            throw new AuthException("DOCUMENT_ALREADY_REGISTERED", "Document already registered");
        long userId = persistence.createUser(new AuthPersistencePort.NewUser(command.firstName().trim(), command.lastName().trim(),
                command.documentType().trim(), command.documentNumber().trim(), email, command.phone().trim(), passwordEncoder.encode(command.password())));
        persistence.assignRole(userId, USER_ROLE);
    }

    @Transactional
    public Tokens login(LoginCommand command) {
        AuthPersistencePort.Account account = persistence.findAccountByEmail(command.email().trim().toLowerCase())
                .filter(AuthPersistencePort.Account::active)
                .filter(a -> passwordEncoder.matches(command.password(), a.passwordHash()))
                .orElseThrow(() -> new AuthException("INVALID_CREDENTIALS", "Invalid credentials"));
        return issueSession(account.id(), account.roles());
    }

    @Transactional
    public Tokens refresh(String rawRefreshToken) {
        Instant now = Instant.now();
        AuthPersistencePort.RefreshSession session = persistence.findActiveRefreshSession(hash(rawRefreshToken), now)
                .orElseThrow(() -> new AuthException("INVALID_REFRESH_TOKEN", "Invalid refresh token"));
        persistence.revokeRefreshSession(session.id(), now);
        return issueSession(session.userId(), session.roles());
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        persistence.findActiveRefreshSession(hash(rawRefreshToken), Instant.now())
                .ifPresent(session -> persistence.revokeRefreshSession(session.id(), Instant.now()));
    }

    private Tokens issueSession(long userId, List<String> roles) {
        String refresh = newRefreshToken();
        persistence.saveRefreshToken(userId, hash(refresh), Instant.now().plus(refreshDays, ChronoUnit.DAYS));
        return new Tokens(accessTokens.issue(userId, roles), refresh);
    }
    private String newRefreshToken() { byte[] bytes = new byte[48]; secureRandom.nextBytes(bytes); return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); }
    private String hash(String value) {
        try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (java.security.NoSuchAlgorithmException exception) { throw new IllegalStateException(exception); }
    }
    public record RegisterCommand(String firstName, String lastName, String documentType, String documentNumber, String email, String phone, String password) { }
    public record LoginCommand(String email, String password) { }
    public record Tokens(String accessToken, String refreshToken) { }
    public static class AuthException extends RuntimeException { private final String code; public AuthException(String code, String message) { super(message); this.code = code; } public String code() { return code; } }
}
