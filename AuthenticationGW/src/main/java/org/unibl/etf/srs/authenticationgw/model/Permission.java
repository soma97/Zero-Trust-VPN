package org.unibl.etf.srs.authenticationgw.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class Permission {
    private String name;

    public Permission(String name) {
        this.name = name;
    }
}
