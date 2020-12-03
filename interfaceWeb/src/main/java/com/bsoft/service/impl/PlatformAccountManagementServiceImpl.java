package com.bsoft.service.impl;

import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.PlatformUser;
import com.bsoft.exception.CommonException;
import com.bsoft.service.PlatformAccountManagementService;
import com.bsoft.util.MD5EncodeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：平台账号管理
 * @modified By：
 * @version: $
 */
@Service
public class PlatformAccountManagementServiceImpl extends MongoDaoSupport<PlatformUser> implements PlatformAccountManagementService {

    /**
     * 账号注册
     *
     * @param platformUser
     */
    @Override
    public String register(PlatformUser platformUser) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        platformUser.setLastTime(null);
        platformUser.setCreateTime(null);
        platformUser.setStatus("0");
        //密码加密
        platformUser.setPassword(MD5EncodeUtil.code(platformUser.getPassword(),32));
        //判断是否已经有该账号信息
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(platformUser.getAccount()));
        List<PlatformUser> list=find(query);
        if(list.size()>0){
            throw new CommonException("该账号已存在");
        }
        platformUser.setCreateTime(date);
        platformUser.setLastTime(date);
        save(platformUser);
            return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";

    }
    /**
     * 账号查询
     *
     * @param platformUser
     */
    @Override
    public String query(PlatformUser platformUser) {
        if(platformUser.getPageSize() == null||platformUser.getPageNum()==null){
            throw new CommonException("pagesize pageNum 不能为空");
        }
        Integer pageSize=platformUser.getPageSize();
        Integer pageNum=platformUser.getPageNum();
        platformUser.setPageNum(null);
        platformUser.setPageSize(null);
        platformUser.setLastTime(null);
        platformUser.setCreateTime(null);
        platformUser.setPassword(null);
        Page<PlatformUser> page=findByConditionPage(platformUser,pageNum,pageSize,"account");
        //把密码去除 不返回
        for(PlatformUser user:page.getContent()){
            user.setPassword(null);

        }
        return getSuccessPage(page);
    }
    /**
     * 账号修改
     *
     * @param platformUser
     */
    @Override
    public String modify(PlatformUser platformUser) throws Exception {
        if(platformUser.getId()==null){
            throw new CommonException("Id 不能为空");
        }
        PlatformUser platformUserFind=new PlatformUser();
        platformUserFind.setStatus(platformUser.getStatus());
        platformUserFind.setPassword(platformUser.getPassword());
        platformUserFind.setId(platformUser.getId());
        platformUserFind.setLastTime(new Date(System.currentTimeMillis()));
          //密码加密
        if(platformUser.getPassword()!=null) {
            platformUserFind.setPassword(MD5EncodeUtil.code(platformUser.getPassword(), 32));
        }
        updateById(platformUser.getId(),platformUserFind);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }
    /**
     * 账号删除
     *
     * @param platformUser
     */
    @Override
    public String delete(PlatformUser platformUser) {
        if(platformUser.getId()==null){
            throw new CommonException("Id 不能为空");
        }
        PlatformUser platformUserFind=new PlatformUser();
        platformUserFind.setStatus("1");
        platformUserFind.setId(platformUser.getId());
        platformUserFind.setLastTime(new Date(System.currentTimeMillis()));
        updateById(platformUser.getId(),platformUserFind);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }


}

