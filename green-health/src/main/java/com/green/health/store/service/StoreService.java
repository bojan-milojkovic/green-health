package com.green.health.store.service;

import java.util.List;
import com.green.health.parents.ServiceParent;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StoreService extends ServiceParent<StoreJPA, StoreDTO>{

	List<StoreDTO> getMyStore(final String username) throws MyRestPreconditionsException;
	
	List<StoreDTO> getStoresByProperties(final StoreDTO model) throws MyRestPreconditionsException;
	
	List<StoreDTO> getAllStores();
	
	boolean checkUserOwnsTheStore(final String username, final Long id);
}
