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

import mblog.base.lang.Consts;
import mblog.modules.user.dao.UserDao;
import mblog.modules.user.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户事件操作, 用于统计用户信息
 * @author langhsu on 2015/8/6.
 */
@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private UserDao userDao;

    @Override
    public void identityPost(Long userId, long postId, boolean identity) {
        userDao.updatePosts(userId, (identity) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP);
    }

    @Override
    public void identityComment(Long userId, long commentId, boolean identity) {
        userDao.updateComments(userId, (identity) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP);
    }

    @Override
    public void identityFollow(Long userId, long followId, boolean identity) {
        userDao.updateFollows(userId, (identity) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP);
    }

    @Override
    public void identityFans(Long userId, long fansId, boolean identity) {
        userDao.updateFans(userId, (identity) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP);
    }

    @Override
    public void identityFavors(Long userId, boolean identity, int targetType, long targetId) {
        userDao.updateFavors(userId, (identity) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP);
    }
}
