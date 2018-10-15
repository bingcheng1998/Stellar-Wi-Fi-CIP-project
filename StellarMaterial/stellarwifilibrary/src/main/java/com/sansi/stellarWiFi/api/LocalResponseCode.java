package com.sansi.stellarWiFi.api;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/8 14:09
 *          类说明
 *          应答码
 */
public enum  LocalResponseCode{
    ok(0x00, "success"), failure(0x01, "failure"), arguments_error(0x02, "arguments error"), command_not_support(0x03, "command not support");
    private final int code;
    private final String msg;
     LocalResponseCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public static LocalResponseCode toResponseCode(int code) {
        switch (code) {
            case 0x00:
                return ok;
            case 0x01:
                return failure;
            case 0x02:
                return arguments_error;
            case 0x03:
                return command_not_support;
        }
        return null;

    }
}
