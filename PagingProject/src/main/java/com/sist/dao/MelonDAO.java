package com.sist.dao;
import java.util.*;
import java.sql.*;
public class MelonDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.219.103:1521:XE";
	public MelonDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception e) {}
	}
	public void disConnection() {
		try {
			if(ps!=null)	ps.close();	
			if(conn!=null)	conn.close();
		}catch (Exception e) {}
	}
	//페이징
	/*
	NO     NOT NULL NUMBER         
	POSTER          VARCHAR2(1000) 
	TITLE           VARCHAR2(200)  
	SINGER          VARCHAR2(200)  
	ALBUM           VARCHAR2(200)  
	HEART           VARCHAR2(10)   
	KEY             VARCHAR2(100)
 */
	//페이지 나누기
	public ArrayList<MelonVO> melonListData(int page){
		ArrayList<MelonVO> list=new ArrayList<MelonVO>();
		try {
			getConnection();
			String sql="SELECT no,poster,title,singer,album,heart,key,num "
					+ "FROM (SELECT no,poster,title,singer,album,heart,key,rownum as num "
					+ "FROM (SELECT no,poster,title,singer,album,heart,key "
					+ "FROM melon_music ORDER BY no ASC)) "
					+ "WHERE num BETWEEN ? AND ?";
			int rowSize=20;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			ps=conn.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				MelonVO vo=new MelonVO();
				vo.setNo(rs.getInt(1));
				vo.setPoster(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setSinger(rs.getString(4));
				vo.setAlbum(rs.getString(5));
				vo.setHeart(rs.getString(6));
				vo.setKey(rs.getString(7));
				list.add(vo);
			}
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
	//총페이지
	public int melonTotalPage() {
		int total=0;
		try {
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/20.0) FROM melon_music";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	public MelonVO melonDetailData(int no) {
		MelonVO vo=new MelonVO();
		try {
			getConnection();
			String sql="SELECT no,title,key "
					+ "FROM melon_music "
					+ "WHERE no="+no;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setTitle(rs.getString(2));
			vo.setKey(rs.getString(3));
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
}
