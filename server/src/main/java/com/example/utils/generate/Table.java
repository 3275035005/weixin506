package com.example.utils.generate;

import java.util.List;

/**
 * 代码生成-数据库表
 */
public class Table {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表注释
     */
    private String tableComment;
    /**
     * 表字段
     */
    private List<TableColumn> tableColumns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<TableColumn> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<TableColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }
}
