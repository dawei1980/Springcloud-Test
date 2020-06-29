package com.springcloud.login.utils;

import java.util.UUID;

public class CommonUtil {
    /**
     * 获取36位uuid
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
