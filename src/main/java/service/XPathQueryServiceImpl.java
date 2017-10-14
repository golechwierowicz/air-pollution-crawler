package service;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grzegorz Olechwierowicz
 * @since 29.03.2017.
 */
public class XPathQueryServiceImpl implements XPathQueryService {
    @Override
    public List<String> query(String query, Document document) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        return evaluate(document, xpath, query);
    }

    @Override
    public Optional<Document> cleanHtml(final String dirtyHtml) {
        TagNode tag = new HtmlCleaner().clean(dirtyHtml);
        try {
            Document doc = new DomSerializer(new CleanerProperties()).createDOM(tag); // TODO: check if it parses DOM correctly
            return Optional.of(doc);
        } catch (ParserConfigurationException e) {
            return Optional.empty();
        }
    }

    private List<String> evaluate(Document doc, XPath xpath, String query) {
        List<String> evalued = new ArrayList<>();
        NodeList result = null;
        try {
            XPathExpression expr =
                    xpath.compile(query);
            result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            evalued =  mapToNodesList(result);
        } catch (AssertionError ae) {
            System.out.println("XPath expression eval returned null.");
            ae.printStackTrace();
        } catch (XPathExpressionException e) {
            System.out.println("XPath expression was evaluated wrongly.");
            e.printStackTrace();
        }
        return evalued;
    }

    private List<String> mapToNodesList(NodeList input) {
        List<String> result = new ArrayList<>();
        assert input != null;
        int size = input.getLength();
        for (int i = 0; i < size; i++) {
            result.add(input.item(i).getTextContent());
        }
        return result;
    }
}
