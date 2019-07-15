package org.nuaa.tomax.harcs.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ToMax
 * @Description: 类型转化工具类
 * @Date: Created in 2018/2/19 10:17
 */
public class CastUtil {
    /**
     * object转字符串(含默认值)
     * @param o
     * @param defaultValue
     * @return
     */
    public static String o2String(Object o, String defaultValue){
        return o != null ? String.valueOf(o) : defaultValue;
    }

    /**
     * object转字符串
     * @param o
     * @return
     */
    public static String o2String(Object o){
        return o2String(o,"");
    }

    /**
     * object转int(含默认值)
     * @param o
     * @param defaultValue
     * @return
     */
    public static int o2Int(Object o, int defaultValue){
        int value = defaultValue;
        if (o != null){
            try {
                value = Integer.parseInt(o2String(o));
            }catch (NumberFormatException e){
                value = defaultValue;
                System.out.println(e);
                System.out.println("o2Int Error");
            }
        }
        return value;
    }

    /**
     * object转int
     * @param o
     * @return
     */
    public static int o2Int(Object o){
        return o2Int(o,0);
    }

    /**
     * object转double(含默认值)
     * @param o
     * @param defaultValue
     * @return
     */
    public static double o2Double(Object o, double defaultValue){
        double value = defaultValue;
        if (o != null){
            try {
                value = Double.parseDouble(o2String(o));
            }catch (NumberFormatException e){
                value = defaultValue;
                System.out.println(e);
                System.out.println("o2Double Error");
            }
        }
        return value;
    }

    /**
     * object转double
     * @param o
     * @return
     */
    public static double o2Double(Object o){
        return o2Double(o,0d);
    }

    /**
     * object转long(含默认值)
     * @param o
     * @param defaultValue
     * @return
     */
    public static long o2Long(Object o, long defaultValue){
        long value = defaultValue;
        if (o != null){
            try {
                value = Long.parseLong(o2String(o));
            }catch (NumberFormatException e){
                value = defaultValue;
                System.out.println(e);
                System.out.println("o2Long Error");
            }
        }
        return value;
    }

    /**
     * object转long
     * @param o
     * @return
     */
    public static long o2Long(Object o){
        return o2Long(o,0);
    }

    /**
     * 数组转化为list
     * @param tArray
     * @param <T>
     * @return
     */
    public static <T> List<T> array2List(T [] tArray){
        if (tArray != null) {
            List<T> tList = new ArrayList<>(tArray.length);
            for (T in : tArray){
                tList.add(in);
            }
            return tList;
        }else {
            return new ArrayList<T>(1);
        }
    }
}
