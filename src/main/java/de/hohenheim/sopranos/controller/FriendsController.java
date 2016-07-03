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
    	request.getSession().removeAttribute("search");
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
        		SopraUser su = sopraUserRepository.findByEmail(email);
        		su.getFriendsList().remove(loginUser);
        		sopraUserRepository.save(su);
        		sopraUserRepository.save(loginUser);
        		attr.addAttribute("remove", "successful");
        		return "redirect:/friends";
        	case "deny":
        		loginUser.getFriendRequestsList().remove(sopraUserRepository.findByEmail(email));
        		sopraUserRepository.save(loginUser);
        		attr.addAttribute("deny", "successful");
        		return "redirect:/friends";
        	 
        	case "add":
        		SopraUser f = sopraUserRepository.findByEmail(email);
        		f.getFriendRequestsList().add(loginUser);
        		sopraUserRepository.save(f);
        		attr.addAttribute("add", "successful");
        		return "redirect:/friends";
        	case "confirm":
        		loginUser.getFriendRequestsList().remove(sopraUserRepository.findByEmail(email));
        		loginUser.getFriendsList().add(sopraUserRepository.findByEmail(email));
//        		SopraUser a = sopraUserRepository.findByEmail(email);
//        		a.getFriendRequestsList().remove(loginUser);
//        		sopraUserRepository.save(a);
        		sopraUserRepository.save(loginUser);
        		attr.addAttribute("confirm", "successful");
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
        search= "%"+search+"%";
        su.addAll(sopraUserRepository.findAllIgnoreCaseByEmailLike(search));
		su.addAll(sopraUserRepository.findAllIgnoreCaseByNameLike(search));
		su.addAll(sopraUserRepository.findAllIgnoreCaseByUsernameLike(search));
        su.removeAll(loginUser.getFriendsListALL());
        su.remove(loginUser);
        su.removeAll(loginUser.getFriendRequestsListALL());
        request.getSession().setAttribute("search", su);
    	model.addAttribute("current", loginUser);
    	return "redirect:/friends";
    }

}

