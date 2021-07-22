package dao;
//����Ŭ ����
import java.util.*;
import java.sql.*;
public class FoodDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	//1.����̹� ���
	public FoodDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	//2.����
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	//3.�ݱ�
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	//4.���
	//4-1. �����÷���Ʈ => ī�װ� ����
	public void foodCategoryInsert(FoodCategoryVO vo) {	//vo=row
		try {
			//1.����
			/*
				��������
					1) ������ �������� : �÷��� �� ��, ������ ���� �� ��
						WHERE �÷���(�񱳿�����)(SELECT ~)
											===========������� �� ��(AVG, MAX..)
					2) ������ �������� : �÷��� �� ��, ������ ���� ������
						WHERE �÷���	IN(SELECT ~) => ���ÿ� ����
									>ANY(SELECT DISTINCT deptno FROM emp)
										=> 10,20,30 ==> ���� ���� �� => 10
									<ANY(SELECT DISTINCT deptno FROM emp)
													==> ���� ū �� => 30
									>ALL(SELECT DISTINCT deptno FROM emp)
													==> ���� ū �� => 30
									<ALL(SELECT DISTINCT deptno FROM emp)
													==> ���� ���� �� => 10
					3) �����÷� �������� : �÷� �� �� �̻�, �����Ͱ� �� ��
						WHERE (job,sal) =(SELECT jab,sal~)
					4) ��Į�� �������� : �÷� ��� ��� => ������� �� ��
						==> �������� ���̺��� ������ ����
						SELECT empno,(SELECT ~),(SELECT ~) FROM emp => JOIN ��� ���
					5) ���̺� ��� ��� : �ζ��κ�
						SELECT ~ FROM (SELECT ~) => ���������� ��µ� �÷��� ��� ����
						���� : SQL���� �������� �� ���� ��� �ѹ��� ����(�ڹٿ��� �ַ� ���)
						���� : TOP-N (�߰����ʹ� ������ ���� ����)
						��� SQL���忡�� ��� ����
			 */
			getConnection();
			String sql="INSERT INTO food_category VALUES((SELECT NVL(MAX(cno)+1,1) FROM food_category),?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getTitle());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getPoster());
			ps.setString(4, vo.getLink());
			ps.executeUpdate();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//4-2. �����÷���Ʈ => ���� ����
	public void foodHouseInsert(FoodHouseVO vo) {
		try {
			//1.����
			getConnection();
			//2.SQL����
			String sql="INSERT INTO food_house VALUES("
					+ "(SELECT NVL(MAX(no)+1,1) FROM food_house),"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			//3.SQL����
			ps=conn.prepareStatement(sql);
			//4.?�� �� ä���
			ps.setInt(1, vo.getCno());
			ps.setString(2, vo.getName());
			ps.setDouble(3, vo.getScore());
			ps.setString(4, vo.getAddress());
			ps.setString(5, vo.getTel());
			ps.setString(6, vo.getType());
			ps.setString(7, vo.getPrice());
			ps.setString(8, vo.getParking());
			ps.setString(9, vo.getTime());
			ps.setString(10, vo.getMenu());
			ps.setString(11, vo.getPoster());
			ps.setInt(12, vo.getGood());
			ps.setInt(13, vo.getSoso());
			ps.setInt(14, vo.getBad());
			//5.����Ŭ�� ������
			ps.executeUpdate();	//autocommit
			//6.=> �ӵ� (������ 15����) => ������ �۾� (������)
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//4-3. INDEX (������ �۰ų�, ���÷� ���� (X)) => �˻�(ī�װ� ���)
	public ArrayList<FoodCategoryVO> foodCategoryListData(){
		ArrayList<FoodCategoryVO> list=new ArrayList<FoodCategoryVO>();
		try {
			//1.����
			getConnection();
			//2.SQL ����
			String sql="SELECT cno,title,subject,poster,link "
					+ "FROM food_category "
					+ "ORDER BY cno ASC";
			//3.SQL ����
			ps=conn.prepareStatement(sql);
			//4.���� �� ����� �ޱ�
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				FoodCategoryVO vo=new FoodCategoryVO();
				vo.setCno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSubject(rs.getString(3));
				vo.setPoster(rs.getString(4));
				vo.setLink("https://www.mangoplate.com"+rs.getString(5));
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
	//ī�װ��� ���� ������ ���� SQL����
	public ArrayList<FoodHouseVO> foodList(int cno){
		ArrayList<FoodHouseVO> list=new ArrayList<FoodHouseVO>();
		try {
			//1.����
			getConnection();
			//2.SQL����
			String sql="SELECT no,name,score,address,tel,poster FROM food_house "
					+ "WHERE cno="+cno
					+" ORDER BY no ASC";
			ps=conn.prepareStatement(sql);
			//3.����
			ResultSet rs=ps.executeQuery();
			//4.������ ArrayList�� ���
			while(rs.next()) {
				FoodHouseVO vo=new FoodHouseVO();
				vo.setNo(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setScore(rs.getDouble(3));
				vo.setAddress(rs.getString(4));
				vo.setTel(rs.getString(5));
				String s=rs.getString(6);
				vo.setPoster(s.substring(0,s.indexOf("^")).replace("#", "&"));
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
	//ī�װ� �� ��� ���
	public FoodCategoryVO foodCategoryInfoData(int cno) {
		FoodCategoryVO vo=new FoodCategoryVO();
		try {
			getConnection();
			String sql="SELECT title,subject "
					+ "FROM food_category "
					+ "WHERE cno="+cno;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setTitle(rs.getString(1));
			vo.setSubject(rs.getString(2));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	/*
		�� => VO => PK���� ã��
		��� => ArrayList => ������ ���� ���(������)
	 */
	//���� �󼼺��� ���
	public FoodHouseVO foodDetail(int no) {
		FoodHouseVO vo=new FoodHouseVO();
		try {
			getConnection();
			String sql="SELECT name,score,address,tel,type,price,parking,time,menu,"
					+ "REPLACE(poster,'#','&'),good,soso,bad "
					+ "FROM food_house "
					+ "WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			//������ vo�� ����
			//��� ��ġ ����
			rs.next();
			vo.setName(rs.getString(1));
			vo.setScore(rs.getDouble(2));
			vo.setAddress(rs.getString(3));
			vo.setTel(rs.getString(4));
			vo.setType(rs.getString(5));
			vo.setPrice(rs.getString(6));
			vo.setParking(rs.getString(7));
			vo.setTime(rs.getString(8));
			vo.setMenu(rs.getString(9));
			vo.setPoster(rs.getString(10));
			vo.setGood(rs.getInt(11));
			vo.setSoso(rs.getInt(12));
			vo.setBad(rs.getInt(13));
			rs.close();
			// FoodHouseVO vo=selectOne(sql) => MyBatis (�� �ٿ� ����)
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
}
