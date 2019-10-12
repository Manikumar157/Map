package com.eot.banking.dto;


public class CustomerProfileDTO {
	
		private String profileId;
		private String profileName;
		private String description;
		private Double authorizedAmount;
		private Integer profileType;
		
		public String getProfileId() {
			return profileId;
		}

		public void setProfileId(String profileId) {
			this.profileId = profileId;
		}

		public String getProfileName() {
			return profileName;
		}

		public void setProfileName(String profileName) {
			this.profileName = profileName;
		}

		public String getDescription() {
       		return description;
       	}
       	
		public void setDescription(String description) {
			this.description = description;
		}

		public Double getAuthorizedAmount() {
			return authorizedAmount;
		}

		public void setAuthorizedAmount(Double authorizedAmount) {
			this.authorizedAmount = authorizedAmount;
		}

		public Integer getProfileType() {
			return profileType;
		}

		public void setProfileType(Integer profileType) {
			this.profileType = profileType;
		}
       
		
}
