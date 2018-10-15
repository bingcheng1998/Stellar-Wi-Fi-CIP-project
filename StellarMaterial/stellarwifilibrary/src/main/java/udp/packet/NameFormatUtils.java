package udp.packet;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/15 19:22
 *          类说明
 */
public class NameFormatUtils {

    public static void checkNameFormat(String name) {
        try {
            byte[] nameBytes = name.getBytes("utf-8");
            if (nameBytes.length > DeviceSetNameReq.LIGHT_NAME_LENGHT)
                throw new IllegalArgumentException("name lenght " + nameBytes.length + " is illegal.");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
