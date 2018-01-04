package com.oracle.igcs.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.igcs.model.IgcsUser;

@Service
public class IgcsUserServiceImpl extends BatchService implements IgcsUserService {
	
	@Override
	@Transactional
	public void saveIgcsUsersInBatch(List<IgcsUser> igcsUserList) {
		
		for (int i = 0; i < igcsUserList.size();) {
			save(igcsUserList.get(i), ++i);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<IgcsUser> searchByMultiHierarchyByStreet(String expression) {
		/*String qlString = "SELECT * FROM igc_user_bulk_v5 WHERE JSON_EXISTS(user_flex_fields_json,'$?(@.user_address.street like \"%"+city+"%\" )')";
		//String qlString = "SELECT * FROM igc_user_bulk_v5 WHERE JSON_EXISTS(user_flex_fields_json,'$?(@.user_address.street like \"%Columbus Pla%\" )')";
		Query query = em.createNativeQuery(qlString, IgcsUser.class);
		List<IgcsUser> userList = query.getResultList();
		return userList;*/
		
		/*String qlString = "select * from igc_user_bulk_v5 a where json_exists (a.user_flex_fields_json, :city)";
		Query query = em.createNativeQuery(qlString, IgcsUser.class);
		query.getParameters().forEach(a -> System.out.println("pos: " + a.getPosition() + " , Name: " + a.getName()));
		query.setParameter("city", "'$.user_address.street'");
		query.getParameters().forEach(a -> System.out.println("pos: " + a.getPosition() + " , Name: " + a.getName() + " , value: " + query.getParameterValue(a)));
		List<IgcsUser> userList = query.getResultList();
		*/
		
		String qlString = "select * from igc_user_bulk_v5 where JSON_EXISTS(user_flex_fields_json, '+" + expression +"')";
		Query query = em.createNativeQuery(qlString, IgcsUser.class);
		query.getParameters().forEach(a -> System.out.println("pos: " + a.getPosition() + " , Name: " + a.getName()));
		query.setParameter("city", 27);
		query.getParameters().forEach(a -> System.out.println("pos: " + a.getPosition() + " , Name: " + a.getName() + " , value: " + query.getParameterValue(a)));
		List<IgcsUser> userList = query.getResultList();
		
		return userList;
	}
}
