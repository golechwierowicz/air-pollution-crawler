package dto;

import com.google.common.base.MoreObjects;

import java.util.List;

public class CrawlingRequest {
    private final String url;
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

    public String getUrl() {
        return url;
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