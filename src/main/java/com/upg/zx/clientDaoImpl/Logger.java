package com.upg.zx.clientDaoImpl;

import redis.clients.jedis.Jedis;

public class Logger {
	public static void main(String[] args) throws Exception {
		while (true) {
			Thread.sleep(1000 * 3);
			debug("当前时间:" + System.currentTimeMillis());
		}
	}

	public static void debug(String key, String message) {
		add(key, message);
	}

	public static void debug(String message) {
		String key = "SYSTEM:LOG:" + DateHelper.currentDate3();
		add(key, message);
	}

	public static Jedis loadJedis() throws Exception {
		Jedis jedis = new Jedis("172.16.2.201", 6379);
		jedis.auth("123456");
		jedis.select(15);
		return jedis;
	}

	public static void add(String key, String value) {
		Jedis jedis = null;
		try {
			if (jedis == null)
				jedis = loadJedis();

			jedis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();
			jedis = null;
		}
	}
}
