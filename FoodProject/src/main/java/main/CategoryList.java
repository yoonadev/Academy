package main;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import dao.*;

@WebServlet("/CategoryList")
public class CategoryList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//service(����) => doGet() + doPost()
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.�������� ������ ��� (html => text/html, xml => text/xml, wml(����:kotlin) => text/plain)
		response.setContentType("text/html;charset=UTF-8");	//ASC(������), Unicode(�ڹ�,�ѱ�)
		//2.�޸𸮿� ��� => HTML�� ����Ѵ� => �������� �о�� �޸� ���� Ȯ��
		PrintWriter out=response.getWriter();
		//3.�޸� ������ HTML���
		out.println("<html>");
		out.println("<head>");
		//���� => CSS/JavaScript
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style type=tex/css>row{margin:0px auto;width:800px}</style>"); // ȭ�� ��� ���� 
		out.println("</head>");
		//�����͸� ������ �´�(����Ŭ)
		FoodDAO dao=new FoodDAO();
		ArrayList<FoodCategoryVO> list=dao.foodCategoryListData();
		out.println("<body>");
		//������ ȭ�� ��� �κ�
		//cno = 1~12 (12)
		//cno = 13~18 (6)
		//cno = 19~30 (12)
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<h3 style=\"color:orange\">�ϰ� ���� ���� ����Ʈ</h3>");
		out.println("<hr>");
		for(int i=0;i<12;i++) {
			FoodCategoryVO vo=list.get(i);
			out.println("<div class=\"col-md-3\">");	// => �� �ٿ� 3���� 12�� �Ǹ� ������ ���
			out.println("<div class=\"thumbnail\">");
			out.println("<a href=\"FoodList?cno="+vo.getCno()+"\">");
			out.println("<img src=\""+vo.getPoster()+"\" title="+vo.getSubject()+" style=\"width:100%\">");
			out.println("<div class=\"caption\">");
			out.println("<p>"+vo.getTitle()+"</p>");
			out.println("</div>");
			out.println("</a>");
			out.println("</div>");
			out.println("</div>");
		}
		
		out.println("<h3 style=\"color:orange\">������ �α� ����</h3>");
		out.println("<hr>");
		for(int i=12;i<18;i++) {
			FoodCategoryVO vo=list.get(i);
			out.println("<div class=\"col-md-4\">");
			out.println("<div class=\"thumbnail\">");
			out.println("<a href=\"FoodList?cno="+vo.getCno()+"\">");
			out.println("<img src=\""+vo.getPoster()+"\" title="+vo.getSubject()+" style=\"width:100%\">");
			out.println("<div class=\"caption\">");
			out.println("<p>"+vo.getTitle()+"</p>");
			out.println("</div>");
			out.println("</a>");
			out.println("</div>");
			out.println("</div>");
		}
		
		out.println("<h3 style=\"color:orange\">�޴��� �α� ����</h3>");
		out.println("<hr>");
		for(int i=18;i<30;i++) {
			FoodCategoryVO vo=list.get(i);
			out.println("<div class=\"col-md-3\">");
			out.println("<div class=\"thumbnail\">");
			out.println("<a href=\"FoodList?cno="+vo.getCno()+"\">");
			out.println("<img src=\""+vo.getPoster()+"\" title="+vo.getSubject()+" style=\"width:100%\">");
			out.println("<div class=\"caption\">");
			out.println("<p>"+vo.getTitle()+"</p>");
			out.println("</div>");
			out.println("</a>");
			out.println("</div>");
			out.println("</div>");
		}
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		/*HTML �±�
		1.�̹��� <img src="��θ�" width="" height="">
		2.��ũ <a href="��ũ">
		3.���� <h1>~<h6>
		4.�Է�â <input type="">
							==
							text => �� �� ���ڿ�
							password
							radio
							checkbox
							file
							button
							submit
							reset
				<select> : �޺��ڽ�
				<textarea>
		5.ȭ����� : <div> <span>
		6.��� : <table> <ul> <ol> <dl>
		7.������ <br>, �ܶ� <p>, ���� <form>
	 */
	}
}
