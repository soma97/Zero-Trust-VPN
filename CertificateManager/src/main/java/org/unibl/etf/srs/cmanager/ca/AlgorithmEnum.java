package org.unibl.etf.srs.cmanager.ca;

public enum AlgorithmEnum {

    SHA256WithRSA("SHA256WithRSA"), AES256CBC("AES-256-CBC"), RSA("RSA");

    private String name;

    AlgorithmEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
