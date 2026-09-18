package co.fcv.citas.infrastructure.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAccessTokenAdapterTest {
    @Test void issuesSignedJwtAccessToken() {
        JwtAccessTokenAdapter adapter = new JwtAccessTokenAdapter("01234567890123456789012345678901", 15);
        String token = adapter.issue(7L, List.of("USER"));
        assertThat(token.split("\\.")).hasSize(3);
    }
}
