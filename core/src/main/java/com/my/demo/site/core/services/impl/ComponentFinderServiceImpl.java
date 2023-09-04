package com.my.demo.site.core.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.foundation.Search;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.my.demo.site.core.services.ComponentFinderService;
//import com.my.demo.site.core.util.ComponentFinderResultItem;
import com.my.demo.site.core.util.ResourceResolverUtil;
import com.my.demo.site.core.util.impl.ResourceResolverUtilImpl;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import com.day.cq.search.result.SearchResult;

import java.util.HashMap;
import java.util.Map;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the ComponentFinderService Implementation which finds the occurrences of a component present on a page.
 */
@Component(service = ComponentFinderService.class, immediate = true)
public class ComponentFinderServiceImpl implements ComponentFinderService {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentFinderService.class);

    /**
     * The ResourceResolverUtil service.
     */
    @Reference
    private ResourceResolverUtil resourceResolverUtil;

    /**
     * createPredicateMap method creates the Predicate Map
     * @param rootPath
     * @param resourceType
     * @return Map<String,String>
     *
     *     QUERYBUILDER QUERY being userd
     *
     *      type=nt:unstructured
     *      path=/content/mydemosite/us/en
     *      group.1_group.1_property=sling:resourceType
     *      group.1_group.1_property.value=mydemosite/components/button
     *      group.2_group.1_property=sling:resourceType
     *      group.2_group.1_property.value=mydemosite/components/experiencefragment
     *      p.limit=-1
     *      group.p.or=true
     */
    private Map<String, String> createPredicateMap(String rootPath, String resourceType) {

        LOGGER.info("::: Start of createPredicateMap method :::");
        Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put("type", "nt:unstructured");
        predicateMap.put("path", rootPath);
        predicateMap.put("group.1_group.1_property","sling:resourceType");
        predicateMap.put("group.1_group.1_property.value",resourceType);
        predicateMap.put("group.2_group.1_property","sling:resourceType");
        predicateMap.put("group.2_group.1_property.value","mydemosite/components/experiencefragment");
        predicateMap.put("p.limit", "-1");
        predicateMap.put("group.p.or","true");
        LOGGER.info("::: End of createPredicateMap method :::");

        return predicateMap;
    }

    /**
     * getQueryResult method executes query and returns the searchResult object
     *
     * @param map
     * @return SearchResult
     */
    private SearchResult getQueryResult(Map<String, String> map) {

        LOGGER.info("::: Start of getQueryResult method :::");
        ResourceResolver resourceResolver = resourceResolverUtil.getResourceResolver(
                "demo-site-content-reader-service-mapper");
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        SearchResult result = null;
        if (builder != null) {
            Query query = builder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
            result = query.getResult();

        }
        LOGGER.info("::: End of getQueryResult method :::");

        return result;
    }

    /**
     * getComponentUsageCount method counts the number of times component is present in the rootPath
     * @param rootPath
     * @param resourceType
     * @return HashMap<String,Integer>
     */
    @Override
    public HashMap<String,Integer> getComponentUsageCount(String rootPath, String resourceType) {

        LOGGER.info("::: Start of getComponentUsageCount method :::");
        HashMap<String,Integer> resItem = new HashMap<>();
        getPageComponents(rootPath, resourceType, resItem);
        LOGGER.info("::: End of getComponentUsageCount method :::");

        return resItem;
    }

    /**
     * getPageComponents is a method which iterates over the results of searchhits and checks
     * if resource is experience fragment
     *
     * @param rootPath
     * @param resourceType
     * @param resItem
     */
    private void getPageComponents(String rootPath, String resourceType, Map<String,Integer> resItem) {

        LOGGER.info("::: Start of getPageComponents method :::");

        Map<String,String> predicateMap = createPredicateMap(rootPath, resourceType);
        ResourceResolver resourceResolver = resourceResolverUtil.getResourceResolver("demo-site-content-reader-service-mapper");
        SearchResult searchResult = getQueryResult(predicateMap);
        for (Hit hit : searchResult.getHits()) {
            try {
                if(hit.getResource().getResourceType().equals("mydemosite/components/experiencefragment")){
                    String fragmentVariationPath = hit.getResource().getValueMap().get("fragmentVariationPath", "");
                    if(resItem.containsKey(pathSplitter(hit.getResource().getPath()))) {
                        int counter = resItem.get(pathSplitter(hit.getResource().getPath()));
                        resItem.put(pathSplitter(hit.getResource().getPath()), getComponentInExpFragment(fragmentVariationPath, resourceType, counter));
                    } else {
                        resItem.put(pathSplitter(hit.getResource().getPath()), getComponentInExpFragment(fragmentVariationPath, resourceType, 0));
                    }
                } else if (hit.getResource().getResourceType().equals(resourceType)){
                    if(resItem.containsKey(pathSplitter(hit.getResource().getPath()))){
                        int counter = resItem.get(pathSplitter(hit.getResource().getPath()))+1;
                        resItem.put(pathSplitter(hit.getResource().getPath()),counter);
                    }
                    else{
                        resItem.put(pathSplitter(hit.getResource().getPath()),1);
                    }
                }
            } catch (RepositoryException e) {
                LOGGER.error("RepositoryException : {}",e.getMessage());
            }
        }

        LOGGER.info("::: End of getPageComponents method :::");
    }

    /**
     * getComponentInExpFragment is a recursive method that counts the occurrence of component
     * @param path
     * @param resourceType
     * @param count
     * @return count
     */
    private int getComponentInExpFragment(String path, String resourceType, int count ) {

        LOGGER.info("::: Start of getComponentInExpFragment method :::");

        Map<String,String> predicateMap = createPredicateMap(path, resourceType);
        SearchResult searchResult = getQueryResult(predicateMap);
        for (Hit hit : searchResult.getHits()) {
            try {
                if(hit.getResource().getResourceType().equals("mydemosite/components/experiencefragment")){
                    String fragmentVariationPath = hit.getResource().getValueMap().get("fragmentVariationPath", "");
                    count = getComponentInExpFragment(fragmentVariationPath, resourceType, count);
                } else if (hit.getResource().getResourceType().equals(resourceType)){
                    count++;
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("::: End of getComponentInExpFragment method :::");
        return count;
    }


    /**
     * pathSplitter is a method that splits the path from jcr:content and returns it
     * @param path
     * @return String
     */
    private String pathSplitter(String path){
        return path.split("/jcr:content")[0];
    }


}
