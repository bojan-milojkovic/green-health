package com.green.health.store.dao;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.store.entities.metamodel.StoreJPA_;
import com.green.health.util.RestPreconditions;

@Repository
public class StoreCriteriaRepository {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	public List<StoreJPA> getStoresByProperties(final StoreDTO model){
		CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
		
		CriteriaQuery<StoreJPA> cquery = builder.createQuery(StoreJPA.class);
		Root<StoreJPA> croot = cquery.from(StoreJPA.class);
		
		cquery.select(croot);
		
		Predicate cPred = builder.conjunction();
		
		if(RestPreconditions.checkString(model.getCountry())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.country), model.getCountry()));
		}
		if(RestPreconditions.checkString(model.getCity())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.city), model.getCity()));
		}
		if(RestPreconditions.checkString(model.getName())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.name), model.getName()));
		}
		if(RestPreconditions.checkString(model.getPostalCode())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.postalCode), model.getPostalCode()));
		}
		if(RestPreconditions.checkString(model.getAddress1())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.address1), model.getAddress1()));
		}
		if(RestPreconditions.checkString(model.getAddress2())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.address2), model.getAddress2()));
		}
		if(RestPreconditions.checkString(model.getPhone1())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.phone1), model.getPhone1()));
		}
		if(RestPreconditions.checkString(model.getPhone2())){
			cPred = builder.and(cPred, builder.equal(croot.get(StoreJPA_.phone2), model.getPhone2()));
		}
		
		return entityManagerFactory.createEntityManager()
				.createQuery(cquery.where(cPred).orderBy(builder.desc(croot.get(StoreJPA_.added))))
				.setFirstResult(0)
				.setMaxResults(40)
				.getResultList();
	}
}
