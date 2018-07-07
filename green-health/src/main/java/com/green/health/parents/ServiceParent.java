package com.green.health.parents;

public interface ServiceParent<J extends PojoParent, M extends PojoParent> {

	J convertModelToJPA(final M model);
	M convertJpaToModel(final J jpa);
	
	boolean isPostDataPresent(final M model);
	boolean isPatchDataPresent(final M model);
	
}