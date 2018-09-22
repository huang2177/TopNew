package com.kw.top.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.kw.top.R;
import com.kw.top.base.BaseActivity;
import com.kw.top.tools.ConstantValue;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * author: Administrator
 * data  : 2018/4/22
 * des   : 通用的条件选择器
 */

public class CommPickerViewActivity extends BaseActivity {

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private String optionGrade = ConstantValue.GRADE_1;//选择器等级，默认为1
    private ArrayList<String> options1Items = new ArrayList<>();
    private String title;
    private OptionsPickerView pvOptions;

    public static void toAtivity(Activity context, int result_code, String title, ArrayList<String> datas) {
        Intent intent = new Intent(context, CommPickerViewActivity.class);
//        intent.putExtra(ConstantValue.JUMP_PICKER_VIEW, optionGrade);
        intent.putExtra("TITLE", title);
        intent.putStringArrayListExtra("DATAS", datas);
        context.startActivityForResult(intent, result_code);
        ((Activity) context).overridePendingTransition(R.anim.activity_alpha_in, 0);
    }

    private void showPickerView() {
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);

                Intent intent = new Intent();
                intent.putExtra("RESULT", tx);
                setResult(RESULT_OK, intent);
                CommPickerViewActivity.this.finish();

            }
        })

                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        if (optionGrade.equals(ConstantValue.GRADE_1)) {
            pvOptions.setPicker(options1Items);
        }

        pvOptions.show();

        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
//                pvOptions.dismiss();
                CommPickerViewActivity.this.finish();
            }
        });
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
    public int getContentView() {
        return R.layout.activity_picker_view;
    }

    public void initData() {
        title = getIntent().getStringExtra("TITLE");
        options1Items = getIntent().getStringArrayListExtra("DATAS");
        showPickerView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        if (null != pvOptions)
//            pvOptions.dismiss();
        CommPickerViewActivity.this.overridePendingTransition(0, R.anim.activity_alpha_out);
    }
}
