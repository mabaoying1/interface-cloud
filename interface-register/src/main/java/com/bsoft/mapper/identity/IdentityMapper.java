package com.bsoft.mapper.identity;

import java.util.Map;

public interface IdentityMapper {
    Map<String,Object> getIdentity(Map<String, Object> paramMap);

    int updateIdentity(Map<String, Object> paramMap);
}