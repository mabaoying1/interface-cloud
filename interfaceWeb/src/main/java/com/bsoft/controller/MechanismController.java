package com.bsoft.controller;

import com.bsoft.entity.Mechanism;
import com.bsoft.entity.PlatformUser;
import com.bsoft.service.MechanismService;
import com.bsoft.service.PlatformAccountManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/23 15:08
 * @description：平台账号管理
 * @modified By：
 * @version: $
 */

@RestController
@RequestMapping("/mechanism")
public class MechanismController {
    private static Logger logger = LoggerFactory.getLogger(MechanismController.class);

    @Resource
    MechanismService mechanismService;

    /**
     * 机构注册
     *
     * @param mechanism
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody Mechanism mechanism)  {

        logger.info("机构注册:入参：" + mechanism);
        String res = mechanismService.register(mechanism);
        logger.info("机构注册:出参：" + res);
        return res;
    }
    /**
     * 机构查询
     *
     * @param mechanism
     */
    @PostMapping("/query")
    public String query( @RequestBody Mechanism mechanism) {

        logger.info("机构查询:入参：" + mechanism);
        String res = mechanismService.query(mechanism);
        logger.info("机构查询:出参：" + res);
        return res;
    }
    /**
     * 机构修改
     *
     * @param mechanism
     */
    @PostMapping("/modify")
    public String modify(@RequestBody Mechanism mechanism) {

        logger.info("机构修改:入参：" + mechanism);
        String res = mechanismService.modify(mechanism);
        logger.info("机构修改:出参：" + res);
        return res;
    }
    /**
     * 机构删除
     *
     * @param mechanism
     */
    @PostMapping("/delete")
    public String delete( @RequestBody Mechanism mechanism) {

        logger.info("机构删除:入参：" + mechanism);
        String res = mechanismService.delete(mechanism);
        logger.info("机构删除:出参：" + res);
        return res;
    }

    /**
     * 查询机构信息，code和name
     *
     * @param mechanism
     */
    @PostMapping("/queryMechainsm")
    public String queryMechainsm() {

        logger.info("查询机构信息，code和name:入参：" );
        String res = mechanismService.queryMechainsm();
        logger.info("查询机构信息，code和name:出参：" + res);
        return res;
    }
}