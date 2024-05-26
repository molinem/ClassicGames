package edu.uclm.esi.tysweb2023.dao;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.tysweb2023.model.Historial;

public interface HistorialDAO extends CrudRepository<Historial, String>{
	
}
