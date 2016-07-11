package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Burakhan on 14.06.2016.
 */
@Transactional
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
    public String post(@RequestParam("name") String name,HttpServletRequest request, Model model,@ModelAttribute("edit") String edit, RedirectAttributes attr) {
    	Post post = null;
    	if( request.getSession().getAttribute("post") !=null){
    		post = postRepository.findOne(Integer.parseInt((String) request.getSession().getAttribute("postid")));
    		System.out.println(post);
    	}
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
    public String postPOST(@RequestParam("name")String name, HttpServletRequest request,
    		String info, String heading, String content, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes attr) {
    	int id = Integer.parseInt(info);
    	Post post = new Post();
		post.setHeading(heading);
		post.setText(content);

    	String text =  post.getText();
    	if(post.getHeading() == null ||post.getHeading().isEmpty()|| post.getHeading().trim() == "" || text == null || text.isEmpty() || text.trim() == "" || text.equals("<p><br></p>")){
    		attr.addFlashAttribute("post",post);
    		attr.addAttribute("name", name);
    		attr.addAttribute("error", "missing");
    		return "redirect:/learninggroup/post";
    	}
    	
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = learningGroupRepository.findByName(name);
//        File file = (File) request.getSession().getAttribute("file");
        File f = null;
        if(file!=null && file.getSize()>0){
			try {
				String s = file.getOriginalFilename();
				f = new File(s);
				f.createNewFile(); 
	           
	            FileOutputStream fos;
				fos = new FileOutputStream(f);
	            fos.write(file.getBytes());
	            fos.close(); 
	           
			} catch (Exception e ) {
				attr.addFlashAttribute("post",post);
	    		attr.addAttribute("name", name);
				attr.addAttribute("error","upload");
				return "redirect:/learninggroup/post";
			} 
        } 
        
        if(id==-1){
        	post.setLearningGroup(lg);
	        post.setSopraUser(current);
	        post.setCreateDate();
	        if(f!=null){
	        	post.setFile(f);
	        }
	        post = postRepository.save(post);
	        
        } else {
        	Post p = postRepository.getOne(id);
        	p.setHeading(post.getHeading());
        	p.setText(post.getText());
	        p.setEditDate();
	        p.setEditUser(current);
	        if(f!=null){
	        	post.setFile(f);
	        }
        	postRepository.save(p);
        }
        if(f!=null){
        	f.delete();
        }
        return "redirect:/learninggroup/home?name=" + name;
    }
    @RequestMapping(value = "/learninggroup/post/download", method = RequestMethod.GET)
    @ResponseBody
    public void getFile(String info,HttpServletRequest request, 
            HttpServletResponse response) {
    	 int id = Integer.valueOf(info);
    	 File f = postRepository.getOne(id).getFile();
    	 System.out.println(f.getName());
    	 String name = f.getName().replaceAll(" ", "-");
    	 System.out.println(name.replaceAll(" ", "-"));
         response.addHeader("Content-Disposition", "attachment; filename="+name);
         try
         {
             Files.copy(f.toPath(), response.getOutputStream());
             response.getOutputStream().flush();
         } 
         catch (IOException ex) {
             ex.printStackTrace();
         }
         f.delete();
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
	        comment.setCreateDate();
	        Comment temp = commentRepository.save(comment);
	        post.getCommentList().add(temp);
	        
        } else {
        	Comment c = commentRepository.getOne(id);
        	c.setText(comment.getText());
        	comment.setEditDate();
        	comment.setEditUser(current);
        	commentRepository.save(c);
        }
        return "redirect:/learninggroup/home?name=" + name;
    }
    
    
    
}
