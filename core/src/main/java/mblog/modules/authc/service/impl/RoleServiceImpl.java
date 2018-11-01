package mblog.modules.authc.service.impl;

import mblog.modules.authc.dao.PermissionDao;
import mblog.modules.authc.dao.RoleDao;
import mblog.modules.authc.dao.UserRoleDao;
import mblog.modules.authc.entity.Permission;
import mblog.modules.authc.entity.Role;
import mblog.modules.authc.entity.RolePermission;
import mblog.modules.authc.entity.UserRole;
import mblog.modules.authc.service.RolePermissionService;
import mblog.modules.authc.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author - langhsu on 2018/2/11
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public Page<Role> paging(Pageable pageable, String name) {
        Page<Role> page = roleDao.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (StringUtils.isNoneBlank(name)) {
                predicate.getExpressions().add(
                        builder.like(root.get("name"), "%" + name + "%"));
            }

            query.orderBy(builder.desc(root.get("id")));
            return predicate;
        }, pageable);
        return page;
    }

    @Override
    public List<Role> list() {
        List<Role> list = roleDao.findAllByStatusOrderByIdDesc(Role.STATUS_NORMAL);
        return list;
    }

    @Override
    public Map<Long, Role> findByIds(Set<Long> ids) {
        List<Role> list = roleDao.findAllByIdIsIn(ids);
        Map<Long, Role> ret = new LinkedHashMap<>();
        list.forEach(po -> {
            Role vo = toVO(po);
            ret.put(vo.getId(), vo);
        });
        return ret;
    }

    @Override
    public Role get(long id) {
        Role po = roleDao.findOne(id);
        return toVO(po);
    }

    @Override
    public void update(Role r, Set<Permission> permissions) {
        Role po = roleDao.findOne(r.getId());

        if (null == po) {
            po = new Role();
            po.setName(r.getName());
        }
        po.setDescription(r.getDescription());
        po.setStatus(r.getStatus());

        roleDao.save(po);

        rolePermissionService.deleteByRoleId(po.getId());

        if (permissions != null && permissions.size() > 0) {
            Set<RolePermission> rps = new HashSet<>();
            long roleId = po.getId();
            permissions.forEach(p -> {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(p.getId());
                rps.add(rp);
            });

            rolePermissionService.add(rps);
        }
    }

    @Override
    public boolean delete(long id) {
        List<UserRole> urs = userRoleDao.findAllByRoleId(id);
        Assert.state(urs == null || urs.size() == 0, "该角色已经被使用,不能被删除");
        roleDao.delete(id);
        rolePermissionService.deleteByRoleId(id);
        return true;
    }

    @Override
    public void activate(long id, boolean active) {
        Role po = roleDao.findOne(id);
        if (po != null) {
            po.setStatus(active ? Role.STATUS_NORMAL : Role.STATUS_CLOSED);
        }
    }

    private Role toVO(Role po) {
        Role r = new Role();
        r.setId(po.getId());
        r.setName(po.getName());
        r.setDescription(po.getDescription());
        r.setStatus(po.getStatus());

        r.setPermissions(rolePermissionService.findPermissions(r.getId()));
        return r;
    }
}
