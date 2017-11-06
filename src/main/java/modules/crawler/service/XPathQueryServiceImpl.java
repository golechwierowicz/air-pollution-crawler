package modules.crawler.service;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XPathQueryServiceImpl implements XPathQueryService {
  private static Logger log = LoggerFactory.getLogger(XPathQueryServiceImpl.class.getName());

  @Override
  public List<String> query(String query, Document document) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return evaluate(document, xpath, query);
  }

  @Override
  public Optional<Document> cleanHtml(final String dirtyHtml) {
    TagNode tag = new HtmlCleaner().clean(dirtyHtml);
    try {
      Document doc = new DomSerializer(new CleanerProperties()).createDOM(tag);
      return Optional.of(doc);
    } catch (ParserConfigurationException e) {
      return Optional.empty();
    }
  }

  private List<String> evaluate(Document doc, XPath xpath, String query) {
    List<String> evaluated = new ArrayList<>();
    NodeList result;
    try {
      XPathExpression expr =
          xpath.compile(query);
      result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      evaluated = mapToNodesList(result);
    } catch (AssertionError ae) {
      log.warn("XPath expression eval returned null.", ae);
    } catch (XPathExpressionException e) {
      log.warn("XPath expression was evaluated wrongly.");
    }
    return evaluated;
  }

  private List<String> mapToNodesList(final NodeList input) {
    assert input != null;
    int size = input.getLength();
    List<String> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++)
      result.add(input.item(i).getTextContent());
    return result;
  }
}
