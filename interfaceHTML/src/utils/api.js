export default {

    'login':'/interfaceWeb/auth/getToken',//登录接口

    'homePage':'/interfaceWeb/integrate/homePage',//欢迎页面

    "projecList":'/interfaceWeb/accountManagement/queryAccount',//获取项目列表
    "OrgList":'/interfaceWeb/mechanism/queryMechainsm',//获取机构列表
    'methodList':'/interfaceWeb/interfaceConfig/queryMethod',//方法名称

    'getInterface':'/interfaceWeb/interfaceConfig/getInterface',//查询接口数据接口
    'addInterface':'/interfaceWeb/interfaceConfig/addInterface',//新增接口信息接口
    'deleteInterface':'/interfaceWeb/interfaceConfig/deleteInterface',//删除接口
    'updateInterface':'/interfaceWeb/interfaceConfig/updateInterface',//修改接口信息

    'getServiceTree':'/interfaceWeb/integrate/getTree',//获取tree
    'getMoneyInfo':'/interfaceWeb/integrate/getMoneyInfo',//   流水信息
    'getErrorLogInfo':'/interfaceWeb/integrate/getErrorLogInfo',//错误日志流水
    'getHoursStatistics':'/interfaceWeb/integrate/getHoursStatistics',//24小时接口统计
    'getMethodStatistics':'/interfaceWeb/integrate/getMethodStatistics',//接口调用时长统计
    'getIogInfo':'/interfaceWeb/integrate/logInfo',//项目下-机构列表
    'getServiceInfo':'/interfaceWeb/integrate/serviceInfo',//项目下-机构列表-服务列表//暂时没用
    'getLogInfo':'/interfaceWeb/integrate/queryLogInfo',//项目下-机构列表-服务列表


    'getProjecManagement':'/interfaceWeb/accountManagement/query',//查询项目数据接口
    'addProjecManagement':'/interfaceWeb/accountManagement/register',//新增项目信息
    'updateProjecManagement':'/interfaceWeb/accountManagement/modify',// 修改项目信息
    'deleteProjecManagement':'/interfaceWeb/accountManagement/delete',//删除接口

    'getAccountManagement':'/interfaceWeb/platformAccountManagement/query',//查询平台账号管理
    'addAccountManagement':'/interfaceWeb/platformAccountManagement/register',//新增平台账号管理
    'updateAccountManagement':'/interfaceWeb/platformAccountManagement/modify',//修改平台账户
    'deleteAccountManagement':'/interfaceWeb/platformAccountManagement/delete',//删除

    'getServiceManagement':'/interfaceWeb/serviceConfigInfo/query',//获取服务配置信息管理
    'getQueryService':'/interfaceWeb/serviceConfigInfo/queryService',//获取服务配置信息管理
    'addServiceManagement':'/interfaceWeb/serviceConfigInfo/register',//新增服务配置
    'updateServiceManagement':'/interfaceWeb/serviceConfigInfo/modify',//修改服务配置信息
    'deleteteServiceManagement':'/interfaceWeb/serviceConfigInfo/delete',//删除服务配置

    'getlog':'/interfaceWeb/logQuery/queryLog',//日志查询

    'getOrg':'/interfaceWeb/mechanism/query',//查询机构
    'addOrg':'/interfaceWeb/mechanism/register',//新增机构
    'updateOrg':'/interfaceWeb/mechanism/modify',//修改机构
    'deleteOrg':'/interfaceWeb/mechanism/delete',//删除机构

}
