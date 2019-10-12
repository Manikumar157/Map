package com.eot.banking.dto;


public class SettlementDto {
		
		private String fromAccountNo;
		private String toAccountNo;
		private int fromAccountHeadId;
		private int toAccountHeadId;
		private String settlementParty;
		private boolean isCredit;
		
		public String getFromAccountNo() {
			return fromAccountNo;
		}
		public void setFromAccountNo(String fromAccountNo) {
			this.fromAccountNo = fromAccountNo;
		}
		public String getToAccountNo() {
			return toAccountNo;
		}
		public void setToAccountNo(String toAccountNo) {
			this.toAccountNo = toAccountNo;
		}
		public int getFromAccountHeadId() {
			return fromAccountHeadId;
		}
		public void setFromAccountHeadId(int fromAccountHeadId) {
			this.fromAccountHeadId = fromAccountHeadId;
		}
		public int getToAccountHeadId() {
			return toAccountHeadId;
		}
		public void setToAccountHeadId(int toAccountHeadId) {
			this.toAccountHeadId = toAccountHeadId;
		}
		public String getSettlementParty() {
			return settlementParty;
		}
		public void setSettlementParty(String settlementParty) {
			this.settlementParty = settlementParty;
		}
		public boolean isCredit() {
			return isCredit;
		}
		public void setCredit(boolean isCredit) {
			this.isCredit = isCredit;
		}
	}