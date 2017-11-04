package modules.crawler.service;

import base.SystemUtils;
import modules.crawler.model.WebContent;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CrawlerServiceImplTest extends SystemUtils {
  @Test
  public void extractByXPath() throws Exception {
    String content = getFile("modules/crawler/service/sample-web-content.html");
    String XPath = "//*/article/h1/a";
    XPathQueryService xPathQueryService = new XPathQueryServiceImpl();
    CrawlerService crawlerService = new CrawlerServiceImpl(xPathQueryService);
    List<WebContent> result = crawlerService.extractByXPath(new WebContent(content), XPath);
    assertEquals(result.size(), 62);
  }
}