package com.eot.banking.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.eot.entity.CustomerScsubscription;

public interface BaseDao {
	
    public Serializable save(Object obj);

    public void update(Object obj);

    public void delete(Object obj);
    
    public void deleteList(Set<Object> objs);
    
    public void saveList(List<Object> list);
    
    public void flush();
    
    public void saveOrUpdate(Object obj);
    
    public void merge(Object obj);
    
    public void evict(Object obj);
}