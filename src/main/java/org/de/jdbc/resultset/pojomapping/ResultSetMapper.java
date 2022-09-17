package org.de.jdbc.resultset.pojomapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {

    public static Product create(ResultSet rs) throws SQLException {
        return new Product(rs.getInt(1), rs.getString(2),
                           rs.getTimestamp(3).toLocalDateTime(), rs.getString(4),
                           rs.getInt(5));
    }
}
