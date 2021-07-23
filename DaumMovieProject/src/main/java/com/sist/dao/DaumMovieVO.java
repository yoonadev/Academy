package com.sist.dao;
/*
	MNO      NOT NULL NUMBER        
	CNO               NUMBER        
	TITLE    NOT NULL VARCHAR2(300) 
	REGDATE           VARCHAR2(100) 
	GENRE    NOT NULL VARCHAR2(200) 
	NATION            VARCHAR2(100) 
	GRADE    NOT NULL VARCHAR2(50)  
	TIME     NOT NULL VARCHAR2(30)  
	SCORE             NUMBER(2,1)   
	SHOWUSER          VARCHAR2(100) 
	POSTER   NOT NULL VARCHAR2(260)
	
	=> VO (Value Object) => Spring
	=> DTO (Date Transfer Object) : 데이터를 모아서 전송할 클래스 => MyBatis
 */
public class DaumMovieVO {
	private int mno,cno;
	private String title,regdate,genre,nation,grade,time,showuser,poster,story;
	private double score;
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getShowuser() {
		return showuser;
	}
	public void setShowuser(String showuser) {
		this.showuser = showuser;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	
}
