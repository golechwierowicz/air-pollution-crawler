package modules.crawler.service;

import modules.crawler.model.WebContent;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class CrawlerServiceImplIT {
    @Test
    public void getWebPageContent() throws Exception { // requires internet
        String testUrl = "https://www.tvn24.pl";
        XPathQueryService xPathQueryService = new XPathQueryServiceImpl();
        CrawlerService crawlerService = new CrawlerServiceImpl(xPathQueryService);
        Optional<WebContent> webContentOpt = crawlerService.getWebPageContent(testUrl);
        assertTrue(webContentOpt.isPresent());
    }
}