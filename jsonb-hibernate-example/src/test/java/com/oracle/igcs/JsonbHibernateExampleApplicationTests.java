package com.oracle.igcs;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.igcs.constants.Constants;
import com.oracle.igcs.model.IgcsUser;
import com.oracle.igcs.repositories.IgcsUserRepository;
import com.oracle.igcs.service.IgcsUserServiceImpl;
import com.oracle.igcs.utils.IGCSUserEntityUtil;
import com.oracle.igcs.utils.JSONParser;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * @author vdawar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonbHibernateExampleApplicationTests {

	Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
    protected IgcsUserServiceImpl igcsUserService;
	
	@Autowired
	private IgcsUserRepository repository;

	private Random random = new Random();
	private String[] city = {"San Jose", "San Diego", "San francisco", "Los Angeles", "Santa Barbara"};
	
	
	/**
	 * Step 1 :
	 * Test reads the Igcs_user.json file and inserts the record in db.
	 * @throws JsonParseException
	 * @throws IOException
	 */
	@Test
	public void testInsertJsonbEntity() throws JsonParseException, IOException {
		List<IgcsUser> personList = new ArrayList<>();
		JsonNode jsonNode = null;
		JSONParser.readJsonFile(Constants.IGCS_USER_FILE_NAME);
		while ((jsonNode = JSONParser.nextJsonNode()) != null) {
			personList.add(IGCSUserEntityUtil.getIGCSUser(jsonNode));
		}
		repository.save(personList);
	}
	
	/**
	 * Step : 2
	 * 
	 * Selects the inserted records for california city
	 * @throws JsonParseException
	 * @throws IOException
	 */
	@Test
	public void testSelectJsonbEntity() throws JsonParseException, IOException {
		log.info("... testSelectJsonbEntity ...");
		List<IgcsUser> p = repository.findByState("California");
		System.out.println(JSONParser.objectToJson(p));
	}
	
	/**
	 * Step 3:
	 * 
	 * Update city using native Oracle Db query via spring data jpa
	 * @throws JsonParseException
	 * @throws IOException
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testUpdateJsonbEntity() throws JsonParseException, IOException {
		String updateTocity = city[random.nextInt(city.length)];
		System.out.println(updateTocity);
		repository.updateCity(updateTocity, "California");
	}
	
	/**
	 * Step : combined 1 & 2
	 * 
	 * Inserts the record from Igcs_user.json file & selects based on state California
	 * @throws JsonParseException
	 * @throws IOException
	 */
	@Test
	public void testInsertAndSelectEntity() throws JsonParseException, IOException {
		testInsertJsonbEntity();
		testSelectJsonbEntity();
	}
	
	
	
	/**
	 * Batch Insert with in-memory data to get performance numbers
	 * @throws JsonParseException
	 * @throws IOException
	 * @throws CloneNotSupportedException 
	 */
	@Test
	public void bulkInsert() throws JsonParseException, IOException, CloneNotSupportedException {
		JSONParser.readJsonFile(Constants.IGCS_USER_FILE_NAME);
		IGCSUserEntityUtil.setSampleIgcsUser(JSONParser.nextJsonNode());
		
		float[] batchInsertionTime = new float[Constants.TOTAL_RECORDS / Constants.BATCH_SIZE];
		int startIndex = 1;
		int batchCount = 0;
		while (startIndex <= Constants.TOTAL_RECORDS) {
			System.out.println("starting to save from " + startIndex);
			List<IgcsUser> personList = IGCSUserEntityUtil.getNextBatchSet(startIndex, Constants.BATCH_SIZE);
			if(personList != null) {
				long startTime = System.currentTimeMillis();
				//save in batch
				igcsUserService.saveIgcsUsersInBatch(personList);
				
				System.out.println("finish to save from " + startIndex + " with batch id: " + batchCount);
				
				long endTime = System.currentTimeMillis();
				float finalTime = (float)(endTime - startTime)/1000;
				batchInsertionTime[batchCount++] = finalTime; 
				startIndex+=Constants.BATCH_SIZE;
				
			}
		}
		
		float allbatchesInsertionTime = 0;
		for (int batchIndex = 0; batchIndex < batchInsertionTime.length; batchIndex++) {
			allbatchesInsertionTime += batchInsertionTime[batchIndex];
			System.out.println("Time to insert Batch-" + (batchIndex+1) + " = " + batchInsertionTime[batchIndex] + " seconds");
		}
		System.out.println("Combined All batched Time = " + allbatchesInsertionTime + " seconds");
	}
	
	@Test
	public void bulkSearchByStreet() throws JsonParseException, IOException, CloneNotSupportedException {
		log.info("... test Select by street  ...");
		long startTime = System.currentTimeMillis();
		List<IgcsUser> p = repository.searchByStreet("Columbus Pla"); //
		long endTime = System.currentTimeMillis();
		float finalTime = (float)(endTime - startTime)/1000;
		System.out.println(JSONParser.objectToJson(p));
		System.out.println("Total record found : " + p.size());
		System.out.println("Total search time : " + finalTime);
	}
	
	@Test
	public void testSelectCertainPropertiesJsonbEntity() throws JsonParseException, IOException {
		log.info("... testSelectJsonbEntity ...");
		List<Object> p = repository.findUserLoginState("California");
		System.out.println(JSONParser.objectToJson(p));
	}
	
	@Test
	public void testSearchMultipleHierarchyByStreet() throws JsonParseException, IOException, CloneNotSupportedException {
		log.info("... test Select by street  ...");
		List<IgcsUser> p = igcsUserService.searchByMultiHierarchyByStreet("Columbus Pla"); //
		System.out.println(JSONParser.objectToJson(p));
		System.out.println("Total record found : " + p.size());
	}
	
	@Test
	public void testSetParameter() throws JsonParseException, IOException, CloneNotSupportedException {
		log.info("... test Select by street  ...");
		List<IgcsUser> p = repository.testParamBindings("California");
		System.out.println(JSONParser.objectToJson(p));
		System.out.println("Total record found : " + p.size());
	}
	
	@Test
	public void testExpressionBinding() throws JsonParseException, IOException, CloneNotSupportedException {
		log.info("... test expression binding  ...");
		List<IgcsUser> p = repository.bindExpression("$.user_address.state");
		System.out.println("Total record found : " + p.size());
	}

}
