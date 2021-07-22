package com.sist.dao;
import java.util.*;
public class MainClass {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.print("페이지 입력:");
		int page=sc.nextInt();
		//데이터베이스 읽기
		BoardDAO dao=new BoardDAO();
		ArrayList<BoardVO> list=dao.boardListData(page);
		for(BoardVO vo:list) {
			System.out.println(vo.getNo());
		}
	}

}
