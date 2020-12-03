package com.bsoft;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2019/11/19.
 */
public class HttpUtil {
    private static JSONObject token_json = null;

    //账号
    public static String HISLOGINID="dyjy";
    //密码
    public static String HISLOGINPSW="bsoft@123";

    /**
     * 调用HTTP 接口的公共方法
     * @param url 接口地址
     * @param method 接口方法
     * @param params 接口参数json字符串
     * @return
     */
    public static String getHttpResult(String url, String serviceId,String method,String params){
        String val = "";
        try{
            if(token_json == null){
                token_json = getTokenJson(url+"auth/getToken",HISLOGINID,HISLOGINPSW);
            }
            Map<String,Object> headers = new HashMap<String,Object>();
            headers.put("Authorization",token_json.getString("token"));
            headers.put("Authorization-re", token_json.getString("refreshToken"));
            headers.put("Request-Service-Id",serviceId);
            headers.put("Request-Service-Method",method);
//			headers.put("Content-Type","application/json");

            String result = sendHttpRequest(url + "interface/business","POST",params,headers);
//			String result = HttpRequest.request(url + "interface/business/", headers, JSONObject.fromObject(params));
            JSONObject obj = JSONObject.fromObject(result);
            String code = "";
            if(obj != null){
                code = obj.get("code") != null?obj.get("code").toString():"";
                String status = obj.get("status") != null?obj.get("status").toString():"";
                if(!"".equals(status) && Integer.parseInt(status) < 500){
                    obj = obj.getJSONObject("msg");
                    code = obj.getString("code");
                    result = obj.toString();
                }
                if(!"".equals(code)){
                    //token失效重新获取后调用接口
                    if("1003".equals(code) || "1004".equals(code)){
                        token_json = getTokenJson(url+"auth/getToken",HISLOGINID,HISLOGINPSW);
                        headers.put("Authorization",token_json.getString("token"));
                        headers.put("Authorization-re", token_json.getString("refreshToken"));
                        result = sendHttpRequest(url + "interface/business","POST",params,headers);
                    }
                }
            }
            val = result;
        }catch (Exception e){
//            logger.error("调接口异常！", e);
        }
        return val;
    }
    /**
     * 获取接口平台的token令牌
     * @param url http://10.32.16.19:9999/auth/getToken
     * @param userName username=zhangsan
     * @param password password=zhangsan
     * @return JSONObject
     */
    public static JSONObject getTokenJson(String url,String userName,String password){
        //url += "?username="+userName+"&password="+password;
        JSONObject paramObj = new JSONObject();
        paramObj.put("username",userName);
        paramObj.put("password",password);
        String param = paramObj.toString(),code = "";

        String result = sendHttpRequest(url,"POST",param,new HashMap<String, Object>());
//		String result = sendPost(url, param);
        try{
            JSONObject obj = JSONObject.fromObject(result),dataJson = null;
            if(obj != null){
                code = obj.get("code") != null?obj.get("code").toString():"";
                if(!"".equals(code)){
                    if("1003".equals(code) || "1004".equals(code)){
//						logger.warn("token失效，重新获取！");
//						result = sendPost(url, param);
//						obj = JSONObject.fromObject(result);
                        dataJson = obj.getJSONObject("data");
                    }else if("1".equals(code)){
                        dataJson = obj.getJSONObject("data");
                    }else{
//                        logger.error("请求token异常！" + obj.get("msg"));
                        return null;
                    }
                }else{
//                    logger.error("请求token异常！未返回code参数！");
                    return null;
                }
            }else{
//                logger.error("请求token异常！返回值为空！");
                return null;
            }
            return dataJson;
        }catch (Exception e) {
//            logger.error("请求业务接口异常！",e);
            return null;
        }
    }

    /**
     *
     * @param requestUrl
     * @param params
     * @param headers
     * @return
     */
    public static String sendHttpRequest(String requestUrl, String requestMethod,
                                         String params,Map<String,Object> headers) {
        String returnStr = "";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(3000);
            con.setReadTimeout(180000);
            con.setRequestMethod(requestMethod);
            if(headers != null){
                for (Map.Entry<String,Object> kv: headers.entrySet()
                        ) {
                    con.setRequestProperty(kv.getKey(),kv.getValue()+"");
                }
            }
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Accept-Encoding", "UTF-8");

            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream(), "UTF-8");

            out.write(new String(params.getBytes("UTF-8"),"UTF-8"));
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();

            InputStream is = null;
            if(responseCode>400){
                is = con.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line = "";
                for (line = br.readLine(); line != null; line = br.readLine()) {
                    returnStr += line;
                }
                JSONObject errObj = new JSONObject();
                errObj.put("status",responseCode);
                errObj.put("msg",returnStr);
                returnStr = errObj.toString();
            }else{
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line = "";
                for (line = br.readLine(); line != null; line = br.readLine()) {
                    returnStr += line;
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnStr;
    }


}
