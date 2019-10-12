package com.eot.banking.utils;

import java.util.List;

public class Page {

	public Integer startPage = 1 ;
	public Integer endPage = 1 ;
	public Integer currentPage = 1 ;
	public Integer resultsPerPage = 2 ;
	public Integer totalCount = 0 ;
	public Integer totalPages = 0 ;
	public String requestPage = null ;
	
	public List results = null ;
	
	public Integer getStartPage() {
		return startPage;
	}
	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}
	public Integer getEndPage() {
		return endPage;
	}
	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}
	public Integer getResultsPerPage() {
		return resultsPerPage;
	}
	public void setResultsPerPage(Integer resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public List getResults() {
		return results;
	}
	public void setResults(List results) {
		this.results = results;
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public String getRequestPage() {
		return requestPage;
	}
	public void setRequestPage(String requestPage) {
		this.requestPage = requestPage;
	}
}
