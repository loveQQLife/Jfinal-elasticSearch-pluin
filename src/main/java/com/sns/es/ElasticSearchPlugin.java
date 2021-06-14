package com.sns.es;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.IPlugin;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * jFinal集成elasticSearch
 */
public class ElasticSearchPlugin implements IPlugin {
    private static List<String> nodeList = new ArrayList<>(8);
    private String clusterName;
    private String endPoint;
    private RestHighLevelClient restHighLevelClient;

    public ElasticSearchPlugin(String endPoint, String clusterName) {
        this.endPoint = endPoint;
        this.clusterName = clusterName;
    }

    @Override
    public boolean start() {

        try {
            if (endPoint == null || endPoint.length() == 0) {
                LogKit.info("No NODES CONFIG");
            }

            String[] node_arr = endPoint.split(",");
            HttpHost[] hosts = new HttpHost[node_arr.length];
            Arrays.stream(node_arr).filter(str -> str.contains(":")).collect(Collectors.toList()).forEach(forEachWithIndex((node, index) -> {
                HttpHost httpHost = new HttpHost(node.substring(0, node.lastIndexOf(":")), Integer.parseInt(node.substring(node.lastIndexOf(":") + 1)));
                hosts[index] = httpHost;
            }));
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(hosts));
            com.sns.es.RestClient.setRestHighLevelClient(restHighLevelClient);
            LogKit.info("elasticsearchClient init success");
            return true;
        } catch (Exception e) {
            LogKit.info("elasticsearchClient init fail : {}", e);
        }
        return false;
    }

    @Override
    public boolean stop() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
            return true;
        } catch (Exception e) {
            LogKit.info("elasticsearchClient stop fail : {}", e);
        }
        return false;
    }

    /**
     * 利用BiConsumer实现foreach循环支持index
     *
     * @param biConsumer
     * @param <T>
     * @return
     */
    private static <T> Consumer<T> forEachWithIndex(BiConsumer<T, Integer> biConsumer) {
        class IncrementInt {
            int i = 0;

            public int getAndIncrement() {
                return i++;
            }
        }
        IncrementInt incrementInt = new IncrementInt();
        return t -> biConsumer.accept(t, incrementInt.getAndIncrement());
    }

}
