package com.green.health.store.service;

import java.util.List;
import com.green.health.parents.ServiceParent;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StoreService extends ServiceParent<StoreJPA, StoreDTO>{

	public List<StoreDTO> getMyStores(final String username) throws MyRestPreconditionsException;
	
	public List<StoreDTO> getStoresByProperties(final StoreDTO model) throws MyRestPreconditionsException;
	
	public List<StoreDTO> getAllStores() throws MyRestPreconditionsException;
	
	public void deleteStore(final Long id, final String username) throws MyRestPreconditionsException;
}
