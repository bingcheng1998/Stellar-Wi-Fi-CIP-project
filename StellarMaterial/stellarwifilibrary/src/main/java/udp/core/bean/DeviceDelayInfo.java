package udp.core.bean;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 22:30
 *          类说明
 */
public class DeviceDelayInfo {
    int birVal; //0~100
    int changetime;//ms
    long delay;//范围为 0~0xFFFFFFFF,以 s 单位。0-立即调光，0xFFFFFFFF-取消延迟调光

    public int getBirVal() {
        return birVal;
    }

    public void setBirVal(int birVal) {
        this.birVal = birVal;
    }

    public int getChangetime() {
        return changetime;
    }

    public void setChangetime(int changetime) {
        this.changetime = changetime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
