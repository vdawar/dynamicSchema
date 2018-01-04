package com.oracle.igcs.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oracle.igcs.constants.Constants;

/**
 * @author vdawar
 *
 */
public class JSONParser {
	
	private static JsonNode jsonNode;
	private static Iterator<JsonNode> jsonNodeIterator;

	public static void main(String[] args) throws JsonParseException, IOException {
		readJsonFile(Constants.FILE_NAME);
		getJsonNodeMap(jsonNode);
	}

	public static Map<String, Object> getJsonNodeMap(JsonNode jsonNode) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(jsonNode, Map.class);
		return result;
	}

	static void readJsonData(JsonNode jsonNode) {
		Iterator<Map.Entry<String, JsonNode>> ite = jsonNode.fields();
		while (ite.hasNext()) {
			Map.Entry<String, JsonNode> entry = ite.next();
			if (entry.getValue().isObject()) {
				readJsonData(entry.getValue());
			} else {
				System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
			}
		}
	}

	public static String objectToJson(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		try {
			// Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonInString;
	}

	public static void readJsonFile(String fileName) throws IOException, JsonParseException {
		JsonFactory jsonFactory = new JsonFactory();
		ClassLoader classLoader = JSONParser.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		JsonParser jp = jsonFactory.createJsonParser(file);
		jp.setCodec(new ObjectMapper());
		jsonNode = jp.readValueAsTree();
		jsonNodeIterator = jsonNode.iterator();
	}
	
	public static JsonNode nextJsonNode() {
		return jsonNodeIterator.hasNext() ? jsonNodeIterator.next() : null;
	}
	
	public static String getKnownFieldsFromJSON(JsonNode jsonNode, String field) {
		String value = null;
		if(jsonNode != null) {
			value = jsonNode.findPath(field).textValue();
			if (jsonNode instanceof ObjectNode) {
		        ObjectNode object = (ObjectNode) jsonNode;
		        object.remove(field);
		    }
			return value;
		}
		return null;
	}

}
