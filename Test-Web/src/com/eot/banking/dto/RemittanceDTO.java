package com.eot.banking.dto;

// TODO: Auto-generated Javadoc
/**
 * The Class RemittanceDTO.
 * @author Sudhanshu
 *
 */
public class RemittanceDTO {

/** The remittance company id. */
private Integer remittanceCompanyId;

/** The remittance company name. */
private String remittanceCompanyName;

/** The remittance transfer type. */
private Integer[] remittanceTransferType;

/** The remittance status. */
private Integer remittanceStatus;

/**
 * Gets the remittance company id.
 *
 * @return the remittance company id
 */
public Integer getRemittanceCompanyId() {
	return remittanceCompanyId;
}

/**
 * Sets the remittance company id.
 *
 * @param remittanceCompanyId the new remittance company id
 */
public void setRemittanceCompanyId(Integer remittanceCompanyId) {
	this.remittanceCompanyId = remittanceCompanyId;
}

/**
 * Gets the remittance company name.
 *
 * @return the remittance company name
 */
public String getRemittanceCompanyName() {
	return remittanceCompanyName;
}

/**
 * Sets the remittance company name.
 *
 * @param remittanceCompanyName the new remittance company name
 */
public void setRemittanceCompanyName(String remittanceCompanyName) {
	this.remittanceCompanyName = remittanceCompanyName;
}

/**
 * Gets the remittance transfer type.
 *
 * @return the remittance transfer type
 */
public Integer[] getRemittanceTransferType() {
	return remittanceTransferType;
}

/**
 * Sets the remittance transfer type.
 *
 * @param remittanceTransferType the new remittance transfer type
 */
public void setRemittanceTransferType(Integer[] remittanceTransferType) {
	this.remittanceTransferType = remittanceTransferType;
}

/**
 * Gets the remittance status.
 *
 * @return the remittance status
 */
public Integer getRemittanceStatus() {
	return remittanceStatus;
}

/**
 * Sets the remittance status.
 *
 * @param remittanceStatus the new remittance status
 */
public void setRemittanceStatus(Integer remittanceStatus) {
	this.remittanceStatus = remittanceStatus;
}

}
