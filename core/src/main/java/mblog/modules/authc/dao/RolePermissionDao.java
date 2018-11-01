package mblog.modules.authc.dao;

import mblog.modules.authc.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author - langhsu
 * @create - 2018/5/18
 */
public interface RolePermissionDao extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {
    @Transactional
    int deleteByRoleId(long roleId);

    int deleteByPermissionId(long permissionId);

    List<RolePermission> findAllByRoleId(long roleId);
}
