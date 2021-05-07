package org.unibl.etf.srs.accessgw.command;

import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class CommandService {
    public void executeCommands(List<String> commands) {
        try {
            for(String command : commands) {
                System.out.println("Executing: " + command);
                Process proc = Runtime.getRuntime().exec(command);

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(proc.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(proc.getErrorStream()));

                System.out.println("Here is the output of the command:\n");
                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
