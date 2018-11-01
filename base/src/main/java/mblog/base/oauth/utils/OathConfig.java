package mblog.base.oauth.utils;

import mblog.base.oauth.APIConfig;















public class OathConfig
{
  public static String getValue(String key)
  {
    return APIConfig.getInstance().getValue(key);
  }
}
