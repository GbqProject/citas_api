package co.fcv.citas.application.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private final AuthPersistencePort persistence = mock(AuthPersistencePort.class);
    private final AccessTokenPort accessTokens = mock(AccessTokenPort.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthService service = new AuthService(persistence, accessTokens, encoder, 7);

    @Test void registerHashesPasswordAndAssignsUserRole() {
        when(persistence.createUser(any())).thenReturn(42L);
        service.register(new AuthService.RegisterCommand("Ana", "Pérez", "CC", "100", "ANA@EXAMPLE.COM", "3001", "Secret123"));
        ArgumentCaptor<AuthPersistencePort.NewUser> user = ArgumentCaptor.forClass(AuthPersistencePort.NewUser.class);
        verify(persistence).createUser(user.capture());
        assertThat(user.getValue().email()).isEqualTo("ana@example.com");
        assertThat(user.getValue().passwordHash()).isNotEqualTo("Secret123");
        assertThat(encoder.matches("Secret123", user.getValue().passwordHash())).isTrue();
        verify(persistence).assignRole(42L, "USER");
    }

    @Test void loginRejectsInvalidCredentials() {
        when(persistence.findAccountByEmail("ana@example.com")).thenReturn(Optional.of(new AuthPersistencePort.Account(1, "ana@example.com", encoder.encode("other"), true, List.of("USER"))));
        assertThatThrownBy(() -> service.login(new AuthService.LoginCommand("ana@example.com", "wrong")))
                .isInstanceOf(AuthService.AuthException.class).hasMessage("Invalid credentials");
    }

    @Test void refreshRevokesCurrentTokenAndIssuesNewSession() {
        when(persistence.findActiveRefreshSession(anyString(), any(Instant.class))).thenReturn(Optional.of(new AuthPersistencePort.RefreshSession(9, 1, List.of("USER"))));
        when(accessTokens.issue(1, List.of("USER"))).thenReturn("access-jwt");
        AuthService.Tokens tokens = service.refresh("valid-refresh-token");
        assertThat(tokens.accessToken()).isEqualTo("access-jwt");
        assertThat(tokens.refreshToken()).isNotBlank().isNotEqualTo("valid-refresh-token");
        verify(persistence).revokeRefreshSession(eq(9L), any(Instant.class));
        verify(persistence).saveRefreshToken(eq(1L), anyString(), any(Instant.class));
    }

    @Test void refreshRejectsUnknownToken() {
        when(persistence.findActiveRefreshSession(anyString(), any(Instant.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.refresh("unknown"))
                .isInstanceOf(AuthService.AuthException.class).hasMessage("Invalid refresh token");
    }
}
