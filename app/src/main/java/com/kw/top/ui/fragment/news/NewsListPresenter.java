package com.kw.top.ui.fragment.news;

import android.os.AsyncTask;

import com.kw.top.base.BasePresenterImpl;
import com.kw.top.bean.BaseBean;
import com.kw.top.retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: zy
 * data  : 2018/6/2
 * des   :
 */

public class NewsListPresenter extends BasePresenterImpl<NewsListContract.View> implements NewsListContract.Presenter {

    private AsyncTask<Void, Void, Void> mAsyncTask;
    private List<String> mFrendsList = new ArrayList<>();
   // private List<EMMessage> mMessageList = new ArrayList<>();
    private List<String> mLetters = new ArrayList<>();

    /**
     * 初始化好友列表
     */
    public void initFriendsList() {
        mAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mFrendsList.clear();
                    //mFrendsList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    /**
     * 初始化消息列表
     */
    public void initMessageList() {
        mAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //    EMClient.getInstance().chatManager().addMessageListener(msgListener);
                return null;
            }
        }.execute();
    }

    /*EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            mMessageList.clear();
            mMessageList = messages;
            for (int i=0;i<messages.size();i++){
                EMMessage emMessage = messages.get(i);
                emMessage.getChatType();
                EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
                Log.e("tag","==============  收到消息：" + messageBody.getMessage()+"  to " + emMessage.getTo() + "  id"+ emMessage.getMsgId()
                +"   type " + emMessage.getType() + "  chatType: " + emMessage.getChatType());
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.e("tag","==============  收到透传消息" + messages.size());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.e("tag","==============  收到已读回执" + messages.size());
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }
        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };
*/

    /**
     * 页面销毁时移除消息监听
     */
    public void removeMessageListener() {
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    public List<String> getLetters() {
        char a = 'A';
        for (int i = 0; i < 26; i++) {
            mLetters.add(a + "");
            a++;
        }
        return mLetters;
    }

    @Override
    public void getFriendsList(String token) {
        Api.getApiService().getFriendsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean baseBean) {
                        mView.getFriendListResult(baseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.getFriendListResult(null);
                    }
                });
    }
}
