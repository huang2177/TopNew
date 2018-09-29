package com.kw.top.ui.activity.person_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kw.top.R;
import com.kw.top.base.MVPBaseActivity;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.InfoBean;
import com.kw.top.bean.event.UserAvatarEvent;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.ConstantValue;
import com.kw.top.ui.activity.login.BaseInfoActivity;
import com.kw.top.ui.activity.login.LoginActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: 正义
 * date  : 2018/4/17
 * desc  :
 */

public class EditInfoActivity extends MVPBaseActivity<PersonInfoContract.View, PersonInfoPresenter> implements PersonInfoContract.View,
        View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.relative_title)
    RelativeLayout mRelativeTitle;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout mRlSex;
    @BindView(R.id.et_profession)
    EditText mEtProfession;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout mRlBirthday;
    @BindView(R.id.et_education)
    EditText mEtEducation;
    @BindView(R.id.tv_height)
    TextView mTvHeight;
    @BindView(R.id.rl_height)
    RelativeLayout mRlHeight;
    @BindView(R.id.tv_income)
    TextView mTvIncome;
    @BindView(R.id.rl_income)
    RelativeLayout mRlIncome;
    @BindView(R.id.tv_treasure)
    TextView mTvTreasure;
    @BindView(R.id.rl_treasure)
    RelativeLayout mRlTreasure;
    @BindView(R.id.tv_life)
    TextView mTvLife;
    @BindView(R.id.rl_life)
    RelativeLayout mRlLife;
    @BindView(R.id.tv_smoking)
    TextView mTvSmoking;
    @BindView(R.id.rl_smoking)
    RelativeLayout mRlSmoking;
    @BindView(R.id.tv_drink)
    TextView mTvDrink;
    @BindView(R.id.rl_drink)
    RelativeLayout mRlDrink;
    @BindView(R.id.et_objective)
    EditText mEtObjective;
    @BindView(R.id.ll_objective)
    RelativeLayout mLlObjective;
    @BindView(R.id.ll_nickname)
    RelativeLayout mLlNickname;
    @BindView(R.id.view_lin)
    View mViewLin;
    private boolean edit = false;
    private ArrayList<String> mInfoList = new ArrayList<>();
    private static int REQUEST_CODE_HEIGHT = 2, REQUEST_CODE_INCOME = 3, REQUEST_CODE_TREASURE = 4,
            REQUEST_CODE_LIFE = 5, REQUEST_CODE_SMOKING = 6, REQUEST_CODE_DRINK = 7;
    private String nickname, profession, birthday, education, height, income, treasure,
            life, smoking, drink, objective;

    public static void startActivity(Context context, boolean edit) {
        Intent intent = new Intent(context, EditInfoActivity.class);
        intent.putExtra("EDIT", edit);
        context.startActivity(intent);
    }

    public static void startActivityForesult(Activity context, boolean edit, int code) {
        Intent intent = new Intent(context, EditInfoActivity.class);
        intent.putExtra("EDIT", edit);
        context.startActivityForResult(intent,code);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_person_info;
    }

    public void initView() {
        edit = getIntent().getBooleanExtra("EDIT", false);
        nickname = "";
        if (edit) {
            mTvTitle.setText("编辑资料");
            mTvTitleRight.setText("保存");
        } else {
            mTvTitle.setText("填写资料");
            mTvTitleRight.setText("下一步");
            mLlNickname.setVisibility(View.GONE);
            mViewLin.setVisibility(View.GONE);

        }
        mRelativeTitle.setBackgroundColor(getResources().getColor(R.color.black_bg));
    }

    public void initData() {
        if (edit) {
            showProgressDialog();
            mPresenter.getInfoData(getToken());
        }
    }

    public void initListener() {
        mTvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = mEtNickname.getText().toString().trim();
//                if (TextUtils.isEmpty(nickname)) {
//                    RxToast.normal("请填写昵称");
//                    return;
//                }
                profession = mEtProfession.getText().toString().trim();
                if (TextUtils.isEmpty(profession)) {
                    RxToast.normal("请填写职业");
                    return;
                }
                if (TextUtils.isEmpty(birthday)) {
                    RxToast.normal("请选择生日");
                    return;
                }
                education = mEtEducation.getText().toString().trim();
                if (TextUtils.isEmpty(education)) {
                    RxToast.normal("请填写学历");
                    return;
                }
                if (TextUtils.isEmpty(height)) {
                    RxToast.normal("请选择身高");
                    return;
                }
                if (TextUtils.isEmpty(income)) {
                    RxToast.normal("请选择年收入");
                    return;
                }
                if (TextUtils.isEmpty(treasure)) {
                    RxToast.normal("请选择总资产");
                    return;
                }
                if (TextUtils.isEmpty(life)) {
                    RxToast.normal("请选择生活品质");
                    return;
                }
                if (TextUtils.isEmpty(life)) {
                    RxToast.normal("请选择生活品质");
                    return;
                }
                if (TextUtils.isEmpty(smoking)) {
                    RxToast.normal("请选择抽烟习惯");
                    return;
                }
                if (TextUtils.isEmpty(drink)) {
                    RxToast.normal("请选择喝酒习惯");
                    return;
                }
                objective = mEtObjective.getText().toString().trim();
                if (TextUtils.isEmpty(objective)) {
                    RxToast.normal("请输入交友目的");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                showProgressDialog();
                map.put("nickName", nickname);
                map.put("job", profession);
                map.put("birthday", birthday);
                map.put("education", education);
                map.put("stature", height);
                map.put("yearIncome", income);
                map.put("totalAssets", treasure);
                map.put("qualityLife", life);
                map.put("smoke", smoking);
                map.put("drink", drink);
                map.put("objective", objective);
                map.put("token", getToken());
                mPresenter.updataInfo(map);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (data.getExtras() == null)
////            return;
//        if (resultCode != RESULT_OK)
//            return;
//        String result = data.getStringExtra("RESULT");
//        switch (requestCode) {
//
//        }
//    }

    private void showTimePicker() {
        new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                birthday = TimeUtils.format(date.getTime(), "yyyy-MM-dd");
                mTvBirthday.setText(birthday);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setSubCalSize(16)
                .setTitleBgColor(Color.WHITE)
                .setTitleText("选择生日")
                .setBgColor(Color.WHITE)
                .setDividerColor(Color.TRANSPARENT)
                .setContentSize(20)
                .build().show();
    }

    @Override
    public void updataResult(BaseBean baseBean) {
        hideProgressDialog();
        if (baseBean == null)return;
        if (null != baseBean && baseBean.isSuccess()) {
            if (edit) {
                //保存
                RxToast.normal("保存成功");
                SPUtils.saveString(this, ConstantValue.KEY_NAME, nickname);
                EventBus.getDefault().post(new UserAvatarEvent(null, nickname));
                setResult(RESULT_OK);
                finish();
            } else {
                //下一步 -->填写基本信息
                startActivity(BaseInfoActivity.class);
                finish();
            }
        } else {
            ComResultTools.resultData(this,baseBean);
        }
    }

    @Override
    public void getDataResult(BaseBean baseBean) {
        hideProgressDialog();
        if (null != baseBean && baseBean.isSuccess()) {
            List<InfoBean.DataBean> dataBeans = null;
            try {
                dataBeans = new Gson().fromJson(baseBean.getJsonData(), new TypeToken<List<InfoBean.DataBean>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (dataBeans == null || dataBeans.size() == 0) return;
            InfoBean.DataBean infoBean = dataBeans.get(0);
            nickname = infoBean.getNickName();
            mEtNickname.setText(nickname);
            profession = infoBean.getJob();
            mEtProfession.setText(profession);
            birthday = infoBean.getBirthday();
            mTvBirthday.setText(birthday);
            education = infoBean.getEducation();
            mEtEducation.setText(education);
            height = infoBean.getStature();
            mTvHeight.setText(height);
            income = infoBean.getYearIncome();
            mTvIncome.setText(income);
            treasure = infoBean.getTotalAssets();
            mTvTreasure.setText(treasure);
            life = infoBean.getQualityLife();
            mTvLife.setText(life);
            smoking = infoBean.getSmoke();
            mTvSmoking.setText(smoking);
            drink = infoBean.getDrink();
            mTvDrink.setText(drink);
            objective = infoBean.getObjective();
            mEtObjective.setText(objective);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @OnClick({R.id.rl_birthday, R.id.rl_height, R.id.rl_income, R.id.rl_treasure, R.id.rl_life, R.id.rl_smoking, R.id.rl_drink, R.id.iv_back})
    public void onClick(View v) {
        mInfoList.clear();
        switch (v.getId()) {
            case R.id.rl_birthday:
                showTimePicker();
                break;
            case R.id.rl_height:
                title = "请选择身高";
                SELECT_ITEM = REQUEST_CODE_HEIGHT;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_HEIGHT));
                showPickerView();
                break;
            case R.id.rl_income:
                title = "请选择年收入";
                SELECT_ITEM = REQUEST_CODE_INCOME;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_INCOME));
                showPickerView();
                break;
            case R.id.rl_treasure:
                title = "请选择总资产";
                SELECT_ITEM = REQUEST_CODE_TREASURE;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_TREASURE));
                showPickerView();
                break;
            case R.id.rl_life:
                title = "请选择生活品质";
                SELECT_ITEM = REQUEST_CODE_LIFE;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_LIFE));
                showPickerView();
                break;
            case R.id.rl_smoking:
                title = "请选择抽烟习惯";
                SELECT_ITEM = REQUEST_CODE_SMOKING;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_SMOKING));
                showPickerView();
                break;
            case R.id.rl_drink:
                title = "请选择饮酒习惯";
                SELECT_ITEM = REQUEST_CODE_DRINK;
                mInfoList.addAll(mPresenter.getInfoList(REQUEST_CODE_DRINK));
                showPickerView();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private ArrayList<String> options1Items = new ArrayList<>();
    private String title;
    private OptionsPickerView pvOptions;
    private String optionGrade = ConstantValue.GRADE_1;//选择器等级，默认为1
    private int SELECT_ITEM;

    private void showPickerView() {
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mInfoList.get(options1);
                switch (SELECT_ITEM) {
                    case 2: //身高
                        mTvHeight.setText(tx);
                        height = tx;
                        break;
                    case 3: //年收入
                        mTvIncome.setText(tx);
                        income = tx;
                        break;
                    case 4: //总资产
                        mTvTreasure.setText(tx);
                        treasure = tx;
                        break;
                    case 5: //生活品质
                        mTvLife.setText(tx);
                        life = tx;
                        break;
                    case 6: //抽烟习惯
                        mTvSmoking.setText(tx);
                        smoking = tx;
                        break;
                    case 7: //喝酒习惯
                        mTvDrink.setText(tx);
                        drink = tx;
                        break;
                }
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
            pvOptions.setPicker(mInfoList);
        }

        pvOptions.show();

        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
            }
        });
    }
}
