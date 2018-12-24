package woo.daykey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import woo.daykey.model.Schedule;

public interface ScheduleDao extends JpaRepository<Schedule, Integer> {

}
