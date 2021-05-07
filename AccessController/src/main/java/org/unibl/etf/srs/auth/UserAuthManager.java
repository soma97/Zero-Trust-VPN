package org.unibl.etf.srs.auth;

import org.unibl.etf.srs.DependencyManager;
import org.unibl.etf.srs.consumer.Consumer;
import org.unibl.etf.srs.model.User;
import org.unibl.etf.srs.model.UserRequest;
import org.unibl.etf.srs.repository.UserRepository;
import org.unibl.etf.srs.service.AccessService;
import org.unibl.etf.srs.service.MailService;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserAuthManager {
    private static UserAuthManager instance;

    private static final int CHECK_PERIOD = 1; // in seconds

    Consumer consumer = DependencyManager.getInstance().getConsumer();
    UserRepository userRepository = DependencyManager.getInstance().getUserRepository();

    private UserAuthManager() {}

    public static UserAuthManager getInstance() {
        if(instance == null) {
            instance = new UserAuthManager();
        }
        return instance;
    }

    public void startAccessStateChecker() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                for (User userToCheck : consumer.getUsersForAuth()) {
                    Optional<User> dbUser = getDbUser(userToCheck);
                    UserRequest userRequest = UserRequest.builder()
                            .email(userToCheck.getEmail())
                            .authenticated(dbUser.map(x -> x.equals(userToCheck)).orElse(false))
                            .code(String.valueOf(1000 + new Random().nextInt(8999))) //code is random value between 1000 and 9999
                            .permissions(dbUser.map(User::getPermissions).orElse(null))
                            .build();

                    consumer.insertObject(userRequest);

                    if(userRequest.isAuthenticated()) {
                        MailService.sendEmail(Collections.singletonList(userRequest.getEmail()), "Verification code", userRequest.getCode());
                        AccessService.getInstance().prepareUserAccessCommands(dbUser.get(), userToCheck.getIp());
                    }
                }

                for(String email : consumer.getAuthenticated()) {
                    AccessService.getInstance().grantUserAccess(email);
                }

                AccessService.getInstance().checkInputLogsAndForbid(consumer.getLogs());

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, 0, CHECK_PERIOD, TimeUnit.SECONDS);
    }

    private Optional<User> getDbUser(User user) {
        return Optional.ofNullable(userRepository.getUserByEmail(user.getEmail()));
    }
}
