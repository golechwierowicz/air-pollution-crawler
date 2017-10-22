package modules.crawler.service;

import modules.crawler.model.WebContent;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CrawlerServiceImpl implements CrawlerService {
    private static Logger log = Logger.getLogger(CrawlerServiceImpl.class.getName());
    private XPathQueryService xPathQueryService;

    public CrawlerServiceImpl(XPathQueryService xPathQueryService) {
        this.xPathQueryService = xPathQueryService;
    }

    @Override
    public Optional<WebContent> getWebPageContent(String url) {
        UrlValidator urlValidator = new UrlValidator();
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/61.0.3163.100 Safari/537.36"; // TODO: move to config

        try {
            Connection.Response response = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .userAgent(userAgent).execute();
            String contentType = response.contentType();

            if(!contentType.contains("text/html"))
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
        if(cleanHtml.isPresent()) {
            org.w3c.dom.Document doc = cleanHtml.get();
            return xPathQueryService.query(XPath, doc)
                    .stream()
                    .map(s -> new WebContent(s, webContent.getUrls(), XPath))
                    .collect(Collectors.toList());
        } else return new ArrayList<>();
    }
}
