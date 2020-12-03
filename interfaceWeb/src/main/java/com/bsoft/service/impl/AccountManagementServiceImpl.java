package com.bsoft.service.impl;

import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.Mechanism;
import com.bsoft.entity.User;
import com.bsoft.exception.CommonException;
import com.bsoft.producer.MsgProducer;
import com.bsoft.service.AccountManagementService;
import com.bsoft.service.MechanismService;
import com.bsoft.util.MD5EncodeUtil;
import com.github.pagehelper.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：接口调用用户账号管理
 * @modified By：
 * @version: $
 */
@Service
public class AccountManagementServiceImpl extends MongoDaoSupport<User> implements AccountManagementService {
    @Resource
    MsgProducer msgProducer;
    @Resource
    MechanismService mechanismService;

    /**
     * 账号注册
     *
     * @param user
     */
    @Override
    public String register(User user) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        user.setLastTime(null);
        user.setCreateTime(null);
        user.setStatus("0");
        //密码加密
        user.setPassword(MD5EncodeUtil.code(user.getPassword(), 32));
        //判断是否已经有该账号信息
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(user.getUsername()));
        List<User> list = find(query);
        if (list.size() > 0) {
            throw new CommonException("该账号已存在");
        }
        user.setCreateTime(date);
        user.setLastTime(date);
        save(user);
        sendMQ(user);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";

    }

    /**
     * 账号查询
     *
     * @param map
     */
    @Override
    public String query(Map<String, Object> map) {
        isNUll(map, Arrays.asList("pageSize", "pageNum"));
        Integer pageSize = Integer.valueOf(map.get("pageSize").toString());
        Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
        Query query = new Query();
        if (map.get("username") != null && !map.get("username").toString().equals("")) {
            query.addCriteria(Criteria.where("username").regex(".*?" + map.get("username").toString() + ".*"));
        }
        if (map.get("state") != null && !map.get("state").toString().equals("")) {
            query.addCriteria(Criteria.where("state").is(map.get("state").toString()));
        }
        if (map.get("projectNumber") != null && !map.get("projectNumber").toString().equals("")) {
            query.addCriteria(Criteria.where("projectNumber").is(map.get("projectNumber").toString()));
        }
        if (map.get("privileges") != null && !map.get("privileges").toString().equals("")) {
            query.addCriteria(Criteria.where("privileges").is(map.get("privileges").toString()));
        }
        if (map.get("projectType") != null && !map.get("projectType").toString().equals("")) {
            query.addCriteria(Criteria.where("projectType").is(map.get("projectType").toString()));
        }
        if (map.get("projectName") != null && !map.get("projectName").toString().equals("")) {
            query.addCriteria(Criteria.where("projectName").regex(".*?" + map.get("projectName").toString() + ".*"));
        }
        if (map.get("mechanisms") != null && !map.get("mechanisms").toString().equals("")) {
            query.addCriteria(Criteria.where("mmechanisms").regex(".*?" + map.get("mechanisms").toString() + ".*"));
        }

        Page<User> page = findPage(query, pageNum, pageSize, "username");
        //把密码去除 不返回
        for (User user_ : page.getContent()) {
            user_.setPassword(null);
        }

        return getSuccessPage(page);
    }

    /**
     * 账号修改
     *
     * @param user
     */
    @Override
    public String modify(User user) throws Exception {
        if (user.getId() == null || user.getId().equals("")) {
            throw new CommonException("Id 不能为空");
        }
        User userFind = new User();
        userFind.setStatus(user.getStatus());
        userFind.setUsername(user.getUsername());
        userFind.setLastTime(new Date(System.currentTimeMillis()));
        userFind.setPrivileges(user.getPrivileges());
        userFind.setMechanisms(user.getMechanisms());
        userFind.setProjectType(user.getProjectType());
        //密码加密
        if (user.getPassword() != null) {
            userFind.setPassword(MD5EncodeUtil.code(user.getPassword(), 32));
        }
        updateById(user.getId(), userFind);
        sendMQ(get(user.getId()));
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 账号删除
     *
     * @param user
     */
    @Override
    public String delete(User user) {
        if (user.getId() == null || user.getId().equals("")) {
            throw new CommonException("id 不能为空");
        }
        User userFind = new User();
        userFind.setStatus("1");
        userFind.setUsername(user.getUsername());
        userFind.setLastTime(new Date(System.currentTimeMillis()));
        updateById(user.getId(), userFind);
        sendMQ(userFind);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 判断用户是否有该接口或者该机构
     *
     * @param
     */
    @Override
    public boolean privilegesQuery(String privileges, String mechanisms) {
        Query query = new Query();
        if (privileges != null && !privileges.equals("")) {
            query.addCriteria(Criteria.where("privileges").is(privileges));
        }
        if (mechanisms != null && !mechanisms.equals("")) {
            query.addCriteria(Criteria.where("mechanisms").is(mechanisms));
        }
        List<User> list = find(query);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }


    /**
     * 把用户信息推送给MQ
     *
     * @param
     */
    public void sendMQ(User user) {
        Map map = new HashMap();
        map.put("mechanisms", new ArrayList<Mechanism>());
        map.put("user", user);
        //如果带有机构信息 查询详细的机构信息插入到MQ中
        if (user.getMechanisms() != null && user.getMechanisms().size() > 0 && user.getStatus().equals("0")) {
            List<Mechanism> list = mechanismService.query(user.getMechanisms());
            map.put("mechanisms", list);
        }
        msgProducer.sendMsg_forUser(JSONObject.fromObject(map).toString(), "user-routingKey_" + user.getProjectNumber());

    }

    /**
     * 账号查询
     */
    @Override
    public String queryAccount(Map<String, Object> map) {
        Criteria criteria = Criteria.where("status").is("0");
//        根据类型来查询项目
        if (map.get("projectType") != null && StringUtil.isNotEmpty(map.get("projectType").toString())) {
            criteria.and("projectType").is(map.get("projectType"));
        }
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "projectName", "status","projectType"),
                Aggregation.match(criteria),
                Aggregation.group("projectNumber").first("projectNumber")
                        .as("projectNumber").first("projectName").as("projectName"),
                Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "projectName")))

        );
        List<User> mechanismsList = findAggregateList(new Query(criteria), "user", customerAgg, User.class);
        List<Map<String, Object>> resList = new LinkedList<>();
        if (mechanismsList != null && mechanismsList.size() > 0) {
            for (int i = 0; i < mechanismsList.size(); i++) {
                Map<String, Object> resmap = new HashMap<>();
                resmap.put("projectName", mechanismsList.get(i).getProjectName());
                resmap.put("projectNumber", mechanismsList.get(i).getProjectNumber());
                resList.add(resmap);
            }
        }
        return getSuccess(resList);
    }
}

