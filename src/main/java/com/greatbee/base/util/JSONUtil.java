package com.greatbee.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.core.bean.constant.CST;
import com.greatbee.core.bean.constant.JSONSchema;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class JSONUtil {
    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static String Resource_Path = "classpath:";

    public static String Model_Path = "classpath:mokelay/model/";

    public static String Service_Path = "classpath:mokelay/service/";

    public static String App_Path = "classpath:mokelay/app/";

    public static JSONObject readJsonFile(String path, String fileName) {
        try {
            return JSONObject.parseObject(readJsonFileString(path, fileName));
        } catch (Exception e) {
            logger.error("[JSONUtil][readJsonFile] content of this json file is not json schema, file name:" + fileName + ".json");
            return null;
        }
    }

    public static String readJsonFileString(String path, String fileName) {
        String fullFileName = fileName + ".json";
        String jsonStr = "";
        try {
            File jsonFile = ResourceUtils.getFile(path + fullFileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1)
                sb.append((char)ch);
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static List<JSONObject> readJsonFileList(String path) {
        try {
            File dir = ResourceUtils.getFile(path);
            if (!dir.isDirectory())
                return null;
            File[] subFiles = dir.listFiles();
            List<JSONObject> list = new ArrayList<>();
            for (int i = 0; i < subFiles.length; i++) {
                File item = subFiles[i];
                if (item.isFile()) {
                    JSONObject fileContent = readJsonFile(path, item.getName().replace(".json", ""));
                    if (fileContent != null)
                        list.add(fileContent);
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static JSONObject readJsonFileFromDir(String dirPath, String fileName) {
        try {
            return JSONObject.parseObject(readJsonFileStringFromDir(dirPath, fileName));
        } catch (Exception e) {
            logger.error("[JSONUtil][readJsonFile] content of this json file is not json schema, file name:" + fileName + ".json");
            return null;
        }
    }

    public static String readJsonFileStringFromDir(String dirPath, String fileName) {
        String jsonStr = "";
        try {
            File dir = ResourceUtils.getFile(dirPath);
            String realFilePath = "";
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    boolean isBreak = false;
                    if (files[i].isDirectory()) {
                        File[] subFiles = files[i].listFiles();
                        for (int j = 0; j < subFiles.length; j++) {
                            String fullFileName = subFiles[j].getName();
                            if (fullFileName.equalsIgnoreCase(fileName + ".json")) {
                                realFilePath = dirPath + files[i].getName() + "/";
                                isBreak = true;
                                break;
                            }
                        }
                    }
                    if (isBreak)
                        break;
                }
            }
            if (StringUtil.isValid(realFilePath))
                jsonStr = readJsonFileString(realFilePath, fileName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return jsonStr;
    }

    private static String goJsonOrDB(String module, String alias) {
        logger.info("json or db, module:" + module + ", alias: " + alias);
        JSONObject data = null;
        if (StringUtil.isInvalid(module))
            return CST.DB.getType();
        JSONObject mokelayJson = readJsonFile(Resource_Path, JSONSchema.Mokelay_Config_File_Name);
        logger.info("mokelay json : " + JSONObject.toJSONString(mokelayJson));
        String env = Global.getInstance().getEnv();
        logger.info("env:" + env);
        logger.info("env22:" + env);
        if (!mokelayJson.containsKey(env)) {
            logger.info("mokelay.json no config of " + env);
            return CST.DB.getType();
        }
        JSONObject mokelay = mokelayJson.getJSONObject(env);
        if (mokelay.containsKey(module))
            data = mokelay.getJSONObject(module);
        if (!data.containsKey(JSONSchema.JSON_Config_Type) || StringUtil.isInvalid(data.getString(JSONSchema.JSON_Config_Type)))
            return CST.DB.getType();
        String type = data.getString(JSONSchema.JSON_Config_Type);
        if (StringUtil.equalsIgnoreCase(type, CST.JSON.getType()))
            return CST.JSON.getType();
        if (StringUtil.equalsIgnoreCase(type, CST.DB.getType()))
            return CST.DB.getType();
        if (StringUtil.equalsIgnoreCase(type, CST.JSON_DB.getType())) {
            if (data.containsKey(JSONSchema.JSON_Config_Json) && data.getJSONArray(JSONSchema.JSON_Config_Json).indexOf(alias) >= 0)
                return CST.JSON.getType();
            return CST.DB.getType();
        }
        if (StringUtil.equalsIgnoreCase(type, CST.DB_JSON.getType())) {
            if (data.containsKey(JSONSchema.JSON_Config_Db) && data.getJSONArray(JSONSchema.JSON_Config_Db).indexOf(alias) >= 0)
                return CST.DB.getType();
            return CST.JSON.getType();
        }
        return CST.DB.getType();
    }

    public static String goJsonOrDBModel(String dsAlias) {
        return goJsonOrDB(JSONSchema.JSON_Config_Model, dsAlias);
    }

    public static String goJsonOrDBService(String apiType) {
        return goJsonOrDB(JSONSchema.JSON_Config_Service, apiType);
    }

    public static String goJsonOrDBApp(String appAlias) {
        return goJsonOrDB(JSONSchema.JSON_Config_App, appAlias);
    }

    public static JSONObject getRootObjByAlias(String type, String alias) {
        if (StringUtil.isInvalid(type))
            return null;
        try {
            List<JSONObject> list;
            int i;
            List<JSONObject> modelList;
            int j;
            JSONObject obj;
            File dirs;
            File[] ats;
            int k;
            JSONObject appObj;
            File appDirs;
            File[] as;
            int m;
            switch (type) {
                case "oi":
                    list = readJsonFileList(Model_Path);
                    for (i = 0; i < list.size(); i++) {
                        JSONObject dsObj = list.get(i);
                        if (dsObj != null) {
                            JSONArray ois = dsObj.getJSONArray(JSONSchema.JSON_Field_OIS);
                            for (int n = 0; n < ois.size(); n++) {
                                JSONObject oiObj = ois.getJSONObject(n);
                                if (oiObj.containsKey(JSONSchema.JSON_Field_Alias) &&
                                        StringUtil.equals(alias, oiObj.getString(JSONSchema.JSON_Field_Alias)))
                                    return dsObj;
                            }
                        }
                    }
                    return null;
                case "connector":
                    modelList = readJsonFileList(Model_Path);
                    for (j = 0; j < modelList.size(); j++) {
                        JSONObject dsObj = modelList.get(j);
                        if (dsObj != null) {
                            JSONArray ois = dsObj.getJSONArray(JSONSchema.JSON_Field_OIS);
                            for (int n = 0; n < ois.size(); n++) {
                                JSONObject oiObj = ois.getJSONObject(n);
                                if (oiObj.containsKey(JSONSchema.JSON_Field_Connectors) && oiObj.getJSONArray(JSONSchema.JSON_Field_Connectors).size() > 0) {
                                    JSONArray connectors = oiObj.getJSONArray(JSONSchema.JSON_Field_Connectors);
                                    for (int i1 = 0; i1 < connectors.size(); i1++) {
                                        JSONObject connObj = connectors.getJSONObject(i1);
                                        if (connObj.containsKey(JSONSchema.JSON_Field_Alias) && StringUtil.equals(alias, connObj.getString(JSONSchema.JSON_Field_Alias)))
                                            return dsObj;
                                    }
                                }
                            }
                        }
                    }
                    return null;
                case "api":
                    obj = new JSONObject();
                    dirs = null;
                    dirs = ResourceUtils.getFile(Service_Path);
                    ats = dirs.listFiles();
                    for (k = 0; k < ats.length; k++) {
                        File item = ats[k];
                        if (!item.isFile()) {
                            File[] apiFiles = item.listFiles();
                            for (int n = 0; n < apiFiles.length; n++) {
                                File apiFile = apiFiles[n];
                                if (StringUtil.equals(apiFile.getName(), alias + ".json")) {
                                    obj.put(JSONSchema.JSON_Field_Alias, item.getName().split(".")[0]);
                                    return obj;
                                }
                            }
                        }
                    }
                    return null;
                case "page":
                    appObj = new JSONObject();
                    appDirs = ResourceUtils.getFile(App_Path);
                    as = appDirs.listFiles();
                    for (m = 0; m < as.length; m++) {
                        File item = as[m];
                        if (!item.isFile()) {
                            File[] pageFiles = item.listFiles();
                            for (int n = 0; n < pageFiles.length; n++) {
                                File pageFile = pageFiles[n];
                                if (StringUtil.equals(pageFile.getName(), alias + ".json")) {
                                    appObj.put(JSONSchema.JSON_Field_Alias, item.getName().split(".")[0]);
                                    return appObj;
                                }
                            }
                        }
                    }
                    return null;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static JSONObject camelJsonName(JSONObject data, JSONObject extraRules) {
        if (data == null)
            return data;
        JSONObject DefaultRules = new JSONObject();
        DefaultRules.put("f_group", "group");
        DefaultRules.put("tddl_appname", "tddlAppName");
        DefaultRules.put("from_oi_alias", "fromOIAlias");
        DefaultRules.put("to_oi_alias", "toOIAlias");
        DefaultRules.put("from_oi_fields", "fromOIFields");
        DefaultRules.put("support_dst", "supportDST");
        DefaultRules.put("execute_bb_method_name", "executeBBMethodName");
        DefaultRules.put("judge_api_alias", "judgeAPIAlias");
        JSONObject mergeRules = new JSONObject();
        mergeRules.putAll((Map)DefaultRules);
        if (extraRules != null && !extraRules.isEmpty())
            mergeRules.putAll((Map)extraRules);
        return loopCamelJson(data, mergeRules);
    }

    private static JSONObject loopCamelJson(JSONObject data, JSONObject rules) {
        Set<String> keys = data.keySet();
        JSONObject newObj = new JSONObject();
        for (String key : keys) {
            Object item = data.get(key);
            Object newSubObj = null;
            if (item != null && item instanceof JSONObject) {
                newSubObj = loopCamelJson((JSONObject)item, rules);
            } else if (item != null && item instanceof JSONArray) {
                JSONArray list = (JSONArray)item;
                JSONArray newList = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject item2 = list.getJSONObject(i);
                    if (item2 != null)
                        newList.add(loopCamelJson(item2, rules));
                }
                newSubObj = newList;
            }
            newObj.put(camelName(key, rules), (newSubObj != null) ? newSubObj : item);
        }
        return newObj;
    }

    public static String camelName(String name, JSONObject rules) {
        if (rules != null && rules.containsKey(name))
            return rules.getString(name);
        StringBuilder result = new StringBuilder();
        if (name == null || name.isEmpty())
            return "";
        if (!name.contains("_"))
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        String[] camels = name.split("_");
        for (String camel : camels) {
            if (!camel.isEmpty())
                if (result.length() == 0) {
                    result.append(camel.toLowerCase());
                } else {
                    result.append(camel.substring(0, 1).toUpperCase());
                    result.append(camel.substring(1).toLowerCase());
                }
        }
        return result.toString();
    }
}
