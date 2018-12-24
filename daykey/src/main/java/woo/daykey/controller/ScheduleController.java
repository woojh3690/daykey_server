package woo.daykey.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import woo.daykey.dao.ScheduleDao;
import woo.daykey.model.Schedule;

@RestController
public class ScheduleController {
	@Autowired
	ScheduleDao scheduleDao;
	
	@GetMapping("/schedule")
	public List<Schedule> index() {
		//책목록
		List<Schedule> list = scheduleDao.findAll();
		return list;
	}
	
	@GetMapping("/schedule/view/{date}")
	public Schedule view(@PathVariable("date")Integer date) {
		Schedule schedule = scheduleDao.findOne(date);
		return schedule;
	}
	
	@RequestMapping(value = "/schedule/save" , method = RequestMethod.POST)
	public void getGeneric(@RequestBody Map<String, Object> payload) throws Exception {
		Schedule schedule = new Schedule();
		schedule.setName(String.valueOf(payload.get("name")));
		schedule.setGrade(Integer.parseInt(String.valueOf(payload.get("grade"))));
		schedule.setClass_(Integer.parseInt(String.valueOf(payload.get("class"))));
		schedule.setPassword(Integer.parseInt(String.valueOf(payload.get("password"))));
		schedule.setYear(String.valueOf(payload.get("year")));
		schedule.setMonth(String.valueOf(payload.get("month")));
		schedule.setDate(String.valueOf(payload.get("date")));
		schedule.setSche(String.valueOf(payload.get("sche")));
		schedule.setEmail(String.valueOf(payload.get("email")));
		schedule.setBooleanPublic(Byte.valueOf(String.valueOf(payload.get("boolean_public"))));
		scheduleDao.save(schedule);
    }
	
	@RequestMapping(value = "/schedule/delete" , method = RequestMethod.POST)
	public void getNum(@RequestBody Map<String, Object> payload) throws Exception {
		String num = String.valueOf(payload.get("num"));
		scheduleDao.delete(Integer.parseInt(num));
    }
}
