package org.unibl.etf.srs.service;

import lombok.Getter;
import lombok.Setter;
import org.unibl.etf.srs.DependencyManager;
import org.unibl.etf.srs.command.Command;
import org.unibl.etf.srs.command.IpTablesCommandManager;
import org.unibl.etf.srs.command.UserAccessCommandHolder;
import org.unibl.etf.srs.consumer.Constants;
import org.unibl.etf.srs.consumer.Consumer;
import org.unibl.etf.srs.model.Permission;
import org.unibl.etf.srs.model.PermissionEnum;
import org.unibl.etf.srs.model.User;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class AccessService {

    private static AccessService instance;

    Consumer consumer = DependencyManager.getInstance().getConsumer();

    private final HashMap<String, UserAccessCommandHolder> userAccessCommandHolderMap = new HashMap<>();
    private final List<Integer> allowedPortsOnGW = Arrays.asList(Constants.AUTH_GW_PORT, Constants.ACCESS_GW_PORT, Constants.CA_MANAGER_PORT);


    private AccessService() {}

    public static AccessService getInstance() {
        if(instance == null) {
            instance = new AccessService();
        }
        return instance;
    }

    public void prepareUserAccessCommands(User user, String sourceIP) {
        user.setIp(sourceIP);
        try {
            UserAccessCommandHolder userAccess = IpTablesCommandManager.getInstance()
                    .allowIpSourceAndPortsCommands(user, mapPermissionToPermissionEnum(user.getPermissions()));

            userAccessCommandHolderMap.put(user.getEmail(), userAccess);
        } catch (IllegalArgumentException iae) {
            System.out.println("Invalid field name in table permission.");
            iae.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
    }

    public void grantUserAccess(String email) throws IOException {
        UserAccessCommandHolder userAccess = userAccessCommandHolderMap.get(email);
        IpTablesCommandManager.getInstance()
                .executeCommands(userAccess.getCommandsAC(), false);

        consumer.sendCommands(userAccess
                .getCommandsAccessGW()
                .stream()
                .map(Command::getOriginalCommand)
                .collect(Collectors.toList()));

        System.out.println("Access granted for user " + email + System.lineSeparator());
    }

    private void forbidUserAccess(UserAccessCommandHolder uac) throws IOException {
        IpTablesCommandManager.getInstance()
                .executeCommands(uac.getCommandsAC(), true);

        consumer.sendCommands(uac
                .getCommandsAccessGW()
                .stream()
                .map(Command::getInverseCommand)
                .collect(Collectors.toList()));

        System.out.println("Access forbidden for user " + uac.getUser().getEmail() + System.lineSeparator());
        userAccessCommandHolderMap.remove(uac.getUser().getEmail());
    }

    public void checkInputLogsAndForbid(String[] logs) throws IOException {
        String sourceIP = "", destPort = "";
        for(String log : logs) {
            String[] data = log.split(" ");

            for(String dataItem : data) {
                if(dataItem.contains("SRC"))
                    sourceIP = dataItem.split("=")[1].trim();

                if(dataItem.contains("DPT"))
                    destPort = dataItem.split("=")[1].trim();
            }

            // do not check AccessController requests and requests for AccessGW and AuthGW services
            if(sourceIP.isEmpty() || sourceIP.equals(Inet4Address.getByName(Constants.MY_IP).getHostAddress()) || allowedPortsOnGW.contains(Integer.parseInt(destPort)))
                continue;

            String finalSourceIP = sourceIP;
            String finalDestPort = destPort;

            Optional<UserAccessCommandHolder> uac = userAccessCommandHolderMap.values().stream().filter(ua -> ua.getUser().getIp().equals(finalSourceIP)).findFirst();

            if(uac.isPresent() && Arrays.stream(mapPermissionToPermissionEnum(uac.get().getUser().getPermissions()))
                                        .noneMatch(pm -> pm.getPort() == Integer.parseInt(finalDestPort))) {
                forbidUserAccess(uac.get());
            }
        }
    }

    private PermissionEnum[] mapPermissionToPermissionEnum(List<Permission> permissions) throws IllegalArgumentException {
        PermissionEnum[] permissionEnumArray = new PermissionEnum[permissions.size()];
        for(int i = 0; i < permissions.size(); ++i) {
            permissionEnumArray[i] = PermissionEnum.valueOf(permissions.get(i).getName());
        }
        return permissionEnumArray;
    }
}
