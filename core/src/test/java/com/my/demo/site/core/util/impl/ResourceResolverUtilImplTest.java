package com.my.demo.site.core.util.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.my.demo.site.core.util.ResourceResolverUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ResourceResolverUtilImplTest {

  private final AemContext context = new AemContext();

  private ResourceResolverUtilImpl resourceResolverUtil;

  @Mock
  private ResourceResolverFactory resourceResolverFactory;

  @Mock
  private ResourceResolver resourceResolver;
  @Test
  void getResourceResolver() throws LoginException {
    context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
    resourceResolverUtil = context.registerInjectActivateService(new ResourceResolverUtilImpl());
    assertNotNull(resourceResolverUtil);
    assertNotNull(resourceResolverUtil.getResourceResolver("test-service-user"));
  }

  @Test
  void getResourceResolverWithExcetion() throws LoginException {
    context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
    resourceResolverUtil = context.registerInjectActivateService(new ResourceResolverUtilImpl());
    assertNotNull(resourceResolverUtil);
    assertNotNull(resourceResolverUtil.getResourceResolver(null));
  }
}