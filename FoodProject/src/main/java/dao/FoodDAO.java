package dao;
//오라클 연결
import java.util.*;
import java.sql.*;
public class FoodDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	//1.드라이버 등록
	public FoodDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	//2.연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	//3.닫기
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	//4.기능
	//4-1. 망고플레이트 => 카테고리 저장
	public void foodCategoryInsert(FoodCategoryVO vo) {	//vo=row
		try {
			//1.연결
			/*
				서브쿼리
					1) 단일행 서브쿼리 : 컬럼이 한 개, 데이터 값이 한 개
						WHERE 컬럼명(비교연산자)(SELECT ~)
											===========결과값이 한 개(AVG, MAX..)
					2) 다중행 서브쿼리 : 컬럼이 한 개, 데이터 값이 여러개
						WHERE 컬럼명	IN(SELECT ~) => 동시에 적용
									>ANY(SELECT DISTINCT deptno FROM emp)
										=> 10,20,30 ==> 가장 작은 값 => 10
									<ANY(SELECT DISTINCT deptno FROM emp)
													==> 가장 큰 값 => 30
									>ALL(SELECT DISTINCT deptno FROM emp)
													==> 가장 큰 값 => 30
									<ALL(SELECT DISTINCT deptno FROM emp)
													==> 가장 작은 값 => 10
					3) 다중컬럼 서브쿼리 : 컬럼 두 개 이상, 데이터값 한 개
						WHERE (job,sal) =(SELECT jab,sal~)
					4) 스칼라 서브쿼리 : 컬럼 대신 사용 => 결과값은 한 개
						==> 여러개의 테이블에서 데이터 수집
						SELECT empno,(SELECT ~),(SELECT ~) FROM emp => JOIN 대신 사용
					5) 테이블 대신 사용 : 인라인뷰
						SELECT ~ FROM (SELECT ~) => 서브쿼리에 출력된 컬럼만 사용 가능
						목적 : SQL문장 여러개를 한 개로 묶어서 한번에 실행(자바에서 주로 사용)
						단점 : TOP-N (중간부터는 가지고 오지 못함)
						모든 SQL문장에서 사용 가능
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
	//4-2. 망고플레이트 => 맛집 저장
	public void foodHouseInsert(FoodHouseVO vo) {
		try {
			//1.연결
			getConnection();
			//2.SQL문장
			String sql="INSERT INTO food_house VALUES("
					+ "(SELECT NVL(MAX(no)+1,1) FROM food_house),"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			//3.SQL전송
			ps=conn.prepareStatement(sql);
			//4.?에 값 채우기
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
			//5.오라클에 실행명령
			ps.executeUpdate();	//autocommit
			//6.=> 속도 (레시피 15만개) => 나눠서 작업 (쓰레드)
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//4-3. INDEX (데이터 작거나, 수시로 변경 (X)) => 검색(카테고리 출력)
	public ArrayList<FoodCategoryVO> foodCategoryListData(){
		ArrayList<FoodCategoryVO> list=new ArrayList<FoodCategoryVO>();
		try {
			//1.열기
			getConnection();
			//2.SQL 문장
			String sql="SELECT cno,title,subject,poster,link "
					+ "FROM food_category "
					+ "ORDER BY cno ASC";
			//3.SQL 실행
			ps=conn.prepareStatement(sql);
			//4.실행 후 결과값 받기
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
	//카테고리별 맛집 가지고 오는 SQL문장
	public ArrayList<FoodHouseVO> foodList(int cno){
		ArrayList<FoodHouseVO> list=new ArrayList<FoodHouseVO>();
		try {
			//1.연결
			getConnection();
			//2.SQL문장
			String sql="SELECT no,name,score,address,tel,poster FROM food_house "
					+ "WHERE cno="+cno
					+" ORDER BY no ASC";
			ps=conn.prepareStatement(sql);
			//3.실행
			ResultSet rs=ps.executeQuery();
			//4.데이터 ArrayList에 담기
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
	//카테고리 내 목록 출력
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
		상세 => VO => PK에서 찾음
		목록 => ArrayList => 조건이 없는 경우(페이지)
	 */
	//맛집 상세보기 출력
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
			//데이터 vo에 저장
			//출력 위치 지정
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
			// FoodHouseVO vo=selectOne(sql) => MyBatis (한 줄에 끝남)
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
}
