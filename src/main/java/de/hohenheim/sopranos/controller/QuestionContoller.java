package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Random;

@Transactional
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Controller
public class QuestionContoller {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    SopraUserRepository sopraUserRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    LearningGroupRepository learningGroupRepository;
    @Autowired
    QuizDuelRepository quizDuelRepository;
    @Autowired
    MessageRepository messageRepository;

    @RequestMapping(value = "/question/create{name}")
    public String create(@RequestParam("name") String name, HttpServletRequest request, Model model, RedirectAttributes attr, @ModelAttribute("question") String question, @ModelAttribute("Answer") Answer answers, @ModelAttribute("amountcount") String amountstring) {
        request.getSession().setAttribute("group", learningGroupRepository.findByName(name));
        if (amountstring == null || amountstring.isEmpty()) {
            amountstring = "4";
        }
        int amount = Integer.parseInt(amountstring);
        if (amount < 1) {
            amount = 1;
        }
        if (amount > 10) {
            amount = 10;
        }
        model.addAttribute("amountcount", amount);
        model.addAttribute("amount", new int[amount]);
        model.addAttribute("question", question);
        model.addAttribute("answer", answers);

        return "/question/create";
    }

    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public String createFinish(HttpServletRequest request, Answer answers, String question, String info, Model model, RedirectAttributes attr) {
        String[] st = info.split("-");
        LearningGroup group = (LearningGroup) request.getSession().getAttribute("group");
        group = learningGroupRepository.getOne(group.getLgId());
        attr.addFlashAttribute("question", question);
        attr.addFlashAttribute("Answer", answers);
        attr.addFlashAttribute("amountcount", answers.getStrings().length);

        attr.addAttribute("name", group.getName());
        if (st.length > 1) {
            int amount = Integer.parseInt(st[1]);
            attr.addFlashAttribute("amount", new int[amount]);
            switch (st[0]) {
                case "add":
                    amount++;
                    attr.addFlashAttribute("amountcount", Integer.toString(amount));
                    return "redirect:/question/create";
                case "sub":
                    amount--;
                    attr.addFlashAttribute("amountcount", Integer.toString(amount));
                    return "redirect:/question/create";
            }
        }
        //hier stand vorher <9 drinne, war das ein tippfehler?
        if (question == null || question.trim().length() <= 5) {
            attr.addAttribute("error", "noquestion");
            return "redirect:/question/create";
        }
        if (answers.getStrings().length == 0) {
            attr.addAttribute("error", "noanswer");
            return "redirect:/question/create";
        }
        for (String string : answers.getStrings()) {
            if (string.trim().isEmpty()) {
                attr.addAttribute("error", "noanswer");
                return "redirect:/question/create";
            }
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        if (answers.getStrings().length > 1) {
            boolean hasAnswer = false;
            for (boolean b : answers.getBooleans()) {
                if (b == true) {
                    hasAnswer = true;
                }
            }
            if (hasAnswer == false) {
                attr.addAttribute("error", "noanswer");
                return "redirect:/question/create";
            }
            String[] strs = answers.getStrings();
            for (int i = 0; i < strs.length; i++) {
                for (int s = i + 1; s < strs.length; s++) {
                    if (strs[i].equalsIgnoreCase(strs[s])) {
                        attr.addAttribute("error", "sameanswer");
                        return "redirect:/question/create";
                    }
                }
            }
        }

        Question qc = new Question();
        qc.setAnswers(answers.getStrings());
        qc.setSolutions(answers.getBooleans());
        qc.setQuestText(question);
        qc.setSopraUser(host);
        qc.setNotReleased(group);
        qc.setCreateDate();
        questionRepository.save(qc);
        group.getNotReleasedQuestionList().add(qc);
        learningGroupRepository.save(group);

        attr.addFlashAttribute("quest", qc);
        return "redirect:/question/comment";
    }

    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    public String quiz(HttpServletRequest request, Model model, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());


        model.addAttribute("groups", current.getLearningGroups());
        return "/quiz";
    }

    @RequestMapping(value = "/quiz", method = RequestMethod.POST)
    public String quizPOST(HttpServletRequest request, String info, String count, String open, String closed, Model model, RedirectAttributes attr) {
        if (info == null || info.isEmpty()) {
            attr.addAttribute("error", "info");
            return "redirect:/quiz";
        }
        int questionCount = Integer.valueOf(count);
        Random r = new Random();
        ArrayList<Question> al = new ArrayList<>();
        ArrayList<Question> qs = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup group = null;

        String[] grs = info.split(",");
        for (String name : grs) {
            group = learningGroupRepository.findByName("name");
            if (group != null && learningGroupRepository.findByName(name) != null) {
                System.out.println("add");
                qs.addAll(group.getQuestList());
            }
        }

        System.out.println(qs.size());
        if (open == null) {
            qs.removeIf(x -> x.isOpenQuestion() == true);
        }
        System.out.println(closed + " after closed " + qs.size());
        if (closed == null) {
            qs.removeIf(x -> x.isOpenQuestion() == false);
        }
        System.out.println(open + " after open " + qs.size());
        qs.removeIf(x -> x.getSopraUser().getEmail().equals(current.getEmail()));

        if (qs.isEmpty()) {
            qs.addAll(questionRepository.findAll());
        }
        if (qs.isEmpty() || qs.size() < questionCount) {
            attr.addAttribute("error", "questioncount");
            return "redirect:/quiz";
        }
        for (int i = 0; i < questionCount; i++) {
            int id = r.nextInt((int) qs.size());
            if (id == 0) {
                id++;
            }
            al.add(qs.get(id - 1));
            qs.remove(id-1);
        }
        //id start at 1
        Quiz q = new Quiz();
        q.setQuestList(al);
        q.setGenerated(current);
        q = quizRepository.save(q);
        attr.addAttribute("id", Integer.toString(q.getQuizId()));
        attr.addAttribute("number", Integer.toString(1));
        model.addAttribute("percentage", Float.toString(100 * ((float) 1 / (float) q.getQuestList().size())));
        return "redirect:/question/answer";

    }

    @RequestMapping(value = "/question/end{id}", method = RequestMethod.GET)
    public String quizEND(@RequestParam("id") String id, HttpServletRequest request, Model model, RedirectAttributes attr) {
        Quiz quiz = quizRepository.getOne(Integer.parseInt(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        if (quiz.getGenerated().getEmail().equals(current.getEmail()) == false) {
            return "redirect:/home";
        }
        quiz.setDone(true);
        ArrayList<Message> msgList = new ArrayList<>();
        ArrayList<Integer> nrList = new ArrayList<>();

        boolean[] correct = new boolean[quiz.getQuestList().size()];
        int points = 0;
        for (int i = 0; i < quiz.getQuestList().size(); i++) {
            Question q = quiz.getQuestList().get(i);
            boolean[] answer = quiz.getAnswersBoolean()[i];
            if (q.isOpenQuestion() == false) {
                System.out.println("mc");
                correct[i] = q.questioncorrected(answer);
                if (correct[i]) {
                    points++;
                    current.increaseRankpoints();
                } else {
                    points--;
                    current.decreaseRankpoints();
                }
            } else {
                //what todo for openquestions?
                //Als Nachricht an den Ersteller
                Message m = new Message();
                m.setCreateDate();
                //m.setSender(current);// shouldnt this be anonymous? create a dummy User?
                m.setSender(sopraUserRepository.findByEmail("system@synapse.de"));
                m.setReceiver(q.getSopraUser());
                m.setTitle("Quizantwort auf einer Ihrer Fragen");
                nrList.add(i);
                msgList.add(m);
            }
        }

        quiz.setPoints(points);
        quiz = quizRepository.save(quiz);
        for (int i = 0; i < msgList.size(); i++) {
            Message m = msgList.get(i);
            m.setMessage("Bitte bewerten sie die folgende Antwort objektiv. \n <a href='/question/correction?quizid=" + quiz.getQuizId() + "&questionNR=" + nrList.get(i) + "'>Bewerten!</a>");
            messageRepository.save(m);
        }
        if (quiz.isPartOfDuel()) {
            request.getSession().setAttribute("quiz", quiz);
            attr.addAttribute("id", quizDuelRepository.findByChallengerQuizOrChallengedQuiz(quiz, quiz).getQuizId());
            attr.addAttribute("name", quizDuelRepository.findByChallengerQuizOrChallengedQuiz(quiz, quiz).getLearningGroup().getName());
            return "redirect:/learninggroup/quizduelend";
        }


        model.addAttribute("questions", quiz.getQuestList());
        model.addAttribute("quiz", quiz);
        model.addAttribute("correct", correct);
        return "question/end";
    }


    @RequestMapping(value = "/question/next{id,number}", method = RequestMethod.GET)
    public String quizNextQuestion(@RequestParam("id") String id, @RequestParam("number") String number, Model model, RedirectAttributes attr) {
        Quiz q = quizRepository.getOne(Integer.parseInt(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        if (q.getGenerated().getEmail().equals(current.getEmail()) == false) {
            return "redirect:/home";
        }
        if (Integer.parseInt(number) > q.getQuestList().size() || q.isDone()) {
            return "redirect:/question/end?id=" + id;
        }
        attr.addAttribute("id", id);
        attr.addAttribute("number", number);
        model.addAttribute("percentage", Float.toString(100 * Float.parseFloat(number) / (float) q.getQuestList().size()));
        return "redirect:/question/answer";
    }

    @RequestMapping(value = "/question/answer{id,number}", method = RequestMethod.GET)
    public String answer(@RequestParam("id") String id, @RequestParam("number") String number, Model model, RedirectAttributes attr) {
        Quiz quiz = quizRepository.getOne(Integer.parseInt(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        if (quiz.getGenerated().getEmail().equals(current.getEmail()) == false) {
            return "redirect:/home";
        }
        if (quiz.isDone()) {
            return "redirect:/question/end?id=" + id;
        }
        Question q = quiz.getQuestList().get(Integer.parseInt(number) - 1);
        Question mc = q;
        model.addAttribute("question", mc.getQuestText());
        model.addAttribute("answerstext", mc.getAnswers());
        model.addAttribute("Answer", new Answer());
        model.addAttribute("id", id);
        model.addAttribute("number", number);
        attr.addAttribute("id", id);
        attr.addAttribute("number", number);
        model.addAttribute("percentage", Float.toString(100 * Float.parseFloat(number) / (float) quiz.getQuestList().size()));
        return "/question/answer";
    }

    @RequestMapping(value = "/question/answer{id,number}", method = RequestMethod.POST)
    public String answerPOST(@RequestParam("id") String id, @RequestParam("number") String number, Answer answer, Model model, String direction, RedirectAttributes attr) {
        Quiz quiz = quizRepository.getOne(Integer.parseInt(id));
        quiz.addAnswer(Integer.parseInt(number), answer);
        if (direction.contains("next")) {
            number = Integer.toString(Integer.parseInt(number) + 1);
        } else if (direction.contains("prev")) {
            number = Integer.toString(Integer.parseInt(number) - 1);
        }
        if (Integer.parseInt(number) < 1) {
            number = "1";
        }
        attr.addAttribute("id", id);
        attr.addAttribute("number", number);

        return "redirect:/question/next";
    }


    @RequestMapping(value = "/question/comment", method = RequestMethod.GET)
    public String comment(HttpServletRequest request, Model model, @ModelAttribute("questiontype") String type, @ModelAttribute("quest") Question question
            , @ModelAttribute("comment") Comment comment, @ModelAttribute("edit") String edit, RedirectAttributes attr) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        LearningGroup lg = (LearningGroup) request.getSession().getAttribute("group");
        lg = learningGroupRepository.getOne(lg.getLgId());
        Hibernate.initialize(lg);
        String name = lg.getName();
        if (lg.getGrayList().contains(current) || lg.getBlackList().contains(current))
            return "redirect:/learninggroup/home?name=" + name;


        model.addAttribute("comment", comment);
        model.addAttribute("name", name);
        question = questionRepository.getOne(question.getQuestId());
        model.addAttribute("question", question);
        attr.addFlashAttribute("quest", question);
        request.getSession().setAttribute("question", question);
        model.addAttribute("edit", Boolean.parseBoolean(edit));
        return "question/comment";
    }

    @RequestMapping(value = "/question/comment", method = RequestMethod.POST)
    public String commentPOST(HttpServletRequest request, @ModelAttribute("quest") Question question, Comment comment, String info, Model model, RedirectAttributes attr) {
        int id = Integer.parseInt(info);
        question = (Question) request.getSession().getAttribute("question");
        question = questionRepository.getOne(question.getQuestId());
        String text = comment.getText();
        if (text == null || text.isEmpty() || text.trim() == "" || text.equals("<p><br></p>")) {
            attr.addFlashAttribute("post", question);
            attr.addAttribute("error", "missing");
            return "learninggroup/comment";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());
        if (id == -1) {
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
        return "redirect:/learninggroup/home?name=" + lg.getName();
    }

    @RequestMapping(value = "/question/correction", method = RequestMethod.GET)
    public String correction(@RequestParam("quizid") String quizID, @RequestParam("questionNR") String questionNumber, Model model, RedirectAttributes attr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());

        Quiz q = quizRepository.getOne(Integer.valueOf(quizID));
        Question quest = q.getQuestList().get(Integer.valueOf(questionNumber));
        if (quest.getSopraUser().getEmail().equals(current.getEmail()) == false) {
            return "redirect:/home";
        }
        if (q.getAnswercorrected()[Integer.valueOf(questionNumber)]) {
            return "redirect:/message/inbox";
        }
        model.addAttribute("quizid", quizID);
        model.addAttribute("questionNR", questionNumber);
        model.addAttribute("Question", quest);
        model.addAttribute("Answer", q.getAnswertext()[Integer.valueOf(questionNumber)]);
        return "/question/correction";
    }

    @RequestMapping(value = "/question/correction", method = RequestMethod.POST)
    public String correctionPOST(@RequestParam("quizid") String quizID, @RequestParam("questionNR") String questionNumber, String info, Model model, RedirectAttributes attr) {
        Quiz q = quizRepository.getOne(Integer.valueOf(quizID));
        q.setSolution(Integer.valueOf(questionNumber), Boolean.parseBoolean(info));
        quizRepository.save(q);
        attr.addAttribute("correction", "successful");
        return "redirect:/message/inbox";
    }
}
