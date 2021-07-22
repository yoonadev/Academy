package com.sist.main;

import java.io.IOException;
import java.io.PrintWriter;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardDetail")
public class BoardDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.�����ϴ� ���� => ������ (HTML/XML)
		response.setContentType("text/html;charset=UTF-8");
		//2.HTML�� ������ �غ� => ��û�� ������� ����
		PrintWriter out=response.getWriter();	//out�̶�� �޸� ������ HTML��� => ���������� �о
		
		//1.������ �ޱ� BoardDetail?no=1
		String no=request.getParameter("no");
		BoardDAO dao=new BoardDAO();
		BoardVO vo=dao.boardDetail(Integer.parseInt(no));
		//�󼼺��� �����͸� ����Ŭ�κ��� ������ �´�
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>row{margin:0px auto;width:500px}</style>"); // ȭ�� ��� ���� 
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<h1 class=text-center>���뺸��</h1>");
		out.println("<table class=table>");
		
		out.println("<tr>");
		out.println("<th width=20% class=text-center>��ȣ</th>");
		out.println("<td width=30% class=text-center>"+vo.getNo()+"</td>");
		out.println("<th width=20% class=text-center>�ۼ���</th>");
		out.println("<td width=30% class=text-center>"+vo.getRegdate().toString()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20% class=text-center>�̸�</th>");
		out.println("<td width=30% class=text-center>"+vo.getName()+"</td>");
		out.println("<th width=20% class=text-center>��ȸ��</th>");
		out.println("<td width=30% class=text-center>"+vo.getHit()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20% class=text-center>����</th>");
		out.println("<td width=30% colspan=3>"+vo.getSubject()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td height=200 valign=top colspan=4>"+vo.getContent()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=4 class=text-right>");
		out.println("<a href=# class=\"btn btn-xs btn-success\">����</a>");
		out.println("<a href=BoardDelete?no="+vo.getNo()+" class=\"btn btn-xs btn-danger\">����</a>");
		out.println("<a href=BoardList class=\"btn btn-xs btn-warning\">���</a>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
