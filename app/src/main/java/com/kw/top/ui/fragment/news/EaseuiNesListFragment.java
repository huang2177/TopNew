package com.kw.top.ui.fragment.news;

import android.widget.TextView;

/**
 * author  ： zy
 * date    ： 2018/7/1
 * des     ：
 */

public class EaseuiNesListFragment  {

    private TextView errorText;
    private boolean focus;
    public static EaseuiNesListFragment fragment;


    public static EaseuiNesListFragment newInstance() {
        if (fragment == null) {
            fragment = new EaseuiNesListFragment();
        }
        return fragment;
    }


    /*@Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
       // errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }*/

   /* @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    String title = "";//昵称，群名
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                            // TODO: 2018/9/11 新加的
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                            title = (group != null ? group.getGroupName() : username);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                            title = (group != null ? group.getGroupName() : username);
                            Log.e("tag", "===========  qunzu " + title);
                        }

                    } else {
                        EMMessage emMessage = conversation.getLastMessage();
                        String receive_head = "";
                        String receive_name = "";
                        try {
//                            title = emMessage.getStringAttribute(ConstantValue.NICK_NAME);
                            receive_head = emMessage.getStringAttribute(EaseConstant.RECEIVE_HEAD_REL);
                            receive_name = emMessage.getStringAttribute(EaseConstant.RECEIVE_NAME);
                            title = receive_name;
                            intent.putExtra(EaseConstant.RECEIVE_HEAD_REL, receive_head);
                            intent.putExtra(EaseConstant.RECEIVE_NAME, receive_name);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                    intent.putExtra(Constant.EXTRA_TOOLBAR_TITLE, title);
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        super.setUpView();
    }
*/
   /* @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }
*/

  /*  @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }
*/
 /*   @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
            // To delete the native stored adked users in this conversation.
            if (deleteMessage) {
                EaseDingMessageHelper.get().delete(tobeDeleteCons);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        return true;
    }
*/

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshNews(RefreshNewsEvent newsEvent) {
        if (newsEvent.isNewsMsg() && focus)
            refresh();
    }
*/
   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitClubEvent(ExitClubEvent exitClubEvent) {
        //删除聊天记录
        if (!TextUtils.isEmpty(exitClubEvent.getGroupId()))
            EMClient.getInstance().chatManager().deleteConversation(exitClubEvent.getGroupId(), true);
    }
*/
  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
*/
    /*@Override
    public void onDestroy() {
       / super.onDestroy();
        EventBus.getDefault().unregister(this);
    }*/

    /*@Override
    public void onResume() {
        super.onResume();
        focus = true;
        // update unread count
        ((NewMainActivity) getActivity()).updateUnreadLabel();
    }*/

    /*@Override
    public void onPause() {
        super.onPause();
        focus = false;
    }*/

}
