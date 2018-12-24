package woo.daykey.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import woo.daykey.dao.HomeDao;
import woo.daykey.dao.NewsDao;
import woo.daykey.dao.SciDao;
import woo.daykey.model.Home;
import woo.daykey.model.News;
import woo.daykey.model.Sci;

import java.io.IOException;

public class BoardParsing{

    private String strUrl;
    
    @Autowired
    NewsDao newsdao;
    
    @Autowired
    HomeDao homedao;
    
    @Autowired
    SciDao scidao;

    public BoardParsing(String url, NewsDao newsdao, int num) {
        this.strUrl = url;
        this.newsdao = newsdao;
        init(num);
    }
    
    public BoardParsing(String url, HomeDao homedao, int num) {
        this.strUrl = url;
        this.homedao = homedao;
        init(num);
    }
    
    public BoardParsing(String url, SciDao scidao, int num) {
        this.strUrl = url;
        this.scidao = scidao;
        init(num);
    }
    
    public void init(int num) {
    	try {
            Document doc = Jsoup.connect(strUrl).get();
            Elements table = doc.select("table.wb");
            table = table.select("tbody");
            Elements tr = table.select("tr");

            for (int i = 0; i < tr.size(); i++) {
                Element elementsTitle = tr.get(i).select("td").get(1);
                String title = elementsTitle.text();
                String notice = tr.get(i).select("td").get(0).text();
                if (notice.equals("")) {
                    title = "[공지]"+ title;
                }

                Elements temp = elementsTitle.select("img");
                if(temp.size() != 0) {
                    title += "(new)";
                }

                String teacherName = tr.get(i).select("td").get(2).text();
                String numOfVisitors = tr.get(i).select("td").get(3).text();
                String date = tr.get(i).select("td").get(4).text();
                String tempUrl = elementsTitle.select("a").attr("onclick");
                int url = Integer.parseInt(tempUrl.substring(27, 34));

                if(num == 1) {
                	News news = new News(url, title, teacherName, numOfVisitors, date);
                	newsdao.save(news);
                } else if (num == 2) {
                	Home home = new Home(url, title, teacherName, numOfVisitors, date);
                	homedao.save(home);
                } else if (num == 3) {
                	Sci sci = new Sci(url, title, teacherName, numOfVisitors, date);
                	scidao.save(sci);
                }
            }
            //Log.i("보드 : ", "데이터 : " + table.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
