package com.bsoft.quartz;

import com.bsoft.mapper.business.RegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**


import com.bsoft.service.impl.CardPipeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author     ：hxy
 * @date       ：Created in 2019/04/01 12:40
 * @description：医院解锁号
 * @modified By：
 * @version: $
 */

@Component
public class ScheduledPutreslist {
    @Resource
    private RegisterMapper registerMapper;

    private Logger logger = LoggerFactory.getLogger(ScheduledPutreslist.class);
    //、cron接受cron表达式，根据cron表达式确定定时规则
    @Scheduled(fixedRate = 120000)
    public void CardPipe_Hospitalization() throws InterruptedException {
        registerMapper.opt_0018_2();
    }

}
