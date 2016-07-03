package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.Message;
import de.hohenheim.sopranos.model.MessageRepository;
import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MessageController {


    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    MessageRepository messageRepository;


    @RequestMapping(value = "/message/postbox", method = RequestMethod.GET)
    public String postbox(Model model) {


        return "/message/postbox";
    }

    @RequestMapping(value = "/message/new", method = RequestMethod.GET)
    public String newMessageGet(Model model, @ModelAttribute("receivers") String receivers, @ModelAttribute("message") String message) {


        model.addAttribute("receivers", receivers);
        model.addAttribute("message", message);
        return "/message/new";
    }

    @RequestMapping(value = "message/new", method = RequestMethod.POST)
    public String newMessagePost(Model model, String receivers, String message) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        List<String> recz = Message.splitString(receivers);
        for (String a : recz) {
            Message msg = new Message();
            msg.setMessage(message);
            msg.setReceiver(sopraUserRepository.findByEmail(a));
            msg.setSender(loginUser);
            msg.setCreateDate();
            messageRepository.save(msg);
        }


        return "redirect:/home";
    }
}
