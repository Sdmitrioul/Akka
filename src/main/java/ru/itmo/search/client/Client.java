package ru.itmo.search.client;

import ru.itmo.search.SearchRequest;

import java.util.concurrent.CompletableFuture;

public interface Client {
    CompletableFuture<String> sendRequest(SearchRequest request);
}
