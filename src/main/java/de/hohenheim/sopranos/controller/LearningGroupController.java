package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MortmannMKII v2 on 08.06.2016.
 */
@Controller
public class LearningGroupController {

    @Autowired
    LearningGroupRepository learningGroupRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SopraUserRepository sopraUserRepository;
    @Autowired
    PostRepository postRepository;
    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.GET)
    public String create(Model model) {

        model.addAttribute("group", new LearningGroup());
        return "learninggroup/create";
    }

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.POST)
    public String createFinish(LearningGroup lg,String confirm, Model model, RedirectAttributes attr) {
        
    	if(lg.getName()==null || lg.getName().isEmpty()){
    		attr.addAttribute("error", "invalid");
    		return "redirect:/learninggroup/create";
    	}
    	if(lg.getPassword()!=null && lg.getPassword().equals(confirm)==false){
    		attr.addAttribute("error", "nomatch");
    		return "redirect:/learninggroup/create";
    	}
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        lg.setSopraHost(host);
        lg.setCreateDate(); 
        learningGroupRepository.save(lg);
        return "redirect:/learninggroup/home?name=" + lg.getName() +"&created";
    }
    @RequestMapping(value = "/learninggroup/ranklist{name}", method = RequestMethod.GET)
    public String ranklist(@RequestParam("name") String name,Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	ArrayList<SopraUser> us = new ArrayList<>();
    	us.addAll(lg.getSopraUsers());
    	us.sort((x,y)->x.getRankpoints().compareTo(y.getRankpoints()));
    	
    	model.addAttribute("users", us);
    	attr.addAttribute("name", name);
    	model.addAttribute("name",name);
        return "learninggroup/ranklist";
    }
     
    @RequestMapping(value = "/learninggroup/mygroups", method = RequestMethod.GET)
    public String myGroups(Model model) { 
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("groups", host.getLearningGroups());
        return "/learninggroup/mygroups";
    }

    @RequestMapping(value = "/learninggroup/join")
    public String join(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());

        List<LearningGroup> lgs = learningGroupRepository.findAll();

        lgs.removeIf(x -> x.getSopraUsers().contains(current));
        lgs.removeIf(y -> y.getBlackList().contains(current));
        model.addAttribute("groups", lgs);
        return "/learninggroup/join";
    }

    @RequestMapping(value = "/learninggroup/home{name}", method = RequestMethod.GET)
    public String home(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        if(lg.getSopraUsers().contains(loginUser) == false){
            if (lg.getBlackList().contains(loginUser)){
                return "redirect:/index";
            }
	        if (lg.getFreeForAll() == false) {
	            return "redirect:/learninggroup/login?name=" + name;
	        }

        	attr.addAttribute("join", "successful");
        	lg.getSopraUsers().add(loginUser);
        	learningGroupRepository.save(lg);
        }
        Post p =new Post();
        p.setText("BLINDTEXT _____-_");
        p.setHeading("POOOOOOOOOOOOOST");
        p.setSopraUser(loginUser);
        p.setLearningGroup(lg);
        p = postRepository.save(p);
//        lg.getPostList().add(p);
        
        attr.addAttribute("name", name);
    	model.addAttribute("name",name);
    	model.addAttribute("loginUser",loginUser);
    	model.addAttribute("isHost",lg.isHost(loginUser));
    	model.addAttribute("posts" , lg.getPostList());
        return "/learninggroup/home";
    }
    @RequestMapping(value = "/learninggroup/home{name}", method = RequestMethod.POST)
    public String postEDIT(@RequestParam("name")
                                         String name, String info, Model model, RedirectAttributes attr) {
    	String[] is = info.split("-");
    	Post p = postRepository.getOne(Integer.valueOf(is[0]));

    	switch(is[1]){
    		case "delete":
    	    	//delete
        		postRepository.delete(p);
        		attr.addAttribute("delete", "successful");
        		attr.addAttribute("name" , name);
        		return "redirect:/learninggroup/home";
    		case "edit":
    	    	//change 
    	    	attr.addFlashAttribute("post",p);
    			attr.addAttribute("name", name);
    			attr.addFlashAttribute("edit", true); 
    	        return "redirect:/learninggroup/post";
    		case "comment":
    			//change 
    	    	attr.addFlashAttribute("postid",p.getId());
    			attr.addAttribute("name", name);
    			attr.addFlashAttribute("edit", false); 
    			return "redirect:/learninggroup/comment";
    		case "rating":
    			//change 
    			System.out.println(info);
    			p.addRating(Float.valueOf(is[2]));
    			attr.addAttribute("rating", "successful");
    			attr.addAttribute("name", name);
    			return "redirect:/learninggroup/home";
    	}
    	return "redirect:/error";
    }
    @RequestMapping(value = "/learninggroup/login{name}", method = RequestMethod.GET, params = {"name"})
    public String loginGroup(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        model.addAttribute("name", name);
        attr.addAttribute("name", name);
        return "/learninggroup/login";
    }

    @RequestMapping(value = "/learninggroup/login{name}", method = RequestMethod.POST)
    public String loginGroupPOSTmapper(@RequestParam("name") String name, String password, Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        if (lg.getPassword().equals(password) == false) {
        	attr.addAttribute("name" , name);
            return "redirect:/learninggroup/login?error";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        attr.addAttribute("join", "successful");
    	lg.getSopraUsers().add(loginUser);
    	learningGroupRepository.save(lg);
        attr.addAttribute("name", name);
        return "redirect:/learninggroup/home";
    }

    @RequestMapping(value = "/learninggroup/option{name}", method = RequestMethod.GET)
    public String lgOptionPre(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	model.addAttribute("group", lg);
    	model.addAttribute("name",name);
        attr.addAttribute("name", name);    	
        return "/learninggroup/option";
    }
    @RequestMapping(value = "/learninggroup/option{name}", method = RequestMethod.POST)
    public String lgOptionPost(@RequestParam("name") String name, LearningGroup group,Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	lg.setDescription(group.getDescription());
    	lg.setPassword(group.getPassword());
        learningGroupRepository.save(lg);
        attr.addAttribute("edited", "successful");
        return "redirect:/learninggroup/home?name="+name;
    }

    @RequestMapping(value = "/learninggroup/users{name}", method = RequestMethod.GET)
    public String lgUser(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	model.addAttribute("users", lg.getSopraUsers());
    	model.addAttribute("bannedusers", lg.getBlackList());
    	model.addAttribute("blockedusers", lg.getGrayList());
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("isHost", lg.isHost(loginUser));
        if(lg.isHost(loginUser)){
        	model.addAttribute("host", loginUser);
        }
    	model.addAttribute("name", name); 
    	attr.addAttribute("name", name);
        return "/learninggroup/users";
    }

    @RequestMapping(value = "/learninggroup/users{name}", method = RequestMethod.POST)
    public String lgUserDelete(@RequestParam("name") String name, String info, Model model, RedirectAttributes attr) {
    	String email = info.split("-")[0];
    	String extra = info.split("-")[1];
    	model.addAttribute("name", name);
    	attr.addAttribute("name", name);
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	if(lg.isHost(sopraUserRepository.findByEmail(email))){
    		return "redirect:/learninggroup/home?deleted=error";
    	}
		switch (extra) {
		case "ban":
			lg.banSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
	        return "redirect:/learninggroup/home?deleted=successful";
		case "block":
			lg.lockSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
    		return "redirect:/learninggroup/home?deleted=successful";
		case "unban":
			lg.unbanSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
	        return "redirect:/learninggroup/home?deleted=successful";
		case "unblock":
			lg.unlockSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
    		return "redirect:/learninggroup/home?deleted=successful";
		}
		return "redirect:/error";
    }
    @RequestMapping(value = "/learninggroup/questionlist{name}", method = RequestMethod.GET)
    public String allquestion(@RequestParam("name")
                                         String name, String info, Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
    	String st = "Was is das richtige?";
    	Question testmc = new Question();
    	testmc.setQuestText(st);
    	String[] strs =  {"a","b","c","d"};
    	testmc.setAnswers(strs);
    	boolean[] b = {true,false,false,false};
    	testmc.setSopraUser(loginUser);
    	testmc.setSolutions(b);
    	testmc.setLearningGroup(lg);
    	testmc = questionRepository.save(testmc);
    	lg.getQuestList().add(testmc);
    	
    	model.addAttribute("quizquestions", lg.getQuestList());
    	model.addAttribute("nonquizquestions", lg.getNotReleasedQuestionList());

    	model.addAttribute("name", name);
    	model.addAttribute("isHost", lg.isHost(loginUser));
    	model.addAttribute("loginUser",loginUser);
    	attr.addAttribute("name", name);
    	return "/learninggroup/questionlist";
    }
    @RequestMapping(value = "/learninggroup/questionlist{name}", method = RequestMethod.POST)
    public String allquestionPOST(@RequestParam("name")
                                         String name, String info, Model model, RedirectAttributes attr) {
    	String[] is = null;
		is=info.split("-");    	
    	Question p = questionRepository.getOne(Integer.valueOf(is[0]));
    	LearningGroup lg = learningGroupRepository.findByName(name);

    	switch(is[1]){
    		case "delete":
    	    	//delete
    			questionRepository.delete(p);
        		attr.addAttribute("delete", "successful");
        		attr.addAttribute("name" , name);
        		return "redirect:/learninggroup/home";
    		case "edit":
    	    	//change 
    	    	attr.addFlashAttribute("quest",p);
    			attr.addAttribute("name", name);
    			attr.addFlashAttribute("edit", true); 
    	        return "redirect:/question/create";
    		case "comment":
    			//change 
    			System.out.println(p.getQuestText());
    	    	attr.addFlashAttribute("quest",p);
    			attr.addAttribute("name", name);
    			attr.addFlashAttribute("edit", false); 
    			return "redirect:/question/comment";
    		case "rating":
    			//change 
    			System.out.println(info);
    			p.addRating(Float.valueOf(is[2]));
    			//up for discussion -- how good rated/how many it have to rate it so it 
    			//can be up for quiz
    			if(p.getRating()>2.5f && p.getRatingCount()>=Math.min(lg.getUserCount(), 5)){
    				lg.getNotReleasedQuestionList().remove(p);
        			lg.getQuestList().add(p);
    			}
    			attr.addAttribute("rating", "successful");
    			attr.addAttribute("name", name);
    			return "redirect:/learninggroup/questionlist";	
    		case "addquiz":
    			lg.getNotReleasedQuestionList().remove(p);
    			lg.getQuestList().add(p);
    			attr.addAttribute("adding", "successful");
    			attr.addAttribute("name", name);
    			return "redirect:/learninggroup/questionlist";	
    	}
    	return "redirect:/error";
    }
    
    
}
 