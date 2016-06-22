package de.hohenheim.sopranos.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;

@Controller
public class QuestionContoller {

    @Autowired
    SopraUserRepository sopraUserRepository;
        
    @RequestMapping(value = "/question/create", method = RequestMethod.GET)
    public String create(Model model) {
    	model.addAttribute("question", new String());
        model.addAttribute("answers", new String[4]);
        return "/question/create";
    }
    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public String createFinish(String[] answers,String question, Model model, RedirectAttributes attr) {
//    	if(lg.getName()==null || lg.getName().isEmpty()){
//    		attr.addAttribute("error", "invalid");
//    		return "redirect:/question/create";
//    	}
//    	if(lg.getPassword()!=null && lg.getPassword().equals(question)==false){
//    		attr.addAttribute("error", "nomatch");
//    		return "redirect:/question/create";
//    	}
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        
        return "redirect:/question/answer";
    }
    @RequestMapping(value = "/question/answer", method = RequestMethod.GET)
    public String answer(Model model) {
    	model.addAttribute("question", "Was is das richtige?");
    	String[] strs =  {"a","b","c","d"};
        model.addAttribute("answerstext",strs);

        return "/question/answer";
    }
    @RequestMapping(value = "/question/answer", method = RequestMethod.POST)
    public String answerPOST(Model model, String answers0, String answers1, String answers2, String answers3, String direction) {
    	System.out.println( answers0+" " + direction);

        return "/question/answer";
    }
}
