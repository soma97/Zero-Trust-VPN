package org.unibl.etf.srs.command;

public class IpTablesCommandBuilder {
    private final StringBuilder command = new StringBuilder();

    public IpTablesCommandBuilder appendToChain(RuleChain chainType) {
        command.append(" -A ").append(chainType.toString());
        return this;
    }

    public IpTablesCommandBuilder removeFromChain(RuleChain chainType) {
        command.append(" -D ").append(chainType.toString());
        return this;
    }

    public IpTablesCommandBuilder nat() {
        command.append(" -t nat");
        return this;
    }

    public IpTablesCommandBuilder allowProtocol(Protocol protocol) {
        command.append(" -p ").append(protocol.toString());
        return this;
    }

    public IpTablesCommandBuilder allowSource(String ip) {
        command.append(" --source ").append(ip);
        return this;
    }

    public IpTablesCommandBuilder allowPort(int port) {
        command.append(" --dport ").append(port);
        return this;
    }

    public IpTablesCommandBuilder all(Rule rule) {
        command.append(" -j ").append(rule.toString());
        return this;
    }

    public IpTablesCommandBuilder toDestination(String ip, int port) {
        command.append(" --to-destination ").append(ip).append(":").append(port);
        return this;
    }

    public String build() {
        return "sudo iptables" + command.toString();
    }

    public static String createInverseCommand(String command) {
        return command.replace("-A", "-D");
    }
}
