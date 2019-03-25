package com.greatbee.base.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * JSON 文件 工具类
 *
 * @Author: haonan
 * @Description
 * @Date: 2:24 PM 2019/3/25
 */
public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static String Model_Path = "classpath:mokelay/model/";
    public static String Service_Path = "classpath:mokelay/service/";
    public static String App_Path = "classpath:mokelay/app/";


    /**
     * 判断json目录中是否有apiAlias的json文件,有则返回文件内容
     *
     * @param path     文件路径，比如: "classpath:api/"
     * @param fileName
     * @return
     */
    public static JSONObject readJsonFile(String path, String fileName) {
        try {
            return JSONObject.parseObject(readJsonFileString(path, fileName));
        } catch (Exception e) {
            logger.error("[JSONUtil][readJsonFile] content of this json file is not json schema, file name:" + fileName + ".json");
        }
        return null;
    }

    /**
     * 判断json目录中是否有apiAlias的json文件,有则返回文件内容
     *
     * @param path     文件路径，比如: "classpath:api/"
     * @param fileName
     * @return
     */
    public static String readJsonFileString(String path, String fileName) {
        String fullFileName = fileName + ".json";
        String jsonStr = "";
        try {
            //所有的接口json文件都放在api目录下面
            File jsonFile = ResourceUtils.getFile(path + fullFileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从某个目录 查找指定json文件
     *
     * @param dirPath
     * @param fileName
     * @return
     */
    public static JSONObject readJsonFileFromDir(String dirPath, String fileName) {
        try {
            return JSONObject.parseObject(readJsonFileStringFromDir(dirPath, fileName));
        } catch (Exception e) {
            logger.error("[JSONUtil][readJsonFile] content of this json file is not json schema, file name:" + fileName + ".json");
        }
        return null;
    }

    /**
     * 从某个文件目录下面找到对应文件名的json文件,中间的api_type 或者 app
     *
     * @param dirPath
     * @param fileName
     * @return jsonStr
     */
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
                                //找到了要找的文件
                                realFilePath = dirPath + files[i] + "/";
                                isBreak = true;
                                break;
                            }
                        }
                    }
                    if (isBreak) {
                        break;
                    }
                }
            }
            if (StringUtil.isValid(realFilePath)) {
                jsonStr = readJsonFileString(realFilePath, fileName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return jsonStr;
    }

    //测试
//    public static void main(String[] args) {
//        try {
//            File modelDir = ResourceUtils.getFile(App_Path);
//            if (modelDir.isDirectory()) {
//                File[] files = modelDir.listFiles();
//                for (int i = 0; i < files.length; i++) {
//                    boolean isBreak = false;
//                    if (files[i].isDirectory()) {
//                        File[] subFiles = files[i].listFiles();
//                        for (int j = 0; j < subFiles.length; j++) {
//                            String fileName = subFiles[j].getName();
//                            if (fileName.equalsIgnoreCase("test.json")) {
//                                System.out.println("fileName :" + fileName);
//                                isBreak = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (isBreak) {
//                        break;
//                    }
//                }
//            }
//
//            String test = readJsonFileString("classpath:", "mokelay");
//            System.out.println("test:" + test);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


}
