/**
 *
 */
package mblog.template.directive;

import mblog.modules.blog.data.PostVO;
import mblog.modules.blog.service.PostService;
import mblog.template.DirectiveHandler;
import mblog.template.TemplateDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 推荐内容查询
 *
 * @author langhsu
 *
 */
@Component
public class BannerDirective extends TemplateDirective {
	@Autowired
    private PostService postService;

    @Override
    public String getName() {
        return "banner";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        List<PostVO> result = postService.findAllFeatured();
        handler.put(RESULTS, result).render();
    }
}
