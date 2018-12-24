package woo.daykey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import woo.daykey.dao.HomeDao;
import woo.daykey.dao.NewsDao;
import woo.daykey.dao.SciDao;
import woo.daykey.model.Home;
import woo.daykey.model.News;
import woo.daykey.model.Sci;
import woo.daykey.parser.BoardParsing;

@Component
public class Scheduler {
	private final String SERVER_KEY = "AAAAHxhfxQY:APA91bF8R6bNpW8upQHiP3eUhGXLvESOHIXpHBhUft2sj30qEUxymw-rtOF8qbG6QfsyzsgFiGjV0RUa8Qm0-x2MdmqMeYiXq1agIvkVu5vvdhjiunlC0EDEkKQAxjDj0QjPJveTsMh9";
	private final String[] urls = {"http://www.daykey.hs.kr/daykey/0701/board/14117", 
			"http://www.daykey.hs.kr/daykey/0601/board/14114",
			"http://www.daykey.hs.kr/daykey/19516/board/20170"};
	@Autowired
    NewsDao newsdao;
    
    @Autowired
    HomeDao homedao;
    
    @Autowired
    SciDao scidao;
    
	@Scheduled(cron = "* 0,30 8-20 * * *")//(fixedDelay=1000 * 60 * 60)//(cron = "30 * * * * *")
	public void alarm() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA );
		Date date = new Date();
		System.out.println(formatter.format(date) + " : alarm 스케쥴러 호출");
		FCM fcm = new FCM();
		
		int[] beginid = getAll();
		new BoardParsing(urls[0], newsdao, 1);
		new BoardParsing(urls[1], homedao, 2);
		new BoardParsing(urls[2], scidao, 3);
		int[] finishid = getAll();
		
		for(int i=0; i < 3; i++) {
			if (beginid[i] != finishid[i]) {
				if(i == 0) {
					System.out.println("뉴스 : "+newsdao.findOne(finishid[i]).getTitle());
					fcm.send_FCM_Notification(SERVER_KEY, newsdao.findOne(finishid[i]).getTitle(), finishid[i], "news");
				} else if(i == 1) {
					System.out.println("가정 : "+homedao.findOne(finishid[i]).getTitle());
					fcm.send_FCM_Notification(SERVER_KEY, homedao.findOne(finishid[i]).getTitle(), finishid[i], "home");
				} else {
					fcm.send_FCM_Notification(SERVER_KEY, scidao.findOne(finishid[i]).getTitle(), finishid[i], "sci");
				}
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("alarm 스케쥴러 완료");
	}
	
	public int[] getAll() {
		List<News> listnews = newsdao.findAll();
		List<Home> listhome = homedao.findAll();
		List<Sci> listsci = scidao.findAll();
		
		int[] ids = {listnews.get(listnews.size() -1).getUrl(),
				listhome.get(listhome.size() -1).getUrl(),
				listsci.get(listsci.size() -1).getUrl()};
		
		return ids;
	}
}