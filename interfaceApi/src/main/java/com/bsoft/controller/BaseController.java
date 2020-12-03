package com.bsoft.controller;

import ch.qos.logback.core.util.SystemInfo;
import com.bsoft.exception.CommonException;
import com.bsoft.util.CommonUtil;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础controoller 用于公共和迭代内容
 * @author liujx
 * @date 2020/1/8
 */
@Controller
//@RequestMapping(value = "publicInterface")
public class BaseController {
    /**
     * 查询服务器内存，磁盘使用情况
     *
     * @param
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "getServerInfo", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getServerInfo() throws CommonException {
        Map<String,Object> resMap=new HashMap<>();

//        List<Map<String,Object>> res=new LinkedList<>();
        // 磁盘使用情况
        StringBuffer res = new StringBuffer();
        File[] files = File.listRoots();
        for (File file : files) {
            Map<String,Object> map =new HashMap<>();
            String total = new DecimalFormat("#.#").format(file.getTotalSpace() * 1.0 / 1024 / 1024 / 1024)
                    + "G";
            String free = new DecimalFormat("#.#").format(file.getFreeSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
            String un = new DecimalFormat("#.#").format(file.getUsableSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
            String path = file.getPath();
            map.put(path.replace(":\\",""),total+":"+un);
            res.append(path.replace(":\\","")+"盘磁盘总大小为："+total+",剩余大小为："+un+";");
        }
        resMap.put("cpqk",res.toString());
        // 总的物理内存
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        String totalMemorySize = new DecimalFormat("#.##").format(osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024 / 1024) + "G";
        // 剩余的物理内存
        String freePhysicalMemorySize = new DecimalFormat("#.##")
                .format(osmxb.getFreePhysicalMemorySize() / 1024.0 / 1024 / 1024) + "G";

        resMap.put("ncqk","总的物理内存："+totalMemorySize+",剩余物理内存为："+freePhysicalMemorySize+";CPU处理器数量："+Runtime.getRuntime().availableProcessors()+";");
        return new CommonUtil().getSuccess(resMap);

    }
}
