package com.auth.dbOperation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth.dbOperation.DbOperationService;
import com.auth.model.UserModel;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class DbOperationServiceImpl implements DbOperationService {

	@Autowired
	private Environment environment;

	@Autowired
	private MongoClient mongoClient;

	public UserModel getUserObj(String email) {
		MongoDatabase db = mongoClient.getDatabase(environment
				.getProperty("mongo.dataBase"));

		MongoCollection<BasicDBObject> coll = db.getCollection(
				environment.getProperty("mongo.userCollection"),
				BasicDBObject.class);
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", email);

		FindIterable<BasicDBObject> obj = coll.find(whereQuery);
		UserModel userModel = new UserModel();
		if (obj.first() != null) {
			userModel = (UserModel) (new Gson()).fromJson(obj.first().toString(),
					UserModel.class);
		}
		return userModel;
	}
}
