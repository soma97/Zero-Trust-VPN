package org.unibl.etf.srs.command;

public enum Rule {
    ACCEPT("ACCEPT"), DROP("DROP"), REJECT("REJECT"), DNAT("DNAT"), SNAT("SNAT");

    private String rule;

    Rule(String rule) {
        this.rule = rule;
    }

    public String toString() {
        return this.rule;
    }
}
