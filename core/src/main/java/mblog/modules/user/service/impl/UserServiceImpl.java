/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.modules.user.service.impl;

import mblog.base.lang.EntityStatus;
import mblog.base.lang.MtonsException;
import mblog.base.utils.MD5;
import mblog.modules.authc.dao.RoleDao;
import mblog.modules.authc.entity.Role;
import mblog.modules.user.dao.UserDao;
import mblog.modules.user.data.AccountProfile;
import mblog.modules.user.data.BadgesCount;
import mblog.modules.user.data.UserVO;
import mblog.modules.user.entity.User;
import mblog.modules.user.service.NotifyService;
import mblog.modules.user.service.UserService;
import mblog.modules.utils.BeanMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "usersCaches")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private NotifyService notifyService;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public AccountProfile login(String username, String password) {
		User po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");

		Assert.state(StringUtils.equals(po.getPassword(), password), "密码错误");

		po.setLastLogin(Calendar.getInstance().getTime());
		userDao.save(po);
		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);
		return u;
	}

	@Override
	@Transactional
	public AccountProfile getProfileByName(String username) {
		User po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");
		po.setLastLogin(Calendar.getInstance().getTime());

		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);

		return u;
	}

	@Override
	@Transactional
	public UserVO register(UserVO user) {
		Assert.notNull(user, "Parameter user can not be null!");

		Assert.hasLength(user.getUsername(), "用户名不能为空!");
//		Assert.hasLength(user.getEmail(), "邮箱不能为空!");
		Assert.hasLength(user.getPassword(), "密码不能为空!");

		User check = userDao.findByUsername(user.getUsername());

		Assert.isNull(check, "用户名已经存在!");

		if (StringUtils.isNotBlank(user.getEmail())) {
			check = userDao.findByEmail(user.getEmail());
			Assert.isNull(check, "邮箱已经被注册!");
		}

		User po = new User();

		BeanUtils.copyProperties(user, po);

		Date now = Calendar.getInstance().getTime();
		po.setPassword(MD5.md5(user.getPassword()));
		po.setStatus(EntityStatus.ENABLED);
		po.setActiveEmail(EntityStatus.ENABLED);
		po.setCreated(now);

		userDao.save(po);

		return BeanMapUtils.copy(po, 0);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#user.getId()")
	public AccountProfile update(UserVO user) {
		User po = userDao.findOne(user.getId());
		if (null != po) {
			po.setName(user.getName());
			po.setSignature(user.getSignature());
			userDao.save(po);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateEmail(long id, String email) {
		User po = userDao.findOne(id);

		if (null != po) {

			if (email.equals(po.getEmail())) {
				throw new MtonsException("邮箱地址没做更改");
			}

			User check = userDao.findByEmail(email);

			if (check != null && check.getId() != po.getId()) {
				throw new MtonsException("该邮箱地址已经被使用了");
			}
			po.setEmail(email);
			po.setActiveEmail(EntityStatus.ENABLED);

			userDao.save(po);
		}

		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Cacheable(key = "#userId")
	public UserVO get(long userId) {
		User po = userDao.findOne(userId);
		UserVO ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 0);
		}
		return ret;
	}

	
	@Override
	public List<UserVO> findHotUserByfans(){
		List<UserVO> rets = new ArrayList<>();
		List<User> list = userDao.findTop12ByOrderByFansDesc();
		for (User po : list) {
			UserVO u = BeanMapUtils.copy(po , 0);
			rets.add(u);
		}
		return rets;
	}
	
	@Override
	public UserVO getByUsername(String username) {
		User po = userDao.findByUsername(username);
		UserVO ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 0);
		}
		return ret;
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateAvatar(long id, String path) {
		User po = userDao.findOne(id);
		if (po != null) {
			po.setAvatar(path);
			userDao.save(po);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	public void updatePassword(long id, String newPassword) {
		User po = userDao.findOne(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (null != po) {
			po.setPassword(MD5.md5(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updatePassword(long id, String oldPassword, String newPassword) {
		User po = userDao.findOne(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (po != null) {
			Assert.isTrue(MD5.md5(oldPassword).equals(po.getPassword()), "当前密码不正确");
			po.setPassword(MD5.md5(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updateStatus(long id, int status) {
		User po = userDao.findOne(id);

		if (po != null) {
			po.setStatus(status);
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public AccountProfile updateActiveEmail(long id, int activeEmail) {
		User po = userDao.findOne(id);

		if (po != null) {
			po.setActiveEmail(activeEmail);
			userDao.save(po);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	public Page<UserVO> paging(Pageable pageable) {
		Page<User> page = userDao.findAllByOrderByIdDesc(pageable);
		List<UserVO> rets = new ArrayList<>();

		for (User po : page.getContent()) {
			UserVO u = BeanMapUtils.copy(po , 1);
			rets.add(u);
		}

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public Map<Long, UserVO> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<User> list = userDao.findAllByIdIn(ids);
		Map<Long, UserVO> ret = new HashMap<>();

		list.forEach(po -> {
			ret.put(po.getId(), BeanMapUtils.copy(po, 0));
		});
		return ret;
	}

}
