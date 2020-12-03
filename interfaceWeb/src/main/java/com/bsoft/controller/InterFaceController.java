package com.bsoft.controller;

import com.bsoft.entity.InterFaceBean;
import com.bsoft.service.InterFaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author liujx
 * @date 2019/9/23
 * 服务器接口管理，对应服务的接口名称等
 */
@RestController
@RequestMapping("/interfaceConfig")
public class InterFaceController {
    private static Logger logger = LoggerFactory.getLogger(InterFaceController.class);

    @Autowired
    InterFaceService interFaceService;

    /**
     * 查询接口信息
     */
    @PostMapping("/getInterface")
    public String getInterface(@RequestBody InterFaceBean reqBean) {
        logger.info("查询接口信息:入参：" + reqBean);
        String res = interFaceService.queryInterface(reqBean);
        logger.info("查询接口信息:出参：" + res);
        return res;
    }

    /**
     * 增加接口信息
     */
    @PostMapping("/addInterface")
    public String addInterface(@Valid @RequestBody InterFaceBean reqBean) {
        logger.info("增加接口信息:入参：" + reqBean);
        String res = interFaceService.addInterface(reqBean);
        logger.info("增加接口信息:出参：" + res);
        return res;
    }

    /**
     * 修改接口信息
     */
    @PostMapping("/updateInterface")
    public String updateInterface(@RequestBody InterFaceBean reqBean) {
        logger.info("修改接口信息:入参：" + reqBean);
        String res = interFaceService.updateInterface(reqBean);
        logger.info("修改接口信息:出参：" + res);
        return res;
    }

    /**
     * 删除接口信息
     */
    @PostMapping("/deleteInterface")
    public String deleteInterface(@RequestBody InterFaceBean reqBean) {
        logger.info("删除接口信息:入参：" + reqBean);
        InterFaceBean interFaceBean = new InterFaceBean();
        interFaceBean.setId(reqBean.getId());
        interFaceBean.setStatus("1");
        String res = interFaceService.updateInterface(interFaceBean);
        logger.info("删除接口信息:出参：" + res);
        return res;
    }

    /**
     * 查询接口信息,获取名字和方法名
     */
    @PostMapping("/queryMethod")
    public String queryMethod() {
        String res = interFaceService.queryMethod();
        logger.info("查询接口信息,获取名字和方法名:出参：" + res);
        return res;
    }

}
