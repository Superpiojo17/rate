package edu.ben.rate_review.daos;

import com.mysql.cj.jdbc.MysqlDataSource;

public class BaseDao {
    protected MysqlDataSource db;

    public BaseDao() {
    }

    public BaseDao(MysqlDataSource db) {
        // Configure MySQL data source
        if (db != null) {
            this.db = db;
        }
    }
}