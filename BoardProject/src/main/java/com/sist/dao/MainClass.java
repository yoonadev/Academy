package com.sist.dao;
import java.util.*;
public class MainClass {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.print("������ �Է�:");
		int page=sc.nextInt();
		//�����ͺ��̽� �б�
		BoardDAO dao=new BoardDAO();
		ArrayList<BoardVO> list=dao.boardListData(page);
		for(BoardVO vo:list) {
			System.out.println(vo.getNo());
		}
	}

}
