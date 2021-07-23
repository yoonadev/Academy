package com.sist.dao;
import java.sql.*;
import java.util.*;
//오라클 연결 => CURD가 가능하게 만든다
public class MovieDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	//드라이버 등록
	public MovieDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");	//패키지명.클래스명
			//등록 => 클래스 메모리 할당 => 리플렉션(Spring 핵심)
		}catch(Exception e) {	//ClassNotFoundException
			e.printStackTrace();
		}
	}
	//오라클 연결 => conn hr/happy => 자바 conn=DriverManager.getConnection(URL,"hr","happy")
	public void getConnection() {
		try {
			// TCP => 신뢰성 뛰어남, 연결지향적 프로토콜 (vs. UDP)
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception e) {}
	}
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch (Exception e) {}
	}
	//기능 설정
	//1. 데이터 첨부
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
 */
	public void daumMovieInsert(DaumMovieVO vo) {
		try {
			getConnection();
			String sql="INSERT INTO daum_movie VALUES("
					+ "daum_mno_seq.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			//SEQUENCE의 단점 : 중간행 삭제해도 번호 채워지지 않음 (증가만)
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getCno());
			ps.setString(2, vo.getTitle());
			ps.setString(3, vo.getRegdate());
			ps.setString(4, vo.getGenre());
			ps.setString(5, vo.getNation());
			ps.setString(6, vo.getGrade());
			ps.setString(7, vo.getTime());
			ps.setDouble(8, vo.getScore());
			ps.setString(9, vo.getShowuser());
			ps.setString(10, vo.getPoster());
			ps.setString(11, vo.getStory());
			
			ps.executeUpdate();	//autocommit
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//데이터 읽기
	public ArrayList<DaumMovieVO> daumMovieListData(int cno){
		ArrayList<DaumMovieVO> list=new ArrayList<DaumMovieVO>();
		try {
			getConnection();
			String sql="SELECT /*+ INDEX_ASC(daum_movie dm_mno_pk) */ mno,poster,title FROM daum_movie "
					+ "WHERE cno="+cno;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				DaumMovieVO vo=new DaumMovieVO();
				vo.setMno(rs.getInt(1));
				vo.setPoster(rs.getString(2));
				vo.setTitle(rs.getString(3));
				list.add(vo);
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	public void daumMovieScoreAvg() {
		try {
			getConnection();
			String sql="SELECT cno,ROUND(AVG(score)) FROM dm "
					+ "GROUP BY cno "
					+ "ORDER BY cno";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt(1)+","+rs.getDouble(2));
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//상세보기
	public DaumMovieVO daumMovieDetailData(int mno) {
		//mno => PK 중복 없기 때문에
		DaumMovieVO vo=new DaumMovieVO();
		try {
			getConnection();
			// * 로 가져오면 테이블에 생성된 순서로 집어넣어주면 됨
			String sql="SELECT mno,title,poster,genre,grade,nation,showUser,score,story,time,regdate "
					+ "FROM dm "
					+ "WHERE mno=?";
			ps=conn.prepareStatement(sql);
			//필요한 데이터가 있는 경우 (?)값을 채운다
			ps.setInt(1, mno);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setMno(rs.getInt(1));
			vo.setTitle(rs.getString(2));
			vo.setPoster(rs.getString(3));
			vo.setGenre(rs.getString(4));
			vo.setGrade(rs.getString(5));
			vo.setNation(rs.getString(6));
			vo.setShowuser(rs.getString(7));
			vo.setScore(rs.getDouble(8));
			vo.setStory(rs.getString(9));
			vo.setTime(rs.getString(10));
			vo.setRegdate(rs.getString(11));
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	//찾기 => LIKE
	public ArrayList<DaumMovieVO> daumMovieFindData(String fs,String ss){
		//title,genre,grade
		ArrayList<DaumMovieVO> list=new ArrayList<DaumMovieVO>();
		try {
			getConnection();
			String sql="SELECT mno,poster,title "
					+ "FROM dm "
					+ "WHERE "+fs+" LIKE '%'||?||'%'";
			ps=conn.prepareStatement(sql);
			ps.setString(1, ss);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				DaumMovieVO vo=new DaumMovieVO();
				vo.setMno(rs.getInt(1));
				vo.setPoster(rs.getString(2));
				vo.setTitle(rs.getString(3));
				list.add(vo);
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	
	
	
	public static void main(String[] args) {
		MovieDAO dao=new MovieDAO();
		//dao.daumMovieScoreAvg();
		Scanner sc=new Scanner(System.in);
		/*
		System.out.println("영화순위(1)");
		System.out.println("박스오피스-주간(2)");
		System.out.println("박스오피스-월간(3)");
		System.out.println("박스오피스-연간(4)");
		System.out.println("OTT(5)");
		System.out.println("Netflix(6)");
		System.out.println("Watcha(7)");
		System.out.println("Kakaopage(8)");
		System.out.println("===============");
		System.out.print("메뉴 선택:");
		int menu=sc.nextInt();
		ArrayList<DaumMovieVO> list=dao.daumMovieListData(menu);
		for(DaumMovieVO vo:list) {
			System.out.println(vo.getTitle());
		}
		*/
		System.out.print("영화 상세보기 선택(1~183):");
		int mno=sc.nextInt();
		DaumMovieVO vo=dao.daumMovieDetailData(mno);
		System.out.println(vo.getMno());
		System.out.println(vo.getTitle());
		System.out.println(vo.getGenre());
		System.out.println(vo.getGrade());
		System.out.println(vo.getNation());
		System.out.println(vo.getTime());
		System.out.println(vo.getShowuser());
		System.out.println(vo.getScore());
		System.out.println(vo.getRegdate());
	}
}
