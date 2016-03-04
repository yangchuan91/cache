package com.cache.local;

import java.net.URL;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cache.config.CacheConfig;

public class Ehcache
  implements LocalCache
{	
private static Logger logger = LoggerFactory.getLogger(Ehcache.class);
  private static Ehcache istance = null;
  private static CacheManager manager = null;
  private static Cache cache = null;

  private Ehcache() {
    String resourceDic = CacheConfig.getConfig("ehcacheDic");
    if (manager == null) {
      if ((resourceDic == null) || ("".equals(resourceDic.trim()))) {
        resourceDic = "src/main/resources/config/ehcache.xml";
      }
      logger.info(String.format("本地缓存初始化，配置路径为：[%s]", resourceDic));
      URL url = getClass().getResource(resourceDic);
      manager = CacheManager.create(url);
    }
    if (cache == null) {
      cache = manager.getCache("LocalEhcache");
    }
    if (cache == null) {
      logger.info(String.format("本地缓存未能初始，请检查配置%s是否有Ehcache实例", new Object[] { resourceDic }));
    }
  }

  public static synchronized Ehcache getInstance() {
    if (istance == null) {
      istance = new Ehcache();
    }
    return istance;
  }

  public void put(Object key, Object value) {
    cache.put(new Element(key, value));
  }

  public void remove(Object key) {
    cache.remove(key);
  }

  public Object get(Object key)
  {
    Element element = cache.get(key);
    if (element == null) {
      return "";
    }
    return element.getObjectValue();
  }
}