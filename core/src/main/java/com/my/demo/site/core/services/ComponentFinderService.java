package com.my.demo.site.core.services;

import com.day.cq.search.result.SearchResult;
import java.util.HashMap;
import java.util.Map;

/**
 * ComponentFinderService interface is a blueprint for serivice impl
 */
public interface ComponentFinderService {

    /**
     * getComponentUsageCount method counts the number of times component is present in the rootPath
     * @param rootPath
     * @param resourceType
     * @return HashMap<String,Integer>
     */
    HashMap<String,Integer> getComponentUsageCount(String rootPath, String resourceType);
}
