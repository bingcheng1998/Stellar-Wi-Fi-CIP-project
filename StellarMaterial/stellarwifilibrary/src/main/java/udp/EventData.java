package udp;

/**
 * Created by uid10964 on 2018/3/2.
 */

final public class EventData {
    final public String rawData;
    final public int type;//0 出去,1//进来

    public EventData(String rawData,int type) {
        this.rawData = rawData;
        this.type=type;
    }
}
