package edu.ben.rate_review.daos;

import com.zaxxer.hikari.HikariDataSource;

public class BaseDao {
    protected HikariDataSource db;

    public BaseDao() {
    }

    public BaseDao(HikariDataSource db) {
        // Configure MySQL data source
        if (db != null) {
            this.db = db;
        }
    }
}