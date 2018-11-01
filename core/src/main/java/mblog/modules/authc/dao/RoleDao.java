package mblog.modules.authc.dao;

import mblog.modules.authc.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    /**
     * 根据角色名称查询角色
     * @param ids 角色名称
     * @return 角色列表
     */
    List<Role> findAllByIdIsIn(Set<Long> ids);
    
    List<Role> findAllByStatusOrderByIdDesc(int status);
}
