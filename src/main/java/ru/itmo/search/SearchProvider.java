package ru.itmo.search;

import ru.itmo.search.client.Client;
import ru.itmo.search.client.StubClient;
import ru.itmo.search.exceptions.ClientException;
import ru.itmo.search.parser.BaseJSONParser;
import ru.itmo.search.parser.ResponseParser;
import ru.itmo.search.result.SearchResult;
import ru.itmo.search.result.SearchResultImpl;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

public enum SearchProvider {
    GOOGLE(new StubClient(1200), new BaseJSONParser()),
    YANDEX(new StubClient(7200), new BaseJSONParser()),
    BING(new StubClient(1500), new BaseJSONParser());
    
    private final Client client;
    private final ResponseParser parser;
    
    SearchProvider(final Client client, final ResponseParser parser) {
        this.client = client;
        this.parser = parser;
    }
    
    public SearchResult ask(SearchRequest request) throws ClientException {
        try {
            return client.sendRequest(request)
                    .handle((result, e) -> new SearchResultImpl(
                            e != null ? Collections.emptyList() : parser.parse(result), this))
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            throw new ClientException(e.getMessage());
        }
    }
}
