package org.unibl.etf.srs.repository;

import org.unibl.etf.srs.model.User;
import java.util.List;

public interface UserRepository {
    List<User> getUsers();
    User getUserByEmail(String email);
    User saveUser(User user);
    void updateUser(User user);
    void deleteUser(String username);
}
