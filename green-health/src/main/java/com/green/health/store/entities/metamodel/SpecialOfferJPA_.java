package com.green.health.store.entities.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import com.green.health.store.entities.SpecialOfferJPA;
import com.green.health.store.entities.StoreJPA;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SpecialOfferJPA.class)
public abstract class SpecialOfferJPA_ {

	public static volatile SingularAttribute<SpecialOfferJPA, StoreJPA> storeJpa;
	public static volatile SingularAttribute<SpecialOfferJPA, Float> price;
	public static volatile SingularAttribute<SpecialOfferJPA, String> name;
	public static volatile SingularAttribute<SpecialOfferJPA, String> description;
	public static volatile SingularAttribute<SpecialOfferJPA, String> currency;
	public static volatile SingularAttribute<SpecialOfferJPA, Long> id;

}

