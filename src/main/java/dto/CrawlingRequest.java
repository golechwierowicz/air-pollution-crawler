package dto;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.UUID;

public class CrawlingRequest {
    private String url;
    private List<String> xPaths;
    private boolean throttlingEnabled;
    private int throttlingSeconds;
    private int depth;
    private String filterWord;
    private UUID requestUUID;

    public CrawlingRequest() {}

    public CrawlingRequest(String url,
                           List<String> XPaths,
                           boolean throttlingEnabled,
                           int throttlingSeconds,
                           int depth,
                           String filterWord,
                           UUID requestUUID) {
        this.url = url;
        this.xPaths = XPaths;
        this.throttlingEnabled = throttlingEnabled;
        this.throttlingSeconds = throttlingSeconds;
        this.depth = depth;
        this.filterWord = filterWord;
        this.requestUUID = requestUUID;
    }

    public static CrawlingRequest copyCrawlingRequest(CrawlingRequest other) {
        return new CrawlingRequest(other.url,
                other.xPaths,
                other.throttlingEnabled,
                other.throttlingSeconds,
                other.depth,
                other.filterWord,
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

    public String getFilterWord() {
        return filterWord;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(UUID requestUUID) {
        this.requestUUID = requestUUID;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("xPaths", xPaths)
                .add("throttlingEnabled", throttlingEnabled)
                .add("throttlingSeconds", throttlingSeconds)
                .add("depth", depth)
                .add("filterWord", filterWord)
                .add("requestUUID", requestUUID)
                .toString();
    }
}