package org.unibl.etf.srs.consumer;

public class Constants {
    public static final String AUTH_GW_HOST = "auth.gw";
    public static final String ACCESS_GW_HOST = "access.gw";
    public static final String DESTINATION_SERVICES_HOST = "dest.srv";
    public static final String MY_IP = "my.ip";

    public static final int AUTH_GW_PORT = 8080;
    public static final int ACCESS_GW_PORT = 8081;
    public static final int CA_MANAGER_PORT = 8443;

    public static final String AUTH_GW_BASE_URL = "https://" + AUTH_GW_HOST + ":" + AUTH_GW_PORT + "/";
    public static final String ACCESS_GW_BASE_URL = "https://" + ACCESS_GW_HOST + ":" + ACCESS_GW_PORT + "/";

    public static final String AUTH_ALL_URL = "auth/all";
    public static final String AUTH_USER_URL = "auth/user";
    public static final String AUTH_AUTHENTICATED = "auth/authenticated";
    public static final String COMMANDS = "command";
    public static final String LOGS = "logs";
}
