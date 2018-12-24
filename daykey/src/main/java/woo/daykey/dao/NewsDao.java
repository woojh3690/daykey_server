package woo.daykey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import woo.daykey.model.News;

public interface NewsDao extends JpaRepository<News, Integer> {
	
}
