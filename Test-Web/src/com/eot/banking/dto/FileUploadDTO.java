/*
 * 
 */
package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUploadDTO.
 */
public class FileUploadDTO {

/** The balance file. */
private MultipartFile balanceFile;

/** The agent file. */
private MultipartFile agentFile;

/** The file. */
private MultipartFile[] file;

/** The voice zip file. */
private MultipartFile voiceZipFile;

private String failedFileName;

@DateTimeFormat(pattern = "dd/MM/yyyy")
private Date fromDate;

/** The registered to date. */
@DateTimeFormat(pattern = "dd/MM/yyyy")
private Date toDate;

private Integer searchPage = 0;

/**
 * Gets the balance file.
 *
 * @return the balance file
 */
public MultipartFile getBalanceFile() {
	return balanceFile;
}

/**
 * Sets the balance file.
 *
 * @param balanceFile the new balance file
 */
public void setBalanceFile(MultipartFile balanceFile) {
	this.balanceFile = balanceFile;
}

/**
 * Gets the agent file.
 *
 * @return the agent file
 */
public MultipartFile getAgentFile() {
	return agentFile;
}

/**
 * Sets the agent file.
 *
 * @param agentFile the new agent file
 */
public void setAgentFile(MultipartFile agentFile) {
	this.agentFile = agentFile;
}

/**
 * Gets the file.
 *
 * @return the file
 */
public MultipartFile[] getFile() {
	return file;
}

/**
 * Sets the file.
 *
 * @param file the new file
 */
public void setFile(MultipartFile[] file) {
	this.file = file;
}

/**
 * Gets the voice zip file.
 *
 * @return the voice zip file
 */
public MultipartFile getVoiceZipFile() {
	return voiceZipFile;
}

/**
 * Sets the voice zip file.
 *
 * @param voiceZipFile the new voice zip file
 */
public void setVoiceZipFile(MultipartFile voiceZipFile) {
	this.voiceZipFile = voiceZipFile;
}

public String getFailedFileName() {
	return failedFileName;
}

public void setFailedFileName(String failedFileName) {
	this.failedFileName = failedFileName;
}

public Date getFromDate() {
	return fromDate;
}

public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
}

public Date getToDate() {
	return toDate;
}

public void setToDate(Date toDate) {
	this.toDate = toDate;
}

public Integer getSearchPage() {
	return searchPage;
}

public void setSearchPage(Integer searchPage) {
	this.searchPage = searchPage;
}

}
