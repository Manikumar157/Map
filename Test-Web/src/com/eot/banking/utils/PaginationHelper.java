package com.eot.banking.utils;

import java.util.List;

public class PaginationHelper {

	public static Page getPage( List list , Integer totalCount, Integer resultsPerPage, Integer pageNumber ){

		Page page = new Page(); 
		page.resultsPerPage = resultsPerPage ;
		page.totalCount = totalCount ;
		page.totalPages = (int) Math.ceil( page.totalCount.floatValue() / page.resultsPerPage.floatValue());
		page.currentPage = pageNumber > page.totalPages || pageNumber < 1 ? 1 : pageNumber ;

		page.startPage = ( page.currentPage - 4 > 1 ) ? page.currentPage - 4 : 1 ;
		page.endPage = ( page.startPage + 9 < page.totalPages ) ? page.startPage + 9 : page.totalPages ;

		page.results = list ;

		return page; 

	}

	public static Page getPage( List list , Integer resultsPerPage, Integer pageNumber ){

		Page page = new Page(); 
		page.resultsPerPage = resultsPerPage ;
		page.totalCount = list.size() ;
		page.totalPages = (int) Math.ceil( page.totalCount.floatValue() / page.resultsPerPage.floatValue());
		page.currentPage = pageNumber > page.totalPages || pageNumber < 1 ? 1 : pageNumber ;

		page.startPage = ( page.currentPage - 4 > 1 ) ? page.currentPage - 4 : 1 ;
		page.endPage = ( page.startPage + 9 < page.totalPages ) ? page.startPage + 9 : page.totalPages ;

		page.results = list.subList((page.currentPage-1)*resultsPerPage, page.currentPage*resultsPerPage > page.totalCount ? page.totalCount : page.currentPage*resultsPerPage ) ;

		return page; 

	}

}
