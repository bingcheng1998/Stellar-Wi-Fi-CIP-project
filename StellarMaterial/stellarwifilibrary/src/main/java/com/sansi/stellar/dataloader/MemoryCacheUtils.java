package com.sansi.stellar.dataloader;

public final class MemoryCacheUtils {

	private static final String URI_AND_SIZE_SEPARATOR = "_";

	private MemoryCacheUtils() {
	}

	public static String generateKey(String method, String mac,Object type ) {
		return new StringBuilder(method).append(URI_AND_SIZE_SEPARATOR)
				.append(mac).append(URI_AND_SIZE_SEPARATOR).append(type).toString();
	}

	public static String  generateStatusCacheKey(String mac,Object type){
		return generateKey("apiServiceAdapter_queryStatus", mac,type);
	}

	public static String  generateStatus2CacheKey(String mac,Object type){
		return generateKey("apiServiceAdapter_queryStatus2", mac,type);
	}

	public static String  generateQueryDelayCacheKey(String mac,Object type){
		return generateKey("localApiService_queryDelay",mac,type);
	}
}