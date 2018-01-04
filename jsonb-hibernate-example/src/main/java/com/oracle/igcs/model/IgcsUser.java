package com.oracle.igcs.model;

import org.hibernate.annotations.Type;

import com.oracle.igcs.constants.Constants;

import javax.persistence.*;
import java.util.Map;

/**
 * @author vdawar
 *
 */
@Entity
@Table(name = Constants.IGCS_USER_TABLE_NAME)
/*@NamedNativeQueries({
    
    @NamedNativeQuery(
            name    =   "getAllEmployeesByDeptId",
            query   =   "SELECT * FROM igc_user_bulk_v5 WHERE JSON_EXISTS(user_flex_fields_json,'$ :dummy (@.user_address.street like %:city% )') ",
                        resultClass=IgcsUser.class
    )
})
*/public class IgcsUser implements Cloneable {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO) //auto doesnt work in Oracle
	// db.
	// but work with postgreSQL
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "igc_user_seq")
	private Long userKey;

	private String userName;

	private String userType;

	private String userStatus;

	private String userEmpType;

	private String userLogin;

	private String userPassword;

	private String userEmail;
	
	private String userCreateBy;
	
	private String userManagerName;
	
	private String userDeleted;

	@Type(type = "JsonDataUserType")
	@Column(name = Constants.IGCS_USER_FLEX_FIELD_COLUMN_NAME)
	private Map<String, Object> userFlexFieldsJson;

	public Long getUserKey() {
		return userKey;
	}

	public void setUserKey(Long userKey) {
		this.userKey = userKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserEmpType() {
		return userEmpType;
	}

	public void setUserEmpType(String userEmpType) {
		this.userEmpType = userEmpType;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserCreateBy() {
		return userCreateBy;
	}

	public void setUserCreateBy(String userCreateBy) {
		this.userCreateBy = userCreateBy;
	}

	public String getUserManagerName() {
		return userManagerName;
	}

	public void setUserManagerName(String userManagerName) {
		this.userManagerName = userManagerName;
	}

	public String getUserDeleted() {
		return userDeleted;
	}

	public void setUserDeleted(String userDeleted) {
		this.userDeleted = userDeleted;
	}

	public Map<String, Object> getUserFlexFieldsJSON() {
		return userFlexFieldsJson;
	}

	public void setUserFlexFieldsJSON(Map<String, Object> userFlexFieldsJson) {
		this.userFlexFieldsJson = userFlexFieldsJson;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
