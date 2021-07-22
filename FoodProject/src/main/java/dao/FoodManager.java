package dao;
//Jsoup => 웹에서 데이터 읽기
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FoodManager {
	public static void main(String[] args) {
		FoodManager fm=new FoodManager();
		//fm.foodGetData();
		//fm.foodHouseAllData();
	}
	public void foodGetData() {
		FoodDAO dao=new FoodDAO();
		try {
			Document doc=Jsoup.connect("https://www.mangoplate.com/").get();
			Elements title=doc.select("ul.list-toplist-slider span.title");
			Elements subject=doc.select("ul.list-toplist-slider p.desc");
			Elements poseter=doc.select("ul.list-toplist-slider img");
			Elements link=doc.select("ul.list-toplist-slider a");
			for(int i=0;i<title.size();i++) {
				System.out.println(title.get(i).text());
				System.out.println(subject.get(i).text());
				System.out.println(poseter.get(i).attr("data-lazy"));
				System.out.println(link.get(i).attr("href"));
				System.out.println("===================================");
				FoodCategoryVO vo=new FoodCategoryVO();
				vo.setTitle(title.get(i).text());
				vo.setSubject(subject.get(i).text());
				vo.setPoster(poseter.get(i).attr("data-lazy"));
				vo.setLink(link.get(i).attr("href"));
				dao.foodCategoryInsert(vo);
			}
		}catch(Exception ex) {}
	}
	public void foodHouseAllData() {
		try {
			FoodDAO dao=new FoodDAO();
			ArrayList<FoodCategoryVO> list=dao.foodCategoryListData();
			for(FoodCategoryVO vo:list) {
				/*System.out.println("카테고리 번호:"+vo.getCno());
				System.out.println("제목:"+vo.getTitle());
				System.out.println("부제목:"+vo.getSubject());
				System.out.println("이미지:"+vo.getPoster());
				System.out.println("링크:"+vo.getLink());
				System.out.println("====================================================");*/
				Document doc=Jsoup.connect(vo.getLink()).get();
				Elements link=doc.select("div.info span.title a");
				for(int i=0;i<link.size();i++) {
					//System.out.println(link.get(i).attr("href"));
					Document doc2=Jsoup.connect("https://www.mangoplate.com/"+link.get(i).attr("href")).get();
					Element title=doc2.selectFirst("span.title h1.restaurant_name");
					Element score=doc2.selectFirst("strong.rate-point span");
					//포스터
					Elements poster=doc2.select("figure.restaurant-photos-item img.center-croping");
					String image="";
					for(int j=0;j<poster.size();j++) {
						image+=poster.get(j).attr("src")+"^";	//구분자로 ^씀
					}
					image=image.substring(0,image.lastIndexOf("^"));	//마지막 ^를 제거
					image=image.replace("&", "#");
					StringTokenizer st=new StringTokenizer(image,"^");
					int k=1;
					while(st.hasMoreTokens()) {
						System.out.println(k+"."+st.nextToken());
						k++;
					}
					/*
					Element address=doc2.select("table.info td").get(0);
					String addr=address.text();
					Element tel=doc2.select("table.info td").get(1);
					String phone=tel.text();
					Element type=doc2.select("table.info td").get(2);
					String tp=type.text();
					String pr="";
					try {
						Element price=doc2.select("table.info th").get(3);
						String s=price.text();
						if(s.equals("가격대")) {
							Element pp=doc2.select("table.info td").get(3);
							pr=pp.text();
						}
					}catch(Exception ex) {
						pr="none";
					}
					
					//Element parking=doc2.selectFirst("");
					//Element time=doc2.selectFirst("");
					//Element menu=doc2.selectFirst("");
					*/
					
					String addr="no",phone="no",tp="no",pr="no",pa="no",ti="no",mu="no";
					Elements etc=doc2.select("table.info th");
					for(int a=0;a<etc.size();a++) {
						String ss=etc.get(a).text();
						if(ss.equals("주소")) {
							Element e=doc2.select("table.info td").get(a);
							addr=e.text();
						}else if(ss.equals("전화번호")) {
							Element e=doc2.select("table.info td").get(a);
							phone=e.text();
						}else if(ss.equals("음식 종류")) {
							Element e=doc2.select("table.info td").get(a);
							tp=e.text();
						}else if(ss.equals("가격대")) {
							Element e=doc2.select("table.info td").get(a);
							pr=e.text();
						}else if(ss.equals("주차")) {
							Element e=doc2.select("table.info td").get(a);
							pa=e.text();
						}else if(ss.equals("영업시간")) {
							Element e=doc2.select("table.info td").get(a);
							ti=e.text();
						}else if(ss.equals("메뉴")) {
							Element e=doc2.select("table.info td").get(a);
							mu=e.text();
						}
					}
					
					System.out.println("업체명:"+title.text());
					System.out.println("점수:"+score.text());
					System.out.println("주소:"+addr);
					System.out.println("전화:"+phone);
					System.out.println("음식종류:"+tp);
					System.out.println("가격대:"+pr);
					System.out.println("주차:"+pa);
					System.out.println("영업시간:"+ti);
					System.out.println("메뉴:"+mu);
					System.out.println("==============================");
					
					//데이터베이스 저장
					FoodHouseVO fvo=new FoodHouseVO();
					fvo.setCno(vo.getCno());
					fvo.setName(title.text());
					fvo.setScore(Double.parseDouble(score.text()));
					fvo.setAddress(addr);
					fvo.setTel(phone);
					fvo.setType(tp);
					fvo.setPrice(pr);
					fvo.setParking(pa);
					fvo.setTime(ti);
					fvo.setMenu(mu);
					fvo.setPoster(image);
					//번호는 자동증가
					Element review=doc2.selectFirst("script#reviewCountInfo");
					//class(.), id(#) => 태그 구분
					//System.out.println(review.data());	//script => data출력(not text)
					String json=review.data();
					JSONParser jp=new JSONParser();
					JSONArray arr=(JSONArray)jp.parse(json);
					String good="",soso="",bad="";
					for(int b=0;b<arr.size();b++) {
						JSONObject obj=(JSONObject)arr.get(b);
						if(b==0) {
							bad=String.valueOf(obj.get("count"));
						}else if(b==1) {
							soso=String.valueOf(obj.get("count"));
						}else if(b==2) {
							good=String.valueOf(obj.get("count"));
						}
					}
					System.out.println("GOOD:"+good);
					System.out.println("SOSO:"+soso);
					System.out.println("BAD:"+bad);
					fvo.setGood(Integer.parseInt(good));
					fvo.setSoso(Integer.parseInt(soso));
					fvo.setBad(Integer.parseInt(bad));
					//데이터베이스에 저장
					dao.foodHouseInsert(fvo);
				}
			}
		}catch(Exception ex) {}
	}
}
