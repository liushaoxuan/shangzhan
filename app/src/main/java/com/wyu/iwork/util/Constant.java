package com.wyu.iwork.util;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class Constant {
    private static final String TAG = "Constant";
    public static final String SECRETKEY = "12345678";
    public static final String F = "ANDROID";
    public static final String V = "1.5.0";

    /**
     * 测试地址
     */
    public static final String URL = "http://sz.51ruanron.com/api.php";
    /**
     * 正式地址
     */
    //public static final String URL = "http://trade.ruanron.com/api.php";

    /**
     * 登录
     */
    public static final String URL_SIGN_IN = URL + "/Moffice/User/Login.html";

    /**
     * 上传 通讯录信息的接口
     */
    public static final String URL_POST_CONSTACTS = URL + "/Moffice/Contacts/SaveAddressList.html";

    /**
     * 找回密码  发送验证码
     */
    public static final String URL_FINDPASS_SMS = URL + "/Moffice/User/RetrievePasswordSmsVerify.html";

    /**
     * 融云token和认证状态
     */
    public static final String URL_TOKEN_URL = URL + "/Moffice/Index/ImmediatelyData.html";

    /**
     * 应用中心-获取应用列表
     */
    public static final String URL_GET_APP_LIST = URL + "/Moffice/Index/GetAppList.html";

    /**
     * 应用中心-获取应用列表V2
     */
    public static final String URL_GET_APP_LIST_V2 = URL + "/Moffice/Index/GetAppList_V2.html";
    /**
     * 用户协议
     */
    public static final String URL_USER_AGREEMENT= URL + "/Moffice/Index/Disclaimer";
    /**
     * 交流-部门管理
     */
    public static final String URL_ORGANZ_MANAGE = URL + "/Moffice/Comm/GetDepartment.html";

    /**
     * 交流-获取部门信息
     */
    public static final String URL_ORGANZ_INFO = URL + "/Moffice/Comm/GetDepartmentInfo.html";

    /**
     * 交流-获取一级部门列表
     */
    public static final String URL_ORGANZ_LIST = URL + "/Moffice/Comm/GetTopDepartmentList.html";

    /**
     * 交流-部门新增或编辑
     */
    public static final String URL_ORGANZ_UPDATE = URL + "/Moffice/Comm/UpdateDepartment.html";

    /**
     * 交流-删除部门
     */
    public static final String URL_ORGANZ_DELETE = URL + "/Moffice/Comm/DeleteDepartment.html";

    /**
     * 交流-管理员新增或编辑用户信息
     */
    public static final String URL_COMMU_USER_CU = URL + "/Moffice/Comm/UpdateEditUserInfo.html";


    /**
     * 交流-获取组织架构
     */
    public static final String URL_COMMU_ORGANZ= URL + "/Moffice/Comm/GetDepartmentUser.html";

    /**
     * 交流-获取公司角色列表
     */
    public static final String URL_ORGANZ_ROLE_LIST = URL + "/Moffice/Comm/GetRoleList.html";

    /**
     * 我的
     */
    public static final String URL_UPDATE_FACE=URL+"/Moffice/User/UpdateFace.html";
    /**
     *
     */
    public static final String URL_GET_USERINFO = URL + "/Moffice/User/GetUserInfo.html";
    /**
     *
     */
    public static final String URL_UPDATE_USERINFO = URL + "/Moffice/User/UpdateUserInfo.html";
    /**
     *
     */
    public static final String URL_UPDATE_USERPASSWORD =URL + "/Moffice/User/UpdateUserPasswd.html";
    /**
     *
     */
    public static final String URL_UPDATE_USERPMOVEBINDING = URL + "/Moffice/User/UserRemoveBinding.html";

    /**
     * 公告-所有公告
     */

    public static final String URL_ALL_NOTICE = URL + "/Moffice/Company/GetNoticeList.html";
    /**
     * 公告-未读公告
     */

    public static final String URL_UNREAD_NOTICE = URL + "/Moffice/Company/UnreadNotice.html";

    /**
     * 公告-已读公告
     */

    public static final String URL_READ_NOTICE = URL + "/Moffice/Company/ReadNotice.html";

    /**
     * 公告-公告发布
     */
    public static final String URL_PUBLISH_NOTICE = URL + "/Moffice/Company/PublishNotice.html";

    /**
     * 公告-公告删除
     */
    public static final String URL_DELETE_NOTICE = URL + "/Moffice/Company/DeleteNotice.html";

    /**
     * 动态-获取系统通知列表
     */
    public static final String URL_SYSTEM_NOTICE = URL + "/Moffice/Comm/GetSystemNotice.html";

    /**
     * 日报-我收到的日报
     */

    public static final String URL_RECEIVE_DAILY = URL + "/Moffice/Daily/ReceiveDaily.html";

    /**
     * 日报-我发送的日报
     */
    public static final String URL_SEND_DAILY = URL + "/Moffice/Daily/SendDaily.html";

    /**
     * 日报详情
     */
    public static final String URL_DETAIL_DAILY = URL + "/Moffice/Daily/DetailDaily.html";

    /**
     * 日报-添加日报
     */
    public static final String URL_ADD_DAILY = URL + "/Moffice/Daily/AddDaily.html";

    /**
     * 日报-删除日报
     */
    public static final String URL_DELETE_DAILY = URL + "/Moffice/Daily/DeleteDaily.html";

    /**
     * 会议-我收到的会议列表
     */
    public static final String URL_GET_MEETING_LIST = URL + "/Moffice/Company/GetMeetingList.html";

    /**
     * 会议-获取我发送的会议列表
     */
    public static final String URL_GET_MEETING_MYSEND_LIST = URL + "/Moffice/Company/GetMeetingMySendList.html";
    /**
     * 会议-发送会议通知
     */
    public static final String URL_SEND_METTING = URL + "/Moffice/Company/SendMeeting.html";

    /**
     * 会议-删除会议通知
     */
    public static final String URL_DELETE_MEETING = URL + "/Moffice/Company/DeleteMeeting.html";
    /**
     * 获取短信验证码
     */

    public static final String URL_GET_PHONE_SIGN = URL + "/Moffice/Index/GetPhoneSign.html";

    /**
     * 我的-用户注册
     */
    public static final String URL_REGISTER = URL + "/Moffice/User/Register.html";

    /**
     * 名片-名片信息识别返回
     */
    public static final String URL_BUSINESS_CARD_INFO = URL + "/Moffice/BusinessCard/GetBusinessCardInfo.html";

    /**
     * 名片-编辑名片
     */
    public static final String URL_EDIT_CARD = URL + "/Moffice/BusinessCard/EditCard.html";
    /**
     * 名片-最近添加的名片(最近三天)
     */
    public static final String URL_LATELY_CARD = URL + "/Moffice/BusinessCard/LatelyCard.html";
    /**
     * 名片-自己的名片
     */
    public static final String URL_MINE_CARD = URL + "/Moffice/BusinessCard/MineCard.html";
    /**
     * 名片-名片夹
     */
    public static final String URL_CARD_LIST = URL + "/Moffice/BusinessCard/CardList.html";

    /**
     * 名片 - 名片转存
     */
    public static final String URL_CARD_SHARD_DATA_IN_MYCARD = URL + "/Moffice/BusinessCard/CardShareDataInMyCard.html";

    /**
     * 名片-删除名片
     */
    public static final String URL_DELETE_CARD = URL + "/Moffice/BusinessCard/DeleteCard.html";
    /**
     * 名片-名片详情
     */
    public static final String URL_CARD_DETAIL = URL + "/Moffice/BusinessCard/CardDetail.html";

    /**
     * 签到-签到页面显示
     */
    public static final String URL_SIGN_DETAIL = URL + "/Moffice/Sign/SignDetail.html";

    /**
     * 签到 - 签到页面显示 V2
     */
    public static final String URL_SIGN_DETAIL_V2 = URL + "/Moffice/Sign/SignDetail_V2.html";
    /**
     * 签到-当前时间UNIX时间戳
     */
    public static final String URL_NOW_UNIX_TIME = URL + "/Moffice/Sign/NowUnixTime.html";

    /**
     * 签到-外勤签到页面显示
     */
    public static final String URL_OUT_SIGN_DETAIL = URL + "/Moffice/Sign/OutSignDetail.html";

    /**
     * 考勤-签到-上下班签到 外勤签到
     */
    public static final String URL_SING = URL + "/Moffice/Sign/Sign.html";

    /**
     * 考勤 - 外勤签到记录列表
     */
    public static final String URL_OUT_SING_LIST = URL + "/Moffice/Sign/OutSignList.html";

    /**
     * 考勤-签到-获取签到设置
     */
    public static final String URL_SIGN_CONF = URL + "/Moffice/Sign/SignConf.html";

    /**
     * 签到 - 获取签到设置 V1.5.0
     */
    public static final String URL_SIGN_CONF_V2 = URL + "/Moffice/Sign/SignConf_V2.html";

    /**
     * 考勤-签到-修改签到设置
     */
    public static final String URL_EDIT_SING_CONF = URL + "/Moffice/Sign/EditSignConf.html";

    /**
     * 签到 - 修改签到时间工作日V1.5.0
     */
    public static final String URL_UPDATE_SIGN_CONF_TIME = URL + "/Moffice/Sign/UpdateSignConfigTime.html";

    /**
     * 签到 - 增加签到地址v1.5.0
     */
    public static final String URL_UPDATE_SIGN_CONF_ADDRESS = URL + "/Moffice/Sign/UpdateSignConfigAddress.html";

    /**
     * 签到-删除办公地点v1.5.0
     */
    public static final String URL_DELETE_SIGN_CONF_ADDRESS = URL + "/Moffice/Sign/DeleteSignConfigAddress.html";

    /**
     * 考勤-签到-统计 我的考勤
     */
    public static final String URL_STATISTICAL_MY = URL + "/Moffice/Sign/StatisticalMy.html";

    /**
     * 考勤-签到-统计 团队考勤 日报
     */
    public static final String URL_STATISTICAL_TEAM_DAY = URL + "/Moffice/Sign/StatisticalTeamDay.html";

    /**
     * 考勤-签到-统计 团队考勤 月报
     */
    public static final String URL_STATISTICAL_TEAM_MONTH = URL + "/Moffice/Sign/StatisticalTeamMonth.html";

    /**
     * 获取二维码扫描信息
     */
    public static final String URL_GET_QRCODE_INFO = URL + "/Moffice/Index/GetQrcodeInfo.html";

    /**
     * 客户-获取 客户/潜在列表
     */
    public static final String URL_GET_CUSTOMER_LIST = URL + "/Moffice/Customer/GetCustomerList.html";

    /**
     * 客户-添加修改 客户/潜在客户 信息
     */
    public static final String URL_UPDATE_CUSTOMER = URL + "/Moffice/Customer/UpdateCustomer.html";

    /**
     * 客户-客户地图
     */
    public static final String URL_CUSTOMER_MAP = URL + "/Moffice/Customer/CustomerMap.html";

    /**
     * 客户-潜在客户跟进
     */
    public static final String URL_GET_CUSTOMER_FOLLOW_LIST = URL + "/Moffice/Customer/GetCustomerFollowList.html";

    /**
     * 客户-删除客户/潜在客户
     */
    public static final String URL_DELETE_CUSTOMER = URL + "/Moffice/Customer/DeleteCustomer.html";

    /**
     * 客户-增加/编辑客户跟进
     */
    public static final String URL_UPDATE_CUSTOMER_FOLLOW = URL + "/Moffice/Customer/UpdateCustomerFollow.html";

    /**
     * 客户-删除潜在客户跟进
     */
    public static final String URL_DELETE_CUSTOMER_FOLLOW = URL + "/Moffice/Customer/DeleteCustomerFollow.html";
    /**
     * 企业认证
     */
    public static final String URL_COMPANY_AUTH = URL + "/Moffice/Company/CompanyAuth.html";

    /**
     * 图片上传
     */
    public static final String URL_UPLOAD_PIC = URL + "/Moffice/Index/UploadPic.html";

    /**
     * 客户-潜在客户公海
     */
    public static final String URL_GET_CUSTOMER_HIGH_SEAS = URL + "/Moffice/Customer/GetCustomerHighSeas.html";

    /**
     * 客户-设置公海超出时间
     */
    public static final String URL_UPDATE_HIGH_SEAS_CONF = URL + "/Moffice/Customer/UpdateHighSeasConf.html";

    /**
     * 客户-客户/潜在客户详情
     */
    public static final String URL_GET_CUSTOMER_DETAIL = URL + "/Moffice/Customer/GetCustomerDetail.html";

    /**
     * 客户-公海客户转存
     */
    public static final String URL_CUSTOMER_TRANSFER = URL + "/Moffice/Customer/CustomerTransfer.html";

    /**
     * 合同-合同列表
     */
    public static final String URL_GET_CONTRACT_LIST = URL + "/Moffice/Crm/GetContractList.html";

    /**
     * 合同-合同详情
     */
    public static final String URL_GET_CONTRACT_DETAIL = URL + "/Moffice/Crm/GetContractDetail.html";

    /**
     * 合同-添加编辑合同
     */
    public static final String URL_UPDATE_CONTRACT = URL + "/Moffice/Crm/UpdateContract.html";

    /**
     * 合同-删除合同
     */
    public static final String URL_DELETE_CONTRACT = URL + "/Moffice/Crm/DeleteContract.html";

    /**
     * 商机-商机列表
     */
    public static final String URL_GET_CHANCE_LIST = URL + "/Moffice/Crm/GetChanceList.html";

    /**
     * 商机 - 商机详情
     */
    public static final String URL_GET_CHANCE_DETAIL = URL + "/Moffice/Crm/GetChanceDetail.html";

    /**
     * 商机 - 增加修改商机信息
     */
    public static final String URL_UPDATE_CHANCE = URL + "/Moffice/Crm/UpdateChance.html";

    /**
     * 商机 - 删除商机
     */
    public static final String URL_DELETE_CHANCE = URL + "/Moffice/Crm/DeleteChance.html";

    /**
     * 商机 - 更新商机状态
     */
    public static final String URL_UPDATE_CHANCE_STATUS = URL + "/Moffice/Crm/UpdateChanceStatus.html";

    /**
     * 活动 - 活动列表
     */
    public static final String URL_GET_ACTIVITY_LIST = URL + "/Moffice/Crm/GetActivityList.html";

    /**
     * 活动 - 获取活动详情
     */
    public static final String URL_GET_ACTIVITY_DETAIL = URL + "/Moffice/Crm/GetActivityDetail.html";

    /**
     * 活动 - 添加修改活动
     */
    public static final String URL_UPDATE_ACTIVITY = URL + "/Moffice/Crm/UpdateActivity.html";

    /**
     * 活动 - 删除活动
     */
    public static final String URL_DELETE_ACTIVITY = URL + "/Moffice/Crm/DeleteActivity.html";

    /**
     * 线索 - 线索列表
     */
    public static final String URL_GET_CLUE_LIST = URL + "/Moffice/Crm/GetClueList.html";

    /**
     * 获取线索详情
     */
    public static final String URL_GET_CLUE_DETAIL = URL + "/Moffice/Crm/GetClueDetail.html";

    /**
     * 线索 - 添加修改线索
     */
    public static final String URL_UPDATE_CLUE = URL + "/Moffice/Crm/UpdateClue.html";

    /**
     * 线索 - 删除线索
     */
    public static final String URL_DELETE_CLUE = URL + "/Moffice/Crm/DeleteClue.html";
    /**
     * 客户 - 查找客户
     */
    public static final String URL_FIND_CUSTOMER = URL + "/Moffice/Customer/FindCustomer.html";

    /**
     * 广告联盟-列表
     */
    public static final String URL_ADTASK_LIST = URL + "/Moffice/AdSense/AdTaskList.html";

    /**
     * 广告联盟-我领取的任务列表
     */
    public static final String URL_MY_TASK_LIST = URL + "/Moffice/AdSense/MyAdTaskList.html";
    // IM START

    /**
     * IM App key
     */
    public static final String IM_APP_KEY = "qf3d5gbjqfboh";

    /**
     * IM App secret
     */
    public static final String IM_APP_SECRET = "L6Z1nIRanqIb";

    /**
     * IM 获获取 token
     */
    public static final String URL_IM_TOKEN = "http://api.cn.ronghub.com/user/getToken.json";


    // IM END

    public static final String DIALOG_TAG_LOADING = "loading";

    public static final String KEY_ID = "id";
    public static final String KEY_ID_2 = "id_2";
    public static final String KEY_DATA = "data";
    public static final String KEY_ACTION_TYPE = "actionType";
    public static final String KEY_ITEM_TYPE = "ItemType";
    public static final String KEY_PARENT_POS = "parentPos";
    public static final String KEY_CHILD_POS = "childPos";
    public static final String KEY_NAME = "name";
    public static final String KEY_PARAM = "param";
    public static final String KEY_ENTITY = "entity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MSG = "msg";

    public static final String VALUE_TYPE_C = "create";
    public static final String VALUE_TYPE_R = "read";
    public static final String VALUE_TYPE_U = "update";
    public static final String VALUE_TYPE_D = "delete";
    public static final String VALUE_TYPE_ITEM_PARENT = "parent";
    public static final String VALUE_TYPE_ITEM_CHILD = "child";
    public static final String VALUE_TYPE_REQUEST_TOKEN = "requestToken";


    public static final int KEY_TAG_ACTION_TYPE = 0;
    public static final int KEY_TAG_ITEM_TYPE = 1;

    public static final int REQUEST_NAME = 0;
    public static final int REQUEST_ENTITY = 0;
    public static final int REQUEST_ENTITY_2 = 1;

    /**
     * 工作-获取公司成员列表
     */
    public static final String URL_GET_COMPANYUSERLIST = URL + "/Moffice/Task/CompanyUserList.html";

    /**
     * 工作-任务列表
     */
    public static final String URL_GET_TASK=URL+"/Moffice/Task/GetTask.html";

    /**
     * 工作-任务列表V2
     */
    public static final String URL_GET_TASK_V2 = URL + "/Moffice/Task/GetTask_V2.html";
    /**
     * 工作-任务-修改/增加
     */
    public static final String URL_GET_UPDATETASK = URL + "/Moffice/Task/UpdateTask.html";

    /**
     * 工作-任务-修改/增加 V2
     */
    public static final String URL_GET_UPDATE_TASK_V2 = URL + "/Moffice/Task/UpdateTask_V2.html";

    /**
     * 工作-删除任务
     */
    public static final String URL_GET_TASKDELETE=URL+"/Moffice/Task/TaskDelete.html";

    /**
     * 工作-任务详情
     */
    public static final String URL_GET_TASKINFO=URL+"/Moffice/Task/TaskInfo.html";

    /**
     * 工作-任务详情  V2
     */
    public static final String URL_GET_TASKINFO_V2 = URL + "/Moffice/Task/TaskInfo_V2.html";

    /**
     * 工作-完成工作
     */
    public static final String URL_GET_TASKFINSH= URL +"/Moffice/Task/TaskToFinsh.html";

    /**
     * 工作-取消工作
     */
    public static final String URL_TASK_CANCEL = URL + "/Moffice/Task/TaskCancel.html";

    /**
     * 工作-已完成工作
     */
    public static final String URL_GET_TASKFINSHLIST= URL +"/Moffice/Task/TaskFinshed.html";

    /**
     * 工作-日程列表
     */
    public static final String URL_GET_SCHEDULE= URL +"/Moffice/Task/ScheduleList.html";

    /**
     * 工作-增加修改日程
     */
    public static final String URL_GET_UPDATESCHEDULE= URL +"/Moffice/Task/UpdateSchedule.html";

    /**
     * 工作-删除日程
     */
    public static final String URL_DELETE_SCHEDULE = URL + "/Moffice/Task/DeleteSchedule.html";

    /**
     * 动态
     */
    public static final String URL_DYNAMIC_LIST=URL+"/moffice/dynamic/getdynamiclist.html";
    public static final String URL_DYNAMIC_DETAIL=URL+"/moffice/dynamic/getdynamicmsg.html";
    public static final String URL_DYNAMIC_RELEASE=URL+"/moffice/dynamic/adddynamiccomment.html";


    /**
     * 点赞、取消点赞
     */
    public static final String URL_PRAISE=URL+"/moffice/dynamic/likedynamic.html";

    /**
     * 联系人-获取联系人列表
     *
     */
    public static final String URL_CONTACTS_LIST=URL+"/Moffice/Contacts/GetContactsList.html";
    /**
     * 联系人-获取联系人列表
     *
     */
    public static final String URL_CONTACTS_LIST2=URL+"/Moffice/Contacts/GetContactsList_V2.html";

    /**
     *  我的-用户信息修改
     */
    public static final String URL_MINE_CHANGE_USERINFO=URL+"/Moffice/User/UpdateUserInfo.html";
    /**
     *  首页——工作-工作页面
     */
    public static final String URL_HOME_WORK=URL+"/Moffice/Task/GetWorkDetail_V2.html";
    /**
     *  日程详情
     */
    public static final String URL_SCHEDULE_DETAIL=URL+"/Moffice/Task/ScheduleDetail.html";

    /**
     * 获取用户头像信息接口  /
     */
    public static final String URL_GET_USER_HEAD_NAME=URL+"/Moffice/User/GetUserChatInfo.html";

    /**
     *  点赞 /取消点赞  moffice/dynamic/likedynamic.html
     */
    public static final String URL_PRAISE_UNPRAISE=URL+"/Moffice/User/GetUserChatInfo.html";

    /**
     *  删除评论
     */
    public static final String URL_DEL_COMMENT=URL+"/Moffice/dynamic/deldynamicmsg.html";

    /**
     *  发布动态
     */
    public static final String URL_ADD_DYNAMIC=URL+"/Moffice/dynamic/adddynamic.html";
    /**
     *  删除动态
     */
    public static final String URL_DEL_DYNAMIC=URL+"/Moffice/dynamic/deldynamiccomment.html";
    /**
     *  找回密码
     */
    public static final String URL_FIND_PASS=URL+"/Moffice/User/RetrievePassword.html";

    /**
     * ERP
     */

      /**
     * 采购-供货商管理
     */
    public static final String URL_SUPPLIER_MANAGER=URL+"/Moffice/Purchase/GetSupplierList.html";
      /**
     * 采购-供货商详情
     */
    public static final String URL_SUPPLIER_DETAIL=URL+"/Moffice/Purchase/SupplierDetail.html";

      /**
     * 采购-删除供货商
     */
    public static final String URL_SUPPLIER_DEL=URL+"/Moffice/Purchase/DeleteSupplier.html";
      /**
     * 采购-增加修改供货商信息
     */
    public static final String URL_SUPPLIER_UPDATE=URL+"/Moffice/Purchase/UpdateSupplier.html";
      /**
     * 采购-采购订单列表
     */
    public static final String URL_PURCHASE_LIST=URL+"/Moffice/Purchase/GetPurchaseOrderList.html";
      /**
     * 采购-采购订单入库列表
     */
    public static final String URL_PURCHASE_IN_LIST=URL+"/Moffice/Purchase/GetPurchaseOrderPutStoreList.html";
      /**
     * 采购-订单详情
     */
    public static final String URL_PURCHASE_DETAIL=URL+"/Moffice/Purchase/PurchaseOrderDetail.html";

    /**
     * 采购-删除采购订单
     */
    public static final String URL_PURCHASE_DELETE=URL+"/Moffice/Purchase/DeletePurchaseOrder.html";

      /**
     * 采购-增加修改采购订单
     */
    public static final String URL_PURCHASE_UPDATE=URL+"/Moffice/Purchase/UpdatePurchaseOrder.html";

    /*************/
    /**
     * 仓库 获取仓库列表
     */
    public static final String URL_STORE_LIST=URL+"/Moffice/Store/GetStoreList.html";
    /**
     * 仓库 仓库详情
     */
    public static final String URL_STORE_DETAIL=URL+"/Moffice/Store/GetStoreDetail.html";
    /**
     * 仓库 增加编辑仓库
     */
    public static final String URL_STORE_UPDATE=URL+"/Moffice/Store/UpdateStore.html";
    /**
     * 仓库 删除仓库
     */
    public static final String URL_STORE_DELETE=URL+"/Moffice/Store/DeleteStore.html";
    /**
     * 仓库 -获取货位列表
     */
    public static final String URL_CARGO_LIST=URL+"/Moffice/Store/GetStoreGoodsList.html";
    /**
     * 仓库 -获取货位详情
     */
    public static final String URL_CARGO_DETAIL=URL+"/Moffice/Store/GetStoreGoodsDetail.html";
    /**
     * 仓库 -增加编辑货位
     */
    public static final String URL_CARGO_UPDATE=URL+"/Moffice/Store/UpdateStoreGoods.html";

    /**
     * 仓库 -删除货位
     */
    public static final String URL_CARGO_DELETE=URL+"/Moffice/Store/DeleteStoreGoods.html";
    /**
     * 仓库 -入库列表
     */
    public static final String URL_INSTORE_LIST=URL+"/Moffice/Store/GetStorePutList.html";
    /**
     * 仓库 -入库详情
     */
    public static final String URL_INSTORE_DETAIL=URL+"/Moffice/Store/GetStorePutDetail.html";
    /**
     * 仓库 -增加编辑入库
     */
    public static final String URL_INSTORE_UPDATE=URL+"/Moffice/Store/UpdateStorePut.html";
    /**
     * 仓库 -删除入库
     */
    public static final String URL_INSTORE_DELETE=URL+"/Moffice/Store/DeleteStorePut.html";

    /**
     * 仓库 -出库列表
     */
    public static final String URL_OUTSTORE_LIST=URL+"/Moffice/Store/GetStoreOutList.html";

    /**
     * 仓库 -出库详情
     */
    public static final String URL_OUTSTORE_DETAIL=URL+"/Moffice/Store/GetStoreOutDetail.html";

    /**
     * 仓库 -增加编辑出库
     */
    public static final String URL_OUTSTORE_UPDATE=URL+"/Moffice/Store/UpdateStoreOut.html";

    /**
     * 仓库 -删除出库
     */
    public static final String URL_OUTSTORE_DELETE=URL+"/Moffice/Store/DeleteStoreOut.html";



    /**
     * 仓库-库存列表
     */
    public static final String URL_STOCK_LIST=URL+"/Moffice/Store/GetStoreRemainList.html";

    /**
     * 仓库-库存详情
     */
    public static final String URL_STOCK_DETAIL=URL+"/Moffice/Store/GetStoreRemainDetail.html";

    /**
     * 仓库-增加编辑库存
     */
    public static final String URL_STOCK_UPDATE=URL+"/Moffice/Store/UpdateStoreRemain.html";

    /**
     * 仓库-删除库存
     */
    public static final String URL_STOCK_DELETE=URL+"/Moffice/Store/DeleteStoreRemain.html";

    /**************/
    /**
     * 商品 -商品列表
     */
    public static final String URL_GOODS_LIST=URL+"/Moffice/Goods/GetGoodsList.html";
    /**
     * 商品 -商品详情
     */
    public static final String URL_GOODS_DETAIL=URL+"/Moffice/Goods/GetGoodsDetail.html";
    /**
     * 商品 -增加编辑商品
     */
    public static final String URL_GOODS_UPDATE=URL+"/Moffice/Goods/UpdateGoods.html";
    /**
     * 商品 -删除商品
     */
    public static final String URL_GOODS_DEL=URL+"/Moffice/Goods/DeleteGoods.html";
    /**
     * 商品 -商品分类
     */
    public static final String URL_GOODS_TYPE=URL+"/Moffice/Goods/GetGoodsTypeList.html";

    /**********销售**********/
    /**
     * 销售-销售订单列表
     */
    public static final String URL_SALE_ORDER_LIST=URL+"/Moffice/Sale/GetSaleOrderList.html";
    /**
     * 销售-销售订单详情
     */
    public static final String URL_SALE_ORDER_DETAI=URL+"/Moffice/Sale/GetSaleOrderDetail.html";
    /**
     * 销售-增加修改销售订单
     */
    public static final String URL_SALE_ORDER_UPDATE=URL+"/Moffice/Sale/UpdateSaleOrder.html";
    /**
     * 销售-删除销售订单
     */
    public static final String URL_SALE_ORDER_DELETE=URL+"/Moffice/Sale/DeleteSaleOrder.html";

    /**
     * 团队考勤——月报  考勤统计
     */
    public static final String URL_TEAM_MONTH=URL+"/Moffice/Sign/StatisticalTeamMonth.html";

    /**
     * OA——移动办公-请假,加班,出差,报销申请
     */
    public static final String URL_OA_APPLY=URL+"/Moffice/Apply/SubmitApplications.html";
    /**
     * OA——移动办公-请假,加班,出差,报销详情
     */
    public static final String URL_OA_APPLY_DETAIL=URL+"/Moffice/Apply/GetApplyDetail.html";
    /**
     * OA——我发起的
     */
    public static final String URL_OA_MY_LAUCH=URL+"/Moffice/Apply/GetMyApply.html";
    /**
     * OA——抄送给我的
     */
    public static final String URL_OA_COPY_SEND_TO_ME=URL+"/Moffice/Apply/GetMyCopyApply.html";

    /**
     * OA——催办
     */
    public static final String URL_OA_REMINDERS=URL+"/Moffice/Apply/HastenApply.html";

    /**
     * OA——撤销
     */
    public static final String URL_OA_REVOKE=URL+"/Moffice/Apply/RepealApply.html";

    /**
     * OA——移动办公-我审批的申请
     */
    public static final String URL_MY_APROVAL_APPLY=URL+"/Moffice/Apply/GetMyApproveApply.html";

    /**
     * OA——移动办公-审核通过申请
     */
    public static final String URL_AGREE_APPLY=URL+"/Moffice/Apply/AgreeApply.html";

    /**
     * OA——移动办公-审核驳回申请
     */
    public static final String URL_REJECT_APPLY=URL+"/Moffice/Apply/RejectApply.html";

    /**
     * OA——移动办公-审核转交申请
     */
    public static final String URL_TRANSFER_APPLY=URL+"/Moffice/Apply/SendOnApply.html";

    /**
     * 生成二维码的接口信息
     */
    public static final String URL_ER_Q=URL+"/Moffice/Index/GetQrcodeData.html";

    /**
     * 生成二维码的接口信息
     */
    public static final String URL_EXIT=URL+"/Moffice/User/Quit.html";

    /**
     * 消息 公告助手
     */
    public static final String URL_NOTICEASSISTANT = URL+"/Moffice/Message/NoticeAssistant.html";
    /**
     * 消息 删除 公告助手
     */
    public static final String URL_DEL_NOTICEASSISTANT = URL+"/Moffice/Company/DeleteNotice.html";

    /**
     * 消息 日程通知
     */
    public static final String URL_SCHEDULE_NOTICE = URL+"/Moffice/Message/TaskMsg.html";
    /**
     * 消息 会议通知
     */
    public static final String URL_METTING_ASSISTENT = URL+"/Moffice/Message/MeetingMsg.html";
    /**
     * 消息 删除会议通知
     */
    public static final String URL_DEL_METTING_ASSISTENT = URL+"/Moffice/Company/DeleteMeeting.html";

    /**
     * 消息 任务助手
     */
    public static final String URL_TASK_ASSISTENT = URL+"/Moffice/Message/TaskAssistant.html";

    /**
     * 消息 审批助手
     */
    public static final String URL_TAPROVAL_ASSISTENT = URL+"/Moffice/Message/ApplyAssistant.html";
    /**
     * 消息 审批助手
     */
    public static final String URL_VIP = URL+"/Moffice/User/GetVIPCard.html";

    /**
     *  会议详情
     */

    public static final String URL_METTINGDETAIL = URL+"/Moffice/Company/GetMeetingDetail.html";

    /**
     * 助手  删除
     */
    public static final String URL_ASSISTENT_DEL = URL+"/Moffice/Message/DeleteMessage.html";

    /**
     *    广告联盟 领取并发布任务
     */
    public static final String URL_AD_TASK_SETTINGS = URL+"/Moffice/AdSense/ReceivePublishAdTask.html";

    /**
     *    广告联盟 广告详情
     */
    public static final String URL_AD_DETAIL = URL+"/Moffice/AdSense/AdDetail.html";
    /**
     *    广告联盟 分享url
     */
    public static final String URL_AD_SHARE = URL+"/Moffice/AdSense/AdArticleShare.html";

}
