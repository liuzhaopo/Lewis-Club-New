package mblog.modules.blog.dao.custom;

import mblog.modules.blog.data.PostVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by langhsu on 2017/9/30.
 */
public interface PostDaoCustom {
    Page<PostVO> search(Pageable pageable, String q) throws Exception;
    Page<PostVO> searchByTag(Pageable pageable, String tag);
    void resetIndexs();
}
