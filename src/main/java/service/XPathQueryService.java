package service;

import org.w3c.dom.Document;

import java.util.List;
import java.util.Optional;

public interface XPathQueryService {
    List<String> query(String xPath, Document document);
    Optional<Document> cleanHtml(String dirtyHtml);
}