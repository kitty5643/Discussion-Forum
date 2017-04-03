package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.dao.MessageUserRepository;
import edu.ouhk.comps380f.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("message")
public class MessageController  {
    @Autowired
    MessageUserRepository gbEntryRepo; 
    
    @RequestMapping(value={"", "view"})
    public String index(ModelMap model) {
        model.addAttribute("entries", gbEntryRepo.findAll());
        return "Message";
    }
    
    @RequestMapping(value="add", method=RequestMethod.GET)
    public ModelAndView addCommentForm() {
        return new ModelAndView("AddComment", "command", new Message());
    }
    
    @RequestMapping(value="add", method=RequestMethod.POST)
    public View addCommentHandle(Message entry) {
        entry.setDate(new Date());
        gbEntryRepo.create(entry);
        return new RedirectView("/message/view", true);
    }
    
    @RequestMapping(value="edit", method=RequestMethod.GET)
    public String editCommentForm(@RequestParam("id") Integer entryId, ModelMap model) {
        model.addAttribute("entry", gbEntryRepo.findById(entryId));
        return "EditComment";
    }
    
    @RequestMapping(value="edit", method=RequestMethod.POST)
    public View editCommentHandle(Message entry) {
        entry.setDate(new Date());
        gbEntryRepo.update(entry);
        return new RedirectView("/message/view", true);
    }
    
}
