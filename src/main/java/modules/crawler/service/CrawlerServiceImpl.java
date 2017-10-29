package modules.crawler.service;

import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.crawler.model.WebContent;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import utils.FixPolishSigns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CrawlerServiceImpl implements CrawlerService {
    private static Config config = ConfigFactory.load().getConfig("crawler");
    private static Logger log = Logger.getLogger(CrawlerServiceImpl.class.getName());
    private XPathQueryService xPathQueryService;

    public CrawlerServiceImpl(XPathQueryService xPathQueryService) {
        this.xPathQueryService = xPathQueryService;
    }

    @Override
    public Optional<WebContent> getWebPageContent(String url) {
        UrlValidator urlValidator = new UrlValidator();
        String userAgent = config.getString("browser_agent");

        try {
            Connection.Response response = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .userAgent(userAgent).execute();
            String contentType = response.contentType();

            if (!contentType.contains("text/html"))
                return Optional.empty();

            Document doc = response.parse();
            List<String> urls = doc.getElementsByTag("a")
                    .stream()
                    .map(p -> p.attr("href"))
                    .filter(urlValidator::isValid)
                    .collect(Collectors.toList());
            return Optional.of(new WebContent(response.body(), urls, url));
        } catch (IOException e) {
            log.log(Level.WARNING, "IOException was thrown", e);
        }

        return Optional.empty();
    }

    @Override
    public List<WebContent> extractByXPath(WebContent webContent, String XPath) {
        assert xPathQueryService != null;
        Optional<org.w3c.dom.Document> cleanHtml = xPathQueryService.cleanHtml(webContent.getContent());
        if (cleanHtml.isPresent()) {
            org.w3c.dom.Document doc = cleanHtml.get();
            return xPathQueryService.query(XPath, doc)
                    .stream()
                    .map(s -> new WebContent(FixPolishSigns.fix(s), webContent.getUrls(), XPath))
                    .collect(Collectors.toList());
        } else return ImmutableList.of(new WebContent("", webContent.getUrls(), null));
    }

    @Override
    public List<WebContent> extractByFilterWordOnly(WebContent webContent, List<String> filterWords) {
        assert xPathQueryService != null;
        List<WebContent> result = new ArrayList<>();
        Optional<org.w3c.dom.Document> cleanHtml = xPathQueryService.cleanHtml(webContent.getContent());
        if (cleanHtml.isPresent()) {
            org.w3c.dom.Document doc = cleanHtml.get();
            DocumentTraversal traversal = (DocumentTraversal) doc;
            NodeIterator iterator = traversal.createNodeIterator(doc.getDocumentElement(),
                    NodeFilter.SHOW_ALL, (n) ->
                            WebContent.containsWord(filterWords, n.getTextContent())
                                    ? NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT, true);
            for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
                WebContent wc = new WebContent(n.getTextContent(), webContent.getUrls(), n.getBaseURI(), webContent.getCrawledUrl());
                result.add(wc);
            }
        }
        return result;
    }
}
