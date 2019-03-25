package com.greatbee.core.bean.constant;

/**
 * CST(Config Source Type 配置数据来源)
 * DB 从db获取model、service、app数据
 * JSON 从json文件获取
 * JSON_DB 从json文件获取指定数据，剩下从DB获取
 * DB_JSON 从DB获取指定数据，剩下从JSON文件获取
 * Created by Xiaobc on 19/03/25.
 */
public enum CST {
    DB("DB"),
    JSON("JSON"),
    JSON_DB("JSON_DB"),
    DB_JSON("DB_JSON");

    private String type;

    CST(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CST getCST(String type) {
        CST[] l = CST.values();
        for (CST v : l) {
            if (v.getType().equalsIgnoreCase(type)) {
                return v;
            }
        }
        return DB;
    }
}
