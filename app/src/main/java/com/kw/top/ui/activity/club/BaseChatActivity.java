package com.kw.top.ui.activity.club;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.message.EMAMessage;
import com.kw.top.R;
import com.kw.top.adapter.ChatAdapter;
import com.kw.top.base.BaseActivity;
import com.kw.top.bean.UploadBean;
import com.kw.top.tools.ConstantValue;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.ScreenUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author  ： zy
 * date    ： 2018/6/20
 * des     ：
 */

public abstract class BaseChatActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public EMAMessage.EMAChatType chatType; //聊天类型  单聊/群聊
    public String toChatUsername; //对方用户/群聊 ID
    public int vip = 0;
    public String headUrl, nickname;
    public static final String VIP = "vip", HEAD_URL = "head_url", NICK_NAME = "nick_name",//自定义扩展消息
            MSG_STATUS = "msg_status";//消息发送的状态，-1失败 0发送中 1成功 本地用
    public ChatAdapter mChatAdapter;
    public List<EMMessage> mList = new ArrayList<>();
    protected String cameraPath;
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvTitleRight;
    RelativeLayout mRelativeTitle;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText mEtMsg;
    ImageView mIvAdd;
    TextView mTvSend;
    LinearLayout mLlBottom;
    RelativeLayout mRootView;
    private String outputCameraPath;
    private String recordVideoSecond = 60 + "";//录制秒数
    private String videoQuality = "1"; //录制视频的质量 0/1
    private RxPermissions rxPermissions;
    public static final String IMAGE_PATH = "image_path";
    private List<UploadBean> mUploadBeanList = new ArrayList<>();
    public int bottomStatusHeight = 0;
    public EMConversation conversation; //加载本地历史会话
    private static final int REFRESH_ADAPTER_SUCCESS = 1, REFRESH_ADAPTER_FILED = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = mList.size() - 1;
            EMMessage emMessage = mList.get(position);
            switch (msg.what) {
                case REFRESH_ADAPTER_FILED:
                    Log.e("tag", "======= handler filed ");
                    emMessage.setAttribute(MSG_STATUS, -1);
                    mList.set(position, emMessage);
                    break;
                case REFRESH_ADAPTER_SUCCESS:
                    Log.e("tag", "======= handler success ");
                    emMessage.setAttribute(MSG_STATUS, 0);
                    mList.set(position, emMessage);
                    break;
            }
            mChatAdapter.notifyItemChanged(position, "refresh");
//            conversation.appendMessage(emMessage);
//            conversation.insertMessage(emMessage);
            mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
        }
    };

    @Override
    public int getContentView() {
        return R.layout.activity_base_chat;
    }

    public void initView() {
        mChatAdapter = new ChatAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mChatAdapter);

        bottomStatusHeight = ScreenUtil.getNavigationBarHeight(this);
//        controlKeyboardLayout(rootView,mRecyclerView);
    }

    public void initData() {
        rxPermissions = new RxPermissions(this);
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
//        EMClient.getInstance().chatManager().importMessages(msgs);
    }

    public void initListener() {
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    mTvSend.setVisibility(View.GONE);
                    mIvAdd.setVisibility(View.VISIBLE);
                } else {
                    mTvSend.setVisibility(View.VISIBLE);
                    mIvAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.iv_back,R.id.tv_send,R.id.iv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                showModPhotosView();
                break;
            case R.id.tv_send:
                String conten = mEtMsg.getText().toString().trim();
                sendTextMsg(conten);
                mEtMsg.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void sendMessage(final EMMessage message) {
        message.setAttribute(MSG_STATUS, 1);
        message.setTo(toChatUsername);
        mList.add(message);
        mChatAdapter.notifyDataSetChanged();
        //发送消息
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("tag", "========= onsuccess");
                mHandler.sendEmptyMessage(REFRESH_ADAPTER_SUCCESS);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("tag", "========= onError " + s);
                mHandler.sendEmptyMessage(REFRESH_ADAPTER_SUCCESS);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.i("tag", "=========  onProgress " + i);
            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送文本消息
     *
     * @param content
     */
    public void sendTextMsg(String content) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage textEmmessage = EMMessage.createTxtSendMessage(content, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == EMAMessage.EMAChatType.GROUP)
            textEmmessage.setChatType(EMMessage.ChatType.GroupChat);
        //发送扩展消息
        textEmmessage.setAttribute(VIP, vip);
        textEmmessage.setAttribute(HEAD_URL, headUrl);
        textEmmessage.setAttribute(NICK_NAME, nickname);
        sendMessage(textEmmessage);
    }

    /**
     * 发送视频消息
     *
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     */
    public void sendVideoMsg(String videoPath, String thumbPath, int videoLength) {
        //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == EMAMessage.EMAChatType.GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        //发送扩展消息
        message.setAttribute(VIP, vip);
        message.setAttribute(HEAD_URL, headUrl);
        message.setAttribute(NICK_NAME, nickname);
        message.setAttribute(IMAGE_PATH, videoPath);
        //发送视频
        sendMessage(message);
//        EMClient.getInstance().chatManager().sendMessage(message);
//        mList.add(message);
//        mUploadBeanList.add(new UploadBean(videoPath, 0, -1));
//        mChatAdapter.setUploadBean(mUploadBeanList);
//        mChatAdapter.notifyDataSetChanged();
    }

    /**
     * 发送图片消息
     *
     * @param imagePath
     */
    public void sendImageMsg(final String imagePath) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        final EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == EMAMessage.EMAChatType.GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        //发送扩展消息
        message.setAttribute(VIP, vip);
        message.setAttribute(HEAD_URL, headUrl);
        message.setAttribute(NICK_NAME, nickname);
        message.setAttribute(MSG_STATUS, 0);
        message.setAttribute(IMAGE_PATH, imagePath);
        sendMessage(message);
    }

    private Dialog dialog;

    public void showModPhotosView() {
        View view = View.inflate(this, R.layout.dialog_choose_circle, null);
        Button select_photo_camera = (Button) view.findViewById(R.id.select_photo_camera_bt);
        Button select_photo_image = (Button) view.findViewById(R.id.select_photo_local_bt);
        Button tv_cancel = (Button) view.findViewById(R.id.tv_cancel);
        select_photo_camera.setOnClickListener(cameraClick);
        select_photo_image.setOnClickListener(cameraClick);
        tv_cancel.setOnClickListener(cameraClick);
        dialog = new Dialog(this, R.style.charge_dialog_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.popupAnimation); // 添加动画
        dialog.show();
    }

    private View.OnClickListener cameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            switch (view.getId()) {
                case R.id.select_photo_local_bt://相册选取
                    PictureSelector.create(BaseChatActivity.this)
                            .openGallery(PictureConfig.TYPE_ALL)
//                        .theme()
                            .maxSelectNum(1)
                            .minSelectNum(1)
                            .imageSpanCount(4)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .previewImage(true)
                            .isCamera(true)
                            .isZoomAnim(true)
//                .selectionMedia(mList)
                            .forResult(ConstantValue.JUMP_RELEASE_IMAGE);
                    break;
                case R.id.select_photo_camera_bt://拍摄
                    outputCameraPath = getDiskCacheDir() + "/top/video/"; //录制视频输出路径
                    getPermission();
                    break;
                case R.id.tv_cancel://取消
                    break;

            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.REQUEST_CAMERA) {
            sendVideoMsg(cameraPath, "", 10);
        } else if (resultCode == RESULT_OK && requestCode == ConstantValue.JUMP_RELEASE_IMAGE) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia media : list) {
                if (media.getPictureType().contains("image")) {

                    sendImageMsg(media.getPath());
                } else {
                    sendVideoMsg(media.getPath(), "", (int) media.getDuration());
                }
                Log.e("tag", "========== type  " + media.getPictureType() + " min " + media.getMimeType());
            }
        }
    }

    /**
     * start to camera、video
     */
    public void startOpenCameraVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(this, PictureConfig.TYPE_VIDEO,
                    outputCameraPath, PictureFileUtils.POST_VIDEO);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(this, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    public void getPermission() {
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            startOpenCameraVideo();
                        } else {
                            RxToast.normal(getString(com.luck.picture.lib.R.string.picture_camera));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public String getDiskCacheDir() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = getExternalCacheDir().getPath();
        } else {
            cachePath = getCacheDir().getPath();
        }
        return cachePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(getContentView());
        ButterKnife.bind(this);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mRelativeTitle = findViewById(R.id.relative_title);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mEtMsg = findViewById(R.id.et_msg);
        mIvAdd = findViewById(R.id.iv_add);
        mTvSend = findViewById(R.id.tv_send);
        mLlBottom = findViewById(R.id.ll_bottom);
        mRootView = findViewById(R.id.root_view);
        initView();
        initData();
        initListener();
    }
}
