package com.sist.dao;
//오라클 연결 => 데이터는 VO(..ArrayList)에 저장 => 브라우저 전송
import java.util.*;	// ArrayList (배열 => 가변형)
import java.sql.*;	// Connection / Statement / ResultSet
/*
	Statement
		= Statement => 입력값과 SQL 동시 처리
		= PreparedStatement => SQL을 먼저 만들고(?) 마지막에 입력값을 채운다 (default)
		= CallableStatement => Procedure 함수 호출시에 사용 (사용자 정의)
							   ========= 보안(ERP)
 */
public class GenieDAO {
	//연결 객체
	private Connection conn;
	//SQL 문장 전송 객체
	private PreparedStatement ps;
	//오라클 주소
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	//1. 드라이버
	public GenieDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	//2. 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	//3. 닫기
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	//4. 기능설정 (인라인뷰를 이용해서 페이지 나누는 방법)
	public ArrayList<GenieVO> genieListData(int page){
		ArrayList<GenieVO> list=new ArrayList<GenieVO>();
		try {
			getConnection();
			String sql="SELECT no,poster,title,singer,album,state,idcrement,num "
					+ "FROM (SELECT no,poster,title,singer,album,state,idcrement,rownum as num "
					+ "FROM (SELECT no,poster,title,singer,album,state,idcrement "
					+ "FROM music_view ORDER BY no ASC)) "
					+ "WHERE num BETWEEN ? AND ?";
			//오라클 => 인라인뷰
			//자바 => while => 전체 200개에서 20개만 추출 => 속도 느려짐
			int rowSize=20;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			// ?에 값 채우기
			ps=conn.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);
			//페이지 나눈다
			//데이터 읽기
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				GenieVO vo=new GenieVO();
				vo.setNo(rs.getInt(1));
				vo.setPoster(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setSinger(rs.getString(4));
				vo.setAlbum(rs.getString(5));
				vo.setState(rs.getString(6));
				vo.setIdcrement(rs.getInt(7));
				vo.setKey(rs.getString(8));
				 
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
	//총페이지
	public int genieTotalPage() {
		int total=0;
		try {
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/20.0) FROM music_view";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	//5. SQL문장을 단순화 => 복합뷰 (조인) => 지니뮤직(동영상X), 멜론
	/*
		SELECT gm.no,gm.poster,gm.title,gm.singer,gm.album,gm.idcrement,gm.state,mm.key
		FROM genie_music gm,melon_music mm
		WHERE gm.title=mm.title;
		
		SELECT * FROM music_view;
	 */
	public GenieVO genieDetailData(int no) {
		GenieVO vo=new GenieVO();
		try {
			getConnection();
			String sql="SELECT no,poster,title,singer,album,key "
					+ "FROM music_view "
					+ "WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setPoster(rs.getString(2));
			vo.setTitle(rs.getString(3));
			vo.setSinger(rs.getString(4));
			vo.setAlbum(rs.getString(5));
			vo.setKey(rs.getString(6));
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
}
