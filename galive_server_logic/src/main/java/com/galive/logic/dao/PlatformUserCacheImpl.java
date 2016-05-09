package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;
import redis.clients.jedis.Jedis;

public class PlatformUserCacheImpl implements PlatformUserCache {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String sharedUdidKey(String udid) {
		return RedisManager.getInstance().keyPrefix() + "platform:shared_udid:" + udid;
	}
	
	private String recentContactKey(String deviceid) {
		return RedisManager.getInstance().keyPrefix() + "platform:recent_contact:" + deviceid;
	}

	@Override
	public void saveSharedDeviceid(String deviceid, String udid) {
		String key = sharedUdidKey(udid);
		jedis.sadd(key, deviceid);
	}

	@Override
	public void removeSharedDeviceid(String deviceid, String udid) {
		String key = sharedUdidKey(udid);
		jedis.srem(key, deviceid);
	}

	@Override
	public void saveContact(String deviceid, String contactDeviceid) {
		String key = recentContactKey(deviceid);
		jedis.zadd(key, System.currentTimeMillis(), contactDeviceid);
	}

	@Override
	public Set<String> listSharedDeviceids(String udid) {
		String key = sharedUdidKey(udid);
		Set<String> result = jedis.smembers(key);
		return result;
	}

	@Override
	public List<String> listContacts(String deviceid, int start, int end) {
		String key = recentContactKey(deviceid);
		List<String> result = new ArrayList<>();
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String contact : sets) {
			result.add(contact);
		}
		return result;
	}


}
