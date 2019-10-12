package com.eot.banking.dao;

import java.util.List;

import com.eot.entity.PendingTransaction;

public interface SchedularDAO extends BaseDao {

	List<PendingTransaction> getUnprocessedPendingTxn();

	void updatePendingTxnByIds(String transactionIds);

}
