/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.event.handler;

import mblog.base.lang.Consts;
import mblog.core.event.PostUpdateEvent;
import mblog.modules.blog.data.FeedsVO;
import mblog.modules.blog.service.FeedsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author langhsu on 2015/8/18.
 */
@Component
public class PostFeedsEventHandler implements ApplicationListener<PostUpdateEvent> {
    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private FeedsService feedsService;

    @Async
    @Override
    public void onApplicationEvent(PostUpdateEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getAction()) {
            case PostUpdateEvent.ACTION_PUBLISH:
                // 创建动态对象
                FeedsVO feeds = new FeedsVO();
                feeds.setType(Consts.FEEDS_TYPE_POST);
                feeds.setOwnId(event.getUserId());
                feeds.setPostId(event.getPostId());
                feeds.setAuthorId(event.getUserId());

                int ret = feedsService.add(feeds);

                log.debug(MessageFormat.format("成功派发 {0} 条动态!", ret));
                break;
            case PostUpdateEvent.ACTION_DELETE:
                feedsService.deleteByTarget(event.getPostId());
                break;
        }

    }
}
