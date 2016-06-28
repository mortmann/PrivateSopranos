package de.hohenheim.sopranos.controller;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

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
    CommentRepository commentRepository;
    @Autowired
    SopraUserRepository sopraUserRepository;
    @Autowired
    McQuestionRepository mcQuestionRepository;
    @Autowired
    OpenQuestionRepository openQuestionRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    LearningGroupRepository learningGroupRepository;
    @RequestMapping(value = "/question/create{name}")
    public String create(@RequestParam("name") String name, HttpServletRequest request,Model model, RedirectAttributes attr,@ModelAttribute("question") String question,@ModelAttribute("Answer") Answer answers,@ModelAttribute("amountcount") String amountstring) {
        request.getSession().setAttribute("group", learningGroupRepository.findByName(name));
    	if(amountstring == null || amountstring.isEmpty()){
    		amountstring = "4";
    	}
    	int amount = Integer.parseInt(amountstring);
    	if(amount<1){
    		amount = 1;
    	}
    	if(amount>10){
    		amount = 10;
    	}
    	model.addAttribute("amountcount",amount);
    	model.addAttribute("amount",new int[amount]);
    	model.addAttribute("question", question); 
        model.addAttribute("answer", answers);
        
        return "/question/create";
    }
    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public String createFinish(HttpServletRequest request, Answer answers, String question,String info, Model model, RedirectAttributes attr) {
    	String[] st = info.split("-");
    	LearningGroup group = (LearningGroup) request.getSession().getAttribute("group");
    	attr.addFlashAttribute("question", question);
    	attr.addFlashAttribute("Answer", answers);
    	attr.addAttribute("name", group.getName());
    	if(st.length>1){
	    	int amount = Integer.parseInt(st[1]);
	    	attr.addFlashAttribute("amount",new int[amount]);
	    	switch(st[0]){
	    		case "add":
	    			amount++;
	    			attr.addFlashAttribute("amountcount",Integer.toString(amount));
	    			return "redirect:/question/create";
	    		case "sub":
	    			amount--; 
	    			attr.addFlashAttribute("amountcount",Integer.toString(amount));
	    			return "redirect:/question/create";
	    	}
    	}
    	if( answers.getStrings().length==0){
    		attr.addAttribute("error", "noanswer");
    		return "redirect:/question/create";
    	}
    	for (String string : answers.getStrings()) {
			if(string.isEmpty()){
				attr.addAttribute("error", "noanswer");
	    		return "redirect:/question/create";
			}
		}
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        if(answers.getStrings().length>1){
        	boolean hasAnswer=false;
        	for (boolean b : answers.getBooleans()) {
        		if(b==true){
        			hasAnswer = true;
        		}
        	}   
        	if(hasAnswer == false){
        		attr.addAttribute("error", "noanswer");    	
        		return "redirect:/question/create";
        	}
        	String[] strs = answers.getStrings();
        	for (int i = 0; i < strs.length; i++) {
        		for (int s = i+1; s < strs.length; s++) {
        			if(strs[i].equalsIgnoreCase(strs[s])){
        				attr.addAttribute("error", "sameanswer");
        	    		return "redirect:/question/create";
        			}
        		}
    		}	
	        McQuestion mc = new McQuestion();
	        mc.setQuestText(question);
	        mc.setSopraUser(host);
	        mc.setAnswers(answers.getStrings());
	        mc.setSolutions(answers.getBooleans());
	        mc.setLearningGroup(group);
	        mc = mcQuestionRepository.save(mc);
        } else {
        	OpenQuestion qc = new OpenQuestion();
        	qc.setAnswer(answers.getAnswertext0());
        	qc.setQuestText(question);
        	qc.setSopraUser(host);
        	qc.setLearningGroup(group);
        	openQuestionRepository.save(qc);
        }
        return "redirect:/quiz";
    }
    
    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    public String quiz(HttpServletRequest request,Model model, RedirectAttributes attr) {
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
    	
    	LearningGroup group = (LearningGroup) request.getSession().getAttribute("group");
    	
    	testmc.setLearningGroup(group);
    	mcQuestionRepository.save(testmc);
    	
    	ArrayList<Question> qs = new ArrayList<>();
    	qs.addAll(mcQuestionRepository.findAll());
    	int questionCount = 10;
    	for (int i = 0; i < questionCount; i++) {
    		int id = r.nextInt((int)qs.size());
        	if(id == 0){
        		id++;
        	}
        	al.add(qs.get(id));
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
    	Quiz quiz =quizRepository.getOne(Integer.parseInt(id));
    	Question q = quiz.getQuestList().get(Integer.parseInt(number)-1);
    	if(q instanceof  McQuestion){
	    	McQuestion mc = (McQuestion)q;
	    	model.addAttribute("question", mc.getQuestText()); 
	        model.addAttribute("answerstext",mc.getAnswers());
    	} else {
    		OpenQuestion oc = (OpenQuestion)q;
	    	model.addAttribute("question", oc.getQuestText()); 
	    	String[] temp = new String[1];
	    	temp[0] = oc.getAnswer();
	    	
	        model.addAttribute("answerstext",temp);
    	}
        model.addAttribute("Answer",new Answer());
        model.addAttribute("id",id); 
        model.addAttribute("number",number); 
        attr.addAttribute("id",id);
        attr.addAttribute("number", number);
        model.addAttribute("percentage", Float.toString(100* Float.parseFloat(number)/(float)quiz.getQuestList().size()));
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
    	Quiz quiz =quizRepository.getOne(Integer.parseInt(id));
    	Question q = quiz.getQuestList().get(Integer.parseInt(number));
    	if(q instanceof  McQuestion){
	    	McQuestion mc = (McQuestion) q;
	    	boolean[] b = answer.getBooleans();
	
	    	for (int i = 0; i < mc.getSolutions().length; i++) {
				if(b[i]!=mc.getSolutions()[i]){
					System.out.println("false");
		    		attr.addAttribute("successful",false);
		    		
		    		return "/quiz";
				}
			}
			System.out.println("true");
	
	    	attr.addAttribute("successful",true);
    	} else {
    		
    		
    		//what todo for openquestions?
    		System.out.println(answer.getAnswer0());
    	}
        return "redirect:/question/next";
    }
    
    
    @RequestMapping(value = "/question/comment", method = RequestMethod.GET)
    public String comment(HttpServletRequest request,Model model,@ModelAttribute("questiontype") String type,@ModelAttribute("Question") Question question
    		,@ModelAttribute("comment") Comment comment,@ModelAttribute("edit") String edit, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = (LearningGroup) request.getSession().getAttribute("group");
        String name = lg.getName();
        if (lg.getGrayList().contains(current) || lg.getBlackList().contains(current))
            return "redirect:/learninggroup/home?name=" + name;
        model.addAttribute("comment", comment);
        model.addAttribute("name", name);
        model.addAttribute("question",question);
        request.getSession().setAttribute("question", question);
        model.addAttribute("edit", Boolean.parseBoolean(edit));
        return "question/comment"; 
    }
    @RequestMapping(value = "/question/comment", method = RequestMethod.POST)
    public String commentPOST(HttpServletRequest request,Comment comment, String info, Model model, RedirectAttributes attr) {
    	int id = Integer.parseInt(info);
    	Question question = (Question) request.getSession().getAttribute("question");
    	String text =  comment.getText();
    	if(text == null || text.isEmpty() || text.trim() == "" || text.equals("<p><br></p>")){
    		attr.addFlashAttribute("post",question);
    		attr.addAttribute("error", "missing");
    		return "learninggroup/comment";
    	}
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        if(id==-1){
        	comment.setQuestion(question);
	        comment.setSopraUser(current);
	        Comment temp = commentRepository.save(comment);
	        question.getCommentList().add(temp);
        } else {
        	Comment c = commentRepository.getOne(id);
        	c.setText(comment.getText());
        	commentRepository.save(c);
        }
        LearningGroup lg = (LearningGroup) request.getSession().getAttribute("group");
        return "redirect:/learninggroup/home?name="+lg.getName();
    }
    
    
    
    
}
