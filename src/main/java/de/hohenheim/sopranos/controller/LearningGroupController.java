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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	@Autowired
	UserEventRepository userEventRepository;
	@Autowired
	QuizDuelRepository quizDuelRepository;
	@Autowired
	QuizRepository quizRepository;

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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
		LearningGroup lg = learningGroupRepository.findByName(name);
		if(lg.getSopraUsers().contains(host)==false){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}


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
	@RequestMapping(value = "/learninggroup/mygroups", method = RequestMethod.POST)
	public String myGroupsPOST(Model model,String info, RedirectAttributes attr) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
		LearningGroup lg = learningGroupRepository.findByName(info);
		if(lg.isHost(host) == true){
			if (lg.getSopraUsers().size()>1){
				lg.setSopraHost(lg.getSopraUsers().get(1));
			} else{
				learningGroupRepository.delete(lg);
				attr.addAttribute("delete", "successful");
				return "redirect:/learninggroup/mygroups";
			}
		}
		lg.getSopraUsers().remove(host);
		learningGroupRepository.save(lg);
		attr.addAttribute("remove", "successful");
		return "redirect:/learninggroup/mygroups";
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
				if(lg.isFreeForFriends() == true){
					if(lg.getSopraHost().getFriendsListALL().contains(loginUser) == false){
						return "redirect:/learninggroup/login?name=" + name;
					}
				} else {
					return "redirect:/learninggroup/login?name=" + name;
				}
			}
			attr.addAttribute("join", "successful");
			lg.getSopraUsers().add(loginUser);

			UserEvent ue = new UserEvent();
			ue.setSopraUser(loginUser);
			ue.setText("Joined");
			ue.setLg(lg);
			ue.setCreateDate();
			System.out.println(ue.getCreateDate());
			ue = userEventRepository.save(ue);
			loginUser.getUserEventList().add(ue);
			sopraUserRepository.save(loginUser);

			learningGroupRepository.save(lg);
		}


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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
		LearningGroup lg = learningGroupRepository.findByName(name);
		if(lg.getSopraUsers().contains(host)==false || lg.isHost(host)){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}
		model.addAttribute("group", lg);
		model.addAttribute("name",name);
		attr.addAttribute("name", name);
		return "/learninggroup/option";
	}
	@RequestMapping(value = "/learninggroup/option{name}", method = RequestMethod.POST)
	public String lgOptionPost(@RequestParam("name") String name,String info, LearningGroup group,Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		if(info!=null && info.contains("delete")){
			learningGroupRepository.delete(learningGroupRepository.findByName(name));
			attr.addAttribute("delete", "successful");
			return "redirect:/home";
		}
		lg.setDescription(group.getDescription());
		lg.setPassword(group.getPassword());
		learningGroupRepository.save(lg);
		attr.addAttribute("edited", "successful");
		return "redirect:/learninggroup/home?name="+name;
	}

	@RequestMapping(value = "/learninggroup/users{name}", method = RequestMethod.GET)
	public String lgUser(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
		if(lg.getSopraUsers().contains(loginUser)==false){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}
		model.addAttribute("users", lg.getSopraUsers());
		model.addAttribute("bannedusers", lg.getBlackList());
		model.addAttribute("blockedusers", lg.getGrayList());
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
			return "redirect:/learninggroup/home?erro=notfound";
		}
		switch (extra) {
			case "ban":
				lg.banSopraUser(sopraUserRepository.findByEmail(email));
				learningGroupRepository.save(lg);
				return "redirect:/learninggroup/home?ban=successful";
			case "block":
				lg.lockSopraUser(sopraUserRepository.findByEmail(email));
				learningGroupRepository.save(lg);
				return "redirect:/learninggroup/home?block=successful";
			case "unban":
				lg.unbanSopraUser(sopraUserRepository.findByEmail(email));
				learningGroupRepository.save(lg);
				return "redirect:/learninggroup/home?unban=successful";
			case "unblock":
				lg.unlockSopraUser(sopraUserRepository.findByEmail(email));
				learningGroupRepository.save(lg);
				return "redirect:/learninggroup/home?unblock=successful";
		}
		return "redirect:/error";
	}
	@RequestMapping(value = "/learninggroup/questionlist{name}", method = RequestMethod.GET)
	public String allquestion(@RequestParam("name")
									  String name, String info, Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
		if(lg.getSopraUsers().contains(loginUser)==false){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}

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
					learningGroupRepository.save(lg);
					p.setNotReleased(null);
					p.setReleased(lg);
					questionRepository.save(p);
				}
				attr.addAttribute("rating", "successful");
				attr.addAttribute("name", name);
				return "redirect:/learninggroup/questionlist";
			case "addquiz":
				lg.getNotReleasedQuestionList().remove(p);
				lg.getQuestList().add(p);
				learningGroupRepository.save(lg);
				p.setNotReleased(null);
				p.setReleased(lg);
				questionRepository.save(p);
				attr.addAttribute("adding", "successful");
				attr.addAttribute("name", name);
				return "redirect:/learninggroup/questionlist";
		}
		return "redirect:/error";
	}
	@RequestMapping(value = "/learninggroup/quizduel{name}", method = RequestMethod.GET)
	public String quizduel(@RequestParam("name")
								   String name, Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
		if(lg.getSopraUsers().contains(loginUser)==false){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}

		List<SopraUser> users =  lg.getSopraUsers();
		users.removeAll(lg.getGrayList());
		users.remove(loginUser);
		List<SopraUser> temp = new ArrayList<>();
		for (QuizDuel qd : loginUser.getChallengedList()) {
			if(qd.isDone() == false){
				temp.add(qd.getChallenged());
				temp.add(qd.getChallenger());
			}
		}
		for (QuizDuel qd : loginUser.getChallengerList()) {
			if(qd.isDone() == false){
				temp.add(qd.getChallenged());
				temp.add(qd.getChallenger());
			}
		}
//    	users.removeAll(quizDuelRepository.findALLChallangedByChallengerAndLearningGroup(loginUser, lg));
//    	users.removeAll(quizDuelRepository.findALLChallangerByChallengedAndLearningGroup(loginUser, lg));

		users.removeAll(temp);

		ArrayList<QuizDuel> temp2 = quizDuelRepository.findALLByChallenged(loginUser);
		temp2.removeIf(x->x.isDone());

		model.addAttribute("users",users);
		model.addAttribute("challenges",temp2);
		model.addAttribute("name", name);
		return "/learninggroup/quizduel";
	}
	@RequestMapping(value = "/learninggroup/quizduel{name}", method = RequestMethod.POST)
	public String quizduelPOST(@RequestParam("name")
									   String name, String info,String count, Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
		if(lg.getSopraUsers().contains(loginUser)==false){
			attr.addAttribute("name", name);
			return "redirect:/learninggroup/home";
		}
		SopraUser other = sopraUserRepository.findByEmail(info.split("-")[0]);
		switch(info.split("-")[1]){
			case "challenge":
				QuizDuel qd = new QuizDuel();
				qd.setChallenger(loginUser);
				qd.setChallenged(other);
				qd.setLearningGroup(lg);
				int questionCount = 1;//Integer.valueOf(count);
				Random r = new Random();
				ArrayList<Question> al = new ArrayList<>();
				ArrayList<Question> qs = new ArrayList<>();
				qs.addAll(lg.getQuestList());
				qs.removeIf(x->x.getSopraUser().getEmail().equals(loginUser.getEmail()));
				qs.removeIf(x->x.getSopraUser().getEmail().equals(other.getEmail()));
				qs.removeIf(x->x.isOpenQuestion()==true);
				if(qs.isEmpty() || qs.size()<questionCount){
					attr.addAttribute("error", "questioncount");
					attr.addAttribute("name", "name");
					return "redirect:/learninggroup/quizduel";
				}
				for (int i = 0; i < questionCount; i++) {
					int id = r.nextInt((int)qs.size());
					if(id == 0){
						id++;
					}
					al.add(qs.get(id-1));
				}
				//id start at 1
				Quiz q = new Quiz();
				q.setQuestList(al);
				q.setGenerated(loginUser);
				q.setEditDate();
				q = quizRepository.save(q);
				q.setPartOfDuel(true);
				q.setCreateDate();
				qd.setChallengerQuiz(q);

				Quiz q2 = new Quiz();
				q2.setQuestList(al);
				q2.setCreateDate();
				q2.setGenerated(other);
				q2 = quizRepository.save(q2);
				q2.setPartOfDuel(true);
				qd.setChallengedQuiz(q2);
				quizDuelRepository.save(qd);
				attr.addAttribute("id", qd.getChallengerQuiz().getQuizId());
				attr.addAttribute("number", 1);
				return "redirect:/question/answer";
			case "deny":
				QuizDuel qdd = quizDuelRepository.findByChallengerAndLearningGroupAndDone(sopraUserRepository.findByEmail(info.split("-")[0]), lg,false);
				quizDuelRepository.delete(qdd);
				attr.addAttribute("deny", "successful");
				return "redirect:/learninggroup/quizduel";
			case "accept":
				QuizDuel qda = quizDuelRepository.findByChallengerAndLearningGroupAndDone(sopraUserRepository.findByEmail(info.split("-")[0]), lg,false);
				qda.getChallengedQuiz().setEditDate();
				quizRepository.save(qda.getChallengedQuiz());
				attr.addAttribute("id", qda.getChallengedQuiz().getQuizId());
				attr.addAttribute("number", 1);
				return "redirect:/question/answer";
		}
		return "/learninggroup/quizduel";
	}
	@RequestMapping(value = "/learninggroup/quizduelend{id,name}", method = RequestMethod.GET)
	public String quizduelEnd(@RequestParam("name")
									  String name, @RequestParam("id") String id,HttpServletRequest request, Model model, RedirectAttributes attr) {
		LearningGroup lg = learningGroupRepository.findByName(name);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
		QuizDuel qd = quizDuelRepository.getOne(Integer.valueOf(id));
		qd.isDone();
		quizDuelRepository.save(qd);
		model.addAttribute("isChallenger", qd.IsChallenger(loginUser));
		model.addAttribute("quizduel",qd );
		return "/learninggroup/quizduelend";
	}


}