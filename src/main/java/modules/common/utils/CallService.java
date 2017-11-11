package modules.common.utils;

import java.util.concurrent.CompletableFuture;

public interface CallService {
  String getContent(String target);

  CompletableFuture<String> getContentAsync(String target);
}
