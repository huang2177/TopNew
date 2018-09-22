package com.kw.top.tools;

import java.util.ArrayList;

/**
 * author: Administrator
 * data  : 2018/4/22
 * des   : 一些需要初始化的数据
 */

public class DataTools {

    /**
     * 获得性别
     * @return
     */
    public static ArrayList<String> getSex(){
        ArrayList<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        return list;
    }

    /**
     * 获得身高列表
     * @return
     */
    public static ArrayList<String> getHeight(){
        ArrayList<String> list = new ArrayList<>();
        for (int i=140; i<=250; i++){
            list.add(i+"");
        }
        return list;
    }

    /**
     * 获得年收入
     * @return
     */
    public static ArrayList<String> getIncome(){
        ArrayList<String> list = new ArrayList<>();
        list.add("100万以下");
        list.add("100万-500万");
        list.add("500万-1000万");
        list.add("1000万-3000万");
        list.add("3000万-5000万");
        list.add("5000万-1亿");
        list.add("1亿以上");
        return list;
    }

    /**
     * 获得总资产
     * @return
     */
    public static ArrayList<String> getTreasure(){
        ArrayList<String> list = new ArrayList<>();
        list.add("50万-100万");
        list.add("100万-500万");
        list.add("500万-1000万");
        list.add("1000万-3000万");
        list.add("3000万-5000万");
        list.add("5000万-1亿");
        list.add("1亿-5亿");
        list.add("5亿以上");
        return list;
    }

    /**
     * 获得生活品质
     * @return
     */
    public static ArrayList<String> getLife(){
        ArrayList<String> list = new ArrayList<>();
        list.add("简约生活");
        list.add("浪漫情调");
        list.add("轻微奢侈");
        list.add("高度奢侈");
        return list;
    }

    /**
     * 获得抽烟习惯
     * @return
     */
    public static ArrayList<String> getSmoking(){
        ArrayList<String> list = new ArrayList<>();
        list.add("从不吸烟");
        list.add("偶尔一根");
        list.add("戒烟中");
        list.add("老烟枪");
        return list;
    }

    public static ArrayList<String> getDrink(){
        ArrayList<String> list = new ArrayList<>();
        list.add("滴酒不沾");
        list.add("小酌一杯");
        list.add("社交应酬");
        list.add("品酒达人");
        list.add("千杯不醉");
        return list;
    }


}
