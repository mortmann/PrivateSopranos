package de.hohenheim.sopranos.model;

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
