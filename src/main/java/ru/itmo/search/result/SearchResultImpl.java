package ru.itmo.search.result;

import ru.itmo.search.SearchProvider;
import ru.itmo.search.parser.ResultEntity;

import java.util.List;

public record SearchResultImpl(List<ResultEntity> result, SearchProvider provider) implements SearchResult {
}
