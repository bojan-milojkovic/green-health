package com.green.health.store.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.green.health.store.entities.ProductDTO;
import com.green.health.store.entities.ProductJPA;
import com.green.health.store.entities.ProductJPA_;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Repository
public class ProductCriteriaRepository {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	public List<ProductJPA> getProductByProperties(final ProductDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(RestPreconditions.checkString(model.getCity()), 
				"Get product by properties Error", "You must specify the city in which to look");
		RestPreconditions.assertTrue(criteriaCheck(model), 
				"Get product by properties Error", "You must specify some search criteria");
		
		CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
		CriteriaQuery<ProductJPA> cquery = builder.createQuery(ProductJPA.class);
		Root<ProductJPA> croot = cquery.from(ProductJPA.class);
		
		cquery.select(croot);
		
		Predicate cpred = builder.conjunction();
		
		cpred = builder.and(cpred, builder.equal(croot.get(ProductJPA_.city), model.getCity()));
		
		if(RestPreconditions.checkString(model.getLocalName())){
			cpred = builder.and(cpred, builder.equal(croot.get(ProductJPA_.localName), model.getLocalName()));
		}
		if(RestPreconditions.checkString(model.getCurrency())){
			cpred = builder.and(cpred, builder.equal(croot.get(ProductJPA_.currency), model.getCurrency()));
		}
		if(model.getMinPrice() != null){
			cpred = builder.and(cpred, builder.ge(croot.get(ProductJPA_.price), model.getMinPrice()));
		}
		if(model.getMaxPrice() != null){
			cpred = builder.and(cpred, builder.le(croot.get(ProductJPA_.price), model.getMaxPrice()));
		}
		if(!model.getContains().isEmpty()){
			cpred = builder.and(cpred, builder.like(croot.get(ProductJPA_.contains), createPattern(model.getContains())));
		}
		if(!model.getTreats().isEmpty()){
			cpred = builder.and(cpred, builder.like(croot.get(ProductJPA_.treats), createPattern(model.getTreats())));
		}
		
		return entityManagerFactory.createEntityManager()
				.createQuery(cquery.where(cpred).orderBy(builder.desc(croot.get(ProductJPA_.price))))
				.getResultList();
	}
	
	private boolean criteriaCheck(ProductDTO model) {
		return !model.getContains().isEmpty() || 
				!model.getTreats().isEmpty() || 
				RestPreconditions.checkString(model.getLocalName()) || 
				RestPreconditions.checkString(model.getCurrency()) || 
				model.getMaxPrice()!=null || 
				model.getMinPrice()!=null;
	}
	
	private String createPattern(Set<Long> set) {
		// %1%
		// %1,%2,%3,%4%
		String pattern = "";
		Iterator<Long> iter = set.iterator();
		while(iter.hasNext()){
			pattern += ("%" + iter.next() + (iter.hasNext() ? "," : "%"));
		}
		return pattern;
	}
}
