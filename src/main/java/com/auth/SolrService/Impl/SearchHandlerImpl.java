package com.auth.SolrService.Impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.config.SolrConfig;
import com.auth.solrService.SearchHandler;

@Service
public class SearchHandlerImpl implements SearchHandler{
	
	private static final Logger logger = Logger.getLogger(SearchHandlerImpl.class.getName());
	
	@Autowired
	private SolrConfig solrconfig;

	@Override
	public SolrDocumentList checkForUser(String email) throws SolrServerException, IOException {
		
		HttpSolrClient server = solrconfig.getSolrClient();
		
		SolrDocumentList docuser = new SolrDocumentList();
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery.setQuery("email:(" + email.toLowerCase() + ")" );
		// solrQuery.setFields("tagName","tagValue");

		QueryResponse rsp = server.query(solrQuery, METHOD.POST);
		logger.info("query = " + solrQuery.toString());
		docuser = rsp.getResults();
		
		return docuser;
	}

}
