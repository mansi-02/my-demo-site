package com.my.demo.site.core.servlets;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.demo.site.core.services.ComponentFinderService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ComponentFinderServletTest {

  private final AemContext context = new AemContext();

  private ComponentFinderServlet componentFinderServlet;

  @Mock
  private ComponentFinderService componentFinderService;
  @Mock
  private SlingHttpServletResponse response;
  @Mock
  private SlingHttpServletRequest request;
  @Mock
  private ServletOutputStream servletOutputStream;
  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Test
  void doPostTest() throws ServletException, IOException {
    String expectedResponse = IOUtils.toString(
        Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/my/demo/site/core/servlets/ComponentFinderServlet/success_response.json")),
        StandardCharsets.UTF_8);
    JsonObject expectedResponseJsonObject = JsonParser.parseString(expectedResponse)
        .getAsJsonObject();
    HashMap<String, Integer> resultMap = new HashMap<>();
    resultMap.put("/content/mydemosite/us/en", 1);
    resultMap.put("/content/mydemosite/us/en1", 1);
    context.registerService(ComponentFinderService.class, componentFinderService);
    when(request.getParameter("rootPath")).thenReturn("/content/mydemosite/us");
    when(request.getParameter("resourceType")).thenReturn("mydemosite/components/title");
    when(componentFinderService.getComponentUsageCount(anyString(), anyString())).thenReturn(
        resultMap);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    componentFinderServlet = context.registerInjectActivateService(new ComponentFinderServlet());
    assertNotNull(componentFinderServlet);
    componentFinderServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseString = new String(outputStream.getValue());
    assertNotNull(responseString);
    JsonObject responseJsonObject = JsonParser.parseString(responseString).getAsJsonObject();
    assertEquals(expectedResponseJsonObject, responseJsonObject);
  }
}