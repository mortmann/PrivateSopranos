package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;

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

/**
 * Created by Burakhan on 14.06.2016.
 */
@Controller
public class PostController {

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;
    
    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.GET)
    public String post(@RequestParam("name") String name, Model model,@ModelAttribute("post") Post post,@ModelAttribute("edit") String edit, RedirectAttributes attr) {
    	post =(Post)model.asMap().get("post");
    	if(post==null){
    		post = new Post();
    		post.setHeading("");
    		post.setText("");
    	}
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = learningGroupRepository.findByName(name);

        if (lg.getGrayList().contains(current) || lg.getBlackList().contains(current))
            return "redirect:/learninggroup/home?name=" + name;
        
        model.addAttribute("post", post);
        model.addAttribute("name", name);
        attr.addAttribute("name", name);
        model.addAttribute("edit", Boolean.parseBoolean(edit));
        return "learninggroup/post";
    }

    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.POST)
    public String postPOST(@RequestParam("name")
                                         String name, String info, Post post, Model model, RedirectAttributes attr) {
    	int id = Integer.parseInt(info);
    	
    	String text =  post.getText();
    	if(post.getHeading() == null ||post.getHeading().isEmpty()|| post.getHeading().trim() == "" || text == null || text.isEmpty() || text.trim() == "" || text.equals("<p><br></p>")){
    		attr.addFlashAttribute("post",post);
    		attr.addAttribute("name", name);
    		attr.addAttribute("error", "missing");
    		return "learninggroup/post";
    	}
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = learningGroupRepository.findByName(name);
        if(id==-1){
	        post.setLearningGroup(lg);
	        post.setSopraUser(current);
	        postRepository.save(post);
        } else {
        	Post p = postRepository.getOne(id);
        	p.setHeading(post.getHeading());
        	p.setText(post.getText());
        	postRepository.save(p);
        }
        return "redirect:/learninggroup/home?name=" + name;
    }
    @RequestMapping(value = "/learninggroup/comment{name}", method = RequestMethod.GET)
    public String comment(@RequestParam("name") String name, HttpServletRequest request,Model model,@ModelAttribute("postid") String id,@ModelAttribute("comment") Comment comment,@ModelAttribute("edit") String edit, RedirectAttributes attr) {
    	Post post = postRepository.getOne(Integer.parseInt(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = learningGroupRepository.findByName(name);

        if (lg.getGrayList().contains(current) || lg.getBlackList().contains(current))
            return "redirect:/learninggroup/home?name=" + name;
        model.addAttribute("comment", comment);
        model.addAttribute("name", name);
        model.addAttribute("post",post);
        System.out.println("getCommentList" + post.getCommentList());
        request.getSession().setAttribute("post", post);
        System.out.println("---ID " + post.getId());
        attr.addAttribute("name", name);
        model.addAttribute("edit", Boolean.parseBoolean(edit));
        return "learninggroup/comment"; 
    }
    @RequestMapping(value = "/learninggroup/comment{name}", method = RequestMethod.POST)
    public String commentPOST(@RequestParam("name")
                                         String name,HttpServletRequest request,Comment comment, String info, Model model, RedirectAttributes attr) {
    	int id = Integer.parseInt(info);
    	Post post = (Post) request.getSession().getAttribute("post");
    	System.out.println("ID " + post.getId());
    	String text =  comment.getText();
    	if(text == null || text.isEmpty() || text.trim() == "" || text.equals("<p><br></p>")){
    		attr.addFlashAttribute("post",post);
    		attr.addAttribute("name", name);
    		attr.addAttribute("error", "missing");
    		return "learninggroup/comment";
    	}
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());

        if(id==-1){
        	comment.setPost(post);
	        comment.setSopraUser(current);
	        Comment temp = commentRepository.save(comment);
	        post.getCommentList().add(temp);
        } else {
        	Comment c = commentRepository.getOne(id);
        	c.setText(comment.getText());
        	commentRepository.save(c);
        }
        return "redirect:/learninggroup/home?name=" + name;
    }
    
    
    
}
