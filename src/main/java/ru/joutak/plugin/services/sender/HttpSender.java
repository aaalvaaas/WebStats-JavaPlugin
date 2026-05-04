package ru.joutak.plugin.services.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.limiter.RateLimiterService;
import ru.joutak.plugin.services.logging.PluginLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpSender {
    private final HttpClient client = HttpClient.newHttpClient();
    private final URI uri;
    private final ObjectMapper mapper = new ObjectMapper();
    private final RateLimiterService rateLimiter;
    private final PluginLogger logger;

    public HttpSender(String endpoint, RateLimiterService rateLimiter, PluginLogger logger) {
        this.uri = URI.create(endpoint);
        this.rateLimiter = rateLimiter;
        this.logger = logger;
    }

    public CompletableFuture<Boolean> send(List<KillEvent> events) {
        if (!rateLimiter.allow()) {
            logger.debug("Rate limit exceeded");
            return CompletableFuture.completedFuture(false);
        }

        try {
            String json = mapper.writeValueAsString(events);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        int code = response.statusCode();

                        if (code >= 200 && code < 300) return true;

                        logger.warn("HTTP error code: " + code);
                        return false;
                    })
                    .exceptionally(ex -> {
                        logger.warn("HTTP send failed: " + ex.getMessage());
                        return false;
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
}
