package com.kw.top.utils;

import java.util.List;

/**
 * author  ： zy
 * date    ： 2018/6/19
 * des     ：
 */

public class ArrayUtils {

    public static String arrayToStr(List<String> list){
        String url ="";
        for (int i=0; i<list.size(); i++){
            if (list.size()==1){
                url = list.get(i);
                return url;
            }else if (i == list.size()-1){
                url += list.get(i);
            }else {
                url += list.get(i)+",";
            }
        }

        return url;
    }

}
