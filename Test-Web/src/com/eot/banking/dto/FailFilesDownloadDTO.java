package com.eot.banking.dto;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class FailFilesDownloadDTO.
 */
public class FailFilesDownloadDTO {
	
	/** The fail file name. */
	private String failFileName;
	
	/** The modified date. */
	private String modifiedDate;
	
	private String folderName;
	
	/** The fail count. */
	private Integer failCount;
	
	/** The success count. */
	private Integer successCount;
	
	/** The process status. */
	private Integer processStatus;
	
	/** The created date. */
	private Date createdDate;

	/**
	 * Gets the fail file name.
	 *
	 * @return the fail file name
	 */
	public String getFailFileName() {
		return failFileName;
	}

	/**
	 * Sets the fail file name.
	 *
	 * @param failFileName the new fail file name
	 */
	public void setFailFileName(String failFileName) {
		this.failFileName = failFileName;
	}

	/**
	 * Gets the modified date.
	 *
	 * @return the modified date
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modified date.
	 *
	 * @param modifiedDate the new modified date
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * Gets the fail count.
	 *
	 * @return the fail count
	 */
	public Integer getFailCount() {
		return failCount;
	}

	/**
	 * Sets the fail count.
	 *
	 * @param failCount the new fail count
	 */
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	/**
	 * Gets the success count.
	 *
	 * @return the success count
	 */
	public Integer getSuccessCount() {
		return successCount;
	}

	/**
	 * Sets the success count.
	 *
	 * @param successCount the new success count
	 */
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	/**
	 * Gets the process status.
	 *
	 * @return the process status
	 */
	public Integer getProcessStatus() {
		return processStatus;
	}

	/**
	 * Sets the process status.
	 *
	 * @param processStatus the new process status
	 */
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
