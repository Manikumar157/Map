package com.eot.banking.dto;

public class HelpDeskDTO {
		
		private Integer helpDeskId;
	    private String mobileNumber;	    
	    private String emailId;
	    private Integer status;
	    
		public String getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public Integer getHelpDeskId() {
			return helpDeskId;
		}
		public void setHelpDeskId(Integer helpDeskId) {
			this.helpDeskId = helpDeskId;
		}

}
