package com.tohant.om2d.storage.database.sqlite;

public class SQLiteDialectUtil {

    private static final String SELECT = "SELECT";
    private static final String ALL = "*";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private static final String EQUAL = "=";
    private static final String IN = "IN";
    private static final String GROUP_BY = "GROUP BY";

    private static final String CREATE = "CREATE";
    private static final String TABLE = "TABLE";
    private static final String IF_NOT_EXIST = "IF NOT EXIST";

    private static final String DELETE = "DELETE";
    private static final String INSERT_INTO = "INSERT INTO";
    private static final String VALUES = "VALUES";

//    private static final String ROOM_TABLE = "ROOM";
//    private static final String EMPLOYEE_TABLE = "EMPLOYEE";
//    private static final String GAME_PROGRESS_TABLE = "GAME_PROGRESS";

    private SQLiteDialectUtil() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final StringBuffer RESULT = new StringBuffer();

        private Builder() {}

        public Builder select() {
            RESULT.append(SELECT);
            return this;
        }

        public Builder all() {
            RESULT.append(ALL);
            return this;
        }

        public Builder fromTable(String tableName) {
            RESULT.append(FROM);
            RESULT.append(" ");
            RESULT.append(tableName);
            return this;
        }

        public Builder where() {
            RESULT.append(WHERE);
            return this;
        }

        public Builder groupBy() {
            RESULT.append(GROUP_BY);
            return this;
        }

        public Builder equal() {
            RESULT.append(EQUAL);
            return this;
        }

        public Builder in() {
            RESULT.append(IN);
            return this;
        }

        public Builder create() {
            RESULT.append(CREATE);
            return this;
        }

        public Builder table() {
            RESULT.append(TABLE);
            return this;
        }

        public Builder ifNotExist() {
            RESULT.append(IF_NOT_EXIST);
            return this;
        }

        public Builder insertIntoValues(String tableName) {
            RESULT.append(INSERT_INTO);
            RESULT.append(" ");
            RESULT.append(tableName);
            RESULT.append(VALUES);
            return this;
        }

        public Builder delete() {
            RESULT.append(DELETE);
            return this;
        }

        public String build() {
            return RESULT.toString();
        }

    }

}
