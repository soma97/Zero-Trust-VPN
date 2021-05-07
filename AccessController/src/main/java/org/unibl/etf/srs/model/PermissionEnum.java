package org.unibl.etf.srs.model;

public enum PermissionEnum {
    FTP(21), SSH(22), DB(3306), HTTP(80), HTTPS(443);

    private final int port;

    PermissionEnum(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }
}