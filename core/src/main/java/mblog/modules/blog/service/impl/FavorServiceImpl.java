package mblog.modules.blog.service.impl;

import mblog.modules.blog.data.FavorVO;
import mblog.modules.blog.data.PostVO;
import mblog.modules.blog.dao.FavorDao;
import mblog.modules.blog.entity.Favor;
import mblog.modules.blog.service.FavorService;
import mblog.modules.blog.service.PostService;
import mblog.modules.utils.BeanMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author langhsu on 2015/8/31.
 */
@Service
public class FavorServiceImpl implements FavorService {
    @Autowired
    private FavorDao favorDao;
    @Autowired
    private PostService postService;

    @Override
    @Transactional
    public void add(long userId, long postId) {
        Favor po = favorDao.findByOwnIdAndPostId(userId, postId);

        Assert.isNull(po, "已经喜欢过此文章了");

        // 如果没有喜欢过, 则添加记录
        po = new Favor();
        po.setOwnId(userId);
        po.setPostId(postId);
        po.setCreated(new Date());

        favorDao.save(po);
    }

    @Override
    @Transactional
    public void delete(long userId, long postId) {
        Favor po = favorDao.findByOwnIdAndPostId(userId, postId);
        Assert.notNull(po, "还没有喜欢过此文章");
        favorDao.delete(po);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavorVO> pagingByOwnId(Pageable pageable, long ownId) {
        Page<Favor> page = favorDao.findAllByOwnIdOrderByCreatedDesc(pageable, ownId);

        List<FavorVO> rets = new ArrayList<>();
        Set<Long> postIds = new HashSet<>();
        for (Favor po : page.getContent()) {
            rets.add(BeanMapUtils.copy(po));
            postIds.add(po.getPostId());
        }

        if (postIds.size() > 0) {
            Map<Long, PostVO> posts = postService.findMapByIds(postIds);

            for (FavorVO t : rets) {
                PostVO p = posts.get(t.getPostId());
                t.setPost(p);
            }
        }
        return new PageImpl<>(rets, pageable, page.getTotalElements());
    }
}
