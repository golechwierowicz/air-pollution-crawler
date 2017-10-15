package model;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.UUID;

public class WebContent {
    private String content;
    private List<String> urls;
    private String XPathUsed;
    private UUID requestID;

    public WebContent(String content, List<String> urls) {
        this.content = content;
        this.urls = urls;
    }

    public WebContent(String content, List<String> urls, String xPath) {
        this.content = content;
        this.urls = urls;
        this.XPathUsed = xPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WebContent(String content) {
        this.content = content;
    }

    public boolean containsWord(String word) {
        return content.contains(word);
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

    public void setRequestID(UUID requestID) {
        this.requestID = requestID;
    }

    public UUID getRequestID() {
        return requestID;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("content", content)
                .add("urls", urls)
                .add("XPathUsed", XPathUsed)
                .toString();
    }
}
