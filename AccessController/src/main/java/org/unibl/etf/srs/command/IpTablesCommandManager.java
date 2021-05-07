package org.unibl.etf.srs.command;

import org.unibl.etf.srs.consumer.Constants;
import org.unibl.etf.srs.model.PermissionEnum;
import org.unibl.etf.srs.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class IpTablesCommandManager {

    private static IpTablesCommandManager instance;

    private IpTablesCommandManager() { }

    public static IpTablesCommandManager getInstance() {
        if(instance == null) {
            instance = new IpTablesCommandManager();
        }
        return instance;
    }

    public void executeCommands(List<Command> commands, boolean executeInverse) throws IOException {
        for(Command command : commands) {
            String commandString = executeInverse ? command.getInverseCommand() : command.getOriginalCommand();
            System.out.println("Executing: " + commandString);
            Process proc = Runtime.getRuntime().exec(commandString);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            System.out.println("Here is the output of the command:\n");
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        }
    }

    public UserAccessCommandHolder allowIpSourceAndPortsCommands(User user, PermissionEnum... protocols) throws UnknownHostException {
        List<Command> commandListAC = new LinkedList<>();
        List<Command> commandListAuthGW = new LinkedList<>();
        for(PermissionEnum protocol : protocols) {
            String commandAC = new IpTablesCommandBuilder()
                    .nat()
                    .appendToChain(RuleChain.PREROUTING)
                    .allowSource(Inet4Address.getByName(Constants.ACCESS_GW_HOST).getHostAddress())
                    .allowProtocol(Protocol.TCP)
                    .allowPort(protocol.getPort())
                    .all(Rule.DNAT)
                    .toDestination(Inet4Address.getByName(Constants.DESTINATION_SERVICES_HOST).getHostAddress(), protocol.getPort())
                    .build();

            String commandAuthGW = new IpTablesCommandBuilder()
                    .nat()
                    .appendToChain(RuleChain.PREROUTING)
                    .allowSource(user.getIp())
                    .allowProtocol(Protocol.TCP)
                    .allowPort(protocol.getPort())
                    .all(Rule.DNAT)
                    .toDestination(Inet4Address.getByName(Constants.MY_IP).getHostAddress(), protocol.getPort())
                    .build();

            commandListAC.add(Command.builder()
                    .originalCommand(commandAC)
                    .inverseCommand(IpTablesCommandBuilder.createInverseCommand(commandAC))
                    .build());

            commandListAuthGW.add(Command.builder()
                    .originalCommand(commandAuthGW)
                    .inverseCommand(IpTablesCommandBuilder.createInverseCommand(commandAuthGW))
                    .build());
        }

        return UserAccessCommandHolder.builder()
                .user(user)
                .commandsAC(commandListAC)
                .commandsAccessGW(commandListAuthGW)
                .build();
    }

}
