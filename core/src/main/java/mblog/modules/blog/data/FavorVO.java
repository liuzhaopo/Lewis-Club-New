package mblog.modules.blog.data;

import mblog.modules.blog.entity.Favor;

/**
 * @author langhsu on 2015/8/31.
 */
public class FavorVO extends Favor {
    // extend
    private PostVO post;

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
