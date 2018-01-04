package com.oracle.igcs.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.oracle.igcs.constants.Constants;



public abstract class BatchService  {

	@PersistenceContext
    protected EntityManager em;
	
	protected void save(Object entity, int i) {
		em.persist(entity);
		if(i % Constants.BATCH_SIZE == 0) {
			em.flush();
			em.clear();
		}		
	}
}
