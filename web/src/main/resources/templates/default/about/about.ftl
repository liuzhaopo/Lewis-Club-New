<#include "/default/utils/ui.ftl"/>

<@layout "关于我">

<div class="row main">
    <div class="col-xs-12 col-md-9 side-left topics-show">
        <!-- view show -->
        <div class="topic panel panel-default">
            <div class="infos panel-heading">

                <h1 class="panel-title topic-title">关于我</h1>

                <div class="meta inline-block">
                    <a class="author" href="http://www.liuzhaopo.top/about">lzhpo</a>
                </div>
                <div class="clearfix"></div>
            </div>

            <div class="content-body entry-content panel-body ">
                <div class="markdown-body" id="emojify">
                    <p><strong>关于Lewis社区</strong></p>
                    <p>Lewis本来是我的English NickName。</p>
                    <p>后来，我换成lzhpo了。</p>
                    <p>但是，还是比较喜欢这个NickName的。</p>
                    <p>所以，这个社区名字就为Lewis社区！</p>

                    <p><strong>关于版权</strong><br/></p>
                    <p>本站所有标注为原创的文章，转载请标明出处。</p>
                    <p>本站所有转载的文章，一般都会在文章中注明原文出处。</p>
                    <p>所有转载的文章皆来源于互联网，若因此对原作者造成侵权，烦请原作者告知，我会立刻删除相关内容。</p>

                    <p><strong>Lewis技术选型</strong><br/></p>
                    <p> - JDK8<br/>
                        - MySQL<br/>
                        - Spring-boot<br/>
                        - Spring-data-jpa<br/>
                        - Shiro<br/>
                        - Hibernate-search<br/>
                        - Ehcache<br/>
                        - Freemarker<br/>
                        - Bootstrap<br/>
                        - SeaJs<br/>
                    </p>

                </div>
            </div>
            <div class="panel-footer operate">
            </div>
            <div class="panel-footer operate">
                <div class="hidden-xs">
                    <div class="social-share" data-sites="weibo, wechat, facebook, twitter, google, qzone, qq"></div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
        <!-- /view show -->
    </div>
    <div class="col-xs-12 col-md-3 side-right hidden-xs hidden-sm">
		<#include "/default/inc/right.ftl"/>
    </div>
</div>

</@layout>