package com.cache.util;

import java.util.HashSet;
import java.util.Set;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cache.config.CacheConfig;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class SentinelPoolUtil {
	private static Logger logger = LoggerFactory.getLogger(SentinelPoolUtil.class);
	private static Set<String> sentinels = null;
	private static GenericObjectPoolConfig jedisPoolConfig = null;
	private static JedisSentinelPool jedisSentinelPool = null;
	private static SentinelPoolUtil sentinelPoolUtil=null;
	//sentinel ip和端口
	private static String sentinel_ip_port_0;
	private static String sentinel_ip_port_1;
	//redis的sentinel名称
	private static String master_name;
	//最大空闲连接数
	private static int maxIdle;
	//最大连接数
	private static int maxTotal;
	//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	private static int maxWaitMillis;
	//最小空闲连接数
	private static int minIdle;
	
	static {
		sentinel_ip_port_0=CacheConfig.getConfig("sentinel_ip_port_0");
		sentinel_ip_port_1=CacheConfig.getConfig("sentinel_ip_port_1");
		master_name=CacheConfig.getConfig("master_name");
		maxIdle=Integer.parseInt(CacheConfig.getConfig("maxIdle"));
		minIdle=Integer.parseInt(CacheConfig.getConfig("minIdle"));
		maxTotal=Integer.parseInt(CacheConfig.getConfig("maxTotal"));
		maxWaitMillis=Integer.parseInt(CacheConfig.getConfig("maxWaitMillis"));
		logger.info(String.format("从配置文件中获取配置信息：sentinel_ip_port_0=[%s],sentinel_ip_port_1=[%s]"
				+ "master_name=[%s],maxIdle=[%s],"
				+ "minIdle=[%s],maxTotal=[%s],maxWaitMillis=[%s]", 
				sentinel_ip_port_0,sentinel_ip_port_1,master_name,maxIdle,minIdle,maxTotal,maxWaitMillis));
	}
	
	public static SentinelPoolUtil getSentinelPoolUtil(){
		if(sentinelPoolUtil==null){
			sentinelPoolUtil=new SentinelPoolUtil();
		}
		return sentinelPoolUtil;
	}
	
	private SentinelPoolUtil(){
		init();
	}
	
	public JedisSentinelPool getJedisSentinelPool(){
		if(jedisSentinelPool==null){
			logger.info("JedisSentinelPool 初始化");
			init();
		}
		return jedisSentinelPool;
	}
	
	public void init(){
		try {
			initSentinelsSet();
			initJedisPoolConfig();
			initJedisSentinelPool();
		} catch (Exception e) {
			logger.error("JedisSentinelPool 初始化失败",e);
		}
		
	}
	
	private void initJedisSentinelPool(){
		jedisSentinelPool=new JedisSentinelPool(master_name, sentinels);
		logger.info("initJedisSentinelPool :"+jedisSentinelPool);
	}
	
	private void initSentinelsSet(){
		sentinels=new HashSet<String>();
		sentinels.add(sentinel_ip_port_0);
		sentinels.add(sentinel_ip_port_1);
		logger.info("initSentinelsSet");
	}
	
	private void initJedisPoolConfig(){
		jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnCreate(true);
		jedisPoolConfig.setTestOnReturn(true);
		logger.info("initJedisPoolConfig :"+jedisPoolConfig);
	}
	

}
