package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.Message;
import edu.ouhk.comps380f.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("message")
public class MessageController {

    private volatile long TICKET_ID_SEQUENCE = 1;
    private Map<Long, Message> ticketDatabase = new LinkedHashMap<>();
    // private String category[]=new String[]{"lecture", "lab", "other"};
    private Message msg = new Message();

    public static class Form {

        private String subject;
        private String body;
        private String category;
        private List<MultipartFile> attachments;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }

    @RequestMapping(value = {"{category}/list"}, method = RequestMethod.GET)
    public String list(@PathVariable("category") String category) {

        ModelAndView modelAndView = new ModelAndView("view");
        //  modelAndView.addObject("category", category);
        return "list";
    }

    /*public String list(ModelMap model) {
        model.addAttribute("ticketDatabase", ticketDatabase);
        return "list";
    } */

    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    public ModelAndView index() {
        //System.out.println("1111111111" + form.getCategory());
        return new ModelAndView("index", "ticketForm", new Form());
    }
   
    @RequestMapping(value = {"", "index"}, method = RequestMethod.POST)
    public Object index(Form form) throws IOException {
        //Message ticket = new Message();
        msg.setCategory(form.getCategory());
        System.out.println("1111111111" + form.getCategory());
        return msg;
    }

    @RequestMapping(value = "{category}/view/{messageId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("messageId") long messageId, @PathVariable("category") String category) {
        Message ticket = this.ticketDatabase.get(messageId);
        if (ticket == null) {
            return new ModelAndView(new RedirectView("/message/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("messageId", Long.toString(messageId));
        modelAndView.addObject("ticket", ticket);
        modelAndView.addObject("msg", msg);
        return modelAndView;
    }

    @RequestMapping(value = "{category}/create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("add", "ticketForm", new Form());
    }

    @RequestMapping(value = "{category}/create", method = RequestMethod.POST)
    public View create(Form form, Principal principal) throws IOException {
        Message ticket = new Message();
        ticket.setId(this.getNextMessageId());
        ticket.setCustomerName(principal.getName());
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                ticket.addAttachment(attachment);
            }
        }
        this.ticketDatabase.put(ticket.getId(), ticket);
        return new RedirectView("/message/{category}/view/" + ticket.getId(), true);
    }

    private synchronized long getNextMessageId() {
        return this.TICKET_ID_SEQUENCE++;
    }

    @RequestMapping(
            value = "/{messageId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("messageId") long messageId,
            @PathVariable("attachment") String name) {
        Message ticket = this.ticketDatabase.get(messageId);
        if (ticket != null) {
            Attachment attachment = ticket.getAttachment(name);
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/message/list", true);
    }

    @RequestMapping(
            value = "/{messageId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View deleteAttachment(@PathVariable("messageId") long messageId,
            @PathVariable("attachment") String name) {
        Message ticket = this.ticketDatabase.get(messageId);
        if (ticket != null) {
            if (ticket.hasAttachment(name)) {
                ticket.deleteAttachment(name);
            }
        }
        return new RedirectView("/message/edit/" + messageId, true);
    }

    @RequestMapping(value = "edit/{messageId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("messageId") long messageId,
            Principal principal, HttpServletRequest request) {
        Message ticket = this.ticketDatabase.get(messageId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/message/list", true));
        }

        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("messageId", Long.toString(messageId));
        modelAndView.addObject("ticket", ticket);

        Form ticketForm = new Form();
        ticketForm.setSubject(ticket.getSubject());
        ticketForm.setBody(ticket.getBody());
        modelAndView.addObject("ticketForm", ticketForm);

        return modelAndView;
    }

    @RequestMapping(value = "edit/{messageId}", method = RequestMethod.POST)
    public View edit(@PathVariable("messageId") long messageId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Message ticket = this.ticketDatabase.get(messageId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new RedirectView("/message/list", true);
        }
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                ticket.addAttachment(attachment);
            }
        }
        this.ticketDatabase.put(ticket.getId(), ticket);
        return new RedirectView("/message/view/" + ticket.getId(), true);
    }

    @RequestMapping(value = "delete/{messageId}", method = RequestMethod.GET)
    public View deleteTicket(@PathVariable("messageId") long messageId) {
        if (this.ticketDatabase.containsKey(messageId)) {
            this.ticketDatabase.remove(messageId);
        }
        return new RedirectView("/message/list", true);
    }

}
