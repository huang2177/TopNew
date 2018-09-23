package com.kw.top.retrofit;

import com.kw.top.base.EaseTokenBean;
import com.kw.top.bean.AuthInfoBean;
import com.kw.top.bean.BaseBean;
import com.kw.top.bean.ClubBean;
import com.kw.top.bean.FriendApplyBean;
import com.kw.top.bean.ReceiveGiftBean;
import com.kw.top.bean.ReceiveRedBean;
import com.kw.top.bean.SendGiftBean;
import com.kw.top.bean.SendRedBean;
import com.kw.top.bean.SplashBean;
import com.kw.top.bean.VersionBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * author: 正义
 * date  : 2018/4/16
 * desc  :
 */

public interface ApiService {

    //用户相关

    @POST("api/userController/login")
    Observable<BaseBean> login(@Query("phone") String phone, @Query("password") String password,
                               @Query("lon") String lon, @Query("lat") String lat,
                               @Query("city") String city, @Query("registrationId") String registrationId);


    /**
     * 新登陆接口
     *
     * @param phone
     * @param phoneCode
     * @param areaCode
     * @return
     */
    @POST("api/userController/login1")
    Observable<BaseBean> login1(@Query("phone") String phone, @Query("phoneCode") String phoneCode,
                                @Query("areaCode") String areaCode);


    @POST("api/videoController/getYunXinToken")
    Observable<BaseBean<EaseTokenBean>> EaseToken(@Query("token") String token);


    @POST("api/userController/getBackPassword")
    Observable<BaseBean> changePwd(@Query("phone") String phone, @Query("phoneCode") String phoneCode,
                                   @Query("newPassword1") String newPassword1, @Query("newPassword2") String newPassword2);

    @POST("api/userController/addLikeObject")
    Observable<BaseBean> addLikeObject(@Query("objectId") String objectId, @Query("token") String token);

    @POST("api/userController/addSex")
    Observable<BaseBean> addSex(@Query("sex") String sex, @Query("token") String token);

    @POST("api/userController/addNicknameAndWX")
    Observable<BaseBean> addNickNameAndWx(@Query("nickName") String nickName, @Query("weChatNum") String weChatNum, @Query("headImg") String headImg, @Query("token") String token);

    @POST("api/userController/sendPhoneCode")
    Observable<BaseBean> sendPhoneCode(@Query("phone") String phone);


    @POST("api/userController/sendPhoneCode")
    Observable<BaseBean> NewsendPhoneCode(@Query("areaCode") String areaCode, @Query("phone") String phone);

    @POST("api/userController/inviteList")
    Observable<BaseBean> inviteList(@Query("token") String token);

    @POST("api/userController/myAccount")
    Observable<BaseBean> myAccount(@Query("token") String token);

    @POST("api/userController/myCouponsGift")
    Observable<BaseBean> myGift(@Query("token") String token);

    //个人中心-送出的礼物
    @POST("api/userController/sendJewelGift")
    Observable<BaseBean<SendGiftBean>> sendGift(@Query("token") String token);


    @POST("api/userController/toData")
    Observable<BaseBean> getInfoData(@Query("token") String token);

    @POST("api/userController/updateData")
    Observable<BaseBean> updataInfo(@QueryMap() Map<String, String> map);

    //Top

    /**
     * @param dynamicType top圈类型('01'.公共圈,'02'.私密圈,'03'.好友圈)
     * @param nowPage
     * @param pageNum
     * @param token
     * @return
     */
    @POST("api/topController/getTOP")
    Observable<BaseBean> getTopCircle(@Query("dynamicType") String dynamicType, @Query("nowPage") String nowPage,
                                      @Query("pageNum") String pageNum, @Query("token") String token);

    @POST("api/topController/getTOPTotal")
    Observable<BaseBean> getTopTotal(@Query("dynamicType") String dynamicType, @Query("token") String token);

    @POST("api/topController/getTOPnews")
    Observable<BaseBean> getTopNews(@Query("token") String token);

    /**
     * @param beautifulCode 美丽榜标识（'01'.魅力,'02'.财富）
     * @param nowPage
     * @param pageNum
     * @param token
     * @return
     */
    @POST("api/topController/getTopBeautiful")
    Observable<BaseBean> getTopBeautifulList(@Query("beautifulCode") String beautifulCode, @Query("nowPage") String nowPage,
                                             @Query("pageNum") String pageNum, @Query("token") String token);

    /**
     * 发布世界圈
     *
     * @param textContent  文字内容
     * @param dynamicPic   图片地址：可以为多个，中间已逗号隔开，如：(pic1.jpg,pic2.jpg,pic3.jpg)
     * @param dynamicVideo 视频地址
     * @param dynamicType  top圈类型('01'.公共圈,'02'.私密圈)
     * @param token
     * @return
     */
    @POST("api/topController/sendTOP")
    Observable<BaseBean> sendTopCircle(@Query("textContent") String textContent, @Query("dynamicPic") String dynamicPic,
                                       @Query("dynamicVideo") String dynamicVideo, @Query("dynamicType") String dynamicType,
                                       @Query("token") String token);

    @POST("api/redPackageController/clickRedPackage")
    Observable<BaseBean> clickRedPackage(@Query("redPackageId") String redPackageId, @Query("token") String token);

    //个人中心-我的账户-收到的红包
    @POST("api/redPackageController/getRedPackage")
    Observable<BaseBean<ReceiveRedBean>> receiveRedPackage(@Query("token") String token);

    //红包详情
    @POST("api/redPackageController/redPackageDetails")
    Observable<BaseBean> redPackageDetails(@Query("redPackageId") String redPackageId, @Query("token") String token);

    /**
     * 发红包
     *
     * @param amountSum      红包总金额
     * @param shareSum       发放份数
     * @param redPackageType 红包类型('0'.礼券,'1'.金钱)
     * @param token
     * @return
     */
    @POST("api/redPackageController/sendRedPackage")
    Observable<BaseBean> sendRedPackage(@Query("groupid") String groupId, @Query("amountSum") String amountSum, @Query("shareSum") String shareSum,
                                        @Query("redPackageType") String redPackageType, @Query("token") String token);

    //个人中心-我的账户-送出的红包
    @POST("api/redPackageController/sendRedPackageList")
    Observable<BaseBean<SendRedBean>> sendRedPackageList(@Query("token") String token);

    //俱乐部模块
    @POST("api/recreationController/applyClub")
    Observable<BaseBean> applyClub(@Query("clubId") String clubId, @Query("token") String token);

    @POST("api/recreationController/clubDetails")
    Observable<BaseBean> clubDetails(@Query("groupid") String clubId, @Query("token") String token);

    @POST("api/recreationController/deleteClubMember")
    Observable<BaseBean> deleteClubMember(@Query("applyId") String clubId, @Query("token") String token);

    /**
     * 创建俱乐部
     *
     * @param clubName          俱乐部名称
     * @param joinClubCondition 入会条件（'0'.女,'1'.男,'2'.全部）
     * @param clubNotice        公告
     * @param sendRedTime       每天发红包时间
     * @param dayRedAmount      每日发红包金额
     * @param token             token值
     * @return
     */
    @POST("api/recreationController/foundClub")
    Observable<BaseBean> createClub(@Query("clubName") String clubName, @Query("joinClubCondition") String joinClubCondition,
                                    @Query("clubNotice") String clubNotice, @Query("sendRedTime") String sendRedTime,
                                    @Query("dayRedAmount") String dayRedAmount, @Query("token") String token);

    //查询申请俱乐部人员
    @POST("api/recreationController/getApplyClubMember")
    Observable<BaseBean> getApplyClubMember(@Query("groupid") String clubId, @Query("token") String token);

    //俱乐部审核人员  applyCode:'0'.拒绝,'1'.同意
    @POST("api/recreationController/getApplyMember")
    Observable<BaseBean> getApplyMember(@Query("applyCode") String applyCode, @Query("applyId") String applyId, @Query("token") String token);

    //礼物相关
    //购买礼物
    @POST("api/giftController/buyGift")
    Observable<BaseBean> buyGift(@Query("token") String token);

    //查询所有礼物
    @POST("api/giftController/getAllGift")
    Observable<BaseBean> getAllGift(@Query("token") String token);

    //查询用户收到的礼物
    @POST("api/giftController/getGiftByUser")
    Observable<BaseBean<ReceiveGiftBean>> getGiftByUser(@Query("token") String token);

    /**
     * 送礼物
     *
     * @param giftId        用户礼物编号
     * @param receiveUserId 接收人ID
     * @param token         token值
     * @return
     */
    @POST("api/giftController/sendGift")
    Observable<BaseBean> sendGift(@Query("giftId") String giftId, @Query("num") String num,
                                  @Query("receiveUserId") String receiveUserId, @Query("token") String token);

    /**
     * 查询所有用户
     *
     * @param distance 距离（按照距离查询传1，否则不传）
     * @param liveness 活跃度（按照活跃度查询传1，否则不传）
     * @param newDate  最新加入（按照最新加入查询传1，否则不传）
     * @param sex      性别(0.女，1.男，查询全部不传)
     * @param nowPage
     * @param pageNum
     * @param token
     * @return
     */
    @POST("api/findUserController/getAllUserList")
    Observable<BaseBean> getAllUserList(@Query("newLoginDate") String newLoginDate
            , @Query("city") String city
            , @Query("distance") String distance
            , @Query("liveness") String liveness
            , @Query("newDate") String newDate
            , @Query("sex") String sex
            , @Query("nowPage") String nowPage
            , @Query("pageNum") String pageNum
            , @Query("token") String token
            , @Query("lon") String lon
            , @Query("lat") String lat);

    //发现 - 世界圈
    @POST("api/findUserController/getTOPListByUser")
    Observable<BaseBean> getTOPListByUser(@Query("userId") String userId, @Query("token") String token);

    //发现 - 世界圈详情
    @POST("api/findUserController/queryDynamicDesc")
    Observable<BaseBean> dynamicDesc(@Query("dynamicId") String dynamicId, @Query("token") String token);

    //发现 - 离我最近 - 详情
    @POST("api/findUserController/queryUserDesc")
    Observable<BaseBean> queryUserDesc(@Query("userId") String userId, @Query("token") String token);


    //获得七牛云文件上传Token
    @POST("api/topController/getUpToken")
    Observable<BaseBean> getUpToken(@Query("token") String token);

    //上传3张最美丽的图片
    @POST("api/userController/addPhoto")
    Observable<BaseBean> addPhoto(@Query("pictures") String pictures, @Query("token") String token);

    //个人中心-充值-钻石列表
    @POST("api/userController/amountToJewelList")
    Observable<BaseBean> amountToJewelList(@Query("token") String token);

    //个人中心-兑换-钻石列表
    @POST("api/userController/couponsToJewelList")
    Observable<BaseBean> couponsToJewelList(@Query("token") String token);

    //查询支付宝账号是否存在
    @POST("api/userController/queryAlipayNum")
    Observable<BaseBean> queryAlipayNum(@Query("token") String token);

    //解绑支付宝
    @POST("api/userController/deleteAlipayNum")
    Observable<BaseBean> unBindAlipay(@Query("token") String token);

    //绑定支付宝
    @POST("api/userController/addAlipayNum")
    Observable<BaseBean> bindAlipay(@Query("alipayNum") String alipayNum, @Query("token") String token);

    //送礼物申请添加好友
    @POST("api/giftController/sendApplyFriendsByGift")
    Observable<BaseBean> sendGiftAddFriend(@Query("giftId") String giftId, @Query("num") String num,
                                           @Query("receiveUserId") String receiveUserId, @Query("token") String token);

    //好友申请列表
    @POST("api/giftController/applyFriendsList")
    Observable<BaseBean<List<FriendApplyBean>>> applyFriendsList(@Query("token") String token);

    //是否同意好友申请
    @POST("api/giftController/getApplyFriendsList")
    Observable<BaseBean> agreeFriendApply(@Query("relationId") String relationId, @Query("isAgree") String isAgree, @Query("token") String token);

    //获取好友列表
    @POST("api/giftController/myFriendsList01")
    Observable<BaseBean> getFriendsList(@Query("token") String token);

    //我的任务
    @POST("api/topTaskController/getMyTaskList")
    Observable<BaseBean> getMyTaskList(@Query("token") String token);

    //任务大厅
    @POST("api/topTaskController/getTaskList")
    Observable<BaseBean> getTaskList(@Query("token") String token);

    //任务奖励列表
    @POST("api/topTaskController/taskProductList")
    Observable<BaseBean> getAwardList(@Query("token") String token);

    //发布任务 01图片 02视频
    @POST("api/topTaskController/mySendTask")
    Observable<BaseBean> sendTask(@Query("describes") String describes, @Query("amount") String amount,
                                  @Query("num") String num, @Query("type") String type, @Query("token") String token);

    //拒绝同意任务  1赞赏 2拒绝
    @POST("api/topTaskController/sendTaskChoose")
    Observable<BaseBean> agreeTask(@Query("userTaskId") String userTaskId, @Query("state") String state, @Query("token") String token);

    //任务详情
    @POST("api/topTaskController/getMyTaskDesList")
    Observable<BaseBean> taskDetails(@Query("taskId") String taskId, @Query("token") String token);

    //任务大厅-完成任务
    @POST("api/topTaskController/getFinishTask")
    Observable<BaseBean> finishTask(@Query("taskId") String taskId, @Query("urlAddress") String urlAddress, @Query("token") String token);

    //上传用户头像
    @POST("api/userController/updateData")
    Observable<BaseBean> upPhoto(@Query("headImg") String headImg, @Query("token") String token);

    //小课堂列表
    @POST("api/activityController/getAllClassroom")
    Observable<BaseBean> getClassroomList(@Query("token") String token);

    //小课堂详情
    @POST("api/activityController/getClassroomDes")
    Observable<BaseBean> getClassroomDes(@Query("classroomId") String classroomId, @Query("token") String token);

    //活动列表
    @POST("api/activityController/getAllActivity")
    Observable<BaseBean> getActiveList(@Query("token") String token);

    //活动详情
    @POST("api/activityController/getActivityDes")
    Observable<BaseBean> getActiveDetails(@Query("activityId") String activityId, @Query("token") String token);

    //点赞活动
    @POST("api/activityController/addGood")
    Observable<BaseBean> activeAward(@Query("activityId") String activityId, @Query("addGoodUserId") String addGoodUserId,
                                     @Query("thumbsUpNum") String thumbsUpNum, @Query("token") String token);

    //我加入的俱乐部
    @POST("api/recreationController/getMyAddClub")
    Observable<BaseBean<List<ClubBean>>> getAddclubList(@Query("token") String token);

    //我创建的俱乐部
    @POST("api/recreationController/getMyCreateClub")
    Observable<BaseBean<List<ClubBean>>> getMyCreateclubList(@Query("token") String token);

    //查询俱乐部全体人员
    @POST("api/recreationController/getClubMember")
    Observable<BaseBean> getClubAllPeople(@Query("groupid") String clubId, @Query("token") String token);

    //支付
    @POST("api/aliPayController/toAliPay")
    Observable<BaseBean> toAlipay();

    //充值产品
    @POST("api/aliPayController/toRecharge")
    Observable<BaseBean> aliPay(@Query("id") String id, @Query("token") String token);

    //获取邀请好友的列表
    @POST("api/userController/getInviteUrl")
    Observable<BaseBean> getInviteUrl(@Query("token") String token);

    //个人中心
    @POST("api/userController/toPersonalCenter")
    Observable<BaseBean> getPersonCenter(@Query("token") String token);

    //上传视频认证URL
    @POST("api/userController/addProveVideoUrl")
    Observable<BaseBean> addVerifyVideoUrl(@Query("proveVideoUrl") String proveVideoUrl, @Query("token") String token);

    //发表评论
    @POST("api/findUserController/addDynamicCom")
    Observable<BaseBean> sendCommnet(@Query("dynamicId") String dynamicId, @Query("retUserid") String retUserid,
                                     @Query("comContent") String comContent, @Query("token") String token);

    //礼券兑换钻石
    @POST("api/aliPayController/couponsToJewel")
    Observable<BaseBean> couponsToJewel(@Query("id") String id, @Query("token") String token);

    //动态点赞
    @POST("api/topController/addGood")
    Observable<BaseBean> addGood(@Query("dynamicId") String dynamicId, @Query("thumbsUpNum") String thumbsUpNum, @Query("token") String token);

    //小课堂添加评论
    @POST("api/activityController/addClassroomCom")
    Observable<BaseBean> addClassComment(@Query("classroomId") String classroomId, @Query("retUserid") String retUserid,
                                         @Query("comContent") String comContent, @Query("token") String token);

    //查询用户是否绑定支付包
    @POST("api/aliPayController/queryAlipayNum")
    Observable<BaseBean> queryBindAlipay(@Query("token") String token);

    //支付宝授权信息
    @POST("api/aliPayController/toBindingAlipay")
    Observable<BaseBean<AuthInfoBean>> toDrawMoney(@Query("token") String token);

    //体现
    @POST("api/aliPayController/withdraw")
    Observable<BaseBean> withDraw(@Query("amount") String amount, @Query("token") String token);

    //所有俱乐部
    @POST("api/recreationController/getAllClub")
    Observable<BaseBean<List<ClubBean>>> getAllClub(@Query("nowPage") String nowPage,
                                                    @Query("pageNum") String pageNum, @Query("token") String token);

    //我的俱乐部
    @POST("api/recreationController/getMyAddClub")
    Observable<BaseBean> getMyClub(@Query("token") String token);

    //删除动态
    @POST("api/topController/deleteDynamic")
    Observable<BaseBean> deleteDynamic(@Query("dynamicId") String dynamicId, @Query("token") String token);

    //获得Top消息（未读评论消息）
    @POST("api/findUserController/getDynamicCom")
    Observable<BaseBean> getDynamicCom(@Query("token") String token);

    //标记评论为已读
    @POST("api/findUserController/upDynamicCom")
    Observable<BaseBean> upDynamicCom(@Query("id") String id, @Query("token") String token);

    //通知中中心
    @POST("api/userController/noticeCenterList")
    Observable<BaseBean> getNoticeCenterList(@Query("token") String token);

    //获取喜欢的对象
    @POST("api/userController/getLikeObject")
    Observable<BaseBean> getLikeObject(@Query("token") String token);

    //获取用户信息
    @POST("api/userController/getEasemobAccount")
    Observable<BaseBean> getUserInfo(@Query("token") String token);

    //任务详情
    @POST("api/topTaskController/getTaskDesList")
    Observable<BaseBean> getTaskDesc(@Query("taskId") String taskId, @Query("token") String token);

    //参加活动
    @POST("api/activityController/addActivity")
    Observable<BaseBean> addActive(@Query("activityId") String activityId, @Query("activityPic") String activityPic,
                                   @Query("activityVideo") String activityVideo, @Query("token") String token);

    //用户活动详情
    @POST("api/activityController/getDynamicCom")
    Observable<BaseBean> userActiveDetails(@Query("userId") String userId,
                                           @Query("activityId") String activityId, @Query("token") String token);

    //俱乐部任务
    @POST("api/groupTaskController/getGroupTaskList")
    Observable<BaseBean> clubTaskList(@Query("groupid") String groupid, @Query("token") String token);

    //俱乐部任务详情
    @POST("api/groupTaskController/getGroupTaskDesList")
    Observable<BaseBean> clubTaskDetails(@Query("taskId") String taskId, @Query("token") String token);

    //发布社团任务
    @POST("api/groupTaskController/sendGroupTask")
    Observable<BaseBean> sendClubTask(@Query("describes") String describes, @Query("groupid") String groupid,
                                      @Query("type") String type, @Query("token") String token);

    //俱乐部任务（成员查看接口）
    @POST("api/groupTaskController/getGroupUserTaskList")
    Observable<BaseBean> clubUserTaskList(@Query("groupid") String groupid, @Query("token") String token);

    //用户完成俱乐部任务
    @POST("api/groupTaskController/getFinishGroupTask")
    Observable<BaseBean> userFinishClubTask(@Query("taskId") String taskId, @Query("taskPic") String taskPic
            , @Query("taskVideo") String taskVideo, @Query("token") String token);

    //查看用户是否完成今日任务
    @POST("api/groupTaskController/finishTaskState")
    Observable<BaseBean> userClubTaskState(@Query("groupid") String groupid, @Query("token") String token);

    //修改社团公告
    @POST("api/recreationController/updateClub")
    Observable<BaseBean> updateClub(@Query("groupid") String groupid, @Query("clubNotice") String clubNotice,
                                    @Query("token") String token);

    //删除好友
    @POST("api/giftController/deleteFriendsById")
    Observable<BaseBean> deleteFriend(@Query("friendsId") String friendsId, @Query("token") String token);

    //删除自己的评论
    @POST("api/findUserController/deleteDynamic")
    Observable<BaseBean> deletePersonComment(@Query("id") String id, @Query("token") String token);

    //删除别人的评论
    @POST("api/findUserController/deleteDynamicComm")
    Observable<BaseBean> deleteOthersComment(@Query("id") String id, @Query("dynamicId") String dynamicId,
                                             @Query("token") String token);

    //俱乐部退群
    @POST("api/recreationController/signOutClub")
    Observable<BaseBean> clubExit(@Query("groupid") String groupid, @Query("token") String token);

    //更新用户位置
    @POST("api/userController/updateAddr")
    Observable<BaseBean> updateAddress(@Query("lon") String lon, @Query("lat") String lat,
                                       @Query("city") String city, @Query("token") String token);

    //app启动页图片
    @POST("api/userController/queryPicture")
    Observable<BaseBean<SplashBean>> queryPicture();


    @POST("api/findUserController/queryVersion")
    Observable<BaseBean<VersionBean>> queryVersion(@Query("type") String type, @Query("token") String token);

    @POST("api/findUserController/queryCity")
    Observable<BaseBean> queryCity(@Query("token") String token);


}
