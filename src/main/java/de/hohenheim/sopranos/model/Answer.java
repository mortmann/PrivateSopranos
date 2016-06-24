package de.hohenheim.sopranos.model;

import java.util.ArrayList;

/**
 * going to replace this class when/if i find a better solution to get alot of data from one site in a list or so
 * @author Mortmann
 *
 */
public class Answer {
	private String answer0;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String answer5;
	private String answer6;
	private String answer7;
	private String answer8;
	private String answer9;
	public String answertext0;
	public String answertext1;
	public String answertext2;
	public String answertext3;
	public String answertext4;
	public String answertext5;
	public String answertext6;
	public String answertext7;
	public String answertext8;
	public String answertext9;
	public String getAnswer0() {
		return answer0;
	}
	public void setAnswer0(String answer0) {
		this.answer0 = answer0;
	}
	public String getAnswer1() {
		return answer1;
	}
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	public String getAnswer2() {
		return answer2;
	}
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	public String getAnswer3() {
		return answer3;
	}
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	public String getAnswer4() {
		return answer4;
	}
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
	public String getAnswer5() {
		return answer5;
	}
	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}
	public String getAnswer6() {
		return answer6;
	}
	public void setAnswer6(String answer6) {
		this.answer6 = answer6;
	}
	public String getAnswer7() {
		return answer7;
	}
	public void setAnswer7(String answer7) {
		this.answer7 = answer7;
	}
	public String getAnswer8() {
		return answer8;
	}
	public void setAnswer8(String answer8) {
		this.answer8 = answer8;
	}
	public String getAnswer9() {
		return answer9;
	}
	public void setAnswer9(String answer9) {
		this.answer9 = answer9;
	}
	public String getAnswertext0() {
		return answertext0;
	}
	public void setAnswertext0(String answertext0) {
		this.answertext0 = answertext0;
	}
	public String getAnswertext1() {
		return answertext1;
	}
	public void setAnswertext1(String answertext1) {
		this.answertext1 = answertext1;
	}
	public String getAnswertext2() {
		return answertext2;
	}
	public void setAnswertext2(String answertext2) {
		this.answertext2 = answertext2;
	}
	public String getAnswertext3() {
		return answertext3;
	}
	public void setAnswertext3(String answertext3) {
		this.answertext3 = answertext3;
	}
	public String getAnswertext4() {
		return answertext4;
	}
	public void setAnswertext4(String answertext4) {
		this.answertext4 = answertext4;
	}
	public String getAnswertext5() {
		return answertext5;
	}
	public void setAnswertext5(String answertext5) {
		this.answertext5 = answertext5;
	}
	public String getAnswertext6() {
		return answertext6;
	}
	public void setAnswertext6(String answertext6) {
		this.answertext6 = answertext6;
	}
	public String getAnswertext7() {
		return answertext7;
	}
	public void setAnswertext7(String answertext7) {
		this.answertext7 = answertext7;
	}
	public String getAnswertext8() {
		return answertext8;
	}
	public void setAnswertext8(String answertext8) {
		this.answertext8 = answertext8;
	}
	public String getAnswertext9() {
		return answertext9;
	}
	public void setAnswertext9(String answertext9) {
		this.answertext9 = answertext9;
	}
	/**
	 * Get the boolean values if you answer a question
	 * dont use this in anyother context
	 * @return
	 */
	public boolean[] getBooleans(){
		boolean[] b = new boolean[10];
		b[0] = isBoolean(answer0);
		b[1] = isBoolean(answer1);
		b[2] = isBoolean(answer2);
		b[3] = isBoolean(answer3);
		b[4] = isBoolean(answer4);
		b[5] = isBoolean(answer5);
		b[6] = isBoolean(answer6);
		b[7] = isBoolean(answer7);
		b[8] = isBoolean(answer8);
		b[9] = isBoolean(answer9);
		return b;
	}
	
	public String getAnswertext(int i){
		return "yeah";
	}
	
	/**
	 * Get the stings values if you create a question
	 * dont use this in anyother context
	 * @return
	 */
	public String[] getStrings(){
		ArrayList<String> s = new ArrayList<>();
		if (answertext0!=null && answertext0.isEmpty() == false)
			s.add(answertext0);
		if (answertext1!=null && answertext1.isEmpty() == false)
			s.add(answertext1);
		if (answertext2!=null && answertext2.isEmpty() == false)
			s.add(answertext2);
		if (answertext3!=null && answertext3.isEmpty() == false)
			s.add(answertext3);
		if (answertext4!=null && answertext4.isEmpty() == false)
			s.add(answertext4);
		if (answertext5!=null && answertext5.isEmpty() == false)
			s.add(answertext5);
		if (answertext6!=null && answertext6.isEmpty() == false)
			s.add(answertext6);
		if (answertext7!=null && answertext7.isEmpty() == false)
			s.add(answertext7);
		if (answertext8!=null && answertext8.isEmpty() == false)
			s.add(answertext8);
		if (answertext9!=null && answertext9.isEmpty() == false)
			s.add(answertext9);
		return (String[]) s.toArray(new String[0]);
	}
	private boolean isBoolean(String s){
		if(s==null){
			return false;
		}
		if(s.contains("on")==true){
			return true;
		} else {
			return false;
		}
	}
}
