package com.greatbee.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.core.bean.constant.JSONSchema;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Global {
    private static final Logger logger = LoggerFactory.getLogger(Global.class);

    public static final String SingleApiJson = "single_api_json";

    public static final String FindDsTypeConnectorAlias = "connector_alias";

    public static final String FindDsTypeConnectorFoi = "connector_foi";

    public static final String FindDsTypeOiAlias = "oi_alias";

    private static Global global;

    private String env = "daily";

    private String mode = "";

    private Map<String, String> routerMap = new HashMap<>();

    private Map<String, JSONObject> apiMap = new HashMap<>();

    private Map<String, JSONObject> dsMap = new HashMap<>();

    public static synchronized Global getInstance() {
        if (global == null)
            global = new Global();
        return global;
    }

    public void setEnv(String value) {
        this.env = value;
    }

    public String getEnv() {
        return this.env;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, JSONObject> getApiMap() {
        return this.apiMap;
    }

    public JSONObject getApi(String apiAlias) {
        return this.apiMap.get(apiAlias);
    }

    public void setApi(String apiAlias, JSONObject value) {
        this.apiMap.put(apiAlias, value);
    }

    public Map<String, JSONObject> getDsMap() {
        return this.dsMap;
    }

    public JSONObject getDS(String dsAlias) {
        return this.dsMap.get(dsAlias);
    }

    public void setDs(String dsAlias, JSONObject value) {
        this.dsMap.put(dsAlias, value);
    }

    public String getRouter(String url) {
        return this.routerMap.get(url);
    }

    public void setRouter(String url, String apiAlias) {
        this.routerMap.put(url, apiAlias);
    }

    public void initJson(JSONObject json) throws Exception {
        for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)json.entrySet()) {
            if ("api".equals(entry.getKey())) {
                JSONObject apiJson = (JSONObject)entry.getValue();
                String apiAlias = apiJson.getString("alias");
                JSONObject config = apiJson.getJSONObject("config");
                logger.info("api " + apiAlias + " config: " + apiJson.getJSONObject("config"));
                if (config != null && config.containsKey("url") && !config.getJSONArray("url").isEmpty() && config.getJSONArray("url").get(0) != null) {
                    JSONArray urls = config.getJSONArray("url");
                    for (int i = 0; i < urls.size(); i++) {
                        String url = urls.getString(i);
                        setRouter(url, apiAlias);
                    }
                } else if (config != null && config.containsKey("methodName") && StringUtil.isValid(config.getString("methodName"))) {
                    String hsfMethodName = config.getString("methodName");
                    setRouter(hsfMethodName, apiAlias);
                }
                setApi(apiAlias, apiJson);
                continue;
            }
            setDs(entry.getKey(), (JSONObject)entry.getValue());
        }
    }

    public JSONObject findDs(String type, String fieldValue) {
        Map<String, JSONObject> dsMap = getInstance().getDsMap();
        for (Map.Entry<String, JSONObject> entry : dsMap.entrySet()) {
            JSONObject dsObj = (JSONObject)entry.getValue();
            JSONArray ois = dsObj.getJSONArray(JSONSchema.JSON_Field_OIS);
            if (StringUtil.equalsIgnoreCase(type, "connector_alias")) {
                for (int i = 0; i < ois.size(); i++) {
                    JSONObject oi = ois.getJSONObject(i);
                    if (oi.containsKey(JSONSchema.JSON_Field_Connectors)) {
                        JSONArray connectors = oi.getJSONArray(JSONSchema.JSON_Field_Connectors);
                        for (int j = 0; j < connectors.size(); j++) {
                            JSONObject connector = connectors.getJSONObject(j);
                            if (connector.containsKey(JSONSchema.JSON_Field_Alias) && connector.getString(JSONSchema.JSON_Field_Alias).equals(fieldValue))
                                return dsObj;
                        }
                    }
                }
                continue;
            }
            if (StringUtil.equalsIgnoreCase(type, "connector_foi")) {
                for (int i = 0; i < ois.size(); i++) {
                    JSONObject oi = ois.getJSONObject(i);
                    if (oi.containsKey(JSONSchema.JSON_Field_Connectors)) {
                        JSONArray connectors = oi.getJSONArray(JSONSchema.JSON_Field_Connectors);
                        for (int j = 0; j < connectors.size(); j++) {
                            JSONObject connector = connectors.getJSONObject(j);
                            if (connector.containsKey(JSONSchema.JSON_Field_From_Oi_Alias) && connector.getString(JSONSchema.JSON_Field_From_Oi_Alias).equals(fieldValue))
                                return dsObj;
                        }
                    }
                }
                continue;
            }
            if (StringUtil.equalsIgnoreCase(type, "oi_alias"))
                for (int i = 0; i < ois.size(); i++) {
                    JSONObject oi = ois.getJSONObject(i);
                    if (StringUtil.equals(oi.getString(JSONSchema.JSON_Field_Alias), fieldValue))
                        return dsObj;
                }
        }
        return null;
    }
}
