package com.sansi.stellarWiFi.util;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/29 13:34
 *          类说明
 */
public class FftDataConverter {
    int[]  mData = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int convert(byte[] fft){
        int sum = 0;//This is a parameter for brightness of the light
        for (int i = 0; i < 32; i++) {

            //calculate height of bar based on sampling 4 data points
            byte rfk = fft[ (10 * i)];
            byte ifk = fft[ (10 * i + 1)];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));
            rfk = fft[ (10 * i + 2)];
            ifk = fft[ (10 * i + 3)];
            magnitude = (rfk * rfk + ifk * ifk);
            dbValue = (int) ( (10 * Math.log10(magnitude)) + dbValue) / 2;

            //Average with previous bars value(reduce spikes / smoother transitions)
            dbValue =( mData[i] +  ((dbValue < 0) ? 0 : dbValue) ) / 2;
            mData[i] = dbValue;

            //only jump height on multiples of 5
            if(dbValue >= 5) {
                dbValue = (int) Math.floor(dbValue/5) * 5;
            }

            //bottom edge of canvas
            int blockHeight = 5;
            int numBlocks = (int) Math.floor((dbValue * 18) / blockHeight);
            if(numBlocks>30) {
                sum+=numBlocks;
            }
        }
        int rate = sum;
        if (rate > 900) {
            rate = 900;
        } else if (100 <= rate && rate < 320) {
            rate = 320;
        } else if (rate < 100) {
            rate = 300;
        }
        int brightness = rate <= 800 ? (rate - 300) * 4 / 50 : (rate - 800) * 6 / 20 + 40;
        return  brightness;
    }
}
