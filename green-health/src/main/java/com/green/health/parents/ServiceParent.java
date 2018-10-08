package com.green.health.parents;

import java.util.List;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface ServiceParent<J extends PojoParent, M extends PojoParent> {

	J convertModelToJPA(final M model);
	M convertJpaToModel(final J jpa);
	
	boolean isPostDataPresent(final M model);
	boolean isPatchDataPresent(final M model);
	
	public List<M> getAll();
	public M getOneById(final Long id) throws MyRestPreconditionsException;
	public void addNew(final M model) throws MyRestPreconditionsException;
	public M edit(M model, final Long id) throws MyRestPreconditionsException;
	public void delete(final Long id) throws MyRestPreconditionsException;
	
	default void checkId(final Long id) throws MyRestPreconditionsException {
		if(!(id!=null && id>0)){
			throw new MyRestPreconditionsException("Find object by id failed", "Id is invalid");
		}
	}
}