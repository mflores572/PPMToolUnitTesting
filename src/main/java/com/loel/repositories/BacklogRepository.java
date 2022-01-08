package com.loel.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.loel.domain.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog,Long>{
	
	Backlog findByProjectIdentifier(String projectIdentifier);
	
}
    