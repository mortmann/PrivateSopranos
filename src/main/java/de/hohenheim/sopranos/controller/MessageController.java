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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MessageController {


    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    MessageRepository messageRepository;


    @RequestMapping(value = "/message/inbox", method = RequestMethod.GET)
    public String inboxGet(Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());



            model.addAttribute("msg", loginUser.getReceivedMessageList());

            return "/message/inbox";

    }

    @RequestMapping(value = "/message/inbox", method = RequestMethod.POST)
    public String inboxPost( Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("msg", loginUser.getReceivedMessageList());

        return "redirect:/message/inbox";
    }

    @RequestMapping(value = "/message/sent", method = RequestMethod.GET)
    public String sentGet( Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());



            model.addAttribute("msg", loginUser.getSendMessageList());

            return "/message/sent";

    }

    @RequestMapping(value = "/message/sent", method = RequestMethod.POST)
    public String sentPost( Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        model.addAttribute("msg", loginUser.getSendMessageList());

        return "redirect:/message/sent";
    }

    @RequestMapping(value = "/message/new", method = RequestMethod.GET)
    public String newMessageGet(Model model, @ModelAttribute("receivers") String receivers, @ModelAttribute("message") String message, @ModelAttribute("title") String title) {


        model.addAttribute("receivers", receivers);
        model.addAttribute("message", message);
        model.addAttribute("title", title);
        return "/message/new";
    }

    @RequestMapping(value = "message/new", method = RequestMethod.POST)
    public String newMessagePost(Model model, String receivers, String message, String title) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        List<String> recz = Message.splitString(receivers);
        for (String a : recz) {
            Message msg = new Message();
            msg.setMessage(message);
            msg.setTitle(title);
            msg.setReceiver(sopraUserRepository.findByEmail(a));
            msg.setSender(loginUser);
            msg.setCreateDate();
            messageRepository.save(msg);
        }


        return "redirect:/message/sent";
    }

    @RequestMapping(value = "/message/delete{id}", method = RequestMethod.GET)
    public String deleteMsgGet(@RequestParam("id") Integer id, Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        Message msg = messageRepository.findById(id);

        if(loginUser != msg.getReceiver())
            return "redirect:/message/delete?error=niceTryGuy";

        else
        return "/message/delete";
    }

    @RequestMapping(value = "/message/delete{id}", method = RequestMethod.POST)
    public String deleteMsgPost(@RequestParam("id") Integer id, Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        Message msg = messageRepository.findById(id);

        messageRepository.delete(msg);

            return "redirect:/message/inbox";
    }

    @RequestMapping(value = "/message/reply{id}", method = RequestMethod.GET)
    public String replyGet(@RequestParam("id") Integer id, Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        Message msg = messageRepository.findById(id);

        model.addAttribute("msg", msg);
        attr.addAttribute("id", id);
        if(loginUser != msg.getReceiver())
            return "redirect:/message/reply?error=niceTryGuy";

        else
            return "/message/reply";
    }

    @RequestMapping(value = "/message/reply{id}", method = RequestMethod.POST)
    public String replyPost(@RequestParam("id") Integer id, Model model, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());

        Message msg = messageRepository.findById(id);

        model.addAttribute("msg", msg);
        attr.addAttribute("id", id);

        return "redirect:/message/reply";
    }
}
