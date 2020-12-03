package com.bsoft.service.impl;

import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.Mechanism;
import com.bsoft.entity.PlatformUser;
import com.bsoft.exception.CommonException;
import com.bsoft.service.AccountManagementService;
import com.bsoft.service.MechanismService;
import com.bsoft.service.PlatformAccountManagementService;
import com.bsoft.util.MD5EncodeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Query.query;


/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：机构管理
 * @modified By：
 * @version: $
 */
@Service
public class MechanismServiceImpl extends MongoDaoSupport<Mechanism> implements MechanismService {
    @Resource
    AccountManagementService accountManagementService;

    /**
     * 机构注册
     *
     * @param mechanism
     */
    @Override
    public String register(Mechanism mechanism) {
        Date date = new Date(System.currentTimeMillis());
        mechanism.setLastTime(null);
        mechanism.setCreateTime(null);
        mechanism.setStatus("0");
        //判断是否已经有该机构信息
        Query query = new Query();
        query.addCriteria(Criteria.where("mechanismCode").is(mechanism.getMechanismCode()));
        List<Mechanism> list = find(query);
        if (list.size() > 0) {
            throw new CommonException("该机构已存在");
        }
        mechanism.setCreateTime(date);
        mechanism.setLastTime(date);
        save(mechanism);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";

    }

    /**
     * 机构查询
     *
     * @param mechanism
     */
    @Override
    public String query(Mechanism mechanism) {
        if (mechanism.getPageSize() == null || mechanism.getPageNum() == null) {
            throw new CommonException("pagesize pageNum 不能为空");
        }
        Integer pageSize = mechanism.getPageSize();
        Integer pageNum = mechanism.getPageNum();
        Query query = new Query();
        if (mechanism.getMechanismCode() != null && !mechanism.getMechanismCode().equals("")) {
            query.addCriteria(Criteria.where("mechanismCode").is(mechanism.getMechanismCode()));
        }
        if (mechanism.getMechanismName() != null && !mechanism.getMechanismName().equals("")) {
            query.addCriteria(Criteria.where("mechanismName").regex(".*?" + mechanism.getMechanismName() + ".*"));
        }
        if (mechanism.getMechanismServiceCode() != null && !mechanism.getMechanismServiceCode().equals("")) {
            query.addCriteria(Criteria.where("mechanismServiceCode").is(mechanism.getMechanismServiceCode()));
        }
        if (mechanism.getStatus() != null && !mechanism.getStatus().equals("")) {
            query.addCriteria(Criteria.where("state").is(mechanism.getStatus()));
        }
        Page<Mechanism> page = findPage(query, pageNum, pageSize, "state");

        return getSuccessPage(page);
    }

    /**
     * 机构修改
     *
     * @param mechanism
     */
    @Override
    public String modify(Mechanism mechanism) {
        if (mechanism.getId() == null || mechanism.getId().equals("")) {
            throw new CommonException("id 不能为空");
        }
        mechanism.setCreateTime(null);
        mechanism.setPageSize(null);
        mechanism.setPageNum(null);
        mechanism.setLastTime(new Date(System.currentTimeMillis()));

        updateById(mechanism.getId(), mechanism);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 机构删除
     *
     * @param mechanism
     */
    @Override
    public String delete(Mechanism mechanism) {
        if (mechanism.getId() == null || mechanism.getId().equals("")) {
            throw new CommonException("id 不能为空");
        }
        //看该接口是不是已经分配给用户

        if (accountManagementService.privilegesQuery(null, mechanism.getId())) {
            throw new CommonException("该机构已分配给用户，请先在用户中删除该机构");
        }


        Mechanism mechanismFind = new Mechanism();
        mechanismFind.setStatus("1");
        mechanismFind.setLastTime(new Date(System.currentTimeMillis()));
        updateById(mechanism.getId(), mechanismFind);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 机构查询
     *
     * @param mechanisms
     */
    @Override
    public List<Mechanism> query(List<String> mechanisms) {

        Query query = new Query();
        query.addCriteria(Criteria.where("mechanismCode").in(mechanisms));
//        query.addCriteria(Criteria.where("mechanismCode").in(mechanisms));

        List<Mechanism> list = find(query);

        return list;
    }

    /**
     * 查询机构信息，code和name:入参
     *
     * @param mechanisms
     */
    @Override
    public String queryMechainsm() {

        Criteria criteria = Criteria.where("status").is("0");
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("mechanismName", "mechanismCode", "status"),
                Aggregation.match(criteria), Aggregation.unwind("mechanismCode"),
                Aggregation.group("mechanismCode").first("mechanismCode")
                        .as("mechanismCode").first("mechanismName").as("mechanismName")
        );
        List<Mechanism> mechanismsList = findAggregateList(new Query(criteria), "mechanism", customerAgg, Mechanism.class);
        List<Map<String,Object>> resList =new LinkedList<>();
        if(mechanismsList!=null &&  mechanismsList.size()>0){
            for(int i=0;i<mechanismsList.size();i++){
                Map<String,Object>  resmap= new HashMap<>();
                resmap.put("mechanismName",mechanismsList.get(i).getMechanismName());
                resmap.put("mechanismCode",mechanismsList.get(i).getMechanismCode());
                resList.add(resmap);
            }
        }
        return getSuccess(resList);
    }

}

