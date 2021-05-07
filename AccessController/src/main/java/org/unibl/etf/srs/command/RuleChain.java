package org.unibl.etf.srs.command;

public enum RuleChain {
    INPUT("INPUT"), FORWARD("FORWARD"), OUTPUT("OUTPUT"), PREROUTING("PREROUTING"), POSTROUTING("POSTROUTING");

    private String ruleChain;

    RuleChain(String ruleChain) {
        this.ruleChain = ruleChain;
    }

    public String toString() {
        return this.ruleChain;
    }
}
