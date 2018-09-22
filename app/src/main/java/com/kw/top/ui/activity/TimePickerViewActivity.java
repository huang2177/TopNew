package com.kw.top.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.PrvCityBean;
import com.kw.top.tools.CommandTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.RxToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 2018/1/18.
 * mail: 623932889@qq.com
 * Describe:
 */

public class TimePickerViewActivity extends BaseActivity {

    private ArrayList<PrvCityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private String optionGrade = ConstantValue.GRADE_3;//选择器等级，默认为3


    public static void toAtivity(Context context, String optionGrade) {
        Intent intent = new Intent(context, TimePickerViewActivity.class);
        intent.putExtra(ConstantValue.JUMP_PICKER_VIEW, optionGrade);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_bottom_open, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_view);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        optionGrade = getIntent().getExtras().getString(ConstantValue.JUMP_PICKER_VIEW, ConstantValue.GRADE_3);

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    RxToast.normal("Parse Succeed");
                    ShowPickerView();
                    break;

                case MSG_LOAD_FAILED:
                    RxToast.normal("解析失败");
                    finish();
                    TimePickerViewActivity.this.overridePendingTransition(0, R.anim.activity_alpha_out);
                    break;

            }
        }
    };

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx;
                if (optionGrade.equals(ConstantValue.GRADE_2)) {
                    tx = options2Items.get(options1).get(options2);
                } else {
                    tx = options1Items.get(options1).getPickerViewText() + " " +
                            options2Items.get(options1).get(options2) + " " +
                            options3Items.get(options1).get(options2).get(options3);
                }


//                Toast.makeText(TimePickerViewActivity.this, tx, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("PLACE", tx);
                setResult(RESULT_OK, intent);
                TimePickerViewActivity.this.finish();
                TimePickerViewActivity.this.overridePendingTransition(0, R.anim.activity_alpha_out);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        if (optionGrade.equals(ConstantValue.GRADE_1)) {
            pvOptions.setPicker(options1Items);
        } else if (optionGrade.equals(ConstantValue.GRADE_2)) {
            pvOptions.setPicker(options1Items, options2Items);
        } else {
            pvOptions.setPicker(options1Items, options2Items, options3Items);
        }
        pvOptions.show();

        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                TimePickerViewActivity.this.finish();
            }
        });
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String cityJson = CommandTools.getJson(this, "city.json");//获取assets目录下的json文件数据

        ArrayList<PrvCityBean> list = null;
        try {
            list = new Gson().fromJson(cityJson, new TypeToken<List<PrvCityBean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            RxToast.error("解析出错了o(╥﹏╥)o");
        }

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = list;

        for (int i = 0; i < list.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < list.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String CityName = list.get(i).getCity().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (list.get(i).getCity().get(c).getArea() == null
                        || list.get(i).getCity().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < list.get(i).getCity().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = list.get(i).getCity().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        if (options3Items.size() > 0)
            mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    /**
     * 点击系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            this.overridePendingTransition(0, R.anim.activity_alpha_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(MSG_LOAD_DATA);
            mHandler.removeMessages(MSG_LOAD_SUCCESS);
            mHandler.removeMessages(MSG_LOAD_FAILED);
        }
    }

    @Override
    public int getContentView() {
        return 0;
    }


}
