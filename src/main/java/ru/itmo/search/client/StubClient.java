package ru.itmo.search.client;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.search.SearchRequest;
import ru.itmo.search.parser.ResultEntity;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StubClient implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(StubClient.class);
    private final int timeout;
    private final Faker faker = new Faker();
    
    public StubClient(final int timeout) {
        this.timeout = timeout;
    }
    
    @Override
    public CompletableFuture<String> sendRequest(final SearchRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                LOG.warn("Error while sleeping in stub client: ", e);
            }
            
            var result = new ArrayList<ResultEntity>();
            
            for (int i = 0; i < (int) (Math.random() * 20) + 2; i++) {
                result.add(new ResultEntity(faker.starTrek()
                        .character(), faker.internet()
                        .url()));
            }
    
            return result.stream()
                    .map(ResultEntity::toString)
                    .collect(Collectors.joining(", ", "{ result : [", "]}"));
        });
    }
}
