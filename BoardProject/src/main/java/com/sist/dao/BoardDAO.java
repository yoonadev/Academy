package com.sist.dao;
//오라클 연결후 처리(CURD)
import java.util.*;
import java.sql.*;
/*
	오라클 연결
	1. 드라이버 등록 => 한번만 수행 (한번만 호출 메소드 => 생성자)
		Class.forName("oracle.jdbc.driver.OracleDriver") => thin(연결만 하는 드라이버)
	2. 연결
		Connection conn=DriverManager.getConnection(URL,"user","password")
	3. SQL문장 전송 (SELECT, INSERT, UPDATE, DELETE)
		PreparedStatement ps=conn.preparedStatement("SQL문장")
	4. 실행
		executeUpdate() => autocommit : 자바는 자동으로 COMMIT 수행(=> 스프링 트랜잭션(롤백가능))
						=> INSERT, UPDATE, DELETE
		executeQuery() => 수행 후 결과값 가져옴(commit X) => SELECT
	5. SELECT 실행된 경우 => 결과값 받는다 => ResultSet (메모리에 저장 후 실행)
	6. 닫기
		ps.close()
		conn.close()
	==============================
	2~6반복 => 메소드
 */
//2차 자바 => 오라클 연결, 웹 출력 라이브러리, 데이터 수집(Jsoup)
//3차 자바 => XML,JSON, ANNOTATION, Spring, 정규식, RESTFUL(모바일 연결)
public class BoardDAO {
	//연결 객체
	private Connection conn;
	//SQL전송 객체
	private PreparedStatement ps;
	//오라클 주소 설정 => 변경되면 안됨
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";	//XE : DB
	
	//드라이버 등록 => 생성자
	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//new없이 클래스 메모리 할당 => 리플렉션(클래스 이름으로 클래스 제어, 메모리 할당) => MyBatis, Spring
			//OracleDriver => ojdbc 안에 있는 클래스 이름임
		}catch(Exception ex) {
			ex.printStackTrace();	//ClassNotFoundException
		}
	}
	
	//오라클 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");	// ==> conn hr/happy
		}catch(Exception ex) {}
	}
	//오라클 닫기 ==> exit
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	
	//기능
	//1. 목록 출력 => SELECT
	public ArrayList<BoardVO> boardListData(int page){
		ArrayList<BoardVO> list=new ArrayList<BoardVO>();
		try {
			//1.연결
			getConnection();
			//2.SQL문장 제작
			String sql="SELECT no,subject,name,regdate,hit "
					+ "FROM board "
					+ "ORDER BY no DESC";	//최신 게시물부터
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int i=0;	//10개씩 나눠주는 변수
			int j=0;	//while이 돌아가는 횟수
			int cnt=(page*10)-10;	//출력 시작위치
			/*
				1page => 0~9(while횟수)
				2page => 10~19
				3page => 20~29
			 */
			while(rs.next()) {
				if(i<10 && j>=cnt) {
					BoardVO vo=new BoardVO();
					vo.setNo(rs.getInt(1));
					vo.setSubject(rs.getString(2));
					vo.setName(rs.getString(3));
					vo.setRegdate(rs.getDate(4));
					vo.setHit(rs.getInt(5));
					list.add(vo);	//저장
					i++;
				}
				j++;
			}
			//3.SQL문장 전송
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	
	//2. 총페이지 구하기 => SELECT (CEIL())
	public int boardTotalPage() {
		int total=0;
		try {
			//1.연결
			getConnection();
			//2.SQL문장 제작
			String sql="SELECT CEIL(COUNT(*)/10.0) FROM board";
			//3.SQL문장 전송 => 결과값
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next(); //데이터 출력 위치에 커서를 이동
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	//3. 내용보기 => SELECT(데이터 읽기), UPDATE(조회수 증가)
	public BoardVO boardDetail(int no) {
		BoardVO vo=new BoardVO();
		try {
			//1.연결
			getConnection();
			//2.SQL문장 (조회수 증가)
			String sql="UPDATE board SET "
					+ "hit=hit+1 "
					+ "WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ps.executeUpdate();	//UPDATE 실행 => COMMIT 포함
			//3.실행
			sql="SELECT no,name,subject,content,regdate,hit "
					+ "FROM board "
					+ "WHERE no="+no;	//PRIMARY KEY 하나이상 반드시 필요 (no자동증가 => SEQUENCE)
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			//데이터 받기
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	//4. 게시물 추가 => INSERT
	public void boardInsert(BoardVO vo) {
		try {
			//1.연결
			getConnection();
			//2.SQL문장 제작
			//2-1. 게시물 번호의 최대값 +1
			//String sql="SELECT NVL(MAX(no)+1,1) FROM board";	//자동증가번호
			//ps=conn.prepareStatement(sql);
			//결과값 받기
			//ResultSet rs=ps.executeQuery();
			//rs.next();	//커서가 다음 입력줄에 가기 때문에 최근 출력값으로 옮기는 것
			//int max=rs.getInt(1);
			//rs.close();
			//=> subquery로
			String sql="INSERT INTO board(no,name,subject,content,pwd) "
					+ "VALUES((SELECT NVL(MAX(no)+1,1) FROM board),?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());	//String => ''자동으로 붙음
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			
			//3.SQL문장 실행
			ps.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//5. 수정하기 => SELECT(본인여부확인:비밀번호 확인), UPDATE(
	
	//6. 삭제하기 => SELECT(본인여부확인:비밀번호 확인), DELETE
	public void boardDelete(int no) {
		try {
			getConnection();
			String sql="DELETE FROM board WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ps.executeQuery();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//7. 검색 => SELECT (LIKE) => WHERE title '%'||값||'%' => 자바에서 LIKE 사용시*****
	public ArrayList<BoardVO> boardFind(String fs,String ss){
		ArrayList<BoardVO> list=new ArrayList<BoardVO>();
		try {
			//1.연결
			getConnection();
			//2.SQL문장 제작
			//fs=name,subject,content
			String sql="SELECT no,subject,name,regdate,hit "
					+ "FROM board "
					+ "WHERE "+fs+" LIKE '%'||?||'%'";	//? => ''붙어지기 때문에 fs는 직접 넣어줌
			//3.실행후 결과값 받기
			ps=conn.prepareStatement(sql);
			ps.setString(1, ss);
			ResultSet rs=ps.executeQuery();
			
			//4.ArrayList에 값 채우기
			while(rs.next()) {
				BoardVO vo=new BoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
			   // 저장
				list.add(vo);
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	
	public int boardFindCount(String fs,String ss)
	   {
		   int count=0;
		   try
		   {
			   //1. 연결
			   getConnection();
			   //2. SQL문장 
			   // fs= name,subject ,content
			   String sql="SELECT COUNT(*) "
					     +"FROM board "
					     +"WHERE "+fs+" LIKE '%'||?||'%'";// 오라클과 다르다 
			   //3. 실행후 결과값 받기
			   ps=conn.prepareStatement(sql);
			   ps.setString(1, ss);
			   ResultSet rs=ps.executeQuery();
			   rs.next();
			   count=rs.getInt(1);
			   rs.close();
		   }catch(Exception ex)
		   {
			  ex.printStackTrace();
		   }
		   finally
		   {
			   disConnection();
		   }
		   return count;
	   }
	
}
