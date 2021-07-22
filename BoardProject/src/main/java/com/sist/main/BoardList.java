package com.sist.main;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import com.sist.dao.*;

@WebServlet("/BoardList")
public class BoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;
/*
	Servlet (20210720������ ���� ����)
 */
/*
	�߿����
		=> �ڹ�
			Ŭ���� <=====> Ŭ����
				   �޼ҵ� (�Ű������� ������ ����)
		=> ���� �����͸� ������ �� URL�� �̿��Ѵ� => ���� ?
			/BoardList?page=1 => �Ű����� ����
*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request => ����� ��û�� �޴� ��쿡 ���
		//response => ����� ��û ó�� �Ŀ� ����(�������� HTML,Cookie����)
		//1.�����ϴ� ���� => ������ (HTML/XML)
		response.setContentType("text/html;charset=UTF-8");
		//2.HTML�� ������ �غ� => ��û�� ������� ����
		PrintWriter out=response.getWriter();	//out�̶�� �޸� ������ HTML��� => ���������� �о
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>row{margin:0px auto;width:800px}</style>"); // ȭ�� ��� ���� 
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<h1 class=text-center>�����Խ���</h1>");
		out.println("<div style=\"height:30px\"></div>");
		out.println("<table class=table>");
		out.println("<tr>");	//row => ���ٸ����
		out.println("<td>");	//data => ������ ��� //������(�÷���) th, ���������ʹ� td
		out.println("<a href=BoardInsert class=\"btn btn-sm btn-info\">����</a>");	//a�±� => ��ũ
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<table class=table>");
		out.println("<tr class=success>");	//���ٸ��鶧 tr
		out.println("<th width=10% class=text-center>��ȣ</th>");
		out.println("<th width=45% class=text-center>����</th>");
		out.println("<th width=15% class=text-center>�̸�</th>");
		out.println("<th width=20% class=text-center>�ۼ���</th>");
		out.println("<th width=10% class=text-center>��ȸ��</th>");
		out.println("</tr>");
		//���������
		//http://localhost:8080/BoardProject/BoardList?page=2
		BoardDAO dao=new BoardDAO();
		//1.page�� �޾ƺ���
		String strPage=request.getParameter("page");	//url�� ���ؼ� ������ ��û�� �� �޾ƿ���
		//ù�������� default
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);	//���� �����ִ� ������************
		int totalpage=dao.boardTotalPage();		//��������************
		ArrayList<BoardVO> list=dao.boardListData(curpage);
		for(BoardVO vo:list) {
			out.println("<tr class>");
			out.println("<th width=10% class=text-center>"+vo.getNo()+"</th>");
			out.println("<th width=45%><a href=BoardDetail?no="+vo.getNo()+">"+vo.getSubject()+"</a></th>");	//���ڼ� �������� ���� ��� ���� ����
			out.println("<th width=15% class=text-center>"+vo.getName()+"</th>");
			out.println("<th width=20% class=text-center>"+vo.getRegdate()+"</th>");
			out.println("<th width=10% class=text-center>"+vo.getHit()+"</th>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("<table class=table>");
		out.println("<tr>");
		out.println("<td class=text-left>");
		out.println("Search:");
		out.println("<select name=fs class input-sm>");	//�޺��ڽ�
		out.println("<option value=name>�̸�</option>");
		out.println("<option value=subject>����</option>");
		out.println("<option value=content>����</option>");
		out.println("</select>");
		out.println("<input type=text name=ss size=10 class=input-sm>");
		out.println("<input type=submit value=�˻� class=\"btn btn-sm btn-success\">");
		out.println("</td>");
		out.println("<td class=text-right>");
		out.println("<a href=BoardList?page="+(curpage>1?curpage-1:curpage)+" class=\"btn btn-sm btn-primary\">����</a>");
		out.println(curpage+" page / "+totalpage+" pages ");
		out.println("<a href=BoardList?page="+(curpage<totalpage?curpage+1:curpage)+" class=\"btn btn-sm btn-primary\">����</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
