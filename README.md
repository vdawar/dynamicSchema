# dynamicSchema

Overview

Oracle 12c database offer lots of proprietary features in addition to the known SQL standard. One example for it is Oracle Json Document Store which allows you to store JSON documents efficiently in a database column. JSON document in a text column (which is part of the SQL standard and supported by Hibernate).

This POC demonstrates the working of Oracle Json Document store with Spring Data JPA using Hibernate.



 

Case Implementation 
Code implementation has been done using Spring Data JPA with Hibernate as the JPA provider & using 12c Oracle Database. A new data type ('JsonDataUserType') has been created in hibernate which would match correspondingly with CLOB of Oracle12cDialect. 


Use Case Implementation Steps 
 

 Required Table with Hybrid JSON structure along with indexes created in PDB schema.
 Bulk insertion of data into Hybrid JSON table using Spring Data JPA using Hibernate.
 Perform DML opertations on the JSON data stored in Hybrid JSON table using Spring Data JPA using Hibernate.


 



Insertion : Code parses JSON & maps them to flex fields and known fields in the table using spring data JPA. 






2. Selection : Selected records with state as "California" using spring data JPA. Address (state) is a flex field and stored as JSON Document. Native Oracle 12c db query performed using spring data jpa.



3. Updating : Updating city of records with state as 'California'. Performed native Oracle 12c DB query using spring data jpa

Query : @Query(value = "UPDATE igc_user_bulk_v5 p SET p.user_flex_fields_json=REPLACE(p.user_flex_fields_json, p.user_flex_fields_json.user_address.city, :city) WHERE json_value(user_flex_fields_json, '$.user_address.state') = :state", nativeQuery = true)





          



Bulk Insert & Search Operation :
1) Inserted 1 Million users with batch size of 5000.

              Performance Result :

                 Time taken for 1 Million Records = 942.527 seconds ~  00:15:42:527  

                Avg Batch time = 4.712 seconds 