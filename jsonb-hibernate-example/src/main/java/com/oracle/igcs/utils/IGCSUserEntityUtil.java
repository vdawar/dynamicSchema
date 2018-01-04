package com.oracle.igcs.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.igcs.constants.Constants;
import com.oracle.igcs.model.IgcsUser;

public class IGCSUserEntityUtil {
	private static IgcsUser IGCS_USER;
	private static Random random = new Random();
	private static String[][] address = {
			{ "123 6th St.", "71 Pilgrim Avenue", "70 Bowman St.South", "11 DavidGasse", "99 MG Road" },
			{ "Melbourne", "Chevy", "Windsor", "Krakow", "Warslow" }, { "FL", "MD", "CT", "CA", "MI" },
			{ "US", "AUS", "IN", "PL", "UAE" }, { "110008", "32904", "20815", "06074", "105974" } };

	public static void setSampleIgcsUser(JsonNode jsonNode) throws JsonParseException, IOException {
		IGCS_USER = getIGCSUser(jsonNode);
	}

	public static List<IgcsUser> getNextBatchSet(int startIndex, int batchSize) throws CloneNotSupportedException {
		List<IgcsUser> personList = new ArrayList<>();
		for (int i = startIndex; i < startIndex + Constants.BATCH_SIZE; i++) {
			IgcsUser p = getModifiedClonedObject(i);
			personList.add(p);
		}
		return personList;
	}

	private static IgcsUser getModifiedClonedObject(int i) throws CloneNotSupportedException {
		IgcsUser p = (IgcsUser) IGCS_USER.clone();
		p.setUserName(i + "-" + p.getUserName());

		if (p.getUserFlexFieldsJSON().containsKey("user_address")) {
			Map<Object, Object> addressMap = (Map<Object, Object>) p.getUserFlexFieldsJSON().get("user_address");
			addressMap.put("street", address[0][random.nextInt(address[0].length)]);
			addressMap.put("city", address[1][random.nextInt(address[1].length)]);
			addressMap.put("state", address[2][random.nextInt(address[2].length)]);
			addressMap.put("country", address[3][random.nextInt(address[3].length)]);
			addressMap.put("zipCode", address[4][random.nextInt(address[4].length)]);
		}
		return p;
	}

	public static IgcsUser getIGCSUser(JsonNode jsonNode) throws JsonParseException, IOException {
		IgcsUser igcsUser = new IgcsUser();

		igcsUser.setUserName(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userName));
		igcsUser.setUserType(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userType));
		igcsUser.setUserStatus(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userStatus));
		igcsUser.setUserEmpType(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userEmpType));
		igcsUser.setUserLogin(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userLogin));
		igcsUser.setUserPassword(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userPassword));
		igcsUser.setUserEmail(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userEmail));
		igcsUser.setUserCreateBy(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userCreateBy));
		igcsUser.setUserManagerName(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userManagerName));
		igcsUser.setUserDeleted(JSONParser.getKnownFieldsFromJSON(jsonNode, Constants.userDeleted));

		Map<String, Object> userFlexFields = JSONParser.getJsonNodeMap(jsonNode);
		igcsUser.setUserFlexFieldsJSON(userFlexFields);

		return igcsUser;
	}

}
