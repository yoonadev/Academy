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
	Servlet (20210720선생님 파일 보기)
 */
/*
	중요사항
		=> 자바
			클래스 <=====> 클래스
				   메소드 (매개변수로 데이터 전송)
		=> 웹은 데이터를 전송할 때 URL을 이용한다 => 끝에 ?
			/BoardList?page=1 => 매개변수 전송
*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request => 사용자 요청을 받는 경우에 사용
		//response => 사용자 요청 처리 후에 응답(브라우저로 HTML,Cookie전송)
		//1.전송하는 형식 => 브라우저 (HTML/XML)
		response.setContentType("text/html;charset=UTF-8");
		//2.HTML을 전송할 준비 => 요청한 사람에게 전송
		PrintWriter out=response.getWriter();	//out이라는 메모리 공간에 HTML출력 => 브라우저에서 읽어감
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>row{margin:0px auto;width:800px}</style>"); // 화면 가운데 정렬 
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<div class=row>");
		out.println("<h1 class=text-center>자유게시판</h1>");
		out.println("<div style=\"height:30px\"></div>");
		out.println("<table class=table>");
		out.println("<tr>");	//row => 한줄만들기
		out.println("<td>");	//data => 데이터 출력 //제목은(컬럼명) th, 실제데이터는 td
		out.println("<a href=BoardInsert class=\"btn btn-sm btn-info\">새글</a>");	//a태그 => 링크
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<table class=table>");
		out.println("<tr class=success>");	//한줄만들때 tr
		out.println("<th width=10% class=text-center>번호</th>");
		out.println("<th width=45% class=text-center>제목</th>");
		out.println("<th width=15% class=text-center>이름</th>");
		out.println("<th width=20% class=text-center>작성일</th>");
		out.println("<th width=10% class=text-center>조회수</th>");
		out.println("</tr>");
		//데이터출력
		//http://localhost:8080/BoardProject/BoardList?page=2
		BoardDAO dao=new BoardDAO();
		//1.page를 받아본다
		String strPage=request.getParameter("page");	//url을 통해서 페이지 요청할 때 받아오는
		//첫페이지는 default
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);	//현재 보고있는 페이지************
		int totalpage=dao.boardTotalPage();		//총페이지************
		ArrayList<BoardVO> list=dao.boardListData(curpage);
		for(BoardVO vo:list) {
			out.println("<tr class>");
			out.println("<th width=10% class=text-center>"+vo.getNo()+"</th>");
			out.println("<th width=45%><a href=BoardDetail?no="+vo.getNo()+">"+vo.getSubject()+"</a></th>");	//글자수 일정하지 않은 경우 왼쪽 정렬
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
		out.println("<select name=fs class input-sm>");	//콤보박스
		out.println("<option value=name>이름</option>");
		out.println("<option value=subject>제목</option>");
		out.println("<option value=content>내용</option>");
		out.println("</select>");
		out.println("<input type=text name=ss size=10 class=input-sm>");
		out.println("<input type=submit value=검색 class=\"btn btn-sm btn-success\">");
		out.println("</td>");
		out.println("<td class=text-right>");
		out.println("<a href=BoardList?page="+(curpage>1?curpage-1:curpage)+" class=\"btn btn-sm btn-primary\">이전</a>");
		out.println(curpage+" page / "+totalpage+" pages ");
		out.println("<a href=BoardList?page="+(curpage<totalpage?curpage+1:curpage)+" class=\"btn btn-sm btn-primary\">다음</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
