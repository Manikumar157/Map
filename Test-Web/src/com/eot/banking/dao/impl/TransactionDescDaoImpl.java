package com.eot.banking.dao.impl;

import com.eot.banking.dao.TransactionDescDao;
import com.eot.entity.TransactionTypesDesc;
import com.eot.entity.TransactionTypesDescPK;

public class TransactionDescDaoImpl extends BaseDaoImpl implements TransactionDescDao {

	public String getTransactionDescForType( String transactionType, String locale ) {
		
		return getHibernateTemplate().get( TransactionTypesDesc.class, 
				new TransactionTypesDescPK( 
						new Integer( transactionType ), locale)).getDescription();
	}
}
