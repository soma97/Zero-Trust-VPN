package org.unibl.etf.srs.repository.mysql;

import org.unibl.etf.srs.DependencyManager;
import org.unibl.etf.srs.model.Permission;
import org.unibl.etf.srs.repository.PermissionRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PermissionRepositoryMySql implements PermissionRepository {

    private final EntityManager entityManager = DependencyManager.getInstance().getEntityManager();

    @Override
    public List<Permission> getPermissions() {
        return entityManager.createQuery("select a from permission a", Permission.class).getResultList();
    }

    @Override
    public Permission getPermissionByUsername(String name) {
        return entityManager.find(Permission.class, name);
    }

    @Override
    public Permission savePermission(Permission permission) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(permission);
        transaction.commit();
        return permission;
    }

    @Override
    public void updatePermission(Permission permission) {
        throw new NotImplementedException();
    }

    @Override
    public void deletePermission(String name) {
        entityManager.getTransaction().begin();
        Permission permission = entityManager.find(Permission.class, name);
        entityManager.remove(permission);
        entityManager.getTransaction().commit();
    }
}
