package mblog.data.test;

import java.text.MessageFormat;

public class FeedsTest {
    static String pattern = "INSERT INTO mto_feeds (own_id, type, post_id, author_id, created) SELECT user_id, {0}, {1,number,###}, {2,number,###}, now() FROM mto_follows WHERE follow_id = {3,number,###}";

    public static void main(String[] args) {
        String sql = MessageFormat.format(pattern, 1, 88888888, 9, 77);

        System.out.println(sql);
    }
}
