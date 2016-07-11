package de.hohenheim.sopranos.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hohenheim.sopranos.model.LearningGroup;
import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.QuestionRepository;
import de.hohenheim.sopranos.model.SopraUserRepository;

@Controller
public class SearchController {
	@Autowired
	LearningGroupRepository learningGroupRepository;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	SopraUserRepository sopraUserRepository;
	@Autowired
	PostRepository postRepository;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@ModelAttribute("learningGroups") ArrayList<LearningGroup> lgs,String info, Model model, RedirectAttributes attr) {
		model.addAttribute("learningGroups", lgs);
		model.addAttribute("info",new String());
		return "/search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchPOST(String info, Model model, RedirectAttributes attr) {
		System.out.println("info " + info); 
		ArrayList<LearningGroup> lgs = new ArrayList<LearningGroup>();
		lgs.addAll(learningGroupRepository.findAllIgnoreCaseByNameLike("%"+info.toLowerCase()+"%"));
		attr.addFlashAttribute("learningGroups", lgs);
		return "redirect:/search";
	}
}
