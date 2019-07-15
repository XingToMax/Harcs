package org.nuaa.tomax.harcs.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/7/14 15:21
 */
public class ConfigUtil {
    /**
     * 加载配置文件
     * @param fileName 文件名称
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null){
                throw new FileNotFoundException(fileName+" file is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            System.out.println(e+" 配置文件"+fileName+"加载失败");
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    /**
     * 根据key获取字符串类型的配置项
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props, String key){
        return getString(props, key, "");
    }

    /**
     * 获取字符串型的配置值
     * @param props 加载好的配置文件
     * @param key
     * @param defaultValue key值为空时的默认值
     * @return
     */
    public static String getString(Properties props, String key, String defaultValue){
        String value = defaultValue;
        if (props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取key值对应的int型配置值，含默认值
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties props, String key, int defaultValue){
        int value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.o2Int(props.getProperty(key),defaultValue);
        }
        return value;
    }

    /**
     * 获取key值对应int型的配置值，含默认值
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props, String key){
        return getInt(props,key,0);
    }

    /**
     * 获取key值对应的double型配置值，含默认值
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getDouble(Properties props, String key, double defaultValue){
        double value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.o2Double(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取key值对应的double型配置值
     * @param props
     * @param key
     * @return
     */
    public static double getDouble(Properties props, String key){
        return getDouble(props,key,0);
    }

    /**
     * 获取key值对应的long型配置值,含默认值
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Properties props, String key, long defaultValue){
        long value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.o2Long(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取key值对应的long型配置值
     * @param props
     * @param key
     * @return
     */
    public static long getLong(Properties props, String key){
        return getLong(props,key,0);
    }

}
