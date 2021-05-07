package org.unibl.etf.srs;

import org.unibl.etf.srs.consumer.Consumer;
import org.unibl.etf.srs.consumer.rest.RestConsumer;
import org.unibl.etf.srs.model.User;
import org.unibl.etf.srs.repository.PermissionRepository;
import org.unibl.etf.srs.repository.UserRepository;
import org.unibl.etf.srs.repository.mysql.PermissionRepositoryMySql;
import org.unibl.etf.srs.repository.mysql.UserRepositoryMySql;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class DependencyManager {
    private static final String DB_UNIT = "access_db_unit";

    private DependencyManager() {}

    private static DependencyManager instance;

    private Consumer consumer;
    private EntityManager entityManager;

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;

    public static DependencyManager getInstance() {
        if(instance == null) {
            instance = new DependencyManager();
        }
        return instance;
    }

    public Consumer getConsumer() {
        if(consumer == null) {
            consumer = new RestConsumer();
        }
        return consumer;
    }

    public EntityManager getEntityManager() {
        if(entityManager == null) {
            entityManager = Persistence.createEntityManagerFactory(DB_UNIT).createEntityManager();
        }
        return entityManager;
    }

    public UserRepository getUserRepository() {
        if(userRepository == null) {
            userRepository = new UserRepositoryMySql();
        }
        return userRepository;
    }

    public PermissionRepository getPermissionRepository() {
        if(permissionRepository == null) {
            permissionRepository = new PermissionRepositoryMySql();
        }
        return permissionRepository;
    }

}
