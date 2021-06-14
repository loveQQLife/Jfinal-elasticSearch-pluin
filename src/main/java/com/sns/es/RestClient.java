package com.sns.es;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author kraken
 * @date 2021/6/15 1:54
 */
public class RestClient {

    private static RestHighLevelClient restHighLevelClient;
    private RestClient(){

    }

    public static void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        RestClient.restHighLevelClient = restHighLevelClient;
    }

    public static RestHighLevelClient getInstance(){
        return restHighLevelClient;
    }
}
