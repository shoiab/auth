package com.auth.controller;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

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
	
	@Autowired
	DataService dataservice;
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public @ResponseBody JSONObject authenticate(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password)
			throws NoSuchAlgorithmException, ParseException, URISyntaxException {
		System.out.println("email"+email+"password"+password);
		return dataservice.authenticate(email, password);
	}

}
