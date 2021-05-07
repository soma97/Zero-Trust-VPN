package org.unibl.etf.srs.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unibl.etf.srs.model.User;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class UserAccessCommandHolder {
    private User user;
    private List<Command> commandsAC = new LinkedList<>(); // Access Controller commands
    private List<Command> commandsAccessGW = new LinkedList<>(); // Authentication Gateway commands

    public UserAccessCommandHolder(User user, List<Command> commandsAC, List<Command> commandsAccessGW) {
        this.user = user;
        this.commandsAC = commandsAC;
        this.commandsAccessGW = commandsAccessGW;
    }
}
