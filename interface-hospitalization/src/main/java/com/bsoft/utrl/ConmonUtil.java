package com.bsoft.utrl;

import com.bsoft.exception.CommonException;
import com.bsoft.mapper.identity.IdentityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/5/7
 */
public class ConmonUtil {
    @Value("${hospitalId-inCode}")
    private String hospitalId;

    @Resource
    private IdentityMapper identityMapper;

    /**
     * create by: hxy
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 更新jgid
     */
    public String change2LocalHospitalId() {
        return hospitalId;
    }
    public Map<String,Object> change2LocalHospitalId(Map<String,Object>  req) {
        req.put("hospitalId",hospitalId);
        return req;
    }
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
     * description: TODO
     * create time: 2019/03/15 14:59
     *
     * @return a
     * @Param: 判断3个字段必填一个字段，如果没有抛出异常
     */
    public void isNUll(Map<String, Object> map, String s1, String s2,String s3,String s4) throws CommonException {
        if (map.get(s1) == null || map.get(s1).toString().equals("")) {
            if (map.get(s2) == null || map.get(s2).toString().equals("")) {
                if (map.get(s3) == null || map.get(s3).toString().equals("")) {
                    if (map.get(s4) == null || map.get(s4).toString().equals("")) {
                        throw new CommonException(s1 + "、" + s2 + "、"+s3+"、"+s4+"必填一个");
                    }
                }
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
     * create by:
     * description: 获取主键方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */
    public Long getIdentity(String identity, String bmc) {
        long dqz = 0L;
        long dzz = 0L;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bmc", bmc);
        paramMap.put(identity, "1");
        int i = 0;
        do {
            Map<String, Object> map = identityMapper.getIdentity(paramMap);
            try {
                dqz = Long.valueOf(map.get("dqz").toString());
                dzz = Long.valueOf(map.get("dzz").toString());
                paramMap.putAll(map);
                i = identityMapper.updateIdentity(paramMap);
            } catch (Exception e) {
                throw new CommonException("获取" + bmc + "主键失败");
            }

        } while (i == 0);
        return dqz + dzz;
    }

    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     *
     * @param testStr 要+1的字符串
     * @return +1后的字符串
     * @throws NumberFormatException
     * @author
     */
    public String addOne(String testStr) {
        String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length - 1];//取出最后一组数字
        if (numStr != null && numStr.length() > 0) {//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
            int n = numStr.length();//取出字符串的长度
            long num = Long.parseLong(numStr) + 1;//将该数字加一
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            throw new NumberFormatException();
        }
    }

    // 将字Clob转成String类型
    public String ClobToString(Clob sc) throws SQLException, IOException {
        String reString = "";
        Reader is = sc.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }
    /**
     * 系统参数写入机构打包成map
     *
     * @param
     * @return
     * @throws
     * @author
     */
    public Map<String ,Object> xtcsMap(String csmc){
        Map<String,Object> map =new HashMap<>();
        map.put("jgid",hospitalId);
        map.put("csmc",csmc);

        return map;
    }
    /**
     * 把map的时间字符串转换为时间类型
     *
     * @param
     * @return
     * @throws
     * @author
     */
    public void stringToDate(Map<String,Object> req,String strDate){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=dateFormat.parse(req.get(strDate).toString());
            req.put(strDate,date);
        } catch (ParseException e) {
            throw new CommonException("传入"+strDate+"时间格式错误不是yyyy-MM-dd HH:mm:ss");
        }

    }
}
