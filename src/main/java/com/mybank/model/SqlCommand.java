package com.mybank.model;

public class SqlCommand {
    public final String stmt;
    public final Object[] values;

    public SqlCommand(String statement) {
        this.stmt = statement;
        this.values = new Object[]{};
    }

    public SqlCommand(String statement, Object[] values) {
        this.stmt = statement;
        this.values = values;
    }
}
