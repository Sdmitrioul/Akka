package ru.itmo.search.parser;

public record ResultEntity(String header, String link) {
    @Override
    public String toString() {
        return '{' + "header: \"" + header + '\"' + ", link: \"" + link + "\"}";
    }
}
