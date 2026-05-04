package ru.joutak.plugin.services.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.joutak.plugin.model.KillEvent;

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

    public HttpSender(String endpoint) {
        this.uri = URI.create(endpoint);
    }

    public CompletableFuture<Boolean> send(List<KillEvent> events) {
        try {
            String json = mapper.writeValueAsString(events);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(r -> r.statusCode() >= 200 && r.statusCode() < 300)
                    .exceptionally(ex -> false);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
}
