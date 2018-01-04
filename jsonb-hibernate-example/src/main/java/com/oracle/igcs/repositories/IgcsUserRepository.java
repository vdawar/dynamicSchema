package com.oracle.igcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.oracle.igcs.constants.Constants;
import com.oracle.igcs.model.IgcsUser;

/**
 * @author vdawar
 *
 */
public interface IgcsUserRepository extends CrudRepository<IgcsUser, Long> {

	String findByState = "select * from " + Constants.IGCS_USER_TABLE_NAME + " where json_value("
			+ Constants.IGCS_USER_FLEX_FIELD_COLUMN_NAME + ", '$.user_address.state') = :state";
	
	String findByState1 = "select USER_LOGIN from " + Constants.IGCS_USER_TABLE_NAME + " where json_value("
			+ Constants.IGCS_USER_FLEX_FIELD_COLUMN_NAME + ", '$.user_address.state') = ?1";

	String updateCity = "UPDATE igc_user_bulk_v5 p SET p.user_flex_fields_json=REPLACE(p.user_flex_fields_json, p.user_flex_fields_json.user_address.city, ?1) WHERE json_value(user_flex_fields_json, '$.user_address.state') = ?2";

	String paramBinding = "SELECT * " + 
			"FROM igc_user_bulk_v5 " + 
			"WHERE JSON_EXISTS(user_flex_fields_json, '$?(@.user_address.state == $bind_val)' PASSING :state AS \"bind_val\")"; 
	
	String expressionBinding = "select * from igc_user_bulk_v5 where JSON_EXISTS(user_flex_fields_json, '/a:expression')"; 
	
	@Query(value = paramBinding, nativeQuery = true)
	List<IgcsUser> testParamBindings(@Param("state") String state);
	
	@Query(value = findByState, nativeQuery = true)
	List<IgcsUser> findByState(@Param("state")String state);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE igc_user_bulk_v5 p SET p.user_flex_fields_json=REPLACE(p.user_flex_fields_json, p.user_flex_fields_json.user_address.city, :city) WHERE json_value(user_flex_fields_json, '$.user_address.state') = :state", nativeQuery = true)
	int updateCity(@Param("city") String updateTocity, @Param("state") String state);

	//@Query(value =  "select * from igc_user_bulk_v5 WHERE json_value(user_flex_fields_json, '$.user_address.street') like %:city%", nativeQuery = true)
	@Query(value =  "SELECT * FROM igc_user_bulk_v5 WHERE JSON_EXISTS(user_flex_fields_json,'$?(@.user_address.street like %:city% )')", nativeQuery = true)
	//@Query(value =  "SELECT * FROM igc_user_bulk_v5 WHERE JSON_EXISTS(user_flex_fields_json,'$?#{true ? '?(@.user_address.street like ' : 'false'} %:city% )')", nativeQuery = true)
	List<IgcsUser> searchByStreet(@Param("city") String updateTocity);
	
	
	@Query(value = findByState1, nativeQuery = true)
	List<Object> findUserLoginState(String state);
	
	@Query(value = expressionBinding, nativeQuery = true) //not working.
	List<IgcsUser> bindExpression(@Param("expression") String expression);
	
}
