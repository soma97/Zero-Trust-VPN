package org.unibl.etf.srs.cmanager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CertStatus {

    private int version;
    private String signatureAlgorithm;
    private Long serial;
    private String issuer;
    private String subject;
    private Date from;
    private Date to;
    private String publicKey;

    private boolean isValid;
    private String reason;

    public CertStatus(int version, String signatureAlgorithm, Long serial, String issuer, String subject, Date from, Date to, String publicKey, boolean isValid, String reason) {
        this.version = version;
        this.signatureAlgorithm = signatureAlgorithm;
        this.serial = serial;
        this.issuer = issuer;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.publicKey = publicKey;
        this.isValid = isValid;
        this.reason = reason;
    }
}
