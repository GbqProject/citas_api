package co.fcv.citas.infrastructure.security;

import co.fcv.citas.application.auth.AccessTokenPort;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JwtAccessTokenAdapter implements AccessTokenPort {
    private final JwtEncoder encoder;
    private final long accessMinutes;
    public JwtAccessTokenAdapter(@Value("${app.security.jwt.access-secret}") String secret,
                                 @Value("${app.security.jwt.access-minutes}") long accessMinutes) {
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(key));
        this.accessMinutes = accessMinutes;
    }
    @Override public String issue(long userId, List<String> roles) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("fcv-citas").subject(Long.toString(userId)).issuedAt(now)
                .expiresAt(now.plus(accessMinutes, ChronoUnit.MINUTES)).claim("roles", roles).claim("token_type", "access").build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }
}
