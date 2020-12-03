//package com.bsoft.controller;
//
//
//import com.bsoft.service.ClinicPaymentService;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/interfaceClient")
//public class InterfaceClient {
////    @Resource
////    private RestTemplate restTemplate;
////
////    @Resource
////    private HttpHeaders httpHeaders;
////    @RequestMapping(value = "getMedicalRecords", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
////    public Object getMedicalRecords(@RequestParam Map<String, Object> reqMap){
//////        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));
////        return restTemplate.exchange("http://CLINICPAYMENT/clinicPayment/getMedicalRecords", HttpMethod.POST,new HttpEntity<>(reqMap,httpHeaders),Object.class).getBody();
//////        return restTemplate.postForObject("http://CLINICPAYMENT/clinicPayment/getMedicalRecords",reqMap,Object.class);
////    }
//
//    @Resource
//    private ClinicPaymentService clinicPaymentService;
//
//    @RequestMapping(value = "getMedicalRecords", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
//    public Object getMedicalRecords(@RequestParam Map<String, Object> reqMap){
////        FilterConstants.SERVICE_ID_KEY
////        RequestContext rc = RequestContext.getCurrentContext();
////        rc.setRouteHost();
//
//        return clinicPaymentService.getMedicalRecords(reqMap);
//    }
//}
