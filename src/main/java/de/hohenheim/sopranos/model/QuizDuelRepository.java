package de.hohenheim.sopranos.model;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HP Brk on 22.06.2016.
 */
public interface QuizDuelRepository extends JpaRepository<QuizDuel, Integer> {
	
      public ArrayList<QuizDuel> findALLByLearningGroup(LearningGroup lg);
      public ArrayList<SopraUser> findALLChallangedByChallengerAndLearningGroup(SopraUser challenger,LearningGroup learningGroup);
      public ArrayList<SopraUser> findALLChallangerByChallengedAndLearningGroup(SopraUser challenged,LearningGroup learningGroup);
//
      public ArrayList<QuizDuel> findALLByChallenged(SopraUser challenged);
//    
      public QuizDuel findByChallengerAndLearningGroupAndDone(SopraUser challenger,LearningGroup learningGroup,boolean done);
      public QuizDuel findByChallengerQuizOrChallengedQuiz(Quiz q,Quiz cq);

}
