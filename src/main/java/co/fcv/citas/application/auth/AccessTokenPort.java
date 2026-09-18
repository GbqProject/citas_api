package co.fcv.citas.application.auth;

import java.util.List;

public interface AccessTokenPort {
    String issue(long userId, List<String> roles);
}
