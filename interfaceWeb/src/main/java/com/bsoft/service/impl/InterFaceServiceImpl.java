package com.bsoft.service.impl;

import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.InterFaceBean;
import com.bsoft.entity.User;
import com.bsoft.exception.CommonException;
import com.bsoft.service.AccountManagementService;
import com.bsoft.service.InterFaceService;
import com.bsoft.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liujx
 * @date 2019/9/23
 */
@Service
public class InterFaceServiceImpl extends MongoDaoSupport<InterFaceBean> implements InterFaceService {
    @Resource
    AccountManagementService accountManagementService;

    /**
     * 查询接口信息
     *
     * @param reqBean
     * @return
     */
    @Override
    public String queryInterface(InterFaceBean reqBean) {
        InterFaceBean interFaceBean = new InterFaceBean();
        BeanUtils.copyProperties(reqBean, interFaceBean);
        interFaceBean.setStatus("0");

        if (reqBean.getPageNum() == null || reqBean.getPageNum().toString().equals("") ||
                reqBean.getPageSize() == null || reqBean.getPageSize().toString().equals("")
        ) {
            throw new CommonException("pageSize,pageNum不能为空");
        }
        Integer pageSize = Integer.valueOf(interFaceBean.getPageSize().toString());
        Integer pageNum = Integer.valueOf(interFaceBean.getPageNum().toString());
        Query query = new Query();
        if (interFaceBean.getInterfaceName() != null && !interFaceBean.getInterfaceName().equals("")) {
            query.addCriteria(Criteria.where("interfaceName").regex(".*?" + interFaceBean.getInterfaceName() + ".*"));

        }
        Page<InterFaceBean> page = findPage(query, pageNum, pageSize, "InterFaceBean");

        return getSuccessPage(page);
    }

    /**
     * 增加接口信息
     *
     * @param reqBean
     * @return
     */
    @Override
    public String addInterface(InterFaceBean reqBean) {
        //判断是否已经有该接口信息
        Query query = new Query();
        query.addCriteria(Criteria.where("interfaceId").is(reqBean.getInterfaceId()));
        List<InterFaceBean> list = find(query);
        if (list.size() > 0) {
            throw new CommonException("该接口已存在");
        }

        InterFaceBean interFaceBean = new InterFaceBean();
        BeanUtils.copyProperties(reqBean, interFaceBean);
        interFaceBean.setCreateTime(new Date());
        interFaceBean.setLastTime(new Date());
        interFaceBean.setStatus("0");
        InterFaceBean bean = save(interFaceBean);
        return getSuccess(bean);
    }

    /**
     * 修改接口信息
     *
     * @param reqBean
     * @return
     */
    @Override
    public String updateInterface(InterFaceBean reqBean) {
        if (reqBean.getId() == null || StringUtils.isEmpty(reqBean.getId())) {
            throw new CommonException("Id 不能为空");
        }
        //如果是删除接口  看该接口是不是已经分配给用户
        if (reqBean.getStatus() != null && reqBean.getStatus().equals("1")) {
            if (accountManagementService.privilegesQuery(reqBean.getInterfaceId(), null)) {
                throw new CommonException("该接口已分配给用户，请先在用户中删除该接口");
            }
        }

        InterFaceBean interFaceBean = new InterFaceBean();
        BeanUtils.copyProperties(reqBean, interFaceBean);
        interFaceBean.setLastTime(new Date());
        updateById(reqBean.getId(), interFaceBean);
        return getSuccess("");
    }

    /**
     * 查询接口信息,获取名字和方法名
     *
     * @param
     * @return
     */
    @Override
    public String queryMethod() {
        Query query = new Query();
//        query.addCriteria(Criteria.where("status").is("0"));
        List<InterFaceBean> list = find(query);
        List<Map<String, Object>> resList = new LinkedList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                String[] split = null;
                split = list.get(i).getInterfaceId().split("/");
                map.put("interfaceId", split.length > 0 ? split[1] : list.get(i).getInterfaceId());
                map.put("interfaceName", list.get(i).getInterfaceName());
                resList.add(map);
            }
        }
        return getSuccess(resList);
    }
}
