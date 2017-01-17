package com.auth.service.data.impl;

import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.auth.config.SolrConfig;
import com.auth.dbOperation.DbOperationService;
import com.auth.model.UserModel;
import com.auth.service.data.DataService;
import com.auth.solrService.SearchHandler;
import com.auth.utils.Encrypt;
import com.auth.utils.UUIDGeneratorForUser;
import com.google.gson.Gson;

@Service
public class DataServiceImpl implements DataService {
	
	private static final Logger logger = Logger.getLogger(DataServiceImpl.class.getName());
	
	@Autowired
	public Encrypt encryptor;
	
	@Autowired
	private UUIDGeneratorForUser generateuuid;

	@Autowired
	public DbOperationService dbservice;
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	@Autowired
	private SearchHandler solrservice;
	
	@Override
	public JSONObject authenticate(String email) throws ParseException, SolrServerException, IOException {
			
			HttpStatus httpstatus = null;
			/*UserModel user = dbservice.getUserObj(email);
			
			boolean authStatus = encryptor.compareWithEncryptText(password, user.getPassword());*/
			
			JSONObject authResponse = new JSONObject();
			SolrDocumentList userdoc = solrservice.checkForUser(email);
			boolean authStatus = false;
			
			if(userdoc.size() > 0){
				authStatus = true;
			}
			
			logger.info("user name :: "+userdoc.get(0).get("emp_name"));
			
			
			if(authStatus){
				UUID uuidForUser = generateuuid.generateUUID();
				System.out.println("uuid :: "+uuidForUser);
				
				Gson gson = new Gson();
			    String json = gson.toJson(userdoc.get(0));    
			    JSONParser parser = new JSONParser();
			    JSONObject jsonobj = (JSONObject) parser.parse(json);
			    //jsonobj.remove("password");

			    logger.info(jsonobj);
			    
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
