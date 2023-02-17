package ru.itmo.search.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseJSONParser implements ResponseParser {
    @Override
    public List<ResultEntity> parse(final String response) {
        final JSONObject obj = new JSONObject(response);
        
        var result = new ArrayList<ResultEntity>();
        
        JSONArray array = obj.getJSONArray("result");
        for (Object el : array) {
            final JSONObject o = (JSONObject) el;
            result.add(new ResultEntity(o.getString("header"), o.getString("link")));
        }
        
        return result;
    }
}
