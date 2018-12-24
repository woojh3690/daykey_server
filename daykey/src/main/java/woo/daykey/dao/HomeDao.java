package woo.daykey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import woo.daykey.model.Home;

public interface HomeDao extends JpaRepository<Home, Integer> {

}
