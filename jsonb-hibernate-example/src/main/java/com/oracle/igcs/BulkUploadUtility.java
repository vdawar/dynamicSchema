package com.oracle.igcs;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.igcs.constants.Constants;
import com.oracle.igcs.model.IgcsUser;
import com.oracle.igcs.repositories.IgcsUserRepository;
import com.oracle.igcs.service.IgcsUserServiceImpl;
import com.oracle.igcs.utils.IGCSUserEntityUtil;
import com.oracle.igcs.utils.JSONParser;

@Component
public class BulkUploadUtility implements CommandLineRunner {

	@Autowired
    protected IgcsUserServiceImpl igcsUserService;
	
	@Autowired
	private IgcsUserRepository repository;
	
	@Override
	public void run(String... arg0) throws Exception {
		/*if(arg0 != null) {
			switch (arg0[0]) {
			case "batchinsert":
				System.out.println();
				break;

			case "search":
				System.out.println();
				break;
				
			default:
				break;
			}
		}*/
		//startBulkUpload();
		//searchByStreet();
	}

	

	private void startBulkUpload()
			throws IOException, JsonProcessingException, JsonParseException, CloneNotSupportedException {
		System.out.println("in comand line");
		//JSONParser.readJsonFile(Constants.IGCS_USER_FILE_NAME);
		//IGCSUserEntityUtil.setSampleIgcsUser(JSONParser.nextJsonNode());
		final JsonNode jsonNode = new ObjectMapper().readTree("[\r\n" + 
				"  {\r\n" + 
				"    \"user_name\": \"TestUserName1\",\r\n" + 
				"    \"user_type\": \"End-User\",\r\n" + 
				"    \"user_status\": \"Active\",\r\n" + 
				"    \"user_emp_type\": \"Full-Time\",\r\n" + 
				"    \"user_login\": \"TestUserName1\",\r\n" + 
				"    \"user_password\": \"35eee540-af7f-4342-a988-4f6c486147f6\",\r\n" + 
				"    \"user_email\": \"klinemunoz@geekmosis.com\",\r\n" + 
				"    \"user_create_by\": \"System\",\r\n" + 
				"    \"user_manager_name\": \"Keith Howard\",\r\n" + 
				"    \"user_deleted\": null,\r\n" + 
				"    \"user_address\": {\r\n" + 
				"      \"street\": \"Columbus Place\",\r\n" + 
				"      \"city\": \"San Deigo\",\r\n" + 
				"      \"state\": \"California\",\r\n" + 
				"      \"zipCode\": 1000,\r\n" + 
				"      \"country\": \"Cuba\"\r\n" + 
				"    },\r\n" + 
				"    \"user_phone\": [\r\n" + 
				"      {\r\n" + 
				"        \"type\": \"Office\",\r\n" + 
				"        \"number\": \"(882) 415-2724\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"type\": \"Mobile\",\r\n" + 
				"        \"number\": \"(857) 476-2230\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"]");
		IGCSUserEntityUtil.setSampleIgcsUser(jsonNode.iterator().next());
		
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
	
	private void searchByStreet() {
		System.out.println("... test Select by street  ...");
		long startTime = System.currentTimeMillis();
		List<IgcsUser> p = repository.searchByStreet("Columbus Pla"); //
		long endTime = System.currentTimeMillis();
		float finalTime = (float)(endTime - startTime)/1000;
		System.out.println(JSONParser.objectToJson(p));
		System.out.println("Total record found : " + p.size());
		System.out.println("Total search time : " + finalTime);		
	}

}
