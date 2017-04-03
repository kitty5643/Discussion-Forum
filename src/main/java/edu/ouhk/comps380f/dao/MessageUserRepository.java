package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.MessageUser;
import java.util.List;

public interface MessageUserRepository {
    public void create(MessageUser user);
    public List<MessageUser> findAll();
    public MessageUser findByUsername(String username);
    public void deleteByUsername(String username);
}
