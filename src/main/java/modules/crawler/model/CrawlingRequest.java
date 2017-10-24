package modules.crawler.model;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.UUID;

public class CrawlingRequest {
    private List<String> filterWords;
    private String url;
    private List<String> xPaths;
    private boolean throttlingEnabled;
    private int throttlingSeconds;
    private int depth;
    private UUID requestUUID;
    private boolean filterByKeywordOnly = false;

    public CrawlingRequest(List<String> filterWords,
                           String url,
                           List<String> xPaths,
                           boolean throttlingEnabled,
                           int throttlingSeconds,
                           int depth,
                           UUID requestUUID,
                           boolean filterByKeywordOnly) {
        this.filterWords = filterWords;
        this.url = url;
        this.xPaths = xPaths;
        this.throttlingEnabled = throttlingEnabled;
        this.throttlingSeconds = throttlingSeconds;
        this.depth = depth;
        this.requestUUID = requestUUID;
        this.filterByKeywordOnly = filterByKeywordOnly;
    }

    public CrawlingRequest() {}

    public CrawlingRequest(String url,
                           List<String> XPaths,
                           boolean throttlingEnabled,
                           int throttlingSeconds,
                           int depth,
                           List<String> filterWords,
                           UUID requestUUID) {
        this.url = url;
        this.xPaths = XPaths;
        this.throttlingEnabled = throttlingEnabled;
        this.throttlingSeconds = throttlingSeconds;
        this.depth = depth;
        this.filterWords = filterWords;
        this.requestUUID = requestUUID;
    }

    public static CrawlingRequest copyCrawlingRequest(CrawlingRequest other) {
        return new CrawlingRequest(other.url,
                other.xPaths,
                other.throttlingEnabled,
                other.throttlingSeconds,
                other.depth,
                other.filterWords,
                other.requestUUID);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getxPaths() {
        return xPaths;
    }

    public boolean isThrottlingEnabled() {
        return throttlingEnabled;
    }

    public int getThrottlingSeconds() {
        return throttlingSeconds;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(UUID requestUUID) {
        this.requestUUID = requestUUID;
    }

    public void setxPaths(List<String> xPaths) {
        this.xPaths = xPaths;
    }

    public void setThrottlingEnabled(boolean throttlingEnabled) {
        this.throttlingEnabled = throttlingEnabled;
    }

    public void setThrottlingSeconds(int throttlingSeconds) {
        this.throttlingSeconds = throttlingSeconds;
    }

    public List<String> getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(List<String> filterWords) {
        this.filterWords = filterWords;
    }

    public boolean isFilterByKeywordOnly() {
        return filterByKeywordOnly;
    }

    public void setFilterByKeywordOnly(boolean filterByKeywordOnly) {
        this.filterByKeywordOnly = filterByKeywordOnly;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("xPaths", xPaths)
                .add("throttlingEnabled", throttlingEnabled)
                .add("throttlingSeconds", throttlingSeconds)
                .add("depth", depth)
                .add("filterWord", filterWords)
                .add("requestUUID", requestUUID)
                .toString();
    }
}