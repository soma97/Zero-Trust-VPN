package org.unibl.etf.srs.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private boolean authenticated;
    private String code;
    private List<Permission> permissions;

    public UserRequest(String email, boolean authenticated, String code, List<Permission> permissions) {
        super();
        this.email = email;
        this.authenticated = authenticated;
        this.code = code;
        this.permissions = permissions;
    }
}
