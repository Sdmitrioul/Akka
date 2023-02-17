package ru.itmo.search.result;

import ru.itmo.search.SearchProvider;
import ru.itmo.search.parser.ResultEntity;

import java.util.List;

public interface SearchResult {
    
    List<ResultEntity> result();
    
    SearchProvider provider();
}
