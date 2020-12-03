package com.bsoft.controller;

import com.bsoft.entity.InterFaceBean;
import com.bsoft.service.InterFaceLogService;
import com.bsoft.service.InterFaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujx
 * @date 2019/10/11
 */
@RestController
@RequestMapping("/interFaceLog")
public class InterFaceLogController {
    private static Logger logger = LoggerFactory.getLogger(InterFaceLogController.class);

    @Autowired
    InterFaceLogService logService;

    /**
     * 查询接口信息
     */
    @PostMapping("/getInterface")
    public String getInterface() {
        logger.info("查询接口信息:入参：");
        String res = logService.query();
        logger.info("查询接口信息:出参：");
        return res;
    }
}
