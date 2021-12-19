package com.cms.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.blog.model.Board;


public interface BoardRepository extends JpaRepository<Board, Integer>{

	
	

}
