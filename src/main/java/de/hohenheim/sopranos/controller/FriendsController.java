package de.hohenheim.sopranos.controller;


import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.MatcherConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FriendsController {


    @Autowired 
    SopraUserRepository sopraUserRepository;

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value="/friends" , method = RequestMethod.GET)
    public String friends(Model model, HttpServletRequest request ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
    	model.addAttribute("current", loginUser);
    	model.addAttribute("search",request.getSession().getAttribute("search"));
    	return "/friends";
    }
    @RequestMapping(value="/friends" , method = RequestMethod.POST)
    public String friendsPOST(String info,Model model, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        String email = info.split("-")[0];
        switch(info.split("-")[1]){
        	case "remove":
        		loginUser.getFriendsList().remove(sopraUserRepository.findByEmail(email));
        		sopraUserRepository.save(loginUser);
        		attr.addAttribute("remove", "successful");
        		return "redirect:/friends";
        	case "add":
        		loginUser.getFriendsList().add(sopraUserRepository.findByEmail(email));
        		sopraUserRepository.save(loginUser);
        		attr.addAttribute("add", "successful");
        		return "redirect:/friends";	
        }
        
    	model.addAttribute("current", loginUser);
    	return "redirect:/friends";
    }
    @RequestMapping(value="/friends/search" , method = RequestMethod.POST)
    public String friendsSearchPOST(HttpServletRequest request,String search,Model model, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        ArrayList<SopraUser> su = new ArrayList<>();
        SopraUser s = new SopraUser();
        s.setName(search);
        s.setUsername(search);
        s.setEmail(search);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.ignoreCase().contains());
        su.addAll(sopraUserRepository.findAll(Example.of(s, matcher)));
        matcher = ExampleMatcher.matching().withMatcher("username", match -> match.ignoreCase().contains());
		su.addAll(sopraUserRepository.findAll(Example.of(s, matcher)));
        matcher = ExampleMatcher.matching().withMatcher("email", match -> match.ignoreCase().contains());
		su.addAll(sopraUserRepository.findAll(Example.of(s, matcher)));
        System.out.println(su.size());
        su.removeAll(loginUser.getFriendsList());
        su.remove(loginUser);
        System.out.println(su.size());
        request.getSession().setAttribute("search", su);
    	model.addAttribute("current", loginUser);
    	return "redirect:/friends";
    }

}

