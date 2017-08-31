package com.szmy.pigapp.utils;

public class UrlEntry {
	public static final String ip = "http://www.my360.cn";// ip地址
//	public static final String ip = "http://171.8.252.29:9101";// ip地址
//		public static final String ip = "http://192.168.0.165:8888";//
//	 ip地址
//	 public static String ip = "http://192.168.0.248:8888";//
//	 public static String ip = "http://192.168.0.175:8081";//
	 public static String ip2 = "http://192.168.0.175:8080/dspt";//
	// public static final String ip =
	// "http://182.116.57.252:8201/animalshop";// ip地址
	public static final String LOGIN_URL = ip + "/appUser!login";// 登录
	public static final String REISTER_URL = ip + "/appUser!regist";// 注册
	public static final String UPLOAD_USERINFO_URL = ip + "/appUser!upload";// 修改个人头像
	public static final String UPDATE_USERINFO_URL = ip + "/appUser!update";// 修改个人头像
	public static final String QUERY_ALL_URL = ip + "/appOrder!selectList";// 查询所有出售信息
    public static final String QUERY_HOT_ALL_URL = ip + "/appOrder!selectListByPriority.action";// 查询所有信息
	public static final String UPLOAD_FILE_URL = ip + "/appOrder!upload";// 上传图片
	public static final String DELETE_MYINFO_BYID_URL = ip + "/appOrder!delete";// 根据id删除信息
	public static final String UPDATE_MYINFO_BYID_URL = ip + "/appOrder!update";// 根据id修改信息
	public static final String ADD_MYINFO_URL = ip + "/appOrder!insert";// 新增发布信息
	public static final String INSERT_PIGFARM_URL = ip + "/appPigFarm!insert";// add认证猪场
	public static final String QUERY_INFODETAIL_URL = ip
			+ "/appOrder!showOrder";// 查询商品详情
	public static final String CHECK_VERSION_URL = ip
			+ "/appVersions!getNewest";// 检查版本更新
	public static final String PAY_URL = ip + "/appOrder!toPay";// 支付 [参数 msg]
	public static final String GETSTRING_URL = ip + "/appOrder!getString";
	public static final String QUERY_PIGFARM_URL = ip + "/appPigFarm!toEdit"; // uuid查询猪场个人认证
	public static final String UPDATE_PIGFARM_URL = ip + "/appPigFarm!update";// 修改认证猪场
	public static final String QUERY_AGENT_URL = ip + "/appBroker!toEdit"; // uuid查询经纪人个人认证
	public static final String INSERT_AGENT_URL = ip + "/appBroker!insert";// add认证经纪人
	public static final String UPDATE_AGENT_URL = ip + "/appBroker!update";// 修改认证经纪人
	public static final String QUERY_SLAUGHTERHOUSE_URL = ip
			+ "/appSlaughter!toEdit"; // uuid查询屠宰场个人认证

	public static final String QUERYBYID_SLAUGHTERHOUSE_URL = ip
			+ "/appSlaughter!queryOne"; // id 查询屠宰场个人认证
	public static final String INSERT_SLAUGHTERHOUSE_URL = ip
			+ "/appSlaughter!insert";// add认证屠宰场
	public static final String UPDATE_SLAUGHTERHOUSE_URL = ip
			+ "/appSlaughter!update";// 修改认证屠宰场
	public static final String QUERY_INDUSTRY_URL = ip
			+ "/appUser!getBusinessList";// 行业
	public static final String QUERY_MYADDRESS_URL = ip
			+ "/szmyAddressActionFront!selectList.action";// 收货地址
	public static final String DELETE_MYADDRESS_URL = ip
			+ "/szmyAddressActionFront!deletes.action";// 删除收货地址
	public static final String INSERT_MYADDRESS_URL = ip
			+ "/szmyAddressActionFront!insert.action";// 新增收货地址
	public static final String UPDATE_MYADDRESS_URL = ip
			+ "/szmyAddressActionFront!update.action";// 修改收货地址
	public static final String QUERY_PIGFARM_LIST_URL = ip
			+ "/appPigFarm!selectListOrder";// 查询猪场列表
	public static final String QUERY_SLAUGHTER_LIST_URL = ip
			+ "/appSlaughter!selectListOrder";// 查询屠宰场列表
	public static final String QUERY_AGENT_LIST_URL = ip
			+ "/appBroker!selectListOrder";// 查询经纪人列表
	public static final String QUERY_NEAR_PIGFARM_URL = ip
			+ "/appPigFarm!selectByXY";// 根据坐标点查询附近数据(猪场)
	public static final String QUERY_NEAR_AGENT_URL = ip
			+ "/appBroker!selectByXY";// 根据坐标点查询附近数据(经纪人)
	public static final String QUERY_NEAR_SLAUGHTER_URL = ip
			+ "/appSlaughter!selectByXY";// 根据坐标点查询附近数据(屠宰场)
	public static final String QUERY_NEAR_VEHICLECOMPANY_URL = ip
			+ "/appVehicleCompanyFront!selectByXY";// 根据坐标点查询附近数据(物流)
	public static final String QUERY_AreaMap_PIGFARM_URL = ip
			+ "/appPigFarm!getAreaMapJSON";// 根据全国/省名称 查询子地区 数量(猪场)

	public static final String QUERY_AreaMap_SLAUGHTER_URL = ip
			+ "/appSlaughter!getAreaMapJSON";// 根据全国/省名称 查询子地区 数量(屠宰场)
	public static final String QUERY_AreaMap_VEHICLECOMPANY_URL = ip
			+ "/appVehicleCompanyFront!getAreaMapJSON";// 根据全国/省名称 查询子地区 数量(物流)

	public static final String QUERY_NEWS_BYID_URL = ip + "/jsp/notices/";// 根据id查看新闻详情
	public static final String QUERY_NEWS_LIST_URL = ip
			+ "/szmyAppNewsActionFront!newsList.action";// 查询新闻列表
	public static final String QUERY_VEHICLECOMPANY_LIST_URL = ip
			+ "/appVehicleCompanyFront!selectList";// 查询物流企业
	public static final String VEHICLECOMPANY_INSERTAUTH_URL = ip
			+ "/appVehicleCompanyFront!insert";// 新增认证物流企业/个人
	public static final String VEHICLECOMPANY_UPDATEAUTH_URL = ip
			+ "/appVehicleCompanyFront!update";// 修改认证物流企业/个人
	public static final String QUERY_VEHICLECOMPANY_BYID_URL = ip
			+ "/appVehicleCompanyFront!selectById";// 根据id查物流企业/个人 详情
	public static final String QUERY_VEHICLECOMPANY_BYUUID_URL = ip
			+ "/appVehicleCompanyFront!toEdit";// 根据uuid查询物流企业/个人详情
	public static final String QUERY_VEHIVLE_ALL_URL = ip
			+ "/appVehicle!selectList"; // 查询所有物流信息
	public static final String DELETE_VEHIVLE_BYID_URL = ip
			+ "/appVehicle!deleteById";// 删除物流信息
	public static final String UPDATE_VEHIVLE_BYID_URL = ip
			+ "/appVehicle!update";// 修改物流信息
	public static final String ADD_VEHIVLE_BYID_URL = ip + "/appVehicle!insert";// 新增物流信息
	public static final String UPLOAD_VEHIVLEFILE_URL = ip
			+ "/appVehicle!upload";// 上传图片
	public static final String QUERY_VEHIVLE_BYID_URL = ip + "/appVehicle!show";// 查看物流详情
	public static final String PAY_VEHIVLEORDER_URL = ip
			+ "/appVehicle!getString";// 支付物流订单
	public static final String QUERY_VEHIVLEORDER_URL = ip
			+ "/appVehicle!selectList";// 查询物流订单
	public static final String EXIT_VEHIVLEORDER_URL = ip
			+ "/appVehicle!delete";// 取消物流订单
	public static final String QUERY_ALL_ORDER_URL = ip + "/appOrder!selectAll"; // 查询所有未下单信息
	public static final String QUERY_ALL_ORDER_AND12h_URL = ip
			+ "/appOrder!selectAllAnd12h"; // 查询所有未下单信息
	/****************************
	 * appProductFront!productList.action?catalogCode=
	 **/
	public static final String QUERY_PRODUCT_ALL_URL = ip
			+ "/appProduct!productList.action";// 商品信息
	public static final String QUERY_PRODUCT_TYPE_URL = ip
			+ "/appProduct!getCatalogs";// 商品类别
	public static final String QUERY_PRODUCT_BYNAME_URL = ip
			+ "/appProduct!search.action";// 根据关键字查询
	public static final String QUERY_PRODUCT_BYID_URL = ip
			+ "/appProduct!product";// 根据关键字查询
	public static final String PAY_ORDER_URL = ip
			+ "/szmyOrdersActionFront!toPay";// 支付
	public static final String QUERY_ORDER_URL = ip
			+ "/szmyOrdersActionFront!getOrdersList";// 查询订单
	// public static final String UPDATE_ORDER_STATUS_URL = ip +
	// "/szmyOrdersActionFront!updateOrderStatus";//修改订单状态
	public static final String UPDATE_ORDER_STATUS_URL = ip
			+ "/szmyOrdersActionFront!receive";// 确认收货
	public static final String ISFLAG_PAY_URL = ip + "/appUser!sfzfdj"; // 是否支付定金
	// //
	// orderType;//WLDJ物流定金,SZDJ生猪定金
	public static final String INSERT_LOCATION_URL = ip + "/appLocation!insert";// 上传用户位置信息
	public static final String QUERY_UUID_BYNAME_URL = ip
			+ "/appUser!getUserByName"; // 根据用户名找到当前用户
	public static final String RESET_PASSWORD_BYUUID_URL = ip
			+ "/appUser!resetPassword";// 重置用户密码
	public static final String INSERT_COLLECT_URL = ip
			+ "/szmyFavoriteAction!insert";// 增加收藏
	public static final String DELETE_COLLECT_URL = ip
			+ "/szmyFavoriteAction!deleteById"; // 取消收藏
	public static final String QUERY_MYCOLLECT_URL = ip
			+ "/szmyFavoriteAction!selectList"; // 查询我的收藏
	public static final String SELECT_ALL_VEHICLE_URL = ip
			+ "/appVehicle!selectAll"; // 在地圖上查詢所有未下单的物流信息
	public static final String INSERT_EVALUATE_URL = ip + "/szmyComment!insert"; // 插入评价
	public static final String SELECT_EVALUATE_URL = ip
			+ "/szmyComment!selectList";// 查询评价
	public static final String SELECT_INTEGRAL_URL = ip
			+ "/appUser!getIntegral";// 查看积分
	public static final String SELECT_INTEGRAL_LIST_URL = ip
			+ "/appTuiGuang!selectList";// 获取积分详情列表
	public static final String GET_BANNER_LIST_URL = ip
			+ "/appUser!getBannerList";// 获取banner列表
	public static final String GET_COUNT_TUIGUANG = ip
			+ "/appTuiGuang!getCount";// 查询推广人数
	/**
	 * 自然人
	 **/
	public static final String NATURAL_QUERYBYID_URL = ip
			+ "/appNatural!toEdit";// 查询
	public static final String NATURAl_UPDATE_URL = ip + "/appNatural!update";// 修改自然人
	public static final String NATURAl_INSERT_URL = ip + "/appNatural!insert";// 新增自然人
	/**
	 * 今日价格
	 **/
	public static final String TODAY_PIGPRICE_URL = ip
			+ "/appUser!getToDayPeice";// 今日价格
	/**
	 * 查询运营商
	 **/
	public static final String NEAR_COMPANY_URL = ip
			+ "/appProduct!getNearbyCompany.action";
	/**
	 * 团购页面
	 **/
	public static final String GROUP_BUYING_URL = ip
			+ "/mAProduct/toLimitTime.html?uuid=";
	/**
	 * 牧易商城m版
	 **/
	public static final String MOBILE_PHONE_URL = ip
			+ "/szmyMobie/index.jsp?uuid=";// 牧易商城m版
	/**
	 * 积分商城
	 **/
	public static final String INTEGRAL_SHOP_URL = ip
			+ "/pointsMall/index.jsp?uuid=";
	/**
	 * 签到
	 **/
	public static final String ADD_SIGN_URL = ip + "/appUser!signIn";
	/**
	 * 查询是否签到
	 **/
	public static final String QUERY_ISSIGN_URL = ip + "/appUser!getSignInInfo";
	/**
	 * 报猪价
	 **/
	public static final String ADD_MYPIGPRICE_URL = ip + "/appQuote!insert";
	/**
	 * 查询我发布的猪价
	 ***/
	public static final String QUERY_MYPIGPRICE_URL = ip
			+ "/appQuote!selectList";
	/**
	 * 收益积分列表
	 **/
	public static final String QUERY_PROFIT_URL = ip + "/appProfit!selectList";
	/**
	 * 查询附近报价列表
	 */
	public static final String QUERY_NEARBY_QUOTE_URL = ip
			+ "/appQuote!getNearbyList";
	/**
	 * 认证检疫员
	 **/
	public static final String INSERT_JYY_URL = ip + "/appJyy!insert";
	/**
	 * 查询个人检疫员认证
	 **/
	public static final String QUERY_JYY_URL = ip + "/appJyy!toEdit";
	/**
	 * 编辑个人检疫员认证
	 **/
	public static final String UPDATE_JYY_URL = ip + "/appJyy!update";
	/**
	 * 认证防疫员
	 **/
	public static final String INSERT_FYY_URL = ip + "/appFyy!insert";
	/**
	 * 查询个人防疫员认证
	 **/
	public static final String QUERY_FYY_URL = ip + "/appFyy!toEdit";
	/**
	 * 修改个人防疫员认证
	 **/
	public static final String UPDATE_FYY_URL = ip + "/appFyy!update";
	/**
	 * 认证监督员
	 **/
	public static final String INSERT_JDY_URL = ip + "/appJdy!insert";
	/**
	 * 查询个人监督员认证
	 ***/
	public static final String QUERY_JDY_URL = ip + "/appJdy!toEdit";
	/**
	 * 修改个人监督员认证
	 ***/
	public static final String UPDATE_JDY_URL = ip + "/appJdy!update";
	/**
	 * 平顶山检疫申报
	 **/
	public static final String QUERY_PDSJYSB_URL = "http://demo.pdsxmj.gov.cn/clientrcp/szmy_treat.action";
	/**
	 * 查询平顶山用户密码
	 **/
	public static final String QUERY_USER_PWD_URL = ip
			+ "/appUser!getConnectUserInfo";

	/**
	 * 查询报检
	 ***/
	public static final String QUERY_DECLARATION_URL = ip
			+ "/appDj!getDeclarationList";
	/**
	 * 附近旗舰店
	 ***/
	public static final String NEAR_SHOP_URL = ip
			+ "/szmyMobileProductActionFront!toProductList?compID=";

	/**
	 * 提交绑定银行卡
	 **/
	public static final String ADD_CARD_URL = ip + "/appUserBankFront!insert";
	/**
	 * 删除绑定银行卡
	 **/
	public static final String DEL_CARD_URL = ip + "/appUserBankFront!delete";
	/**
	 * 修改绑定的银行卡
	 **/
	public static final String UPP_CARD_URL = ip + "/appUserBankFront!update";
	/**
	 * 查询绑定的银行卡列表
	 **/
	public static final String QUERY_CARD_URL = ip
			+ "/appUserBankFront!getBankList";
	/**
	 * 积分提现
	 **/
	public static final String INTEGRAL_TO_CASH_URL = ip
			+ "/appTiXianActionFront!tiXian";
	/***
	 * 积分提示
	 ***/
	public static final String INTEGRAL_TIPTEXT_URL = ip + "/appTiXianActionFront!getTxInfo";
	/**
	 * 判断是否绑定银行卡
	 **/
	public static final String QUERY_ISBINDING_URL = ip
			+ "/appUserBankFront!bankTypeByUuid";
	/**
	 * 上传过磅单/白条肉单图片
	 **/
	public static final String UPLOAD_BILLIMG_URL = ip + "/appOrder!uploadBill";
	/**
	 * 根据id查询银行卡信息
	 **/
	public static final String QUERY_CARDBYUUID_URL = ip
			+ "/appUserBankFront!toEdit";
	/***
	 * 上传延期支付天数
	 ***/
	public static final String INSERT_DELAYDAY_URL = ip
			+ "/appOrder!updatePaymentDays";
	/**
	 * 根据发布信息匹配相关信息
	 ***/
	public static final String QUERY_LIKELIST_URL = ip
			+ "/appOrder!matchingOrder";
	/**
	 * 生猪交易订单区域地图查询
	 ***/
	public static final String QUERY_MAP_AREA = ip + "/appOrder!getAreaMapJSON";
	/**
	 * 查询我的所有关注
	 ***/
	public static final String QUERY_MYCARELIST_URL = ip
			+ "/friend!selectMyFriends.action";
	/**
	 * 新增关注
	 **/
	public static final String ADD_MYCARE_URL = ip + "/friend!insert.action";
	/**
	 * 取消关注
	 **/
	public static final String DELETE_MYCARE_URL = ip + "/friend!remove.action";
	/**
	 * 获取服务器省市县区域
	 ***/
	public static final String GET_ADDRESS_URL = ip + "/appUser!getAreaByPcode";
	/**
	 * 建议申报单
	 ***/
	public static final String QUERY_JYSB_LIST_URL = "http://192.168.0.239:8787/pdsxm/clientrcp/szmy_treat.action?api_method=c.sbadd";
	/***
	 * 根据坐标点查询附近经纪人
	 ***/
	public static final String QUERY_AGENT_NEARBY_URL = ip
			+ "/appBroker!selectByXY";
	/**
	 * 根据全国/省名称 查询子地区经纪人 数量
	 ***/
	public static final String QUERY_AGENT_AREA_URL = ip
			+ "/appBroker!getAreaMapJSON";
	/**
	 * 根据資訊id獲取詳情
	 ***/
	public static final String GET_NEWS_DETAILS_BY_ID_URL = ip
			+ "/szmyAppNewsActionFront!toEdit.action";
	/**
	 * 获取平顶山猪圈列表
	 **/
	public static final String GET_ZHUJUAN_LIST_URL = ip + "/appOrder!getZhuJuanList";
	/**
	 * 新增检疫申报pds
	 **/
	public static final String INSERT_PDSSB_URL = ip + "/appOrder!insertSB_pds";
	/**
	 * 扫码登录
	 **/
	public static final String SCAN_LOGIN_URL = ip + "/appUser!updateUuid";
	/**
	 * 获取签约状态和是否有支付密码
	 **/
	public static final String CHECK_TYPE_URL = ip + "/appUserBankFront!getQyAndPwd";

	/**
	 * 修改支付密码
	 **/
	public static final String SET_PAYPWD_URL = ip + "/appUser!updatePayPassword";
	/**
	 * 支付
	 ***/
	public static final String PHONE_PAY_URL = ip + "/appOrder!mobilePay";// 支付 [参数 msg]
	/***
	 * 注册协议
	 **/
	public static final String AGREEMENT_URL = "http://www.my360.cn/zmtzcxy.html";
	/****/
	public static final String JF_URL = "http://www.my360.cn/activity/zmt.jsp?img=zmt_jf";
	/**
	 * 更多天气
	 **/
	public static final String WEATAHER_MORE_URL = "http://apis.baidu.com/apistore/weatherservice/recentweathers";
	/***
	 * 免责声明
	 **/
	public static final String MZSM_URL = ip + "/appmzsm.html";
	/***
	 * 关于我们
	 ***/
	public static final String ABOUT_URL = ip + "/appgywm.html";
	/***
	 * 判断是否为管理员用户
	 */
	public static final String IS_ADMIN_URL = ip + "/appOrder!isAdmin";
	/***
	 * 获取管理员统计列表
	 */
	public static final String GET_ADMIN_DATA_URL = ip + "/appOrder!getAdminData";
	/***
	 * 查询个人统计列表
	 */
	public static final String GET_USER_DATA_URL = ip + "/appOrder!getUserData";
	/***
	 * 获取服务费用信息
	 **/
	public static final String Get_XXF_INFO_URL = ip + "/appOrder!getXxfDetails";
	/**
	 * 下单人支付服务费
	 ***/
	public static final String PAY_XIADAN_PRICE_URL = ip + "/appOrder!updateXd";
	/***
	 * 发布人支付服务费
	 ***/
	public static final String PAY_QUEREN_PRICE_URL = ip + "/appOrder!updateQr";
	/***
	 * 产地交易服务信息费协议
	 **/
	public static final String XIEYI_XXF_URL = ip + "/xxfxy.jsp";
	/***
	 * 查询订单浏览记录
	 **/
	public static final String LIULAN_URL = ip + "/appBrowse!selectList.action";
	/**
	 * 判断是否签约银行卡
	 ***/
	public static final String IS_QIANYUE_CARD = ip + "/appUser!issmrz";
	/**
	 * 查询价格最近一个月的曲线统计
	 **/
	public static final String GETGRAPHDATA_URL = ip + "/appPrice!toGraph.action";

	/**********加盟分销商*************/
	/**
	 * 查询加盟信息
	 **/
	public static final String QUERY_DISTRIBUTOR_URL = ip + "/appDistributor!toEdit";
	/**
	 * 修改加盟信息
	 **/
	public static final String UPDATE_DISTRIBUTOR_URL = ip + "/appDistributor!update";
	/**
	 * 提交加盟信息
	 **/
	public static final String INSERT_DISTRIBUTOR_URL = ip + "/appDistributor!insert";
	/**
	 * 获取加盟商审核状态
	 **/
	public static final String GET_DISTRIBUTORTYPE_URL = ip + "/appDistributor!getStatus";
	/**
	 * 查看附近加盟商
	 **/
	public static final String GET_NEAR_DISTRIBUTOR_URL = ip + "/appDistributor!selectByXY";
	/**
	 * 查看加盟商列表
	 **/
	public static final String GET_LIST_DISTRIBUTOR_URL = ip + "/appDistributor!getAreaMapJSON";
	/**
	 * 根据全国/省名称 查询子地区 数量
	 **/
	public static final String GET_AREA_DISTRIBUTOR_URL = ip + "/appDistributor!getAreaMapJSON";
	/**
	 * 分销商协议
	 **/
	public static final String XIEYI_DISTRIBUTOR_URL = ip + "/szmyMobie/fxsProtocol.jsp";
	/**
	 * 获取今日最新资讯
	 **/
	public static final String TODAY_NEWS_URL = ip + "/szmyAppNewsActionFront!getNewNews4";
	/**
	 * 分销商收益列表
	 **/
	public static final String INTEGRAL_DISTRIBUTOE_URL = ip + "/appProfit!selectList";
	/**
	 * 分销商总收益
	 **/
	public static final String TOTAL_INTEGRAL_DISTRIBUTOE_URL = ip + "/appProfit!getDistributorProfitCount";
	/**
	 * 牧易商城订单
	 **/
	public static final String MYSHOP_ORDER_URL = ip + "/szmyMobie/userInfo/orders.jsp?uuid=";
	/**
	 * 查询当前用户是否是市场人员
	 **/
	public static final String IS_SCRY_URL = ip + "/myForward!szmyApp";


	/***********牧易商城app******************/
	/**
	 * 牧易优选
	 **/
	public static final String YOUXUAN_SZMY_URL = ip + "/szmyMobileProductActionFront!toProductList?isMyyx=y&uuid=";
	/**
	 * 购物车
	 **/
	public static final String GOUWUCHE_SZMY_URL = ip + "/szmyMobie/cart.jsp?uuid=";
	/***
	 * 收藏商品
	 **/
	public static final String SHOUCANG_PRODUCT_SZMY_URL = ip + "/mAccount/toShowFavorite.html?uuid=";
	/**
	 * 收藏店铺
	 **/
	public static final String SHOUCANG_SHOP_SZMY_URL = ip + "/mAccount/toShowFavorite.html?e.type=2&uuid=";
	/**
	 * 神州牧易的商品搜索
	 **/
	public static final String SEARCH_SZMY_URL = ip + "/szmyMobileProductActionFront!toProductList?name=";
	/**
	 * 牧易商城优选搜索
	 **/
	public static final String SEARCHYX_SZMY_URL = ip + "/szmyMobileProductActionFront!toProductList?isMyyx=y&name=";
	/**
	 * 牧易商城旗舰店搜索
	 ***/
	public static final String SEARCHQJD_SZMY_URL = ip + "/mCompany/toCompany.html?name=";
	/**
	 * 注册神州牧易账号
	 **/
	public static final String REISTER_SZMY_URL = ip + "/myForward!szmyApp";
	public static final String REISTER_SZMY_URL2 = ip + "/company!doRegisterApp";


	/**
	 * 获取动检所电话
	 **/
	public static final String GET_DJSPHONE_URL = ip + "/appUser!getJdsdh";

	/***
	 * 判断付款是否在正常时间段
	 ***/
	public static final String GET_PAYTIME_URL = ip + "/appOrder!isPayTime";

	/**判断用户是否已经实名认证*/
	public static final String IS_USERSMRX_URL = ip + "/appUser!getJsrz";
	/**评论列表***/
	public static final String COMMENT_LIST_URL = ip + "/newsCommentFront!selectPagerList.action";
	/**添加评论、回复***/
	public static final String ADD_COMMENT_URL = ip + "/newsCommentFront!toInsert.action";

	/**点赞**/
	public static final String ADD_DIANZAN_URL = ip + "/newsCommentFront!zan.action";
	/***猪病诊断——获得猪种类**/
	public static final String GET_PIG_TYPE_URL = ip + "/appPig!getPig";
	/***猪病诊断--获取选择题***/
	public static final String GET_QUAESATION_URL = ip + "/appPigRelevanceDisease!selectSymptom";
	/**猪病诊断--获取诊断结果***/
	public static final String GET_ANSWER_URL = ip + "/appPigRelevanceDisease!diagnose";
	/***猪病诊断---获取诊断结果详情**/
	public static final String GET_ANSWER_INFO_URL = ip + "/appPigDisease/pigDisease.html?id=";
	/***专家在线***/
	public static final String GET_ZHUANJIA_INFO_URL = ip + "/appPigDisease/expert.html";
	/***病科送检*/
	public static final String GET_SONGJIAN_INFO_URL = ip + "/appPigDisease/pigDiagnose.html";
	/***获取用户等级**/
	public static final String GET_USER_LEVE_URL = ip +"/level!getLevel";

	/***我的店铺---商城**/
	public static final String MY_SHOP_URL = ip+"/szmyMobileProductActionFront!toProductList?isMyyx=y?uuid=";
	/***新闻点赞、收藏***/
	public static final String SEND_ZAN_SHOUCANG_URL = ip + "/dianzan!toInsert";
	/***我收藏的资讯列表***/
	public static final String GET_MYNEWS_SHOUCANG_URL = ip + "/dianzan!toPagerList";
	/**获取生猪单价最大值最小值***/
	public static final String GET_MINMAX_PRICE_URL = ip + "/appOrder!getZmtPrice";
	/***代金券列表****/
	public static final String GET_DAIJINQUAN_LIST_URL = ip + "/coupon!selectList.action";
	/***推荐的牧易优选商品**/
	public static final String GET_MYYXLIST_URL = ip + "/szmyMobileProductActionFront!listYxtj";
	/**查看商品详情**/
	public static final String TO_PRODUCT_DETAIL_URL = ip + "/mProduct/";
	/**融资贷款**/
	public static final String SHENQING_DAIKUAN_URL = ip + "/loan!loan?uuid=";
	/**融资贷款列表**/
	public static final String SHENQING_DAIKUAN_LIST_URL = ip + "/loan!loanList?uuid=";
	/**报价**/
	public static final String QUOTED_PRICE_ADD_URL = ip + "/appQuote!quote.action";
	/**查询最后一次报价信息**/
	public static final String QUOTED_PRICE_LAST_URL = ip + "/appQuote!getLast.action";
	/***查询报价列表**/
	public static final String QUOTED_PRICE_LIST_URL = ip + "/appQuote!selectList";

	public static final String QUOTED_PRICE_ZDLIST_URL = ip + "/appQuote!selectZdList";
	/***查看价格判断积分*/
	public static final String GET_QUOTED_PRICE_POINT_URL = ip + "/appQuote!checkQuote.action";
	/**查看价格详情**/
	public static final String GET_QUOTED_PRICE_DTEAIL_URL = ip + "/appQuote!info";
	/**赚取积分**/
	public static final String GET_POINT_URL = ip + "/activity/zmt.jsp?img=zmt_jf";
	//浙商银行支付
	/**查询代收协议**/
	public static final String QUERY_AGREEMENTSIGNING_URL = ip + "/appOrder!queryCollection";
	/**代收协议签约**/
	public static final String AGREEMENTSIGNING_URL = ip + "/appOrder!bindCollection";
	/**代收协议修改***/
	public static final String UPP_AGREEMENTSIGNING_URL = ip + "/appOrder!modifyCollection";
	/**开立电子账户**/
	public static final String ELECTRONICACCOUNTS_URL = ip + "/appOrder!openECarddirect";
	/**修改电子账户***/
	public static final String UPP_ELECTRONICACCOUNTS_URL = ip + "/appOrder!modifyECard";
	/**查询电子账户***/
	public static final String QUERY_ELECTRONICACCOUNTS_URL = ip + "/appOrder!queryECard";
	/***获取验证码**/
	public static final String GET_MESSAGE_CODE = ip + "/appOrder!sendMessage";
	/******/
	public static final String ZHESHANG_PAY_URL = ip + "/gateway";
	/***查询银行卡类型列表***/
	public static final String BANK_LIST_URL = ip2 + "/bank/getChinaBank";
	/***查询银行卡省份；列表**/
	public static final String PROVINCE_LIST_URL = ip2 + "/bank/getprovinces";
	/**查询银行卡城市列表***/
	public static final String CITYS_LIST_URL = ip2 + "/bank/getcitys";
	/***查询开户行列表***/
	public static final String SUBBRANCHS_LIST_URL = ip2 + "/bank/getsubbranchs";
	/***模糊查询开户行***/
	public static final String LIKE_SUBBRANCHS_LIST_URL = ip + "/bank/bankMessage";

	/***查询电子账户和代收协议状态***/
	public static final String GET_PAYBANK_STATUS_URL = ip + "/appOrder!queryAppPayBank";
	/***订单支付***/
	public static final String PAYBANK_ORDER_URL = ip + "/appOrder!smartPay";
	/**输入验证码确认支付***/
	public static final String PAYBANK_ORDER_YZM_URL = ip + "/appOrder!quickPayTransfer";
	/***获取未读消息**/
	public static final String GET_NOTICES_LIST_URL = ip + "/appPushMessageFront!selectList";
	/**修改消息已读状态***/
	public static final String UPP_NOTICE_STATUS_URL = ip + "/appPushMessageFront!readOne";
	/***删除消息***/
	public static final String DEL_NOTICE_STATUS_URL = ip + "/appPushMessageFront!deletes";
	/***修改所有消息为已读**/
	public static final String READALL_NOTICE_STATUS_URL = ip + "/appPushMessageFront!readAll";



}

