package tarea.server.model;

import java.io.Serializable;

public class SearchCriteria implements Serializable {
    private String key;
    private String operation;
    private String value;

    public SearchCriteria(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public String getValue() {
        return value;
    }
}
