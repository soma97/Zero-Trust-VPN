package org.unibl.etf.srs.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Entity(name = "permission")
public class Permission {
    @Id
    private String name;

    public Permission(String name) {
        this.name = name;
    }
}
