package com.sist.main;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;
@WebServlet("/MusicList")
public class MusicList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//출력된 HTML만 브라우저에서 읽어 간다
		//브라우저에 HTML전송 설정
		response.setContentType("text/html;charset=UTF-8");
		//브라우저에서 HTML을 읽는 위치를 지정
		PrintWriter out=response.getWriter();
		//데이터 읽기
		GenieDAO dao=new GenieDAO();
		//1.사용자가 요청한 페이지 받아오기 (getParameter()는 String만 가져올 수 있음)
		// MusicList?page=1
		String strPage=request.getParameter("page");
		//처음 실행시 페이지 받지 못함 => null
		if(strPage==null)
			strPage="1";	//default
		int curpage=Integer.parseInt(strPage);
		int totalpage=dao.genieTotalPage();
		ArrayList<GenieVO> list=dao.genieListData(curpage);
		
		out.println("<html>");
		out.println("<head>");
		/*CSS / JavaScript 등록
		CSS
			= 인라인 CSS => <img style="color:orange">
			= 내부 CSS => <style type="text/css">
						 row{
						 }
						 </style>
			= 외부 CSS => 파일을 만든다 <link href="파일명(내부,원격)">
		JavaScript
			<script type="txt/javascript"> ES5.0 => jquery,ajax
			<script type="txt/babel"> ES6.0 => react,vue
		 */
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>.row{margin:0px auto;width:1000px}</style>");
		/*
			style => selector
				태그명 {
					스타일 지정
				}
				예)
					body{
						background-color:red
						================
						속성명이 CSS에 만들어져있음
					}
					<a class="aaa">
					.aaa{
					}
					<a id="bbb">
					#bbb{
					}
		 */
		out.println("</head>");
		out.println("<body>");
		out.println("<div style=\"height:50px\">");	//html은 값 첨부할 때 공백X => ""
		out.println("<div class=container>");
		out.println("<div class=row>");
		//타이틀
		out.println("<h1 class=text-center>지니뮤직 Top200</h1>");
		out.println("<table class=table>");
		out.println("<tr class=warning>");
		out.println("<th class=text-center>순위</th>");
		out.println("<th class=text-center></th>");
		out.println("<th class=text-center>곡명</th>");
		out.println("<th class=text-center>가수명</th>");
		out.println("<th class=text-center>앨범</th>");
		out.println("</tr>");
		//데이터
		for(GenieVO vo:list) {
			out.println("<tr>");
			out.println("<td class=text-center>"+vo.getNo()+"</td>");
			out.println("<td class=text-center><img src="+vo.getPoster()+" width=30 height=30></td>");
			out.println("<td><a href=GenieDetail?no="+vo.getNo()+">"+vo.getTitle()+"</a></td>");
			out.println("<td>"+vo.getSinger()+"</td>");
			out.println("<td>"+vo.getAlbum()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		//페이지버튼
		out.println("<table class=table>");
		out.println("<tr>");
		out.println("<td class=text-center>");
		out.println("<a href=MusicList?page="+(curpage>1?curpage-1:curpage)+" class=\"btn btn-sm btn-danger\">이전</a>");
		out.println(curpage+" page / "+totalpage+" pages ");
		out.println("<a href=MusicList?page="+(curpage<totalpage?curpage+1:curpage)+" class=\"btn btn-sm btn-success\">다음</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=text-center>");
		for(int i=1;i<=totalpage;i++) {
		out.println("<a href=MusicList?page="+i+">"+i+"</a>");
		}
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		//
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
