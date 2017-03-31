/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Message;import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kitty
 */

public class MessageRepositoryImpl implements MessageRepository{
    
    DataSource dataSource;
    private final JdbcOperations jdbcOp;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcOp = new JdbcTemplate(this.dataSource);
    }

private static final String SQL_INSERT_ENTRY
        = "insert into guestbook (name, message, date) values (?, ?, ?)";

@Override
public void create(Message entry) {
    jdbcOp.update(SQL_INSERT_ENTRY,
            entry.getName(),
            entry.getMessage(),
            new Timestamp(entry.getDate().getTime())
    );
}

    private static final String SQL_SELECT_ALL_ENTRY
            = "select id, name, message, date from guestbook";

@Override
public List<Message> findAll() {

    List<Message> entries = new ArrayList<>();
    List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_ENTRY);
    for (Map<String, Object> row : rows) {
        Message entry = new Message();
        entry.setId((int) row.get("id"));
        entry.setName((String) row.get("name"));
        entry.setMessage((String) row.get("message"));
        entry.setDate(toDate((Timestamp) row.get("date")));
        entries.add(entry);
    }
    return entries;
}

    private static final String SQL_SELECT_ENTRY
            = "select id, name, message, date from guestbook where id = ?";

@Override
public Message findById(int id) {
    return jdbcOp.queryForObject(SQL_SELECT_ENTRY, new EntryRowMapper(), id);
}

private static final String SQL_UPDATE_ENTRY
        = "update guestbook set name = ?, message = ?, date = ? where id = ?";

@Override
public void update(Message entry) {
    jdbcOp.update(SQL_UPDATE_ENTRY,
            entry.getName(),
            entry.getMessage(),
            new Timestamp(entry.getDate().getTime()),
            entry.getId());
}

    public static Date toDate(Timestamp timestamp) {
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
        return new java.util.Date(milliseconds);
    }

    private static final class EntryRowMapper implements RowMapper<Message> {

        @Override
        public Message mapRow(ResultSet rs, int i) throws SQLException {
            Message entry = new Message();
            entry.setId(rs.getInt("id"));
            entry.setName(rs.getString("name"));
            entry.setMessage(rs.getString("message"));
            entry.setDate(toDate(rs.getTimestamp("date")));
            return entry;
        }
    }
}


