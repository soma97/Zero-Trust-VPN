package org.unibl.etf.srs.repository.mysql;

import org.unibl.etf.srs.DependencyManager;
import org.unibl.etf.srs.model.User;
import org.unibl.etf.srs.repository.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserRepositoryMySql implements UserRepository {

    private final EntityManager entityManager = DependencyManager.getInstance().getEntityManager();

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("select a from user a", User.class).getResultList();
    }

    @Override
    public User getUserByEmail(String email) {
        return entityManager.find(User.class, email);
    }

    @Override
    public User saveUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return user;
    }

    @Override
    public void updateUser(User user) {
        entityManager.getTransaction().begin();
        User userUpdate = entityManager.find(User.class, user.getEmail());

        userUpdate.setPassword(user.getPassword());
        userUpdate.setPermissions(user.getPermissions());

        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteUser(String username) {
        entityManager.getTransaction().begin();
        User user = entityManager.find(User.class, username);
        entityManager.remove(user);
        entityManager.getTransaction().commit();
    }
}
