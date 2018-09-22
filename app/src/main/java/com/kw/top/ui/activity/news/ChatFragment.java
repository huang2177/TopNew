package com.kw.top.ui.activity.news;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.utils.EaseSPUtils;
import com.hyphenate.easeui.utils.PreferenceManager;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;
import com.kw.top.R;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.chat.RobotUser;
import com.kw.top.retrofit.Api;
import com.kw.top.retrofit.HttpHost;
import com.kw.top.tools.ComResultTools;
import com.kw.top.tools.Constant;
import com.kw.top.tools.ConstantValue;
import com.kw.top.tools.DemoHelper;
import com.kw.top.ui.activity.circle.UserCircleActivity;
import com.kw.top.ui.activity.club.ClubDetailsActivity;
import com.kw.top.ui.activity.club.VipManagerActivity;
import com.kw.top.ui.activity.find.FindDetailsActivity;
import com.kw.top.ui.activity.task.ClubTaskListActivity;
import com.kw.top.ui.activity.user_center.RedPacketDetailsActivity;
import com.kw.top.utils.RxToast;
import com.kw.top.utils.SPUtils;
import com.kw.top.view.EaseChatRecallPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.kw.top.tools.ConstantValue.VIP;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    private static final int ITEM_REDBAG = 15;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int REQUEST_CODE_SEND_REDBAG = 16;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;
    private String headUrl;
    private String nickname;
    private String userId;
    private String receive_headurl;
    private String receive_name;


    /**
     * if it is chatBot
     */
    private boolean isRobot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState,
                DemoHelper.getInstance().getModel().isMsgRoaming() && (chatType != EaseConstant.CHATTYPE_CHATROOM));
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        headUrl = HttpHost.qiNiu + SPUtils.getString(getContext(), ConstantValue.KEY_HEAD, "");
        nickname = SPUtils.getString(getContext(), ConstantValue.KEY_NAME, "");
        userId = SPUtils.getString(getContext(), ConstantValue.KEY_USER_ID, "");
        receive_headurl = getArguments().getString(EaseConstant.RECEIVE_HEAD_REL);
        receive_name = getArguments().getString(EaseConstant.RECEIVE_NAME);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
        // set click listener
        titleBar.setTitle(getArguments().getString(EaseConstant.EXTRA_TOOLBAR_TITLE));
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (EasyUtils.isSingleActivity(getActivity())) {
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                }
                onBackPressed();
            }
        });
        titleBar.setBackgroundColor(getResources().getColor(R.color.black_bg));
//        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
//                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
//                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            titleBar.setRightLayoutClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClubDetailsActivity.startActivity(getContext(), toChatUsername);
                }
            });
        } else {
            titleBar.setRightLayoutVisibility(View.GONE);
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        if (chatType == Constant.CHATTYPE_SINGLE) {
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        }
        //红包的menu
        inputMenu.registerExtendMenuItem(R.string.attach_red_bag, R.drawable.icon_red_bag, ITEM_REDBAG, extendMenuItemClickListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    // To delete the ding-type message native stored acked users.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                startActivity(intent);
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL://recall
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                                msgNotification.setStatus(EMMessage.Status.SUCCESS);
                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                messageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();

                    // Delete group-ack data according to this message.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                case REQUEST_CODE_SEND_REDBAG:
                    if (data != null) {
                        String redbagId = data.getStringExtra("REDBAG_ID");
                        String content = data.getStringExtra("CONTENT");
                        sendRedbagMessage(content, redbagId);
                    }
                    break;
                default:
                    break;
            }
        }
        if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
            switch (resultCode) {
//                case GroupDetailsActivity.RESULT_CODE_SEND_GROUP_NOTIFICATION:
//                    // Start the ding-type msg send ui.
//                    EMLog.i(TAG, "Intent to the ding-msg send activity.");
//                    Intent intent = new Intent(getActivity(), EaseDingMsgSendActivity.class);
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, toChatUsername);
//                    startActivityForResult(intent, REQUEST_CODE_DING_MSG);
//                    break;
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
//        message.setAttribute(VIP, "");
        message.setAttribute(ConstantValue.HEAD_URL, headUrl);
        message.setAttribute(ConstantValue.NICK_NAME, nickname);
        message.setAttribute(EaseConstant.USER_ID, userId);
        message.setAttribute(EaseConstant.RECEIVE_HEAD_REL, receive_headurl);
        message.setAttribute(EaseConstant.RECEIVE_NAME, receive_name);
        Log.e("tag", "========== receive url " + receive_headurl);
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
//        	startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(EMMessage emMessage) {
        //handling when user click avatar
        Intent intent = new Intent(getActivity(), FindDetailsActivity.class);
        intent.putExtra("USERID", emMessage.getStringAttribute(EaseConstant.USER_ID, ""));
        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        if (message.getType() == EMMessage.Type.TXT && !TextUtils.isEmpty(message.getStringAttribute(EaseConstant.REDBAG_ID, ""))) {
            String redbagId = message.getStringAttribute(EaseConstant.REDBAG_ID, "");
            //是否已经被领取
            String click_redbag_id = EaseSPUtils.getString(getContext(), redbagId, "");
            Log.e("tag", "============ fragment  redid " + redbagId + "   clickid " + click_redbag_id);
            if (TextUtils.isEmpty(click_redbag_id)) {
                //没有领取，点击领取
                clickRedbag(redbagId);
            } else {
                //已经领取，跳转详情
                RedPacketDetailsActivity.startActivity(getContext(), redbagId);
            }
        }
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
        if (message.getType() == EMMessage.Type.TXT && TextUtils.isEmpty(message.getStringAttribute(EaseConstant.REDBAG_ID, ""))) {
            EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(messageBody.getMessage());
            RxToast.normal("复制成功");
        }
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            case ITEM_REDBAG:
                startRedbag();
                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    private void startRedbag() {
//        SendRedbagActivity.startActivity(getActivity(),"0",toChatUsername,REQUEST_CODE_SEND_REDBAG);
        Intent intent = new Intent(getActivity(), SendRedbagActivity.class);
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            intent.putExtra("RED_TYPE", "1");
            intent.putExtra("GROUPID", toChatUsername);
        } else {
            intent.putExtra("RED_TYPE", "0");
        }
        intent.putExtra("TO_USER", getArguments().getString(EaseConstant.EXTRA_TOOLBAR_TITLE));
        intent.putExtra("HEAD", headUrl);
        startActivityForResult(intent, REQUEST_CODE_SEND_REDBAG);
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
//                    EaseChatRowPresenter presenter = new EaseChatVoiceCallPresenter();
                    EaseChatRowPresenter presenter = new EaseChatRecallPresenter();
                    return presenter;
                }
                //recall message
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    EaseChatRowPresenter presenter = new EaseChatRecallPresenter();
                    return presenter;
                }
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private long mlickTime = 0;


    /**
     * 领取红包点击事件
     *
     * @param redbagId
     */
    private void clickRedbag(final String redbagId) {

        if (System.currentTimeMillis() - mlickTime <= 1000) {
            return;
        }
        mlickTime = System.currentTimeMillis();
        Api.getApiService().clickRedPackage(redbagId, SPUtils.getString(getContext(), ConstantValue.KEY_TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        if (baseBean.isSuccess()) {
                            //保存领取过的红包ID
                            EaseSPUtils.saveString(getContext(), redbagId, redbagId);
                            messageList.refresh();
                            RedPacketDetailsActivity.startActivity(getContext(), redbagId);
                        } else {
                            RedPacketDetailsActivity.startActivity(getContext(), redbagId);
//                            ComResultTools.resultData(getContext(), baseBean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

}
