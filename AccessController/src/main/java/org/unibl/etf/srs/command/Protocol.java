package org.unibl.etf.srs.command;

public enum Protocol {
    TCP("TCP"), UDP("UDP"), SCTP("SCTP"), DCCP("DCCP");

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    public String toString() {
        return this.protocol;
    }
}
