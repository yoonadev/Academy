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
		//��µ� HTML�� ���������� �о� ����
		//�������� HTML���� ����
		response.setContentType("text/html;charset=UTF-8");
		//���������� HTML�� �д� ��ġ�� ����
		PrintWriter out=response.getWriter();
		//������ �б�
		GenieDAO dao=new GenieDAO();
		//1.����ڰ� ��û�� ������ �޾ƿ��� (getParameter()�� String�� ������ �� ����)
		// MusicList?page=1
		String strPage=request.getParameter("page");
		//ó�� ����� ������ ���� ���� => null
		if(strPage==null)
			strPage="1";	//default
		int curpage=Integer.parseInt(strPage);
		int totalpage=dao.genieTotalPage();
		ArrayList<GenieVO> list=dao.genieListData(curpage);
		
		out.println("<html>");
		out.println("<head>");
		/*CSS / JavaScript ���
		CSS
			= �ζ��� CSS => <img style="color:orange">
			= ���� CSS => <style type="text/css">
						 row{
						 }
						 </style>
			= �ܺ� CSS => ������ ����� <link href="���ϸ�(����,����)">
		JavaScript
			<script type="txt/javascript"> ES5.0 => jquery,ajax
			<script type="txt/babel"> ES6.0 => react,vue
		 */
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style>.row{margin:0px auto;width:1000px}</style>");
		/*
			style => selector
				�±׸� {
					��Ÿ�� ����
				}
				��)
					body{
						background-color:red
						================
						�Ӽ����� CSS�� �����������
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
		out.println("<div style=\"height:50px\">");	//html�� �� ÷���� �� ����X => ""
		out.println("<div class=container>");
		out.println("<div class=row>");
		//Ÿ��Ʋ
		out.println("<h1 class=text-center>���Ϲ��� Top200</h1>");
		out.println("<table class=table>");
		out.println("<tr class=warning>");
		out.println("<th class=text-center>����</th>");
		out.println("<th class=text-center></th>");
		out.println("<th class=text-center>���</th>");
		out.println("<th class=text-center>������</th>");
		out.println("<th class=text-center>�ٹ�</th>");
		out.println("</tr>");
		//������
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
		//��������ư
		out.println("<table class=table>");
		out.println("<tr>");
		out.println("<td class=text-center>");
		out.println("<a href=MusicList?page="+(curpage>1?curpage-1:curpage)+" class=\"btn btn-sm btn-danger\">����</a>");
		out.println(curpage+" page / "+totalpage+" pages ");
		out.println("<a href=MusicList?page="+(curpage<totalpage?curpage+1:curpage)+" class=\"btn btn-sm btn-success\">����</a>");
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
