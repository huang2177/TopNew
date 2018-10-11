package com.kw.top.ui.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.kw.top.R;
import com.kw.top.adapter.ImagePickerAdapter;
import com.kw.top.base.BaseActivity_;
import com.kw.top.bean.BaseBean;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.Logger;
import com.kw.top.ui.activity.login.presenter.UpMorePresenter;
import com.kw.top.ui.activity.login.presenter.UpSinglePresenter;
import com.kw.top.ui.activity.login.presenter.RegisterView;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.TimeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class GirlInfoActivity extends BaseActivity_ implements ImagePickerAdapter.OnRecyclerViewItemClickListener, RegisterView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.girl_head)
    CircleImageView hadeImage;
    @BindView(R.id.girl_recylerview)
    RecyclerView recyclerView;
    @BindView(R.id.girl_et_nikename)
    EditText edNikename;
    @BindView(R.id.girl_et_wechat)
    EditText edWechat;
    @BindView(R.id.girl_hight)
    TextView tvHight;
    @BindView(R.id.girl_wight)
    TextView tvWight;
    @BindView(R.id.girl_age)
    TextView tvAge;
    @BindView(R.id.girl_job)
    EditText tvJob;
    @BindView(R.id.girl_aihao)
    EditText edAihao;
    @BindView(R.id.girl_caiyi)
    EditText edCaiyi;
    @BindView(R.id.girl_qianmin)
    EditText edQianmin;


    private String nikeName, wechta, hight, wight, age, aihao, job, caiyi, qianmin, path, headImg;

    private ArrayList<String> mInfoList;
    private String title;
    private OptionsPickerView pvOptions;
    private String optionGrade = ConstantValue.GRADE_1;//选择器等级，默认为1


    private LocalMedia localMedia;
    private ImagePickerAdapter adapter;
    private ArrayList<LocalMedia> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数
    private ArrayList<LocalMedia> images = null;
    public static final int IMAGE_ITEM_ADD = -1;


    private UpSinglePresenter presenter;
    private UpMorePresenter upMorePresenter;
    private Map<String, String> map;
    private StringBuilder stringBuilder;

    @Override
    public int getContentView() {
        return R.layout.activity_girl_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initviews();
    }

    /**
     * 初始化
     */
    private void initviews() {
        tvTitle.setText("填写资料");
        mInfoList = new ArrayList<>();

        initRecyler();
        presenter = new UpSinglePresenter(this, this);
        upMorePresenter = new UpMorePresenter(this, this);
    }


    /**
     * 初始化recylerview
     */
    private void initRecyler() {
        selImageList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @OnClick({R.id.iv_back, R.id.girl_into, R.id.girl_head, R.id.girl_hight, R.id.girl_wight, R.id.girl_age})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.girl_head:
                PictureSelector.create(this)
                        .openGallery(PictureConfig.TYPE_IMAGE)
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .isZoomAnim(true)
                        .enableCrop(true)// 是否裁剪 true or false
                        .withAspectRatio(1, 1)
                        .isDragFrame(true)// 是否可拖动裁剪框(固定)
                        .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格
                        .forResult(ConstantValue.JUMP_RELEASE_IMAGE);
                break;
            case R.id.girl_hight:
                title = "请选择身高";
                mInfoList.clear();
                mInfoList.addAll(getInfoList(1));
                showPickerView(1);
                break;
            case R.id.girl_wight:
                title = "请选择体重";
                mInfoList.clear();
                mInfoList.addAll(getInfoList(2));
                showPickerView(2);
                break;
            case R.id.girl_age:
                title = "请选择年龄";
                mInfoList.clear();
                mInfoList.addAll(getInfoList(3));
                showPickerView(3);
                break;
            case R.id.girl_into:
                initCommit();
                break;
        }
    }

    private void initCommit() {
        nikeName = edNikename.getText().toString().trim();
        wechta = edWechat.getText().toString().trim();
        aihao = edAihao.getText().toString().trim();
        caiyi = edCaiyi.getText().toString().trim();
        qianmin = edQianmin.getText().toString().trim();
        job = tvJob.getText().toString().trim();

        if (TextUtils.isEmpty(headImg)) {
            RxToast.normal("头像不能为空");
            return;
        }
        if (TextUtils.isEmpty(wechta)) {
            RxToast.normal("微信号不能为空");
            return;
        }
        if (selImageList.size() == 0) {
            RxToast.normal("照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(nikeName)) {
            RxToast.normal("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(hight)) {
            RxToast.normal("身高不能为空");
            return;
        }
        if (TextUtils.isEmpty(wight)) {
            RxToast.normal("体重不能为空");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            RxToast.normal("年龄不能为空");
            return;
        }
        if (TextUtils.isEmpty(job)) {
            RxToast.normal("职业不能为空");
            return;
        }
        map = new HashMap<>();
        map.put("weChatNum", wechta);
        map.put("headImg", headImg);
        map.put("pictures", stringBuilder.toString());
        map.put("nickName", nikeName);
        map.put("stature", hight);
        map.put("weight", wight);
        map.put("age", age);
        map.put("job", job);
        map.put("interest", aihao);
        map.put("advantage", caiyi);
        map.put("autograph", qianmin);
        map.put("token", getToken());
        upMorePresenter.addGirlData(map);
    }

    /**
     * 条件选择器
     *
     * @param type
     */
    private void showPickerView(final int type) {
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mInfoList.get(options1);
                switch (type) {
                    case 1: //身高
                        tvHight.setText(tx);
                        hight = tx;
                        break;
                    case 2: //年收入
                        tvWight.setText(tx);
                        wight = tx;
                        break;
                    case 3:
                        tvAge.setText(tx);
                        age = tx;
                }
            }
        }).setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        if (optionGrade.equals(ConstantValue.GRADE_1)) {
            pvOptions.setPicker(mInfoList);
        }
        pvOptions.show();
    }


    private ArrayList<String> getInfoList(int type) {
        ArrayList list = new ArrayList<>();
        list.clear();
        switch (type) {
            case 1:             //获得身高列表
                for (int i = 160; i <= 220; i++) {
                    list.add(i + "");
                }
                break;
            case 2:             //获得体重列表
                for (int i = 40; i <= 60; i++) {
                    list.add(i + "");
                }
                break;
            case 3:             //获得年龄
                for (int i = 18; i <= 40; i++) {
                    list.add(i + "");
                }
                break;
        }
        return list;
    }


    /**
     * 选择图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {     //一张图片
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            if (list.size() > 0) {
                localMedia = list.get(0);
                path = localMedia.getCutPath();
                Glide.with(this).load(localMedia.getCutPath()).into(hadeImage);
                presenter.getQiniuToken(getToken(), path);

            }
        } else if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGES) {  //多张图片
            images = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
            if (images != null) {
                selImageList.addAll(images);
                adapter.setImages(selImageList);
                showProgressDialog();
                upMorePresenter.getQiniuToken(getToken(), images);       //上传图片
            }
        }
    }

    /**
     * 添加图片
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        if (position == IMAGE_ITEM_ADD) {
            PictureSelector.create(this)
                    .openGallery(PictureConfig.TYPE_IMAGE)
                    .maxSelectNum(maxImgCount)
                    .minSelectNum(1)
                    .imageSpanCount(3)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .previewImage(true)
                    .isCamera(true)
                    .isZoomAnim(true)
                    .selectionMedia(images)
                    .forResult(ConstantValue.JUMP_RELEASE_IMAGES);
        }

    }


    /**
     * 上传单张图片的时候
     *
     * @param resKey
     */
    @Override
    public void UpPicSuccsee(String resKey) {
        headImg = resKey;
        Logger.e("---resKey---", resKey);
    }

    /**
     * 上传多张图片
     *
     * @param listkey
     */
    @Override
    public void UpMorePicSuccess(List<String> listkey) {
        hideProgressDialog();
        stringBuilder = new StringBuilder();

        for (int i = 0; i < listkey.size(); i++) {
            stringBuilder.append(listkey.get(i)).append(",");
        }
        Logger.e("----stringBuilder--", stringBuilder.toString());
    }

    /**
     * 上传资料
     *
     * @param baseBean
     */
    @Override
    public void UpGirlInfoSuccess(BaseBean baseBean) {

        RxToast.normal("资料完善成功");
        startActivity(VideoVerifyActivity.class);
    }

    /**
     * 设置男生资料
     *
     * @param baseBean
     */
    @Override
    public void UpSingInfoSuccess(BaseBean baseBean) {

    }
}