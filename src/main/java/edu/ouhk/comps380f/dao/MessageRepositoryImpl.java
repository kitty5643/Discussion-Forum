package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    DataSource dataSource;

    private static final String SQL_INSERT_ENTRY
            = "insert into guestbook (name, message, date) values (?, ?, ?)";

    @Override
    public void create(Message entry) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_ENTRY);
            stmt.setString(1, entry.getName());
            stmt.setString(2, entry.getMessage());
            stmt.setTimestamp(3, new Timestamp(entry.getDate().getTime()));
            stmt.execute();
        } catch (SQLException e) {
            // do something ... not sure what, though
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Even less sure about what to do here
            }
        }
    }

private static final String SQL_SELECT_ALL_ENTRY
        = "select id, name, message, date from guestbook";

@Override
public List<Message> findAll() {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
        conn = dataSource.getConnection();
        stmt = conn.prepareStatement(SQL_SELECT_ALL_ENTRY);
        rs = stmt.executeQuery();
        List<Message> entries = new ArrayList<>();
        while (rs.next()) {
            Message entry = new Message();
            entry.setId(rs.getInt("id"));
            entry.setName(rs.getString("name"));
            entry.setMessage(rs.getString("message"));
            entry.setDate(toDate(rs.getTimestamp("date")));
            entries.add(entry);
        }
        return entries;
    } catch (SQLException e) {
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }
    return null;
}

private static final String SQL_SELECT_ENTRY
        = "select id, name, message, date from guestbook where id = ?";

@Override
public Message findById(int id) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
        conn = dataSource.getConnection();
        stmt = conn.prepareStatement(SQL_SELECT_ENTRY);
        stmt.setInt(1, id);
        rs = stmt.executeQuery();
        Message entry = null;
        if (rs.next()) {
            entry = new Message();
            entry.setId(rs.getInt("id"));
            entry.setName(rs.getString("name"));
            entry.setMessage(rs.getString("message"));
            entry.setDate(toDate(rs.getTimestamp("date")));
        }
        return entry;
    } catch (SQLException e) {
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }
    return null;
}

private static final String SQL_UPDATE_ENTRY
        = "update guestbook set name = ?, message = ?, date = ? where id = ?";

@Override
public void update(Message entry) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
        conn = dataSource.getConnection();
        stmt = conn.prepareStatement(SQL_UPDATE_ENTRY);
        stmt.setString(1, entry.getName());
        stmt.setString(2, entry.getMessage());
        stmt.setTimestamp(3, new Timestamp(entry.getDate().getTime()));
        stmt.setInt(4, entry.getId());
        stmt.execute();
    } catch (SQLException e) {
    } finally {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }
}

    public static Date toDate(Timestamp timestamp) {
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
        return new java.util.Date(milliseconds);
    }

}
