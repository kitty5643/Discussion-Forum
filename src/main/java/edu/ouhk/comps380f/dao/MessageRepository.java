/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Message;
import java.util.List;

/**
 *
 * @author Kitty
 */
public interface MessageRepository {
    public void create(Message entry);
    public List<Message> findAll();
    public Message findById(int id);
    public void update(Message entry);
}
