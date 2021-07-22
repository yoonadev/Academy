package main;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.*;

@WebServlet("/FoodDetail")
public class FoodDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//HTML ���
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		//������ �ޱ�
		String no=request.getParameter("no");
		FoodDAO dao=new FoodDAO();
		FoodHouseVO vo=dao.foodDetail(Integer.parseInt(no));
		
		//vo�� �ִ� ������ ���
		out.println("<html>");
		out.println("<head>");
		//���� => CSS/JavaScript
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style type=tex/css>row{margin:0px auto;width:800px}</style>"); // ȭ�� ��� ���� 
		out.println("</head>");
		out.println("<div style=\"height:30px\"></div>");
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<table class=table>");
		out.println("<tr>");
		StringTokenizer st=new StringTokenizer(vo.getPoster(),"^");
		while(st.hasMoreTokens()) {
			out.println("<td>");
			out.println("<img src="+st.nextToken()+" width=100%>");
			out.println("</td>");
		}
		out.println("</tr>");
		out.println("</table>");
		out.println("<table class=table>");
		out.println("<tr>");
		out.println("<td colspan=2><h3>"+vo.getName()+" <span style=\"color:orange\">"+vo.getScore()+"</span></h3>");
		out.println("</tr>");
		//�ּ�
		out.println("<tr>");
		out.println("<th width=20% class=text-right>�ּ�</th>");
		out.println("<td width=80%>"+vo.getAddress()+"</td>");
		out.println("</tr>");
		//��ȭ
		out.println("<tr>");
		out.println("<th width=20% class=text-right>��ȭ</th>");
		out.println("<td width=80%>"+vo.getTel()+"</td>");
		out.println("</tr>");
		//��������
		out.println("<tr>");
		out.println("<th width=20% class=text-right>��������</th>");
		out.println("<td width=80%>"+vo.getType()+"</td>");
		out.println("</tr>");
		//���ݴ�
		out.println("<tr>");
		out.println("<th width=20% class=text-right>���ݴ�</th>");
		out.println("<td width=80%>"+vo.getPrice()+"</td>");
		out.println("</tr>");
		//����
		out.println("<tr>");
		out.println("<th width=20% class=text-right>����</th>");
		out.println("<td width=80%>"+vo.getParking()+"</td>");
		out.println("</tr>");
		//�����ð�
		out.println("<tr>");
		out.println("<th width=20% class=text-right>�����ð�</th>");
		out.println("<td width=80%>"+vo.getTime()+"</td>");
		out.println("</tr>");
		//�޴�
		out.println("<tr>");
		out.println("<th width=20% class=text-right>�޴�</th>");
		out.println("<td width=80%>");
		if(!vo.getMenu().equals("no")) {
			out.println("<ul>");
			st=new StringTokenizer(vo.getMenu(),"��");
			while(st.hasMoreTokens()) {
				out.println("<li>"+st.nextToken()+"��</li>");
			}
			out.println("</ul>");
		}
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
