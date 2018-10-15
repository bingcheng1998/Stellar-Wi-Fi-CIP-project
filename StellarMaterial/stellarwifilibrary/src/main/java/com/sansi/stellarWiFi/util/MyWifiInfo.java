package com.sansi.stellarWiFi.util;

public  class MyWifiInfo {
         String ssid;
         String pwd;

        public MyWifiInfo() {
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public MyWifiInfo(String ssid, String pwd) {
            this.ssid = ssid;
            this.pwd = pwd;
        }

        @Override
        public String toString() {
            return "MyWifiInfo{" +
                    "ssid='" + ssid + '\'' +
                    ", pwd='" + pwd + '\'' +
                    '}';
        }
    }