package com.bsoft.util;

import com.bsoft.exception.CommonException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/5/7
 */
public class CommonUtil {
    /**
     * create by: hxy
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 判断必填字段，如果没有抛出异常
     */
    public void isNUll(Map<String, Object> map, String s) throws CommonException {
        if (map.get(s) == null || map.get(s).toString().equals("")) {
            throw new CommonException(s + "不能为空");
        }
    }

    public void isNUll(Map<String, Object> map, List<String> list) throws CommonException {
        for (String s : list) {
            if (map.get(s) == null || map.get(s).toString().equals("")) {
                throw new CommonException(s + "不能为空");
            }
        }
    }

    /**
     * create by: hxy
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 判断2个字段必填一个字段，如果没有抛出异常
     */
    public void isNUll(Map<String, Object> map, String s1, String s2) throws CommonException {
        if (map.get(s1) == null || map.get(s1).toString().equals("")) {
            if (map.get(s2) == null || map.get(s2).toString().equals("")) {
                throw new CommonException(s1 + "或者" + s2 + "必填一个");
            }
        }
    }

    /**
     * create by: hxy
     * description: 成功返回方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */
    public String getSuccess(Object data) {
        JSONObject json = new JSONObject();
        json.put("code", 1);
        json.put("data", data);
        json.put("msg", "请求成功");
        return json.toString();
    }

    public String getSuccessList(Object data) {
        JSONObject json = new JSONObject();
        JSONObject temp = (JSONObject) data;
        JSONArray jsonArray = (JSONArray) temp.get("list");
        if (!jsonArray.isEmpty() && jsonArray.size() > 0) {
            json.put("code", 1);
        } else {
            json.put("code", 0);
        }

        json.put("data", data);
        json.put("msg", "请求成功");
        return json.toString();
    }

    /**
     * create by: hxy
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 判断存储过程有没有报错，如果报错抛出异常
     */
    public void prm_ErrorMsg(Map<String, Object> req) throws CommonException {
        if (!(req.get("prm_errorMsg") == null || req.get("prm_errorMsg").toString().equals(""))) {
            throw new CommonException(req.get("prm_errorMsg").toString());
        }
    }

    /**
     * create by: hxy
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 改方法用于分页
     */
    public void pagination(Map<String, Object> req) throws CommonException {
        isNUll(req, "pageNum");
        isNUll(req, "pageSize");
        Integer pageNum = Integer.valueOf(req.get("pageNum").toString());
        Integer pageSize = Integer.valueOf(req.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);

    }

    /**
     * create by: hxy
     * description: 分页成功返回方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */
    public String getSuccessPagination(List<Map<String, Object>> list) {
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        JSONArray jsonArray = JSONArray.fromObject(pageInfo.getList());
        JSONObject json = new JSONObject();
        json.put("total", pageInfo.getTotal());
        json.put("list", jsonArray);
        return getSuccess(json);
    }
    /**
     * create by: hxy
     * description: 分页成功返回方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */

    public String toMapString(Map<String, Object> resultMap) {
        JSONObject jArray = new JSONObject();
        jArray.putAll(resultMap);
        String str = jArray.toString();
        return str;
    }

    /**
     * 把map的key加上prm_传入存储过程
     */
    public Map<String, Object> toMap_prm(Map<String, Object> map) {
        Map<String, Object> new_map = new HashMap();
        for (String key : map.keySet()) {
            new_map.put("prm_" + key, map.get(key));
        }
        return new_map;
    }

    /**
     * 此方法处理字符串，在字符串上加"",如 11,22,33,44 变为'11','22','33','44'
     */
    public void toCharacterString(Map<String, Object> map, String s) {


        if (map.get(s) != null && map.get(s) != "") {
            String str = map.get(s).toString().replace(" ", "");
            StringBuilder sb = new StringBuilder();
            String as[] = str.split(",");
            for (int i = 0; i < as.length; i++) {
                sb.append("'" + as[i] + "'");//拼接单引号,到数据库后台用in查询.
                if (i != as.length - 1) {//前面的元素后面全拼上",",最后一个元素后不拼
                    sb.append(",");
                }
            }
            map.put(s, sb.toString());
        }


    }

    /**
     * create by: hxy
     * description: 存储过程成功返回方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */
    public String getSuccessPrm(Map<String, Object> data) {
        if (!(data.get("prm_errorMsg") == null || data.get("prm_errorMsg").toString().equals(""))) {
            throw new CommonException(data.get("prm_errorMsg").toString());
        }
        JSONObject json = new JSONObject();
        json.put("code", 1);
        json.put("data", "");
        json.put("msg", "请求成功");
        return json.toString();
    }

    /**
     * 元转分，确保price保留两位有效数字
     * @return
     */
    public static int changeY2F(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        int money = (int)(price * 100);
        return money;
    }

    /**
     * 分转元，转换为bigDecimal在toString
     * @return
     */
    public static String changeF2Y(String price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }
}
