package com.imaginea.dc.api;

import java.io.IOException;
import java.util.List;

import com.imaginea.dc.entities.NewsArticle;

public interface NewsReader {
	
	/* RSS Feeds */
	
	List<NewsArticle> processRSSfeed(String rssFeedUrl) throws IOException;
	
	
	/* Web Crawling for individual Article content */
	
	String crawlUrlForContent(String articleUrl) throws IOException;
	
	
	/* Archive processing */
	
	
	
}
