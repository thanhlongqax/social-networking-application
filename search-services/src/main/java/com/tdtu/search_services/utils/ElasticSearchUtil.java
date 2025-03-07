package com.tdtu.search_services.utils;


import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
@Component
public class ElasticSearchUtil {

    public static Supplier<Query> supplier(){
        Supplier<Query> supplier = ()->Query.of(q->q.matchAll(matchAllQuery()));
        return supplier;
    }

    public static MatchAllQuery matchAllQuery(){
        val  matchAllQuery = new MatchAllQuery.Builder();
        return matchAllQuery.build();
    }

    public static Supplier<Query> supplierWithNameField(String fieldValue){
        Supplier<Query> supplier = ()->Query.of(q->q.match(matchQueryWithNameField(fieldValue)));
        return supplier;
    }

    public static MatchQuery matchQueryWithNameField(String fieldValue){
        val  matchQuery = new MatchQuery.Builder();
        return matchQuery.field("name").query(fieldValue).build();
    }
}
