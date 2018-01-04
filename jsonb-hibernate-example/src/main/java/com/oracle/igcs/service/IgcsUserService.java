package com.oracle.igcs.service;

import java.util.List;

import com.oracle.igcs.model.IgcsUser;

public interface IgcsUserService {

	void saveIgcsUsersInBatch(List<IgcsUser> personList);
	
	List<IgcsUser> searchByMultiHierarchyByStreet(String string);

}
