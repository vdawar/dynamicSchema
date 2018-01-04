CREATE TABLE igc_user_bulk_v5 (
	USER_KEY NUMBER, 
	USER_NAME VARCHAR2(4000), 
	USER_TYPE VARCHAR2(50), 
	USER_STATUS VARCHAR2(50), 
	USER_EMP_TYPE VARCHAR2(50), 
	USER_LOGIN VARCHAR2(4000), 
	USER_PASSWORD VARCHAR2(4000), 
	USER_EMAIL VARCHAR2(100),  
	USER_CREATE_BY VARCHAR2(100), 
	USER_MANAGER_NAME VARCHAR2(4000), 
	USER_DELETED VARCHAR2(30), 
	USER_FLEX_FIELDS_JSON CLOB,
  CONSTRAINT user_key_pk PRIMARY KEY (USER_KEY),
  CONSTRAINT user_flex_fields_json_chk CHECK (USER_FLEX_FIELDS_JSON IS JSON)
);

CREATE sequence igc_user_seq START WITH 1 INCREMENT BY 1;

CREATE BITMAP INDEX igc_user_bulk_bidx1 ON igc_user_bulk_v5 (json_value(user_flex_fields_json, '$.user_address.city'));

CREATE BITMAP INDEX igc_user_bulk_bidx2 ON igc_user_bulk_v5 (json_value(user_flex_fields_json, '$.user_address.state'));