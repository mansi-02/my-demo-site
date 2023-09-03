package com.my.demo.site.core.util;


import org.apache.sling.api.resource.ResourceResolver;


/**
 * ResourceResolverUtil is a utility interface.
 */
public interface ResourceResolverUtil {
    ResourceResolver getResourceResolver(String serviceBindingName);
}
