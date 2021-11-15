package jessie.cs175.hw4_translator;

import java.util.List;

/**
 * Copyright 2021 bejson.com
 */

public class YoudaoBean {

    private Result result;
    private Data data;
    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

}

class Result {

    private String msg;
    private int code;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}

class Data {

    private List<Entries> entries;
    private String query;
    private String language;
    private String type;
    public void setEntries(List<Entries> entries) {
        this.entries = entries;
    }
    public List<Entries> getEntries() {
        return entries;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    public String getQuery() {
        return query;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLanguage() {
        return language;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

}


class Entries {

    private String explain;
    private String entry;
    public void setExplain(String explain) {
        this.explain = explain;
    }
    public String getExplain() {
        return explain;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
    public String getEntry() {
        return entry;
    }

}