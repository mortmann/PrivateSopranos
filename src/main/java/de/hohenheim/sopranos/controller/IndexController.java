package de.hohenheim.sopranos.controller;


import de.hohenheim.sopranos.model.DateClass;
import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.QuizDuel;
import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController {


    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/index")
    public String index(String username, Model model) {
        model.addAttribute("loggedin",SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User );
        return "index";
    }

    @RequestMapping("/signuppage")
    public String signUpPage(String username, Model model) {
        model.addAttribute("loggedin",SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User );


        return "signuppage";
    }
    @RequestMapping("/login")
    public String loginPage(String username, Model model) {
        model.addAttribute("loggedin",SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User );


        return "login";
    }
    @RequestMapping("/basistemplate")
    public String basisTemplate(String username, Model model) {

        return "basistemplate";
    }

    @RequestMapping("/home")
    public String home(String username, Model model) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
    	ArrayList<DateClass> all = new ArrayList<>();
    	all.addAll(loginUser.getPostList());
    	all.addAll(loginUser.getCommentList());
    	all.addAll(loginUser.getQuestList());
    	all.addAll(loginUser.getQuizList());
    	all.addAll(loginUser.getUserEventList());
    	
    	Collections.sort(all, 
                (o1, o2) -> o1.getCreateDate().compareTo(o2.getCreateDate()));
    	for (DateClass dateClass : all) {
			System.out.println(dateClass.getPostDateString());
		}
    	model.addAttribute("activities", all.toArray()); 
        return "home"; 
    }
    
    @RequestMapping(value = "/myquizduels", method = RequestMethod.GET)
    public String allquizduel(Model model, RedirectAttributes attr) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        List<QuizDuel> qd = loginUser.getChallengedList();
        qd.addAll(loginUser.getChallengerList());
        
    	model.addAttribute("duels",qd);
		return "/myquizduels";
    }
}

