package com.green.health.parents;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface ServiceParent<J extends PojoParent, M extends PojoParent> {

	J convertModelToJPA(final M model) throws MyRestPreconditionsException;
	M convertJpaToModel(final J jpa);
	void getPostValidationErrors(final M model, List<String> list);
	boolean isPatchDataPresent(final M model);
	public JpaRepository<J, Long> getRepository();
	public String getName();
	
	public default String addOrEdit(final M model){
		return model.getId()==null ? "Add" : "Edit";
	}
	
	default void isPostDataPresent(final M model) throws MyRestPreconditionsException{
		MyRestPreconditionsException ex = new MyRestPreconditionsException("Failed to create a new "+getName()+" object",
				"The following request form data is missing or invalid :");
		
		getPostValidationErrors(model, ex.getErrors());
		
		if(!ex.getErrors().isEmpty()){
			throw ex;
		}
	}
	
	public default List<M> getAll(){
		return getRepository().findAll().stream().map(jpa -> convertJpaToModel(jpa)).collect(Collectors.toList());
	}
	
	public default M getOneById(final Long id) throws MyRestPreconditionsException{
		checkId(id, "Find "+getName()+" error");
		return convertJpaToModel(RestPreconditions.checkNotNull(getRepository().getOne(id), 
				"Find "+getName()+" error", "Cannot find the "+getName()+" with id = "+id));
	}
	
	public default void addNew(final M model) throws MyRestPreconditionsException{
		RestPreconditions.checkNotNull(model, "Create "+getName()+" error",
				"You are sending a request without the object");
		model.setId(null);
		isPostDataPresent(model);
	}
	
	public default M edit(M model, final Long id) throws MyRestPreconditionsException{
		checkId(id,"Edit "+getName()+" error");
		
		RestPreconditions.checkNotNull(model, "Edit "+getName()+" error",
				"You are sending a request without body");
		model.setId(id);
		RestPreconditions.assertTrue(isPatchDataPresent(model), "Edit "+getName()+" error", 
				"Your edit request is invalid - You must provide some editable data");
		
		return model;
	}
	
	public default void delete(final Long id) throws MyRestPreconditionsException {
		checkId(id,"Delete "+getName()+" error");
		
		getRepository().delete(RestPreconditions.checkNotNull(getRepository().getOne(id), "Delete "+getName()+" error",
				getName()+" with id = "+ id + " does not exist in our database."));
	}
	
	public default void checkId(final Long id, final String title) throws MyRestPreconditionsException {
		if(id==null || id<1){
			throw new MyRestPreconditionsException(title,"Id you entered is missing or invalid");
		}
	}
}