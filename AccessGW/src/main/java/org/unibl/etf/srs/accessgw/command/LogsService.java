package org.unibl.etf.srs.accessgw.command;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class LogsService {

    @Getter
    @Setter
    private List<String> logs = new LinkedList<>();

    public void readLogsInLoop() {
        new Thread(() -> {
            try {
                BufferedReader br = new BufferedReader(new FileReader("/var/log/kern.log"));
                String line;
                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        //wait until there is more of the file for us to read
                        Thread.sleep(1000);
                    } else {
                        logs.add(line);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
