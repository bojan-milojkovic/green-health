package com.green.health.store.entities;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.green.health.security.entities.UserSecurityJPA;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductJPA.class)
public abstract class ProductJPA_ {

	public static volatile SingularAttribute<ProductJPA, String> localName;
	public static volatile SingularAttribute<ProductJPA, String> treats;
	public static volatile SingularAttribute<ProductJPA, LocalDate> expires;
	public static volatile SingularAttribute<ProductJPA, String> contains;
	public static volatile SingularAttribute<ProductJPA, StoreJPA> storeJpa;
	public static volatile SingularAttribute<ProductJPA, String> city;
	public static volatile SingularAttribute<ProductJPA, Double> price;
	public static volatile SingularAttribute<ProductJPA, String> localDescription;
	public static volatile SingularAttribute<ProductJPA, String> currency;
	public static volatile SingularAttribute<ProductJPA, Long> id;
	public static volatile SetAttribute<ProductJPA, UserSecurityJPA> raters;

}

