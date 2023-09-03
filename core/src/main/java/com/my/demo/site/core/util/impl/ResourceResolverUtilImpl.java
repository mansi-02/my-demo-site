package com.my.demo.site.core.util.impl;

import com.my.demo.site.core.util.ResourceResolverUtil;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * ResourceResolverUtil is a utility service to get ResourceResolver via service user.
 */
@Component(service = ResourceResolverUtil.class, immediate = true)
public class ResourceResolverUtilImpl implements ResourceResolverUtil {


    /**
     * The constant LOGGER.
     */
    private static final Logger logger = LoggerFactory.getLogger(ResourceResolverUtilImpl.class);

    /**
     * The ResourceResolverFactory Service
     */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;


    /**
     * getResourceResolver method gets the resource resolver via service user and returns it.
     * @param serviceBindingName
     * @return ResourceResolver
     */
    @Override
    public ResourceResolver getResourceResolver(String serviceBindingName) {
        logger.debug("start of getResourceResolver method with serviceBindingName: {}", serviceBindingName);
        ResourceResolver resourceResolver = null;
        final Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, serviceBindingName);
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
        } catch (LoginException e) {
            logger.error("Error while getting resource resolver: {}", e.getMessage());
        }
        logger.debug("end of getResourceResolver method with resourceResolver: {}", resourceResolver);
        return resourceResolver;
    }
}