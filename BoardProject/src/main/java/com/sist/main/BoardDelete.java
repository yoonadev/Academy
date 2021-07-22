package com.sist.main;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.sist.dao.*;
@WebServlet("/BoardDelete")
public class BoardDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.클라이언트가 보내준 데이터 받기 => ?no=
		String no=request.getParameter("no");
		//2.DAO연동
		BoardDAO dao=new BoardDAO();
		dao.boardDelete(Integer.parseInt(no));
		//3.화면이동
		response.sendRedirect("BoardList");
	}

}
