package main;

import java.io.*;
import java.util.ArrayList;

import dao.FoodCategoryVO;
import dao.FoodDAO;
import dao.FoodHouseVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FoodList")
public class FoodList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.���۹��
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		//���� => CSS/JavaScript
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style type=tex/css>row{margin:0px auto;width:800px}</style>"); // ȭ�� ��� ���� 
		out.println("</head>");
		//�����͸� ������ �´�(����Ŭ)
		FoodDAO dao=new FoodDAO();
		String cno=request.getParameter("cno");
		FoodCategoryVO vo=dao.foodCategoryInfoData(Integer.parseInt(cno));
		ArrayList<FoodHouseVO> list=dao.foodList(Integer.parseInt(cno));
		out.println("<div class=container>");
		out.println("<div class=jumbotron>");
		out.println("<h1 class=text-center>"+vo.getTitle()+"</h1>");
		out.println("<h3 class=text-center>"+vo.getSubject()+"</h3>");
		out.println("</div>");
		out.println("<div class=row>");
		for(FoodHouseVO fvo:list) {
			out.println("<table class=table>");
			out.println("<tr>");
			out.println("<td width=30% class=text-center rowspan=3>");
			out.println("<a href=FoodDetail?no="+fvo.getNo()+">");
			out.println("<img src="+fvo.getPoster()+" width=300 height=150 class=img-rounded>");
			out.println("</a>");
			out.println("</td>");
			out.println("<td width=70%>");
			out.println("<a href=FoodDetail?no="+fvo.getNo()+">");
			out.println("<h3>"+fvo.getName()+"</a> <span style=\"color:orange\">"+fvo.getScore()+"</span></h3>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr height=40>");
			out.println("<td width=70%>");
			out.println(fvo.getAddress());
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr height=40>");
			out.println("<td width=70%>");
			out.println(fvo.getTel());
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
