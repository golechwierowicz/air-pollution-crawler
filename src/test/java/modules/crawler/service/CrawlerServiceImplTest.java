package modules.crawler.service;

import base.SystemUtils;
import com.google.common.collect.ImmutableList;
import modules.crawler.model.WebContent;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CrawlerServiceImplTest extends SystemUtils {
  private CrawlerService crawlerService = null;

  @Before
  public void before() {
    crawlerService = new CrawlerServiceImpl(new XPathQueryServiceImpl());
  }

  @Test
  public void extractByXPathSamplePage() throws Exception {
    final String content = getFile("modules/crawler/service/sample-web-content.html");
    final String XPath = "//*/article/h1/a";
    List<WebContent> result = crawlerService.extractByXPath(new WebContent(content), XPath);
    assertEquals(62, result.size());
  }

  @Test
  public void extractByXPathBasic() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "</a>");
    final WebContent content = new WebContent(input);
    final String xpath = "//a/b";
    final List<WebContent> result = crawlerService.extractByXPath(content, xpath);
    assertEquals(1, result.size());
    assertEquals("\n1\n", result.get(0).getContent());
  }

  @Test
  public void extractByXPathMultiple() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "<b>",
        "2",
        "</b>",
        "</a>");
    final WebContent content = new WebContent(input);
    final String xpath = "//a/b";
    final List<WebContent> result = crawlerService.extractByXPath(content, xpath);
    assertEquals(2, result.size());
    assertEquals("\n1\n", result.get(0).getContent());
    assertEquals("\n2\n", result.get(1).getContent());
  }

  @Test
  public void extractByXpathNone() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "<b>",
        "2",
        "</b>",
        "</a>");
    final WebContent content = new WebContent(input);
    final String xpath = "//a/c";
    List<WebContent> result = crawlerService.extractByXPath(content, xpath);
    assertEquals(0, result.size());
  }

  @Test
  public void extractByFilterWordOnlyExistsOne() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "<b>",
        "2",
        "</b>",
        "</a>");
    List<WebContent> result = crawlerService.extractByFilterWordOnly(new WebContent(input), ImmutableList.of("2"));
    assertEquals(1, result.size());
    assertEquals("\n2\n", result.get(0).getContent());
  }

  @Test
  public void extractByFilterWordOnlyExistsMultiple() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "<b>",
        "2",
        "</b>",
        "</a>");
    List<WebContent> result = crawlerService.extractByFilterWordOnly(new WebContent(input), ImmutableList.of("1", "2"));
    assertEquals(2, result.size());
    assertEquals("\n1\n", result.get(0).getContent());
    assertEquals("\n2\n", result.get(1).getContent());
  }

  @Test
  public void extractByFilterWordOnlyExistsNone() throws Exception {
    final String input = String.join("\n",
        "<a>",
        "<b>",
        "1",
        "</b>",
        "<b>",
        "2",
        "</b>",
        "</a>");
    List<WebContent> result = crawlerService.extractByFilterWordOnly(new WebContent(input), ImmutableList.of("3"));
    assertEquals(0, result.size());
  }

}