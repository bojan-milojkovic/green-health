package com.green.health.store.entities;

import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.user.entities.UserJPA;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StoreJPA.class)
public abstract class StoreJPA_ {

	public static volatile SingularAttribute<StoreJPA, String> storeWebSite;
	public static volatile SingularAttribute<StoreJPA, String> country;
	public static volatile SingularAttribute<StoreJPA, LocalDate> added;
	public static volatile SingularAttribute<StoreJPA, String> city;
	public static volatile SingularAttribute<StoreJPA, String> address2;
	public static volatile SetAttribute<StoreJPA, ProductJPA> specialOffers;
	public static volatile SetAttribute<StoreJPA, UserSecurityJPA> raters;
	public static volatile SingularAttribute<StoreJPA, String> address1;
	public static volatile SingularAttribute<StoreJPA, String> googleMapsLocation;
	public static volatile SingularAttribute<StoreJPA, String> workHours;
	public static volatile SingularAttribute<StoreJPA, String> postalCode;
	public static volatile SingularAttribute<StoreJPA, String> description;
	public static volatile SingularAttribute<StoreJPA, LocalDate> closedUntil;
	public static volatile SingularAttribute<StoreJPA, UserJPA> userJpa;
	public static volatile SingularAttribute<StoreJPA, String> phone;
	public static volatile SingularAttribute<StoreJPA, String> name;
	public static volatile SingularAttribute<StoreJPA, Long> id;
	public static volatile SingularAttribute<StoreJPA, String> email;

}
