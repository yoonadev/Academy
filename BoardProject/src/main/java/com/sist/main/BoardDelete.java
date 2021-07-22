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
		//1.Ŭ���̾�Ʈ�� ������ ������ �ޱ� => ?no=
		String no=request.getParameter("no");
		//2.DAO����
		BoardDAO dao=new BoardDAO();
		dao.boardDelete(Integer.parseInt(no));
		//3.ȭ���̵�
		response.sendRedirect("BoardList");
	}

}
