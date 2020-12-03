package com.bsoft.service;
import com.bsoft.model.RequestRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
public interface LogEsService {

    public void createIndex(String idxName,String idxSQL);

    public boolean insertOrUpdateOne(String idxName ,String id,  Map object) ;

    public boolean deleteOne(String idxName ,String id) ;

    public boolean deleteAll(String idxName) ;

    public List<com.bsoft.entity.RequestRecord> searchMatch(String idxName , String key, String value) throws IOException ;

    boolean addPlayer(RequestRecord player, String id) throws IOException;

    Map<String,Object> getPlayer(String id) throws IOException;

    boolean updatePlayer(RequestRecord player,String id) throws IOException;

    boolean deletePlayer(String id) throws IOException;

    boolean deleteAllPlayer() throws IOException;

    boolean importAll() throws IOException;


    List<RequestRecord> searchTerm(String key,String value) throws IOException;

    List<RequestRecord> searchMatchPrefix(String key,String value) throws IOException;
}
