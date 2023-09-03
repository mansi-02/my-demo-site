package com.my.demo.site.core.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.google.gson.JsonArray;
import com.my.demo.site.core.services.ComponentFinderService;
import com.my.demo.site.core.util.ComponentFinderResultItem;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Session;
import com.day.cq.search.result.SearchResult ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentFinderServiceImpl implements ComponentFinderService {

    @Override
    public Map<String, String> createPredicateMap() {
        Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put("type", "nt:unstructured");
        predicateMap.put("path", "/content/we-retail/us/en");
        predicateMap.put("1_property","sling:resourceType");
        predicateMap.put("1_property.value","weretail/components/content/button");
        predicateMap.put("p.limit", "-1");
        return predicateMap;
    }

    @Override
    public SearchResult getQueryResult(Map<String, String> map, ResourceResolver resourceResolver) {
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
//        LOGGER.info("Start of getQueryResult");
        SearchResult result = null;
        if (builder != null) {
            Query query = builder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
            result = query.getResult();

        }
//        LOGGER.info("End of getQueryResult");
        return result;
    }


    @Override
    public JsonArray getPageOccurrenceQueryResult( SearchResult searchResult,ResourceResolver resourceResolver) {

        JsonArray result = new JsonArray();
        try {
            if (resourceResolver != null) {
                Session session = resourceResolver.adaptTo(Session.class);
                ComponentFinderResultItem tempItem = new ComponentFinderResultItem();
                if (session != null && session.isLive()) {
                    ArrayList<ComponentFinderResultItem> resItem = new ArrayList<>();
                    for (Hit hit : searchResult.getHits()) {
                        if(resItem == null){
                            //simple addition
                            tempItem  = null;
                            tempItem.setPath(hit.getPath());
                            tempItem.setIsExperienceFragment(false);
                            tempItem.setOccurrenceCounter(tempItem.getOccurrenceCounter()+1);
                            resItem.add(tempItem);
                        }
                        else{

                            if(resItem.contains(hit.getPath())){
                                // find index of hit path => iterator for list -> update counter value
                            }
                            else{
                                tempItem = null;
                                tempItem.setPath(hit.getPath());
                                tempItem.setIsExperienceFragment(false);
                                tempItem.setOccurrenceCounter(tempItem.getOccurrenceCounter()+1);
                                resItem.add(tempItem);
                            }
                        }
                    }
                }
            }


        } catch (Exception re) {
//            LOGGER.error("Error in GlobalSearchService Class - getAllResults() method : {}",
//                    re.getMessage());
        }
        return result;

    }
}
