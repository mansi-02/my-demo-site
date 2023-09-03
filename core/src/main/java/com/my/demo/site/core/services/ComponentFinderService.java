package com.my.demo.site.core.services;

import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Map;

public interface ComponentFinderService {
    public Map<String,String> createPredicateMap();

    public JsonArray getPageOccurrenceQueryResult(SearchResult searchResult,ResourceResolver resourceResolver) ;

    public SearchResult getQueryResult(Map<String, String> map, ResourceResolver resourceResolver) ;
}
