package com.auth.service.data;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface DataService {

	public JSONObject authenticate(JSONObject userobj) throws ParseException;
}
