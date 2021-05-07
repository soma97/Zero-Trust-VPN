package org.unibl.etf.srs.consumer;

import org.unibl.etf.srs.model.User;
import org.unibl.etf.srs.model.UserRequest;
import java.util.List;

public interface Consumer {
    User[] getUsersForAuth();
    User getObjectById(Long id);
    String[] getAuthenticated();
    public String[] getLogs();
    void insertObject(UserRequest object);
    void sendCommands(List<String> commands);
    void updateObject(UserRequest object);
    void removeObjectById(Long id);

}
