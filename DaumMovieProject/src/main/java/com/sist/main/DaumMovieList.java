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
		//Servlet => 웹에서 실행하는 자바 파일 (웹 보안이 필요한 경우, 소스를 공개하지 않는 경우)
		//JSP => 웹에서 실행되는 파일 (보안X, 전체 소스가 공개됨) => View
		//Spring => 웹관련 라이브러리 => Servlet(소스 공개하지 않는다)
		/*
			request : 사용자가 전송한 데이터 목록을 가지고 있음
						?no=1 ==> request에 묶어서 들어온다 (톰캣에 의해 처리)
			response : 응답 (사용자=> html, cookie) 
			session : 서버에 사용자의 일부 정보를 저장 => id, name.. (사용자가 빠져나가면 해제)
			cookie : 시용자 컴퓨터에 저장 => 최근 방문..
		 */
		//1. 응답 스타일 (text/html, text/xml, text/plain(json))
		response.setContentType("text/html;charset=UTF-8");
		//메모리에 HTML 저장 => 브라우저가 읽어감
		PrintWriter out=response.getWriter();	//response에는 서버에 접속한 사용자의 IP => 사용자 개별 서버 생성됨(쓰레드)
		String strCno=request.getParameter("cno");
		if(strCno==null)
			strCno="1";	//default
		MovieDAO dao=new MovieDAO();
		ArrayList<DaumMovieVO> list=dao.daumMovieListData(Integer.parseInt(strCno));
		//HTML을 출력 => 해당 사용자가 읽어 간다
		out.println("<html>");	//ML <a>, </a>, <br/> => XML(스프링,마이바티스에서 사용, 태그가 만들어져있지X),WML,TDML...
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>.row{margin:0px auto;width:1200px}</style>");	//row를 제어하려면 클래스명이기 때문에 . 찍어줘야 함
		//						================ 가운데 정렬
		out.println("</head>");
		out.println("<body>");
		out.println("<div style=\"height:50px\"></div>");
		out.println("<div class=container>");	//container/wrap
		out.println("<div class=row>");
		//메뉴
		out.println("<table>");
		out.println("<th width=75%>");
		out.println("<a href=DaumMovieList?cno=1 class=\"btn btn-sm btn-danger\">영화순위</a>");
		out.println("<a href=DaumMovieList?cno=2 class=\"btn btn-sm btn-success\">박스오피스</a>");
		out.println("<a href=DaumMovieList?cno=5 class=\"btn btn-sm btn-info\">OTT</a>");
		out.println("<a href=DaumMovieList?cno=6 class=\"btn btn-sm btn-warning\">Netflix</a>");
		out.println("<a href=DaumMovieList?cno=7 class=\"btn btn-sm btn-primary\">Watcha</a>");
		out.println("<a href=DaumMovieList?cno=8 class=\"btn btn-sm btn-default\">Kakaopage</a>");
		out.println("</th>");
		//검색창
		out.println("<th width=25%>");
		out.println("<form action=DaumMovieFind class=text-right>");
		out.println("<select name=fs class=input-sm>");
		out.println("<option value=title>영화명</option>");
		out.println("<option value=genre>장르</option>");
		out.println("</select>");
		out.println("<input type=text name=ss class=input-sm size=15>");
		out.println("<input type=submit value=검색 class=\"btn btn-danger btn-sm\">");
		out.println("</form>");
		out.println("</th>");
		out.println("</table>");
		//
		out.println("</div>");
		out.println("<div style=\"height:30px\"></div>");
		out.println("<div class=row>");
		//영화 목록
		for(DaumMovieVO vo:list) {
			out.println("<div class=col-sm-3>");	//12가 한 줄
			out.println("<img src="+vo.getPoster()+" width=100% title=\""+vo.getTitle()+"\">");
			out.println("</div>");
		}
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
