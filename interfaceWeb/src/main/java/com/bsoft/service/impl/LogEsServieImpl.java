package com.bsoft.service.impl;

import com.bsoft.entity.RequestRecord;
import com.bsoft.service.LogEsService;
import com.bsoft.service.LogService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LogEsServieImpl<T> implements LogEsService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /** 创建索引
     * @author fugl
     * @param idxName
     * @param idxSQL
     */
    public void createIndex(String idxName,String idxSQL){
        try {
            if (this.isExistsIndex(idxName)) {
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSQL, XContentType.JSON);
            // 设置别名 方便以后的扩展
            request.alias(new Alias(idxName));
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.exit(0);
        }
    }

    /** 断某个index是否存在
     * @author fugl
     * @param idxName
     */
    public boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName),RequestOptions.DEFAULT);
    }

    /** 设置分片
     * @author fugl
     * @param request
     */
    public void buildSetting(CreateIndexRequest request){
        request.settings(Settings.builder().put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));
    }

    public boolean insertOrUpdateOne(String idxName ,String id, Map object) {
        IndexRequest request = new IndexRequest(idxName).id(id).source(object);

        try {
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            System.out.println(JSONObject.fromObject(response));
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
        return true ;
    }

    public boolean deleteOne(String idxName ,String id) {
        DeleteRequest request = new DeleteRequest(idxName,id);

        try {
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            System.out.println(JSONObject.fromObject(response));
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
        return true ;
    }

    public boolean deleteAll(String idxName) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(QueryBuilders.matchAllQuery());
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
//            System.out.println(JSONObject.fromObject(response));
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
        return true ;
    }

    @Override
    public List<RequestRecord> searchMatch(String idxName ,String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(idxName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
        searchSourceBuilder.from(2);
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.fromObject(response));

        SearchHit[] hits = response.getHits().getHits();
        List<RequestRecord> playerList = new LinkedList<>();
//        for(SearchHit hit: hits){
//            JSONObject.toBean(hit.getSourceAsString(),RequestRecord.class);
//            playerList.add(player);
//        }

        return playerList;
    }

    @Override
    public boolean addPlayer(com.bsoft.model.RequestRecord player, String id) throws IOException {
        return false;
    }

    @Override
    public Map<String, Object> getPlayer(String id) throws IOException {
        return null;
    }

    @Override
    public boolean updatePlayer(com.bsoft.model.RequestRecord player, String id) throws IOException {
        return false;
    }

    @Override
    public boolean deletePlayer(String id) throws IOException {
        return false;
    }

    @Override
    public boolean deleteAllPlayer() throws IOException {
        return false;
    }

    @Override
    public boolean importAll() throws IOException {
        return false;
    }



    @Override
    public List<com.bsoft.model.RequestRecord> searchTerm(String key, String value) throws IOException {
        return null;
    }

    @Override
    public List<com.bsoft.model.RequestRecord> searchMatchPrefix(String key, String value) throws IOException {
        return null;
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if(beanMap.get(key) != null)
                    map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
