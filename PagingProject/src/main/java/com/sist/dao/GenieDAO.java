package com.sist.dao;
//����Ŭ ���� => �����ʹ� VO(..ArrayList)�� ���� => ������ ����
import java.util.*;	// ArrayList (�迭 => ������)
import java.sql.*;	// Connection / Statement / ResultSet
/*
	Statement
		= Statement => �Է°��� SQL ���� ó��
		= PreparedStatement => SQL�� ���� �����(?) �������� �Է°��� ä��� (default)
		= CallableStatement => Procedure �Լ� ȣ��ÿ� ��� (����� ����)
							   ========= ����(ERP)
 */
public class GenieDAO {
	//���� ��ü
	private Connection conn;
	//SQL ���� ���� ��ü
	private PreparedStatement ps;
	//����Ŭ �ּ�
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	//1. ����̹�
	public GenieDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	//2. ����
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	//3. �ݱ�
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();
			if(conn!=null)	conn.close();
		}catch(Exception ex) {}
	}
	//4. ��ɼ��� (�ζ��κ並 �̿��ؼ� ������ ������ ���)
	public ArrayList<GenieVO> genieListData(int page){
		ArrayList<GenieVO> list=new ArrayList<GenieVO>();
		try {
			getConnection();
			String sql="SELECT no,poster,title,singer,album,state,idcrement,num "
					+ "FROM (SELECT no,poster,title,singer,album,state,idcrement,rownum as num "
					+ "FROM (SELECT no,poster,title,singer,album,state,idcrement "
					+ "FROM music_view ORDER BY no ASC)) "
					+ "WHERE num BETWEEN ? AND ?";
			//����Ŭ => �ζ��κ�
			//�ڹ� => while => ��ü 200������ 20���� ���� => �ӵ� ������
			int rowSize=20;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			// ?�� �� ä���
			ps=conn.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);
			//������ ������
			//������ �б�
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
	//��������
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
	//5. SQL������ �ܼ�ȭ => ���պ� (����) => ���Ϲ���(������X), ���
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
