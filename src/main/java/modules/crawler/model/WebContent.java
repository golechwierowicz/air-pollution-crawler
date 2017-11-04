package modules.crawler.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class WebContent implements Serializable {
  private String content;
  private List<String> urls;
  private String XPathUsed;
  private UUID requestID;
  private String crawledUrl;

  public WebContent(String content, List<String> urls) {
    this.content = content;
    this.urls = urls;
  }

  public WebContent(String content, List<String> urls, String xPath) {
    this.content = content;
    this.urls = urls;
    this.XPathUsed = xPath;
  }

  public WebContent(String content, List<String> urls, String xPath, String crawledUrl) {
    this.content = content;
    this.urls = urls;
    this.XPathUsed = xPath;
    this.crawledUrl = crawledUrl;
  }

  public WebContent(String content) {
    this.content = content;
  }

  public static boolean containsWord(List<String> words, String text) {
    for (String word : words) {
      if (text.contains(word)) {
        return true;
      }
    }
    return false;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean containsWord(List<String> words) {
    for (String word : words) {
      if (content.contains(word)) {
        return true;
      }
    }
    return false;
  }

  public List<String> getUrls() {
    return urls;
  }

  public void setUrls(List<String> urls) {
    this.urls = urls;
  }

  public String getXPathUsed() {
    return XPathUsed;
  }

  public void setXPathUsed(String XPathUsed) {
    this.XPathUsed = XPathUsed;
  }

  public UUID getRequestID() {
    return requestID;
  }

  public void setRequestID(UUID requestID) {
    this.requestID = requestID;
  }

  public String getCrawledUrl() {
    return crawledUrl;
  }

  public void setCrawledUrl(String crawledUrl) {
    this.crawledUrl = crawledUrl;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("content", content)
        .add("urls", urls)
        .add("XPathUsed", XPathUsed)
        .add("requestID", requestID)
        .add("crawledUrl", crawledUrl)
        .toString();
  }
}
