package de.hohenheim.sopranos.controller;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hohenheim.sopranos.model.*;


@Controller
public class QuestionContoller {

    @Autowired
    SopraUserRepository sopraUserRepository;
     
    @Autowired
    McQuestionRepository mcQuestionRepository;
      
    @Autowired
    QuizRepository quizRepository;
    
    @RequestMapping(value = "/question/create")
    public String create(Model model, RedirectAttributes attr,@ModelAttribute("question") String question,@ModelAttribute("Answer") Answer answers) {
    	int amount = 4;
    	model.addAttribute("amount",new int[amount]);
    	model.addAttribute("question", question); 
        model.addAttribute("answer", answers);
        return "/question/create";
    }
    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public String createFinish(Answer answers, String question, Model model, RedirectAttributes attr) {
    	boolean hasAnswer=false;
    	for (boolean b : answers.getBooleans()) {
    		if(b==true){
    			hasAnswer = true;
    		}
    	}   
    	if(hasAnswer == false){
    		attr.addAttribute("error", "noanswer");
        	int amount = 4;
        	attr.addFlashAttribute("amount",new int[amount]);
        	attr.addFlashAttribute("question", question);
        	attr.addFlashAttribute("Answer", answers);
    		return "redirect:/question/create";
    	}
    	String[] strs = answers.getStrings();
    	for (int i = 0; i < strs.length; i++) {
    		for (int s = 0; s < strs.length; s++) {
    			if(strs[i].equalsIgnoreCase(strs[s])){
    				attr.addAttribute("error", "sameanswer");
    		    	int amount = 4;
    		    	attr.addFlashAttribute("amount",new int[amount]);
    		    	attr.addFlashAttribute("question", question);
    		    	attr.addFlashAttribute("Answer", answers);
    	    		return "redirect:/question/create";
    			}
    		}
		}	
    	
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        McQuestion mc = new McQuestion();
        mc.setQuestText(question);
        mc.setSopraUser(host);
        mc.setAnswers(answers.getStrings());
        mc.setSolutions(answers.getBooleans());
        System.out.println("question " + question);
        mc = mcQuestionRepository.save(mc);
        return "redirect:/quiz";
    }
    
    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    public String quiz(Model model, RedirectAttributes attr) {
    	Random r = new Random();
    	ArrayList<Question> al = new ArrayList<>();
    	// setup for test so there is always a question
    	String st = "Was is das richtige?";
    	McQuestion testmc = new McQuestion();
    	testmc.setQuestText(st);
    	String[] strs =  {"a","b","c","d"};
    	testmc.setAnswers(strs);
    	boolean[] b = {true,false,false,false};
    	testmc.setSolutions(b);
    	mcQuestionRepository.save(testmc);
    	int questionCount = 10;
    	for (int i = 0; i < questionCount; i++) {
    		int id = r.nextInt((int)mcQuestionRepository.count());
        	if(id == 0){
        		id++;
        	}
        	al.add(mcQuestionRepository.getOne(id));
		}
    	//id start at 1

    	Quiz q = new Quiz();
    	q.setQuestList(al);
    	q = quizRepository.save(q);
    	attr.addAttribute("id", Integer.toString(q.getQuizId()));
    	attr.addAttribute("number", Integer.toString(1));
    	model.addAttribute("percentage", Float.toString( 100*((float)1/(float)q.getQuestList().size())));
        return "redirect:/question/answer";
    }
    @RequestMapping(value = "/question/next{id,number}", method = RequestMethod.GET)
    public String quizNextQuestion(@RequestParam("id") String id,@RequestParam("number") String number,Model model, RedirectAttributes attr) {
    	Quiz q =quizRepository.getOne(Integer.parseInt(id));
    	if(Integer.parseInt(number) > q.getQuestList().size()){
    		//TODO end site to add
    		return "/home";
    	}
        attr.addAttribute("id",id);
        attr.addAttribute("number", number);
        model.addAttribute("percentage", Float.toString(100* Float.parseFloat(number)/(float)q.getQuestList().size()));
    	return "redirect:/question/answer";
    }
    @RequestMapping(value = "/question/answer{id,number}", method = RequestMethod.GET)
    public String answer(@RequestParam("id") String id,@RequestParam("number") String number,Model model, RedirectAttributes attr) {
    	Quiz q =quizRepository.getOne(Integer.parseInt(id));
    	McQuestion mc = (McQuestion)q.getQuestList().get(Integer.parseInt(number)-1);
    	model.addAttribute("question", mc.getQuestText()); 
        model.addAttribute("answerstext",mc.getAnswers());
        model.addAttribute("Answer",new Answer());
        model.addAttribute("id",id); 
        model.addAttribute("number",number);
        attr.addAttribute("id",id);
        attr.addAttribute("number", number);
        model.addAttribute("percentage", Float.toString(100* Float.parseFloat(number)/(float)q.getQuestList().size()));
        return "/question/answer";
    }
    @RequestMapping(value = "/question/answer{id,number}", method = RequestMethod.POST)
    public String answerPOST(@RequestParam("id") String id,@RequestParam("number") String number,Answer answer, Model model,  String direction, RedirectAttributes attr) {
    	if(direction.contains("next")){
    		number = Integer.toString(Integer.parseInt(number)+1);
    	} else 
    	if(direction.contains("prev")){
    		number = Integer.toString(Integer.parseInt(number)-1);
    	}
    	if(Integer.parseInt(number)<1){
    		number = "1";
    	}
    	attr.addAttribute("id", id);
    	attr.addAttribute("number", number);
    	
    	McQuestion mc = mcQuestionRepository.getOne(Integer.parseInt(id));
    	boolean[] b = answer.getBooleans();

    	for (int i = 0; i < mc.getSolutions().length; i++) {
			if(b[i]!=mc.getSolutions()[i]){
				System.out.println("false");
	    		attr.addAttribute("successful",false);
	    		
	    		return "/question/quiz";
			}
		}
		System.out.println("true");

    	attr.addAttribute("successful",true);
    	
        return "redirect:/question/next";
    }
}
