package service;

import model.WebContent;

import java.util.List;
import java.util.Optional;

public interface CrawlerService {
    Optional<WebContent> getWebPageContent(String url);

    List<WebContent> extractByXPath(WebContent webContent, String XPath);
}