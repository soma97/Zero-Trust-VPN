package org.unibl.etf.srs.cmanager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CertificateRequest {
    @NotEmpty
    private String name;

    @Min(1)
    @Max(5)
    private int yearsValid;

    @Pattern(regexp = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$")
    private String domain;

    public CertificateRequest(String name, int yearsValid, String domain) {
        this.name = name;
        this.yearsValid = yearsValid;
        this.domain = domain;
    }
}
