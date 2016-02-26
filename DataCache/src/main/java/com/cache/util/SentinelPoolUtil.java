package com.cache.util;

import java.util.HashSet;
import java.util.Set;

import javax.naming.InitialContext;

import com.cache.config.CacheConfig;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class SentinelPoolUtil {
	
	private static Set<String> sentinels = null;
	private static JedisPoolConfig jedisPoolConfig = null;
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
			init();
		}
		return jedisSentinelPool;
	}
	
	public void init(){
		initSentinelsSet();
		initJedisPoolConfig();
		initJedisSentinelPool();
	}
	
	private void initJedisSentinelPool(){
		jedisSentinelPool=new JedisSentinelPool(master_name, sentinels, jedisPoolConfig);
	}
	
	private void initSentinelsSet(){
		sentinels=new HashSet<String>();
		sentinels.add(sentinel_ip_port_0);
		sentinels.add(sentinel_ip_port_1);
	}
	
	private void initJedisPoolConfig(){
		jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
	}
	

}
