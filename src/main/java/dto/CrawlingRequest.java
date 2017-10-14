package dto;

import com.google.common.base.MoreObjects;

import java.util.List;

public class CrawlingRequest {
    private String url;
    private final List<String> xPaths;
    private final boolean throttlingEnabled;
    private final int throttlingSeconds;
    private int depth;
    private String filterWord;

    public CrawlingRequest(String url, List<String> XPaths, boolean throttlingEnabled, int throttlingSeconds, int depth, String filterWord) {
        this.url = url;
        this.xPaths = XPaths;
        this.throttlingEnabled = throttlingEnabled;
        this.throttlingSeconds = throttlingSeconds;
        this.depth = depth;
        this.filterWord = filterWord;
    }

    public CrawlingRequest(CrawlingRequest other) {
        this.url = other.url;
        this.xPaths = other.xPaths;
        this.depth = other.depth;
        this.throttlingEnabled = other.throttlingEnabled;
        this.throttlingSeconds = other.throttlingSeconds;
        this.depth = other.depth;
        this.filterWord = other.filterWord;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("xPaths", xPaths)
                .add("throttlingEnabled", throttlingEnabled)
                .add("throttlingSeconds", throttlingSeconds)
                .add("depth", depth)
                .add("filterWord", filterWord)
                .toString();
    }
}