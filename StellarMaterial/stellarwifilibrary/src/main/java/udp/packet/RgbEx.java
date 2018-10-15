package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Kits;
import udp.core.Utils;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 15:39
 *          类说明
 */
public class RgbEx {
    public byte mode;
    public short time;
    public int []content;//rgbw 整形每8位表示 r g b w

    public  byte[]toBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(3 + content.length * 4);
        buffer.put(mode).putShort(time);
        for(int i=0;i<content.length;i++){
            buffer.putInt(content[i]);
        }
        return (byte[]) buffer.flip().array();
    }

    @Override
    public String toString() {
        ByteBuffer buffer=ByteBuffer.allocate(content.length*4);
        for(int i=0;i<content.length;i++){
            buffer.putInt(content[i]);
        }
        String contents= Utils.byte2hex((byte[]) buffer.flip().array());
        StringBuilder sb=new StringBuilder();
        sb.append("mode=").append(Kits.bytetoHexString(mode))
                .append(",time=").append(Utils.shortToHexString(time))
                .append(",content=").append(contents);
        return sb.toString();
    }
}
