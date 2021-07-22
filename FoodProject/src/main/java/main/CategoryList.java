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
	//service(통합) => doGet() + doPost()
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.브라우저로 보내는 방법 (html => text/html, xml => text/xml, wml(무선:kotlin) => text/plain)
		response.setContentType("text/html;charset=UTF-8");	//ASC(브라우저), Unicode(자바,한글)
		//2.메모리에 출력 => HTML만 출력한다 => 브라우저가 읽어가는 메모리 공간 확보
		PrintWriter out=response.getWriter();
		//3.메모리 공간에 HTML출력
		out.println("<html>");
		out.println("<head>");
		//선언 => CSS/JavaScript
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style type=tex/css>row{margin:0px auto;width:800px}</style>"); // 화면 가운데 정렬 
		out.println("</head>");
		//데이터를 가지고 온다(오라클)
		FoodDAO dao=new FoodDAO();
		ArrayList<FoodCategoryVO> list=dao.foodCategoryListData();
		out.println("<body>");
		//브라우저 화면 출력 부분
		//cno = 1~12 (12)
		//cno = 13~18 (6)
		//cno = 19~30 (12)
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<h3 style=\"color:orange\">믿고 보는 맛집 리스트</h3>");
		out.println("<hr>");
		for(int i=0;i<12;i++) {
			FoodCategoryVO vo=list.get(i);
			out.println("<div class=\"col-md-3\">");	// => 한 줄에 3개씩 12가 되면 다음줄 출력
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
		
		out.println("<h3 style=\"color:orange\">지역별 인기 맛집</h3>");
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
		
		out.println("<h3 style=\"color:orange\">메뉴별 인기 맛집</h3>");
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
		/*HTML 태그
		1.이미지 <img src="경로명" width="" height="">
		2.링크 <a href="링크">
		3.제목 <h1>~<h6>
		4.입력창 <input type="">
							==
							text => 한 줄 문자열
							password
							radio
							checkbox
							file
							button
							submit
							reset
				<select> : 콤보박스
				<textarea>
		5.화면분할 : <div> <span>
		6.목록 : <table> <ul> <ol> <dl>
		7.다음줄 <br>, 단락 <p>, 전송 <form>
	 */
	}
}
