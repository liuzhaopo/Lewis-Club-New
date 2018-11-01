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

import mblog.base.lang.MtonsException;
import mblog.modules.user.data.UserVO;
import mblog.modules.user.dao.FollowDao;
import mblog.modules.user.entity.Follow;
import mblog.modules.user.entity.User;
import mblog.modules.user.service.FollowService;
import mblog.modules.user.service.UserEventService;
import mblog.modules.utils.BeanMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author langhsu
 *
 */
@Service
public class FollowServiceImpl implements FollowService {
	@Autowired
	private FollowDao followDao;
	@Autowired
	private UserEventService userEventService;

	@Override
	@Transactional
	public long follow(long userId, long followId) {
		long ret = 0;

		Assert.state(userId != followId, "您不能关注自己");

		Follow po = followDao.findByUserAndFollow(new User(userId), new User(followId));

		if (po == null) {
			po = new Follow();
			po.setUser(new User(userId));
			po.setFollow(new User(followId));
			po.setCreated(new Date());

			followDao.save(po);

			ret = po.getId();

			userEventService.identityFollow(userId, followId, true);
			userEventService.identityFans(followId, userId, true);
		} else {
			throw new MtonsException("您已经关注过此用户了");
		}
		return ret;
	}

	@Override
	@Transactional
	public void unfollow(long userId, long followId) {
		int ret = followDao.deleteByUserAndFollow(new User(userId), new User(followId));

		if (ret <= 0) {
			throw new MtonsException("取消关注失败");
		} else {
			userEventService.identityFollow(userId, followId, false);
			userEventService.identityFans(followId, userId, false);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserVO> follows(Pageable pageable, long userId) {
		Page<Follow> page = followDao.findAllByUser(pageable, new User(userId));
		List<UserVO> rets = new ArrayList<>();

		for (Follow po : page.getContent()) {
			rets.add(BeanMapUtils.copy(po.getFollow(), 0));
		}
		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserVO> fans(Pageable pageable, long userId) {
		Page<Follow> page = followDao.findAllByFollow(pageable, new User(userId));
		List<UserVO> rets = new ArrayList<>();

		for (Follow po : page.getContent()) {
			rets.add(BeanMapUtils.copy(po.getUser(), 0));
		}

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional
	public boolean checkFollow(long userId, long followId) {
		return (followDao.findByUserAndFollow(new User(userId), new User(followId)) != null);
	}

	@Override
	@Transactional
	public boolean checkCrossFollow(long userId, long targetUserId) {
		List<Follow> list = followDao.findAllCrossFollow(userId, targetUserId);
		return  list != null && list.size() > 0;
	}

}
