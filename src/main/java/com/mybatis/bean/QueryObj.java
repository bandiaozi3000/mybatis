package com.mybatis.bean;

import java.util.List;
import java.util.Map;

public class QueryObj {

    private String table;

    private List<String> fields;

    private Map<String,String> param;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
}
