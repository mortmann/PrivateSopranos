package de.hohenheim.sopranos.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import de.hohenheim.sopranos.model.*;
import org.omg.CORBA.Request;
import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Controller
public class ProfilController {

    @Autowired
    SopraUserRepository sopraUserRepository;

    @RequestMapping(value = "/profil/edit", method = RequestMethod.GET)
    public String editUserGet(Model model, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("user", loginUser);
        return "/profil/edit";
    }

    @RequestMapping(value = "/profil/edit", method = RequestMethod.POST)
    public String editUserPost(Model model, SopraUser su, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        loginUser.setUsername(su.getUsername());
        loginUser.setPassword(su.getPassword());
        loginUser.setLinkToPicture(su.getLinkToPicture());
        loginUser.setName(su.getName());
        loginUser.setCourseOfStudys(su.getCourseOfStudys());
        sopraUserRepository.save(loginUser);
        model.addAttribute("user", loginUser);
        attr.addAttribute("edit", "successful");
        return "/profil/edit";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String removeuser(RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
       //TODO
        loginUser.deleteSopraUser();
        sopraUserRepository.save(loginUser);
        return "redirect:/logout";
    }


    @RequestMapping(value = "/profil/me", method = RequestMethod.GET)
    public String profileME( Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());


        return "redirect:/profil/user?e=" + loginUser.getEmail();
    }

    @RequestMapping(value = "/profil/user{e}", method = RequestMethod.GET)
    public String profileGET(@RequestParam("e") String e, Model model) {
        SopraUser profileUser = sopraUserRepository.findByEmail(e);


        ArrayList<DateClass> all = new ArrayList<>();
        all.addAll(profileUser.getPostList());
        all.addAll(profileUser.getCommentList());
        all.addAll(profileUser.getQuestList());
        all.addAll(profileUser.getQuizList());
        all.addAll(profileUser.getUserEventList());
        Collections.sort(all,
                (o1, o2) -> o1.getCreateDate().compareTo(o2.getCreateDate()));

        List<SopraUser> allFriends = profileUser.getFriendsListALL();
        ArrayList<SopraUser> fewFriends = new ArrayList<>();

        int sayz = profileUser.getFriendsListALL().size();
        while (fewFriends.size() < 5 && fewFriends.size() < sayz) {
            int p = (int) Math.random() * allFriends.size();
            fewFriends.add(allFriends.get(p));
            allFriends.remove(p);

        }

        List<LearningGroup> allGroups = profileUser.getLearningGroups();
        List<LearningGroup> fewGroups = new ArrayList<>();

        int size = profileUser.getLearningGroups().size();

        while (fewGroups.size() < 5 && fewGroups.size() < size) {
            int t = (int) Math.random() * allGroups.size();

            fewGroups.add(allGroups.get(t));
            allGroups.remove(t);

        }

        model.addAttribute("friends", fewFriends);
        model.addAttribute("learningGroups", fewGroups);
        model.addAttribute("activities", all.toArray());
        model.addAttribute("profileUser", profileUser);


        return "/profil/user";
    }


}
 