package com.auth.solrService;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

public interface SearchHandler {

	SolrDocumentList checkForUser(String email) throws SolrServerException, IOException;

}
