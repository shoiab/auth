package com.auth.service.data.impl;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.auth.dbOperation.DbOperationService;
import com.auth.model.UserModel;
import com.auth.service.data.DataService;
import com.auth.utils.Encrypt;
import com.auth.utils.UUIDGeneratorForUser;
import com.google.gson.Gson;

@Service
public class DataServiceImpl implements DataService {
	
	@Autowired
	public Encrypt encryptor;
	
	@Autowired
	private UUIDGeneratorForUser generateuuid;

	@Autowired
	public DbOperationService dbservice;
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	@Override
	public JSONObject authenticate(JSONObject userobj) throws ParseException {
			
			HttpStatus httpstatus = null;
			UserModel user = dbservice.getUserObj(userobj.get("email").toString());
			
			boolean authStatus = encryptor.compareWithEncryptText(userobj.get("password").toString(), user.getPassword());
			JSONObject authResponse = new JSONObject();
			
			
			if(authStatus){
				UUID uuidForUser = generateuuid.generateUUID();
				System.out.println("uuid :: "+uuidForUser);
				
				Gson gson = new Gson();
			    String json = gson.toJson(user);    
			    JSONParser parser = new JSONParser();
			    JSONObject jsonobj = (JSONObject) parser.parse(json);
			    jsonobj.remove("password");

			    System.out.println(jsonobj);
			    
			    httpstatus = setUserInRedis(uuidForUser, jsonobj);
			    authResponse.put("key", "user:"+uuidForUser);
			    authResponse.put("status", HttpStatus.OK.value());
			}else{
				authResponse.put("status", HttpStatus.BAD_GATEWAY.value());
			}
			
			return authResponse;
		}
	
	private HttpStatus setUserInRedis(UUID uuid,final JSONObject usermodel) {
		final String key = String.format("user:%s", uuid);
	
		template.opsForHash().putAll(key, usermodel);
		//template.expire(key, 2, TimeUnit.MINUTES);
		return HttpStatus.OK;
	}
}
