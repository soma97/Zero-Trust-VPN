package org.unibl.etf.srs.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class Command {
    private String originalCommand;
    private String inverseCommand;

    public Command(String originalCommand, String inverseCommand) {
        this.originalCommand = originalCommand;
        this.inverseCommand = inverseCommand;
    }
}
