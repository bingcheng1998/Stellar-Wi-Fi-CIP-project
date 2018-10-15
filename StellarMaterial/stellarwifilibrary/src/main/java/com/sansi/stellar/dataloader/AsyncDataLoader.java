package com.sansi.stellar.dataloader;

import android.view.View;

import com.sansi.stellarWiFi.api.Call;
import com.sansi.stellarWiFi.api.Callback;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 10:06
 *          类说明
 */
public class AsyncDataLoader {
    public static final String TAG = AsyncDataLoader.class.getSimpleName();
    private final Map<Integer, String> cacheKeysForViewAwares = Collections.synchronizedMap(new HashMap<Integer, String>());
    private final Map<String, CacheObject> memoryCache = Collections.synchronizedMap(new HashMap<String, CacheObject>());
    private static AsyncDataLoader defaltAsyncDataLoader;

    public synchronized static AsyncDataLoader getDefaltAsyncDataLoader() {
        if (defaltAsyncDataLoader == null) {
            defaltAsyncDataLoader = new AsyncDataLoader();
        }
        return defaltAsyncDataLoader;
    }

    final static class CacheObject {
        final Object obj;
        final long createTime;
        final long lifeTime;
        boolean isInvalide = false;

        CacheObject(long cacheTime, Object obj) {
            this.createTime = System.currentTimeMillis();
            this.lifeTime = cacheTime;
            this.obj = obj;
        }

        boolean isCacheInvalide() {
            return isInvalide || ((System.currentTimeMillis() - createTime) > lifeTime);
        }
    }

    public <T> void asyncDataLoader(View view, Call<T> call, String cacheKey, final DataBindViewHandler<T> dataBindViewHandler) {
        ViewAwareImpl viewAware = new ViewAwareImpl<T>(view) {
            @Override
            public boolean setData(View view, T data) {
                if (dataBindViewHandler != null) {
                    dataBindViewHandler.onBindView(data, view);
                    return true;
                } else {
                    return super.setData(view, data);
                }
            }
        };
        asyncDataLoader(viewAware, call, cacheKey);
    }
    private <T> void asyncDataLoader(final ViewAware viewAware, Call<T> call, final String cacheKey){
        asyncDataLoader(viewAware,call,cacheKey,10_000);
    }

    private <T> void asyncDataLoader(final ViewAware viewAware, Call<T> call, final String cacheKey, final long cacheTime) {
        CacheObject cacheObject = memoryCache.get(cacheKey);
        if (cacheObject != null) {
            prepareDisplayTaskFor(viewAware, cacheKey);
            L.d(cacheKey+"缓存命中,使用缓存...");
            T data = (T) cacheObject.obj;
            DisplayDataTask displayDataTask = new DisplayDataTask(data, viewAware, this, cacheKey, LoadedFrom.MEMORY_CACHE);
            displayDataTask.run();
        }

        if (cacheObject == null || cacheObject.isCacheInvalide()) {
            prepareDisplayTaskFor(viewAware, cacheKey);
            if(cacheObject == null){
                L.d(cacheKey+"缓存未命中,异步加载...");
            }else {
                L.d(cacheKey+"缓存已失效,异步加载...");
            }
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(T t) {
                    memoryCache.put(cacheKey, new CacheObject(cacheTime, t));
                    DisplayDataTask displayDataTask = new DisplayDataTask(t, viewAware, AsyncDataLoader.this,
                            cacheKey, LoadedFrom.NETWORK);
                    displayDataTask.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) t.printStackTrace();
                }
            });
        }

    }

    private void prepareDisplayTaskFor(ViewAware viewAware, String memoryCacheKey) {
        cacheKeysForViewAwares.put(viewAware.getId(), memoryCacheKey);
    }

    void cancelDisplayTaskFor(ViewAware viewAware) {
        cacheKeysForViewAwares.remove(viewAware.getId());
    }


    String getLoadingUriForView(ViewAware viewAware) {
        return cacheKeysForViewAwares.get(viewAware.getId());
    }

    public void clearMemoryCache() {
        this.memoryCache.clear();
    }

    public void invalideMemoryCache() {
        for (CacheObject cacheObject : memoryCache.values()) {
            cacheObject.isInvalide = true;
        }
    }

    public Object getMemoryCacheObject(String cacheKey) {
        CacheObject cacheObject = memoryCache.get(cacheKey);
        if (cacheObject != null) {
            return cacheObject.obj;
        } else {
            return null;
        }
    }

    /**
     * Checks whether memory cache key  for current ViewAware is actual
     */
    private boolean isViewWasReused(ViewAware viewAware, String memoryCacheKey) {
        String currentCacheKey = getLoadingUriForView(viewAware);
        return !memoryCacheKey.equals(currentCacheKey);
    }
}
