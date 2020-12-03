//package com.bsoft.config.interceptor;
//
//
//import com.alibaba.dubbo.common.utils.IOUtils;
//import com.bsoft.constants.InterfaceConstants;
//import com.bsoft.model.HospitalInfo;
//import com.bsoft.model.RequestRecord;
//import com.bsoft.producer.MsgProducer;
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;
//import java.util.UUID;
//
//public class InterfaceInterceptor implements HandlerInterceptor {
//
//    @Resource
//    MsgProducer msgProducer;
//
//    @Value("${projectNumber}")
//    String projectNumber ;
//
//    @Value("${server.port}")
//    String port ;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        try {
//            JSONObject requestParams = new JSONObject();
//
//
//            Map requestMap = request.getParameterMap() ;
//            if(requestMap == null || requestMap.isEmpty()){
//                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//                String body = IOUtils.read(reader);
//                requestParams = JSONObject.fromObject(body);
//            }else{
//                requestParams = JSONObject.fromObject(requestMap);
//            }
//
//            String hospitalId = requestParams.getString(InterfaceConstants.HOSPITAL_ID);
//            String [] uri = request.getRequestURI().split("/");
//
//            RequestRecord rr = new RequestRecord();
//            rr.setId(UUID.randomUUID().toString());
//            rr.setHospitalId(hospitalId);
//            rr.setServiceId(uri[uri.length-2]);
//            rr.setMethodId(uri[uri.length-1]);
//            rr.setStatus("0");
//            rr.setReponseStatus("0");
//            rr.setRequestParams(requestParams.toString());
//            rr.setTakeUpTime(System.currentTimeMillis()+"");
//            rr.setResponseParams("");
//            rr.setMethod(request.getMethod());
//            rr.setProjectNumber(projectNumber);
//            rr.setPort(port);
//            Date nowDate = new Date();
//            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
//            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
//            int year = c.get(Calendar.YEAR) ;
//            int month = c.get(Calendar.MONTH);
//            int date = c.get(Calendar.DATE);
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//            rr.setRequestTime(sdf.format(nowDate));
//            rr.setRequestStamp(nowDate.getTime());
//            rr.setYear(year+"");
//            rr.setMonth(month+1+"");
//            rr.setDay(date+"");
//            rr.setHour(hour+"");
//            rr.setMinute(minute+"");
//
//            HospitalInfo hi = new HospitalInfo();
//            hi.setId(hospitalId);
//            hi.setHospitalId(hospitalId);
////            hi.setHospitalName(hospitalName);
////            hi.setCityId(cityId);
////            hi.setCityName(cityName);
////            hi.setProvinceId(provinceId);
////            hi.setProvinceName(provinceName);
////            hi.setDistrictId(districtId);
////            hi.setDistrictName(districtName);
//            hi.setProjectNumber(projectNumber);
//            hi.setRequestCount(0l);
//            hi.setSuccessCount(0l);
//            hi.setErrorCount(0l);
//            JSONObject requestTemp = JSONObject.fromObject(rr);
//            requestTemp.put("hospitalInfo",hi);
//            request.setAttribute(InterfaceConstants.REQUESTPARAM_KEY,requestTemp);
//            /*******调用 MQ **********/
//            msgProducer.sendMsg_forRequest_run(requestTemp.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        try {
//
//            String responseData =  request.getAttribute(InterfaceConstants.RESPONSEPARAM_KEY).toString();
//
//            JSONObject requestTemp = JSONObject.fromObject(request.getAttribute(InterfaceConstants.REQUESTPARAM_KEY));
//
//            String requestParams =  requestTemp.getString("requestParams");
//
//            HospitalInfo hospitalInfo = (HospitalInfo)JSONObject.toBean(requestTemp.getJSONObject("hospitalInfo"),HospitalInfo.class);
//
//            requestTemp.remove("hospitalInfo");
//            requestTemp.remove("requestParams");
//            RequestRecord tempRecord = (RequestRecord)JSONObject.toBean(requestTemp,RequestRecord.class);
//
//
//            tempRecord.setRequestParams(requestParams);
//            tempRecord.setReponseStatus(response.getStatus()+"");
//            tempRecord.setResponseParams(responseData);
//            tempRecord.setTakeUpTime((System.currentTimeMillis() - Long.valueOf(tempRecord.getTakeUpTime()))+"");
//
//            System.out.println("======================= "+responseData);
//            try {
//                JSONObject    responseTemp = JSONObject.fromObject(responseData);
//                tempRecord.setStatus(responseTemp.getString("code"));
//            }catch(JSONException e){
//                tempRecord.setStatus(InterfaceConstants.SYSTEMATIC_ERROR_CODE+"");
//            }
//
//
//
//            JSONObject tr = JSONObject.fromObject(tempRecord) ;
//            tr.put("hospitalInfo" , hospitalInfo);
//            /*******调用 MQ **********/
//
//            msgProducer.sendMsg_forResponse_run(tr.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}
