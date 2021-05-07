package org.unibl.etf.srs.repository;

import org.unibl.etf.srs.model.Permission;
import org.unibl.etf.srs.model.User;
import java.util.List;

public interface PermissionRepository {
    List<Permission> getPermissions();
    Permission getPermissionByUsername(String name);
    Permission savePermission(Permission permission);
    void updatePermission(Permission permission);
    void deletePermission(String name);
}
