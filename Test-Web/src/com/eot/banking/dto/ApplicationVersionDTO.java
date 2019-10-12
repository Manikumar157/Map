package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ApplicationVersionDTO {
	
	private Integer versionId;

	private String versionNumber;

	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date releaseDate;

	private Integer releaseNumber;

	private String channel;

	private String moduleName;

	private String functionalityName;

	private String currentVersion;

	private String description;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(Integer releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFunctionalityName() {
		return functionalityName;
	}

	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the versionId
	 */
	public Integer getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId the versionId to set
	 */
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}


}
