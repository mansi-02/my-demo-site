package com.my.demo.site.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.my.demo.site.core.util.ResourceResolverUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ComponentFinderServiceImplTest {

  private final AemContext context = new AemContext();

  private ComponentFinderServiceImpl componentFinderServiceImpl;

  @Mock
  private ResourceResolverUtil resourceResolverUtil;

  @Mock
  private ResourceResolver resourceResolver;

  @Mock
  private QueryBuilder builder;

  @Mock
  private Query query, query1;

  @Mock
  private SearchResult result, result1;

  @Mock
  private Session session;

  @Mock
  private Hit hit, hit1, hit2, hit3;

  @BeforeEach
  void setUp() {
    context.registerService(ResourceResolverUtil.class, resourceResolverUtil);
  }

  @Test
  void getComponentUsageCountWithoutExperienceFragment() throws RepositoryException {
    Resource resource = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource.json",
            "/content/mydemosite/us/en/jcr:content/root/container/title");
    Resource resource2 = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource2.json",
            "/content/mydemosite/us/en/jcr:content/root/container/title2");
    List<Hit> hits = new ArrayList<>();
    hits.add(hit);
    hits.add(hit2);
    when(resourceResolverUtil.getResourceResolver(anyString())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(builder);
    when(builder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
    when(resourceResolver.adaptTo(eq(Session.class))).thenReturn(session);
    when(query.getResult()).thenReturn(result);
    when(result.getHits()).thenReturn(hits);
    when(hit.getResource()).thenReturn(resource);
    when(hit2.getResource()).thenReturn(resource2);
    componentFinderServiceImpl = context.registerInjectActivateService(
        new ComponentFinderServiceImpl());
    assertNotNull(componentFinderServiceImpl);
    HashMap<String,Integer> resultMap = componentFinderServiceImpl.getComponentUsageCount("/content/mydemosite/us",
        "mydemosite/components/title");
    assertNotNull(resultMap);
    assertEquals(2, resultMap.get("/content/mydemosite/us/en"));
  }

  @Test
  void getComponentUsageCountWithoutExperienceFragmentWithTwoComponents() throws RepositoryException {
    Resource resource = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource.json",
            "/content/mydemosite/us/test");
    List<Hit> hits = new ArrayList<>();
    hits.add(hit);
    when(resourceResolverUtil.getResourceResolver(anyString())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(builder);
    when(builder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
    when(resourceResolver.adaptTo(eq(Session.class))).thenReturn(session);
    when(query.getResult()).thenReturn(result);
    when(result.getHits()).thenReturn(hits);
    when(hit.getResource()).thenReturn(resource);
    componentFinderServiceImpl = context.registerInjectActivateService(
        new ComponentFinderServiceImpl());
    assertNotNull(componentFinderServiceImpl);
    HashMap<String,Integer> resultMap = componentFinderServiceImpl.getComponentUsageCount("/content/mydemosite/us",
        "mydemosite/components/title");
    assertNotNull(resultMap);
    assertEquals(1, resultMap.get("/content/mydemosite/us/test"));
  }

  @Test
  void getComponentUsageCountWithExperienceFragment() throws RepositoryException {
    Resource resource1 = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource1.json",
            "/content/mydemosite/us/en/jcr:content/root/container/expfrag");
    Resource resource2 = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource2.json",
            "/content/mydemosite/us/en/jcr:content/root/container/title");
      List<Hit> hits = new ArrayList<>();
      hits.add(hit);
      hits.add(hit2);
      when(resourceResolverUtil.getResourceResolver(anyString())).thenReturn(resourceResolver);
      when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(builder);
      when(builder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
      when(resourceResolver.adaptTo(eq(Session.class))).thenReturn(session);
      when(query.getResult()).thenReturn(result);
      when(result.getHits()).thenReturn(hits);
      when(hit.getResource()).thenReturn(resource1);
      when(hit2.getResource()).thenReturn(resource2);
      List<Hit> hits1 = new ArrayList<>();
      hits1.add(hit1);
      Resource resource = context.load(true)
          .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource.json",
              "/content/experience-fragments/mydemosite/us/en/site/header/master");
    when(builder.createQuery(argThat(grp -> "path=path: path=/content/experience-fragments/mydemosite/us/en/site/header/master".equals(grp.get(1).toString())), any(Session.class))).thenReturn(query1);
      when(query1.getResult()).thenReturn(result1);
      when(result1.getHits()).thenReturn(hits1);
      when(hit1.getResource()).thenReturn(resource);

    componentFinderServiceImpl = context.registerInjectActivateService(
        new ComponentFinderServiceImpl());
    assertNotNull(componentFinderServiceImpl);
    HashMap<String,Integer> resultMap = componentFinderServiceImpl.getComponentUsageCount("/content/mydemosite/us",
        "mydemosite/components/title");
    assertNotNull(resultMap);
    assertEquals(2, resultMap.get("/content/mydemosite/us/en"));
  }

  @Test
  void getComponentUsageCountWithExperienceFragment2() throws RepositoryException {
    Resource resource1 = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource1.json",
            "/content/mydemosite/us/en/jcr:content/root/container/expfrag");
    Resource resource2 = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource2.json",
            "/content/mydemosite/us/en/jcr:content/root/container/title");
    List<Hit> hits = new ArrayList<>();
    hits.add(hit2);
    hits.add(hit);
    when(resourceResolverUtil.getResourceResolver(anyString())).thenReturn(resourceResolver);
    when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(builder);
    when(builder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
    when(resourceResolver.adaptTo(eq(Session.class))).thenReturn(session);
    when(query.getResult()).thenReturn(result);
    when(result.getHits()).thenReturn(hits);
    when(hit.getResource()).thenReturn(resource1);
    when(hit2.getResource()).thenReturn(resource2);
    List<Hit> hits1 = new ArrayList<>();
    hits1.add(hit1);
    Resource resource = context.load(true)
        .json("/com/my/demo/site/core/services/ComponentFinderServiceImpl/resource.json",
            "/content/experience-fragments/mydemosite/us/en/site/header/master");
    when(builder.createQuery(argThat(grp -> "path=path: path=/content/experience-fragments/mydemosite/us/en/site/header/master".equals(grp.get(1).toString())), any(Session.class))).thenReturn(query1);
    when(query1.getResult()).thenReturn(result1);
    when(result1.getHits()).thenReturn(hits1);
    when(hit1.getResource()).thenReturn(resource);

    componentFinderServiceImpl = context.registerInjectActivateService(
        new ComponentFinderServiceImpl());
    assertNotNull(componentFinderServiceImpl);
    HashMap<String,Integer> resultMap = componentFinderServiceImpl.getComponentUsageCount("/content/mydemosite/us",
        "mydemosite/components/title");
    assertNotNull(resultMap);
    assertEquals(2, resultMap.get("/content/mydemosite/us/en"));
  }
}