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

import mblog.base.utils.MD5;
import mblog.modules.user.data.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.modules.user.data.OpenOauthVO;
import mblog.modules.user.dao.OpenOauthDao;
import mblog.modules.user.dao.UserDao;
import mblog.modules.user.entity.OpenOauth;
import mblog.modules.user.entity.User;
import mblog.modules.user.service.OpenOauthService;
import mblog.modules.utils.BeanMapUtils;

/**
 * 第三方登录授权管理
 * @author langhsu on 2015/8/12.
 */
@Service
public class OpenOauthServiceImpl implements OpenOauthService {
    @Autowired
    private OpenOauthDao openOauthDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public UserVO getUserByOauthToken(String oauth_token) {
        OpenOauth thirdToken = openOauthDao.findByAccessToken(oauth_token);
        User userPO = userDao.findOne(thirdToken.getId());
        return BeanMapUtils.copy(userPO, 0);
    }

    @Override
    @Transactional
    public OpenOauthVO getOauthByToken(String oauth_token) {
        OpenOauth po = openOauthDao.findByAccessToken(oauth_token);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
    }

    @Override
    @Transactional
    public OpenOauthVO getOauthByUid(long userId) {
        OpenOauth po = openOauthDao.findByUserId(userId);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
    }

    @Override
    @Transactional
    public boolean checkIsOriginalPassword(long userId) {
        OpenOauth po = openOauthDao.findByUserId(userId);
        if (po != null) {
            User upo = userDao.findOne(userId);

            String pwd = MD5.md5(po.getAccessToken());
            // 判断用户密码 和 登录状态
            if (pwd.equals(upo.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void saveOauthToken(OpenOauthVO oauth) {
        OpenOauth po = new OpenOauth();
        BeanUtils.copyProperties(oauth, po);
        openOauthDao.save(po);
    }

	@Override
	@Transactional
	public OpenOauthVO getOauthByOauthUserId(String oauthUserId) {
		OpenOauth po = openOauthDao.findByOauthUserId(oauthUserId);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
	}

}
