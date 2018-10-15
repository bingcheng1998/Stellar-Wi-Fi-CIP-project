package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/7/19 15:12
 *          类说明 Wifi相关工具类
 */
public class WifiUtils {

    public static final String WPA_SUPPLICANT_CONF_FILE="wpa_supplicant_conf";
    public static final String NETWORK="network";

    public static boolean isWifiConnected(Context context) {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空 并且类型是否为WIFI
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return networkInfo.isAvailable();
        } else {
            return false;
        }
    }
    /**

     作者：AlicFeng
     链接：http://www.jianshu.com/p/10ed9ae02775
     來源：简书
     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    /**
     * 获取当前wifi环境的广播地址
     * */
    public static InetAddress getBroadcastAddress(Context ctx) {
        try {
            WifiManager myWifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo myDhcpInfo = myWifiManager.getDhcpInfo();
            if (myDhcpInfo == null) {
               L.w("Could not get broadcast address");
                return null;
            }
            int broadcast = (myDhcpInfo.ipAddress & myDhcpInfo.netmask)
                    | ~myDhcpInfo.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++)
                quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            return InetAddress.getByAddress(quads);
        }catch(Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public static void storeWifiSSIDPwd(Context context,String ssid,String pwd ){
        if(TextUtils.isEmpty(ssid))throw  new IllegalArgumentException("ssid is empty");
        Set<MyWifiInfo> networks = getMyWifiInfos(context);
        MyWifiInfo myWifiInfo = getMyWifiInfo(ssid, networks);
        if (myWifiInfo != null) networks.remove(myWifiInfo);
        networks.add(new MyWifiInfo(ssid,pwd));
        storeMyWifiInfos(context,networks);

    }

    private static void storeMyWifiInfos(Context context, Set<MyWifiInfo> networks) {
        Gson gson = new Gson();
        SharedPreferences sp = context.getSharedPreferences(WPA_SUPPLICANT_CONF_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(NETWORK, gson.toJson(networks)).commit();
    }

    public static MyWifiInfo getWifiInfo(Context context, String ssid) {
        if (TextUtils.isEmpty(ssid)) throw new IllegalArgumentException("ssid is empty");
        Set<MyWifiInfo> networks = getMyWifiInfos(context);
        if (networks == null || networks.isEmpty()) return null;
        MyWifiInfo networkInfo = getMyWifiInfo(ssid, networks);
        return  networkInfo;
    }

    @NonNull
    private static Set<MyWifiInfo> getMyWifiInfos(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = context.getSharedPreferences(WPA_SUPPLICANT_CONF_FILE, Context.MODE_PRIVATE);
        String networkstr = sp.getString(NETWORK, null);
        Set<MyWifiInfo> networks = new HashSet<>();
        if (!TextUtils.isEmpty(networkstr)) {
            HashSet<MyWifiInfo> network = gson.fromJson(networkstr, new TypeToken<HashSet<MyWifiInfo>>() {
            }.getType());
            if (network != null && !network.isEmpty()) {
                networks.addAll(network);
            }
        }
        return networks;
    }


    @Nullable
    public static MyWifiInfo tryGetSystemWifiInfo(String ssid) {
        try {
            List<MyWifiInfo> myWifiInfos = tryReadSystemWifiInfo();
            L.d("系统wifi信息:"+myWifiInfos);
            MyWifiInfo networkInfo = getMyWifiInfo(ssid, myWifiInfos);
            return  networkInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private static MyWifiInfo getMyWifiInfo(String ssid, Collection<MyWifiInfo> networks) {
        Iterator<MyWifiInfo> iterator = networks.iterator();
        while (iterator.hasNext()){
            MyWifiInfo networkInfo = iterator.next();
            if(networkInfo.ssid.equals(ssid)){
                return  networkInfo;
            }
        }
        return null;
    }

    public static int getCipherType(ScanResult scResult) {
        String capabilities = scResult.capabilities;
        L.i("capabilities="+capabilities);
        if (!TextUtils.isEmpty(capabilities)) {
            final String capa = capabilities.toUpperCase();
            if (capa.contains("WEP")) {
                return SECURITY_WEP;
            } else if (capa.contains("PSK")) {
                return SECURITY_PSK;
            } else if (capa.contains("EAP")) {
                return SECURITY_EAP;
            } else {
                return SECURITY_NONE;
            }
        }
        return SECURITY_NONE;
    }

    /**
     * These values are matched in string arrays -- changes must be kept in sync
     */
    public static final int SECURITY_UNKOWNE = -1;
    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

    public static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)
                || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    public static int getCipherType(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> list = wifiManager.getScanResults();
        for (ScanResult scResult : list) {

            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
                return getCipherType(scResult);
            }
        }
        return SECURITY_UNKOWNE;
    }

    public static WifiConfiguration createWifiInfoCfg(String SSID, String Password, int securityType) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        if (!(SSID.startsWith("\"") && SSID.endsWith("\""))) {
            config.SSID = "\"" + SSID + "\"";
        }
        if(securityType == SECURITY_NONE) //WIFICIPHER_NOPASS
        {
//              config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//              config.wepTxKeyIndex = 0;
        }
        if(securityType == SECURITY_WEP) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(securityType ==SECURITY_PSK || securityType ==SECURITY_EAP) //WIFICIPHER_WPA
        {
            config.hiddenSSID = true;
            config.preSharedKey = "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }






    public static List<MyWifiInfo> tryReadSystemWifiInfo() throws Exception {
        List<MyWifiInfo> wifiInfos=new ArrayList<>();
        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream
                    .writeBytes("cat /data/misc/wifi/*.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                throw e;
            }
        }


        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString() );
        while (networkMatcher.find() ) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);

            if (ssidMatcher.find() ) {
                MyWifiInfo wifiInfo=new MyWifiInfo();
                wifiInfo.ssid=ssidMatcher.group(1);
                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find() ) {
                    wifiInfo.pwd=pskMatcher.group(1);
                } else {
                    wifiInfo.pwd=null;
                }
                wifiInfos.add(wifiInfo);
            }

        }

        return wifiInfos;
    }

    /**
     * scan timeout of milliseconds
     */
    static final long SCAN_TIMEOUT = 3 * 1000;

    /**
     * scan suc checking interval of milliseconds
     */
    static final long SCAN_CHECK_INTERVAL = 100;

    public static List<ScanResult> scan(Context context)
    {
        WifiManager mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mWifiManager.startScan();
        List<ScanResult> wifiList = null;
        final long timeout = SCAN_TIMEOUT;
        long start = System.currentTimeMillis();
        long runtime;
        do
        {
            try
            {
                Thread.sleep(SCAN_CHECK_INTERVAL);
            }
            catch (InterruptedException e)
            {
                L.w("##scan(): is interrupted, return empty list");
                return Collections.emptyList();
            }

            wifiList = mWifiManager.getScanResults();
            if (wifiList != null)
            {
                List<ScanResult> result = new ArrayList<ScanResult>();
                for (ScanResult scan : wifiList)
                {
                    if (!TextUtils.isEmpty(scan.SSID))
                    {
                        result.add(scan);
                    }
                }
                L.i("##scan(): " + result);
                return result;
            }

            runtime = System.currentTimeMillis() - start;
        } while (runtime < timeout);

        L.w("##scan(): fail, return empty list");
        // if fail, don't return null
        return Collections.emptyList();
    }

}
