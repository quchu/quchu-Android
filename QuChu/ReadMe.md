#
margin值根据 Material Design 推荐设置为8的倍数
    <dimen name="dialog_margin">24dp</dimen>
    <dimen name="base_margin">16dp</dimen>
    <dimen name="half_margin">8dp</dimen>
    <dimen name="quarter_margin">4dp</dimen>
<!--字号-->
    <dimen name="word_large_number">34sp</dimen>    大号数字字号
    <dimen name="word_title_body">20sp</dimen>      标题字号
    <dimen name="word_subhead_size">16sp</dimen>    副标题字号
    <dimen name="word_size_body">14sp</dimen>       正文字号
    <dimen name="word_size_time">12sp</dimen>       正文时间戳推荐字号
    <dimen name="word_size_minimum">10sp</dimen>    正文中最小字号如标签
 Constants
    public static final int ISDEBUG = 2; // 0=正式环境  1 =uat环境   2=sit环境
    public static final boolean ISPRINTLOG = true; //是否打印log
    public static final boolean ISSTARTINGPKG = false;//是否显示360首发

 注意：打包时需按照开发阶段切换服务器环境。

#version1.0.1
新增用户点击“想去”按钮数据采集
新增用户中心
新增游客模式
#version1.0_20160129
屏蔽台北地区趣处导航
修改账户登录后显示趣基因
修改趣处详情封面图片显示比例

#version 1.2.0
修复 地图混淆后打开崩溃的bug

#version 1.1.7
修复 详情界面两个联系电话的显示及拨打电话bug
修复 获取趣处详情失败后长时间弹出数据加载界面的bug
修复 明信片界面趣处名称过长与地理位置重叠的bug
新增 设置界面检查版本更新按钮。
优化内存中。。。

#version 1.1.3
规避 红米手机发送图片前压缩处理出现的内存溢出（重新优化图片选中控件，并修改最多上传数为5）
#version 1.1.2
修复 跳转网页时重定向导致重复刷新（跳转浏览器打开web）
修复 创建明信片_添加明信片拍照删除再拍照崩溃bug
修复 首页_趣处页点击去过按钮返回再进入取消去过后趣处消失(点击去过后数据刷新机制bug)bug
修复 搜索_无网络打开app搜索活动一直提示数据加载中只能结束app进程
修复 无数据界面_无数据界面点击'到别处逛逛'无反应（点击关闭当前界面）
遗留bug 小米手机选中9张照片点击确定后闪退（内存溢出）
#version 1.1.1
修复 相册_无网络状态下进入相册点击收藏或大小图切换时崩溃bug
修复 进入我的明信片点击到空区域崩溃bug
修复 未收藏趣处时进入我收藏的趣处页面程序崩溃
修复 我的明信片分享到微信(朋友圈)无法发送
修复 由我收藏的趣处列表进入趣处详情点击取消收藏程序崩溃
修复 进入系统明信片页面提示“网络出错”
修复 明信片列表中明信片显示两个☆按钮
修复 按钮重复点时间内快速点击的bug
#version:1.1.0
修复 分享界面简介乱码bug
修复 首页无网络状态第二次打开app崩溃bug
修复 创建明信片时内容包含emoji表情无法成功创建明信片（含有emoji表情弹出提醒）
修复 我留下的明信片按钮点击无响应
修复 创建明信片后列表无刷新bug
修复 打开快速反馈或关于我们，关闭后直接返回首页
修复 退出登陆后的登录界面按返回键出现首页的bug
修复 取消收藏后再点击进入收藏夹应用停止运行bug
无法分享第三方（并没有重现出bug）

#version:1.0.9
修复 无网络状态下进去首页后网络恢复不重新获取数据的bug
修复 无网络时点击定位闪退bug
修复 无网络时登录界面点击第三方登录闪退bug
修复 微博登录后点击“拆开明信片”后返回重新点击“拆开明信片”闪退bug
修复 账户注册后闪退bug
修复 无网络状态时点击去过闪退bug
修复 无网络状态下访问网络弹窗提示文字显示不全bug（无网络弹窗提示文字内容待确认）
修复 系统明信片详情点击进入趣处程序崩溃
修复 删除我的明信片再次点击进入后应用停止运行

#version:1.0.8
新增 消息中心界面
修复 "推荐/分类"在4.1.2上点击的bug
修复 "收藏界面"数据为空时崩溃的bug
