package com.sist.main;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;

@WebServlet("/DaumMovieList")
public class DaumMovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Servlet => ������ �����ϴ� �ڹ� ���� (�� ������ �ʿ��� ���, �ҽ��� �������� �ʴ� ���)
		//JSP => ������ ����Ǵ� ���� (����X, ��ü �ҽ��� ������) => View
		//Spring => ������ ���̺귯�� => Servlet(�ҽ� �������� �ʴ´�)
		/*
			request : ����ڰ� ������ ������ ����� ������ ����
						?no=1 ==> request�� ��� ���´� (��Ĺ�� ���� ó��)
			response : ���� (�����=> html, cookie) 
			session : ������ ������� �Ϻ� ������ ���� => id, name.. (����ڰ� ���������� ����)
			cookie : �ÿ��� ��ǻ�Ϳ� ���� => �ֱ� �湮..
		 */
		//1. ���� ��Ÿ�� (text/html, text/xml, text/plain(json))
		response.setContentType("text/html;charset=UTF-8");
		//�޸𸮿� HTML ���� => �������� �о
		PrintWriter out=response.getWriter();	//response���� ������ ������ ������� IP => ����� ���� ���� ������(������)
		String strCno=request.getParameter("cno");
		if(strCno==null)
			strCno="1";	//default
		MovieDAO dao=new MovieDAO();
		ArrayList<DaumMovieVO> list=dao.daumMovieListData(Integer.parseInt(strCno));
		//HTML�� ��� => �ش� ����ڰ� �о� ����
		out.println("<html>");	//ML <a>, </a>, <br/> => XML(������,���̹�Ƽ������ ���, �±װ� �����������X),WML,TDML...
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>.row{margin:0px auto;width:1200px}</style>");	//row�� �����Ϸ��� Ŭ�������̱� ������ . ������ ��
		//						================ ��� ����
		out.println("</head>");
		out.println("<body>");
		out.println("<div style=\"height:50px\"></div>");
		out.println("<div class=container>");	//container/wrap
		out.println("<div class=row>");
		//�޴�
		out.println("<table>");
		out.println("<th width=75%>");
		out.println("<a href=DaumMovieList?cno=1 class=\"btn btn-sm btn-danger\">��ȭ����</a>");
		out.println("<a href=DaumMovieList?cno=2 class=\"btn btn-sm btn-success\">�ڽ����ǽ�</a>");
		out.println("<a href=DaumMovieList?cno=5 class=\"btn btn-sm btn-info\">OTT</a>");
		out.println("<a href=DaumMovieList?cno=6 class=\"btn btn-sm btn-warning\">Netflix</a>");
		out.println("<a href=DaumMovieList?cno=7 class=\"btn btn-sm btn-primary\">Watcha</a>");
		out.println("<a href=DaumMovieList?cno=8 class=\"btn btn-sm btn-default\">Kakaopage</a>");
		out.println("</th>");
		//�˻�â
		out.println("<th width=25%>");
		out.println("<form action=DaumMovieFind class=text-right>");
		out.println("<select name=fs class=input-sm>");
		out.println("<option value=title>��ȭ��</option>");
		out.println("<option value=genre>�帣</option>");
		out.println("</select>");
		out.println("<input type=text name=ss class=input-sm size=15>");
		out.println("<input type=submit value=�˻� class=\"btn btn-danger btn-sm\">");
		out.println("</form>");
		out.println("</th>");
		out.println("</table>");
		//
		out.println("</div>");
		out.println("<div style=\"height:30px\"></div>");
		out.println("<div class=row>");
		//��ȭ ���
		for(DaumMovieVO vo:list) {
			out.println("<div class=col-sm-3>");	//12�� �� ��
			out.println("<img src="+vo.getPoster()+" width=100% title=\""+vo.getTitle()+"\">");
			out.println("</div>");
		}
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
