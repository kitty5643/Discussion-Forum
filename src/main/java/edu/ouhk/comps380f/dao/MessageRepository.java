package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Message;
import java.util.List;

public interface MessageRepository {
    public void create(Message entry);
    public List<Message> findAll();
    public Message findById(int id);
    public void update(Message entry);
}
