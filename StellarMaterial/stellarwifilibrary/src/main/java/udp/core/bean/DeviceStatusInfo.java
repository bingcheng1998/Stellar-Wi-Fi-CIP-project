package udp.core.bean;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 16:20
 *          类说明
 */
public class DeviceStatusInfo {
    int bir;
    int rgbw;
    int cctVal;
    int modeId;
    int modeChangeTime;
    int fault;
    String name;

    public int getBir() {
        return bir;
    }

    public void setBir(int bir) {
        this.bir = bir;
    }

    public int getRgbw() {
        return rgbw;
    }

    public void setRgbw(int rgbw) {
        this.rgbw = rgbw;
    }

    public int getCctVal() {
        return cctVal;
    }

    public void setCctVal(int cctVal) {
        this.cctVal = cctVal;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public int getModeChangeTime() {
        return modeChangeTime;
    }

    public void setModeChangeTime(int modeChangeTime) {
        this.modeChangeTime = modeChangeTime;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
