package com.green.health.illness.entities.metamodel;

import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.entities.IllnessLocaleJPA;
import com.green.health.ratings.entities.LinkJPA;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IllnessJPA.class)
public abstract class IllnessJPA_ {

	public static volatile SingularAttribute<IllnessJPA, String> symptoms;
	public static volatile SingularAttribute<IllnessJPA, String> treatment;
	public static volatile SingularAttribute<IllnessJPA, String> engName;
	public static volatile SingularAttribute<IllnessJPA, String> description;
	public static volatile SingularAttribute<IllnessJPA, String> cause;
	public static volatile SetAttribute<IllnessJPA, LinkJPA> links;
	public static volatile SingularAttribute<IllnessJPA, Long> id;
	public static volatile SingularAttribute<IllnessJPA, String> latinName;
	public static volatile SetAttribute<IllnessJPA, IllnessLocaleJPA> illnessLocales;

}

