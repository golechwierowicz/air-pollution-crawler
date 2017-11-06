package modules.crawler.service;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class XPathQueryServiceImplTest {
  private XPathQueryService xPathQueryService;

  @Before
  public void before() {
    xPathQueryService = new XPathQueryServiceImpl();
  }

  @Test
  public void querySingle() throws Exception {
    final String content = String.join("\n",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
        "<a>",
        "<b>",
        "</b>",
        "</a>");
    ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8.name()));
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
    List<String> result = xPathQueryService.query("//a/b", doc);
    assertEquals(1, result.size());
  }

  @Test
  public void queryMultiple() throws Exception {
    final String content = String.join("\n",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
        "<a>",
        "<b>",
        "</b>",
        "<b>",
        "</b>",
        "</a>");
    ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8.name()));
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
    List<String> result = xPathQueryService.query("//a/b", doc);
    assertEquals(2, result.size());
  }

  @Test
  public void queryNone() throws Exception {
    final String content = String.join("\n",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
        "<a>",
        "<b>",
        "</b>",
        "<b>",
        "</b>",
        "</a>");
    ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8.name()));
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
    List<String> result = xPathQueryService.query("//a/c", doc);
    assertEquals(0, result.size());
  }

  @Test
  public void cleanHtmlOpenTag() throws Exception {
    final String content = String.join("\n",
        "<a>",
        "<b>",
        "</a>");
    final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
        "<html>\n" +
        "<head/>\n" +
        "<body>\n" +
        "<a>\n" +
        "<b>\n" +
        "</b>\n" +
        "</a>\n" +
        "</body>\n" +
        "</html>\n";

    Optional<Document> document = xPathQueryService.cleanHtml(content);
    assertEquals(true, document.isPresent());
    assertEquals(expected, DocToString(document.get()));
  }

  @Test
  public void cleanHtmlMisusedTag() throws Exception {
    final String content = String.join("\n",
        "<a>",
        "<b>",
        "</a>",
        "</b>");
    final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
        "<html>\n" +
        "<head/>\n" +
        "<body>\n" +
        "<a>\n" +
        "<b>\n" +
        "</b>\n" +
        "</a>\n" +
        "</body>\n" +
        "</html>\n";

    Optional<Document> document = xPathQueryService.cleanHtml(content);
    assertEquals(true, document.isPresent());
    assertEquals(expected, DocToString(document.get()));
  }

  @Test
  public void cleanHtmlMalformedTag() throws Exception {
    final String content = String.join("\n",
        "<a>",
        "<b>",
        "</b>",
        "<");
    final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
        "<html>\n" +
        "<head/>\n" +
        "<body>\n" +
        "<a>\n" +
        "<b>\n" +
        "</b>\n" +
        "&amp;lt;</a>\n" +
        "</body>\n" +
        "</html>\n";

    Optional<Document> document = xPathQueryService.cleanHtml(content);
    assertEquals(true, document.isPresent());
    assertEquals(expected, DocToString(document.get()));
  }

  private String DocToString(Document doc) {
    try {
      StringWriter sw = new StringWriter();
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      transformer.transform(new DOMSource(doc), new StreamResult(sw));
      return sw.toString();
    } catch (Exception ex) {
      throw new RuntimeException("Error converting to String", ex);
    }
  }

}