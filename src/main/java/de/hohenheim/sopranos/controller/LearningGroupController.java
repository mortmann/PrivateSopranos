package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by MortmannMKII v2 on 08.06.2016.
 */
@Controller
public class LearningGroupController {

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    SopraUserRepository sopraUserRepository;

    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.GET)
    public String post(Model model) {
        model.addAttribute("posttest", new Post());
        return "learninggroup/post";
    }

    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.POST)
    public String registerSubmit(Post post, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = user.getUsername();
        String s = post.getText();
        Post p = new Post();
        
        p.setText(s.toString() + " by " + mail);
        model.addAttribute("post", p);
        return "learninggroup/group";
    }

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.GET)
    public String create(Model model) {

        model.addAttribute("group", new LearningGroup());
        return "learninggroup/create";
    }

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.POST)
    public String createFinish(LearningGroup lg, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        lg.setSopraHost(host);
        learningGroupRepository.save(lg);
        return "redirect:/index";
    }


    @RequestMapping(value = "/learninggroup/join", method = RequestMethod.GET)
    public String join(Model model) {
<<<<<<< HEAD
        model.addAttribute("groups", learningGroupRepository.findAll());

        return "learninggroup/join";
    }

    @RequestMapping(value = "/learninggroup/join", method = RequestMethod.POST)
    public String afterJoin(LearningGroup lg, Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser newUser = sopraUserRepository.findByEmail(user.getUsername());
        lg.sopraUsers.add(newUser);
        newUser.learningGroups.add(lg);

        //eventuell raus
        learningGroupRepository.save(lg);
        sopraUserRepository.save(newUser);
        return "redirect:index";
    }

    @RequestMapping("/learninggroup/group")
    public String joinPost(@RequestParam("name") String name) {
        System.out.println(name + " yeah ");
        learningGroupRepository.findOne(0);
        return "learninggroup/group";
=======
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup g =  learningGroupRepository.findAll().get(0);
//        loginUser.learningGroups.add(g);
        g.sopraUsers.add(loginUser);
        learningGroupRepository.save(g);
        List<LearningGroup> lgs = learningGroupRepository.findAll();
        // remove the groups when the user that is logged already is in it
        // because he doesnt need to join again
        lgs.removeIf(x -> x.sopraUsers.contains(loginUser));
        model.addAttribute("groups",lgs);
        return "/learninggroup/join"; 
    } 
    @RequestMapping("/learninggroup/group")
    public String joinPost(@RequestParam("name") String name,Model model) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        if(lg.getFreeForAll() == false){
        	return "redirect:/learninggroup/login?name="+name;
        }
        return "/learninggroup/group";
>>>>>>> refs/remotes/origin/mortmann
    }
    @RequestMapping(value ="/learninggroup/login{name}", method = RequestMethod.GET,params={"name"})
    public String loginGroup(@RequestParam("name") String name,Model model, RedirectAttributes attr) {
        model.addAttribute("name", name);
    	attr.addAttribute("name", name);
    	return "/learninggroup/login";
    }
    @RequestMapping(value ="/learninggroup/login{name}", method = RequestMethod.POST)
    public String loginGroupPOSTmapper(@RequestParam("name") String name,String password,Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        if(lg.getPassword().equals(password) == false){
        	return "redirect:/learninggroup/join?error";
        }
        attr.addAttribute("name",name);
        return "redirect:/learninggroup/home";
    }
    @RequestMapping(value ="/learninggroup/home{name}")
    public String lgHome(@RequestParam("name") String name,String password,Model model, RedirectAttributes attr) {
        
        return "/learninggroup/home";
    }    
    
}
 