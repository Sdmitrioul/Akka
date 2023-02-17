package ru.itmo.search.parser;

import java.util.List;

public interface ResponseParser {
    List<ResultEntity> parse(String response);
}
