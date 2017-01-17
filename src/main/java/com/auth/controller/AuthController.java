package com.auth.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.data.DataService;

@RestController
public class AuthController {
	
	public static final Logger logger = Logger.getLogger(AuthController.class.getName());
	
	@Autowired
	DataService dataservice;
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public @ResponseBody JSONObject authenticate(
			@RequestParam(value = "email") String email)
			throws NoSuchAlgorithmException, ParseException, URISyntaxException, SolrServerException, IOException {
		logger.info("email"+email);
		return dataservice.authenticate(email);
	}

}
