package com.green.health.parents;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface ServiceParent<J extends PojoParent, M extends PojoParent> {

	J convertModelToJPA(final M model) throws MyRestPreconditionsException;
	M convertJpaToModel(final J jpa);
	
	void isPostDataPresent(final M model) throws MyRestPreconditionsException;
	boolean isPatchDataPresent(final M model);
	
	public List<M> getAll();
	public M getOneById(final Long id) throws MyRestPreconditionsException;
	public void addNew(final M model) throws MyRestPreconditionsException;
	public M edit(M model, final Long id) throws MyRestPreconditionsException;
	
	public JpaRepository<? extends PojoParent, Long> getRepository();
	
	public default void delete(final Long id, final String object) throws MyRestPreconditionsException {
		checkId(id);
		
		RestPreconditions.checkNotNull(getRepository().getOne(id), object+" delete error",
					object+" with id = "+ id + " does not exist in our database.");
		
		getRepository().deleteById(id);
	}
	
	public default void checkId(final Long id) throws MyRestPreconditionsException {
		if(!(id!=null && id>0)){
			throw new MyRestPreconditionsException("Find object by id failed", "Id is invalid");
		}
	}
}