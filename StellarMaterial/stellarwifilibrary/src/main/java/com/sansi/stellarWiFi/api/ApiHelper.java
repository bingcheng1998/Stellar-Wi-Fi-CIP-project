package com.sansi.stellarWiFi.api;

import android.os.Handler;
import android.os.Looper;

import com.sansi.stellarWiFi.util.L;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 18:33
 *          类说明
 */
public class ApiHelper {

    private static ApiHelper instace = new ApiHelper();
    private static LocalApiService localApiService;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private ApiHelper() {
    }

    public static ApiHelper getInstace() {
        return instace;
    }



    public static Map<String,String> getLightAddress(){
        return UdpInstance.getInstance().getMc2AdressMap();
    }

    private static <T> T createService(Class<T> service,InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(),
                new Class<?>[]{service}, handler);
    }



    public synchronized LocalApiService localApiService() {
        if (localApiService == null) {
            localApiService = createService(LocalApiService.class, new UdpInvocationHandler(executorService));
        }
        return localApiService;
    }



    static class UdpInvocationHandler implements InvocationHandler {
        private UdpInstance udpInstance;
        private final Handler handler=new Handler(Looper.getMainLooper());
        ExecutorService executorService;
        private final static int MAX_TRY_COUNT=3;

        UdpInvocationHandler(UdpInstance udpInstance, ExecutorService executorService) {
            if(executorService==null){
                executorService=Executors.newCachedThreadPool();
            }
            this.udpInstance = udpInstance;
            this.executorService = executorService;
        }

        UdpInvocationHandler(ExecutorService executorService) {
            this(UdpInstance.getInstance(),executorService);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final Auth auth = method.getAnnotation(Auth.class);
            if(method.getReturnType()==Call.class){
                final Call retCall = (Call) method.invoke(udpInstance, args);
                return  new RealCall(){
                    @Override
                    public Object execute() throws Exception {
                        synchronized (this) {
                            if (retCall.isExecuted()) throw new IllegalStateException("Already Executed");
                            if(retCall instanceof RealCall){
                                ((RealCall) retCall).executed=true;
                            }
                        }
                        udpInstance.checkUdpConnectionStatus();
                        int tryNum = MAX_TRY_COUNT;
                        do {
                            try {
                                int i = MAX_TRY_COUNT - tryNum;
                                if (i > 0) {
                                    L.i("第" + i + "次重试");
                                }
                                return retCall.execute();
                            } catch (Exception e) {
//                                e.printStackTrace();
                                if (e instanceof udp.core.NoResponseException) {
                                    if(tryNum >0){
                                        tryNum--;
                                    }else{
                                        throw e;
                                    }
                                } else {
                                    throw e;
                                }
                            }
                        } while (true);
                    }

                    @Override
                    public void cancel() {
                        retCall.cancel();
                    }

                    @Override
                    public boolean isExecuted() {
                        return retCall.isExecuted();
                    }

                    @Override
                    public boolean isCanceled() {
                        return retCall.isCanceled();
                    }

                    @Override
                    public void enqueue(final Callback callback) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Object retVal = execute();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                callback.onResponse(retVal);
                                                if(callback instanceof Callback2){
                                                    ((Callback2) callback).onResponse(retCall,retVal);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (final Exception e) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                callback.onFailure(e);
                                                if(callback instanceof Callback2){
                                                    ((Callback2) callback).onFailure(retCall,e);
                                                }
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                };
            }else {
                if(auth ==null || auth.isCheckStatus()) {
                    udpInstance.checkUdpConnectionStatus();
                }
                return method.invoke(udpInstance, args);
            }
        }
    }


}
