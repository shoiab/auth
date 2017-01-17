package com.auth.service.data;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface DataService {

	public JSONObject authenticate(String email) throws ParseException, SolrServerException, IOException;
}
