package com.sist.dao;
//����Ŭ ������ ó��(CURD)
import java.util.*;
import java.sql.*;
/*
	����Ŭ ����
	1. ����̹� ��� => �ѹ��� ���� (�ѹ��� ȣ�� �޼ҵ� => ������)
		Class.forName("oracle.jdbc.driver.OracleDriver") => thin(���Ḹ �ϴ� ����̹�)
	2. ����
		Connection conn=DriverManager.getConnection(URL,"user","password")
	3. SQL���� ���� (SELECT, INSERT, UPDATE, DELETE)
		PreparedStatement ps=conn.preparedStatement("SQL����")
	4. ����
		executeUpdate() => autocommit : �ڹٴ� �ڵ����� COMMIT ����(=> ������ Ʈ�����(�ѹ鰡��))
						=> INSERT, UPDATE, DELETE
		executeQuery() => ���� �� ����� ������(commit X) => SELECT
	5. SELECT ����� ��� => ����� �޴´� => ResultSet (�޸𸮿� ���� �� ����)
	6. �ݱ�
		ps.close()
		conn.close()
	==============================
	2~6�ݺ� => �޼ҵ�
 */
//2�� �ڹ� => ����Ŭ ����, �� ��� ���̺귯��, ������ ����(Jsoup)
//3�� �ڹ� => XML,JSON, ANNOTATION, Spring, ���Խ�, RESTFUL(����� ����)
public class BoardDAO {
	//���� ��ü
	private Connection conn;
	//SQL���� ��ü
	private PreparedStatement ps;
	//����Ŭ �ּ� ���� => ����Ǹ� �ȵ�
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";	//XE : DB
	
	//����̹� ��� => ������
	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//new���� Ŭ���� �޸� �Ҵ� => ���÷���(Ŭ���� �̸����� Ŭ���� ����, �޸� �Ҵ�) => MyBatis, Spring
			//OracleDriver => ojdbc �ȿ� �ִ� Ŭ���� �̸���
		}catch(Exception ex) {
			ex.printStackTrace();	//ClassNotFoundException
		}
	}
	
	//����Ŭ ����
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");	// ==> conn hr/happy
		}catch(Exception ex) {}
	}
	//����Ŭ �ݱ� ==> exit
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	
	//���
	//1. ��� ��� => SELECT
	public ArrayList<BoardVO> boardListData(int page){
		ArrayList<BoardVO> list=new ArrayList<BoardVO>();
		try {
			//1.����
			getConnection();
			//2.SQL���� ����
			String sql="SELECT no,subject,name,regdate,hit "
					+ "FROM board "
					+ "ORDER BY no DESC";	//�ֽ� �Խù�����
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int i=0;	//10���� �����ִ� ����
			int j=0;	//while�� ���ư��� Ƚ��
			int cnt=(page*10)-10;	//��� ������ġ
			/*
				1page => 0~9(whileȽ��)
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
					list.add(vo);	//����
					i++;
				}
				j++;
			}
			//3.SQL���� ����
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	
	//2. �������� ���ϱ� => SELECT (CEIL())
	public int boardTotalPage() {
		int total=0;
		try {
			//1.����
			getConnection();
			//2.SQL���� ����
			String sql="SELECT CEIL(COUNT(*)/10.0) FROM board";
			//3.SQL���� ���� => �����
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next(); //������ ��� ��ġ�� Ŀ���� �̵�
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	//3. ���뺸�� => SELECT(������ �б�), UPDATE(��ȸ�� ����)
	public BoardVO boardDetail(int no) {
		BoardVO vo=new BoardVO();
		try {
			//1.����
			getConnection();
			//2.SQL���� (��ȸ�� ����)
			String sql="UPDATE board SET "
					+ "hit=hit+1 "
					+ "WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ps.executeUpdate();	//UPDATE ���� => COMMIT ����
			//3.����
			sql="SELECT no,name,subject,content,regdate,hit "
					+ "FROM board "
					+ "WHERE no="+no;	//PRIMARY KEY �ϳ��̻� �ݵ�� �ʿ� (no�ڵ����� => SEQUENCE)
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			//������ �ޱ�
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
	//4. �Խù� �߰� => INSERT
	public void boardInsert(BoardVO vo) {
		try {
			//1.����
			getConnection();
			//2.SQL���� ����
			//2-1. �Խù� ��ȣ�� �ִ밪 +1
			//String sql="SELECT NVL(MAX(no)+1,1) FROM board";	//�ڵ�������ȣ
			//ps=conn.prepareStatement(sql);
			//����� �ޱ�
			//ResultSet rs=ps.executeQuery();
			//rs.next();	//Ŀ���� ���� �Է��ٿ� ���� ������ �ֱ� ��°����� �ű�� ��
			//int max=rs.getInt(1);
			//rs.close();
			//=> subquery��
			String sql="INSERT INTO board(no,name,subject,content,pwd) "
					+ "VALUES((SELECT NVL(MAX(no)+1,1) FROM board),?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());	//String => ''�ڵ����� ����
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			
			//3.SQL���� ����
			ps.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//5. �����ϱ� => SELECT(���ο���Ȯ��:��й�ȣ Ȯ��), UPDATE(
	
	//6. �����ϱ� => SELECT(���ο���Ȯ��:��й�ȣ Ȯ��), DELETE
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
	//7. �˻� => SELECT (LIKE) => WHERE title '%'||��||'%' => �ڹٿ��� LIKE ����*****
	public ArrayList<BoardVO> boardFind(String fs,String ss){
		ArrayList<BoardVO> list=new ArrayList<BoardVO>();
		try {
			//1.����
			getConnection();
			//2.SQL���� ����
			//fs=name,subject,content
			String sql="SELECT no,subject,name,regdate,hit "
					+ "FROM board "
					+ "WHERE "+fs+" LIKE '%'||?||'%'";	//? => ''�پ����� ������ fs�� ���� �־���
			//3.������ ����� �ޱ�
			ps=conn.prepareStatement(sql);
			ps.setString(1, ss);
			ResultSet rs=ps.executeQuery();
			
			//4.ArrayList�� �� ä���
			while(rs.next()) {
				BoardVO vo=new BoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
			   // ����
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
			   //1. ����
			   getConnection();
			   //2. SQL���� 
			   // fs= name,subject ,content
			   String sql="SELECT COUNT(*) "
					     +"FROM board "
					     +"WHERE "+fs+" LIKE '%'||?||'%'";// ����Ŭ�� �ٸ��� 
			   //3. ������ ����� �ޱ�
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
