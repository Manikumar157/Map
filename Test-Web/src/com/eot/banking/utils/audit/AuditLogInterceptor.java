package com.eot.banking.utils.audit;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import com.eot.entity.AuditLog;
import com.eot.entity.Entity;

public class AuditLogInterceptor extends EmptyInterceptor
{

	private static final long serialVersionUID = 936338274997153375L;
	private Logger log;
	private SessionFactory sessionFactory;
	private static final String UPDATE = "update";
	private static final String INSERT = "insert";
	private static final String DELETE = "delete";

	private ThreadLocal<HashSet<AuditLog>> inserts = new ThreadLocal<HashSet<AuditLog>>();
	private ThreadLocal<HashSet<AuditLog>> deletes = new ThreadLocal<HashSet<AuditLog>>();
	private ThreadLocal<HashSet<AuditLog>> updates = new ThreadLocal<HashSet<AuditLog>>();

	private List auditableClassList;

	public void setAuditableClassList(List auditableClassList) {
		this.auditableClassList = auditableClassList;
	}

	public void setSessionFactory(SessionFactory sessionfactory)
	{
		sessionFactory = sessionfactory;
	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
			throws CallbackException
			{

		updates.set(new HashSet<AuditLog>());

		if( CollectionUtils.contains(auditableClassList.iterator(), entity.getClass().getSimpleName()) ) {
			org.hibernate.classic.Session session = sessionFactory.openSession();
			Class class1 = entity.getClass();
			String s = class1.getName();
			String as1[] = s.split("\\.");
			int i = as1.length - 1;
			s = as1[i];
			Serializable serializable1 = getObjectId(entity);
			Object obj1 = session.get(class1, id);
			try
			{
				logChanges(entity, obj1, null, serializable1.toString(), "update", s);
			}
			catch(IllegalArgumentException illegalargumentexception)
			{
				illegalargumentexception.printStackTrace();
			}
			catch(IllegalAccessException illegalaccessexception)
			{
				illegalaccessexception.printStackTrace();
			}
			catch(InvocationTargetException invocationtargetexception)
			{
				invocationtargetexception.printStackTrace();
			}
			session.close();
		}
		return false;
			}


	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		inserts.set(new HashSet<AuditLog>());

		if( CollectionUtils.contains(auditableClassList.iterator(), entity.getClass().getSimpleName()) ) {
			try
			{
				Class class1 = entity.getClass();
				String s = class1.getName();
				String as1[] = s.split("\\.");
				int i = as1.length - 1;
				s = as1[i];
				logChanges(entity, null, null, null, "insert", s);
			}
			catch(IllegalArgumentException illegalargumentexception)
			{
				illegalargumentexception.printStackTrace();
			}
			catch(IllegalAccessException illegalaccessexception)
			{
				illegalaccessexception.printStackTrace();
			}
			catch(InvocationTargetException invocationtargetexception)
			{
				invocationtargetexception.printStackTrace();
			}
		}
		return false;

	}

	public void onDelete(Object entity, Serializable serializable, Object aobj[], String as[], Type atype[])
			throws CallbackException
			{
		deletes.set(new HashSet<AuditLog>());

		if( CollectionUtils.contains(auditableClassList.iterator(), entity.getClass().getSimpleName()) ) 
			try
		{
				Class class1 = entity.getClass();
				String s = class1.getName();
				String as1[] = s.split("\\.");
				int i = as1.length - 1;
				s = as1[i];
				logChanges(entity, null, null, serializable.toString(), "delete", s);
		}
		catch(IllegalArgumentException illegalargumentexception)
		{
			illegalargumentexception.printStackTrace();
		}
		catch(IllegalAccessException illegalaccessexception)
		{
			illegalaccessexception.printStackTrace();
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			invocationtargetexception.printStackTrace();
		}
			}

	public void postFlush(Iterator iterator) throws CallbackException {
		org.hibernate.classic.Session session = sessionFactory.openSession();
		try
		{
			if(inserts.get()!=null){
				AuditLog auditlogrecord;
				for(Iterator iterator1 = inserts.get().iterator(); iterator1.hasNext(); session.save(auditlogrecord))
				{
					auditlogrecord = (AuditLog)iterator1.next();
					auditlogrecord.setEntityId(getObjectId(auditlogrecord.getEntity()).toString());
				}
			}
			if(updates.get()!=null){
				AuditLog auditlogrecord1;
				for(Iterator iterator2 = updates.get().iterator(); iterator2.hasNext(); session.save(auditlogrecord1))
					auditlogrecord1 = (AuditLog)iterator2.next();
			}
			if(deletes.get()!=null){
				AuditLog auditlogrecord2;
				for(Iterator iterator3 = deletes.get().iterator(); iterator3.hasNext(); session.save(auditlogrecord2))
					auditlogrecord2 = (AuditLog)iterator3.next();
			}
		}
		catch(HibernateException hibernateexception)
		{
			throw new CallbackException(hibernateexception);
		}
		finally
		{
			if(inserts.get()!=null){
				inserts.get().clear();
			}
			if(updates.get()!=null){
				updates.get().clear();
			}
			if(deletes.get()!=null){
				deletes.get().clear();
			}
			inserts.remove();
			updates.remove();
			deletes.remove();
			session.flush();
			session.close();
		}
	}

	/**
	 * Logs changes to persistent data
	 * @param newObject the object being saved, updated or deleted
	 * @param existingObject the existing object in the database.  Used only for updates
	 * @param parentObject the parent object. Set only if passing a Component object as the newObject
	 * @param persistedObjectId the id of the persisted object.  Used only for update and delete
	 * @param event the type of event being logged.  Valid values are "update", "delete", "save"
	 * @param className the name of the class being logged.  Used as a reference in the auditLogRecord
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("all")
	private void logChanges(Object newObject, Object existingObject, Object parentObject, String persistedObjectId, String event, String className)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException  {

		try {
			Class objectClass = newObject.getClass();      
			//get an array of all fields in the class including those in superclasses if this is a subclass.
			Field[] fields = getAllFields(objectClass, null);

			// Iterate through all the fields in the object

			fieldIteration: for (int ii = 0; ii < fields.length; ii++) {

				//make private fields accessible so we can access their values
				fields[ii].setAccessible(true);

				//if the current field is static, transient or final then don't log it as 
				//these modifiers are v.unlikely to be part of the data model.
				if(Modifier.isTransient(fields[ii].getModifiers())
						|| Modifier.isFinal(fields[ii].getModifiers())
						|| Modifier.isStatic(fields[ii].getModifiers())) {
					continue fieldIteration;
				}

				String fieldName = fields[ii].getName();

				//System.out.println("fieldName : " + fieldName );

				Class interfaces[] = fields[ii].getType().getInterfaces();
				for (int i = 0; i < interfaces.length;i++) {

					//System.out.println("interface : " + interfaces[i].getName() );

					if (interfaces[i].getName().equals("java.util.Collection")) {
						continue fieldIteration;
						//If the field is a class that is a component (Hibernate mapping type) then iterate through its fields and log them
					} else if(interfaces[i].getName().equals("com.thinkways.gim.entity.Entity")){

						Object newComponent = fields[ii].get(newObject);
						Object existingComponent = null;

						if(event.equals(UPDATE)) {
							existingComponent = fields[ii].get(existingObject);
							if(existingComponent != null && newComponent != null  ){

								String newObjId = ((Entity)newComponent).getEntityId().toString() ;
								String existingObjId = ((Entity)existingComponent).getEntityId().toString() ;

								if(! newObjId.equals(existingObjId) ){
									AuditLog logRecord = new AuditLog();
									logRecord.setEntityName(className);
									logRecord.setEntityAttribute(fieldName);
									logRecord.setMessage(event);
									logRecord.setUpdatedBy(this.getUserName());
									logRecord.setUpdatedDate(new Date());
									logRecord.setNewValue(newObjId);
									logRecord.setOldValue(existingObjId);
									logRecord.setEntityId(persistedObjectId);
									if(parentObject == null) {
										logRecord.setEntity((Entity) newObject);
									} else {
										logRecord.setEntity((Entity) parentObject);
									}

									updates.get().add(logRecord);

								}

							} 

						}

						continue fieldIteration;

					}

				}

				String propertyNewState;
				String propertyPreUpdateState;

				//get new field values
				try {
					Object objPropNewState = fields[ii].get(newObject);
					if (objPropNewState != null) {
						propertyNewState = objPropNewState.toString();
					} else {
						propertyNewState = "";
					}

				} catch (Exception e) {
					propertyNewState = "";
				}

				if(event.equals(UPDATE)) {

					try {
						Object objPreUpdateState = fields[ii].get(existingObject);
						if (objPreUpdateState != null) {
							propertyPreUpdateState = objPreUpdateState.toString();
						} else {
							propertyPreUpdateState = "";
						}
					} catch (Exception e) {
						propertyPreUpdateState = "";
					}

					// Now we have the two property values - compare them
					if (propertyNewState.equals(propertyPreUpdateState)) {
						continue; // Values haven't changed so loop to next property
					} else  {
						AuditLog logRecord = new AuditLog();
						logRecord.setEntityName(className);
						logRecord.setEntityAttribute(fieldName);
						logRecord.setMessage(event);
						logRecord.setUpdatedBy(this.getUserName());
						logRecord.setUpdatedDate(new Date());
						logRecord.setNewValue(propertyNewState);
						logRecord.setOldValue(propertyPreUpdateState);
						logRecord.setEntityId(persistedObjectId);
						if(parentObject == null) {
							logRecord.setEntity((Entity) newObject);
						} else {
							logRecord.setEntity((Entity) parentObject);
						}

						updates.get().add(logRecord);

					}


				} else if(event.equals(DELETE)) {
					Object returnValue = fields[ii].get(newObject);

					AuditLog logRecord = new AuditLog();
					logRecord.setEntityName(className);
					logRecord.setEntityAttribute(fieldName); 
					logRecord.setMessage(event);
					logRecord.setUpdatedBy(this.getUserName());
					logRecord.setUpdatedDate(new Date());
					logRecord.setNewValue("");
					if (returnValue != null)
						logRecord.setOldValue(returnValue.toString());
					if (persistedObjectId != null)
						logRecord.setEntityId(persistedObjectId);

					if(parentObject == null) {
						logRecord.setEntity((Entity) newObject);
					} else {
						logRecord.setEntity((Entity) parentObject);
					}

					deletes.get().add(logRecord);

				} else if(event.equals(INSERT)) {

					Object returnValue = fields[ii].get(newObject);

					AuditLog logRecord = new AuditLog();
					logRecord.setEntityName(className);
					logRecord.setEntityAttribute(fieldName); 
					logRecord.setMessage(event);
					logRecord.setUpdatedBy(this.getUserName());
					logRecord.setUpdatedDate(new Date());
					logRecord.setOldValue("");

					if (returnValue != null) {
						logRecord.setNewValue(returnValue.toString());
					} else
						logRecord.setNewValue("");

					if(parentObject == null) {
						logRecord.setEntity((Entity) newObject);
					} else {
						logRecord.setEntity((Entity) parentObject);
					}

					inserts.get().add(logRecord);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Returns an array of all fields used by this object from it's class and all superclasses.
	 * @param objectClass the class 
	 * @param fields the current field list
	 * @return an array of fields
	 */
	private Field[] getAllFields(Class objectClass, Field[] fields) {

		Field[] newFields = objectClass.getDeclaredFields();

		int fieldsSize = 0;
		int newFieldsSize = 0;

		if(fields != null) {
			fieldsSize = fields.length;
		}
		if(newFields != null) {
			newFieldsSize = newFields.length;
		}

		Field[] totalFields = new Field[fieldsSize + newFieldsSize];
		if(fieldsSize > 0) {
			System.arraycopy(fields, 0, totalFields, 0, fieldsSize);
		}

		if(newFieldsSize > 0) { 
			System.arraycopy(newFields, 0, totalFields, fieldsSize, newFieldsSize);
		}
		Class superClass = objectClass.getSuperclass();

		Field[] finalFieldsArray;

		if (superClass != null && ! superClass.getName().equals("java.lang.Object")) {
			finalFieldsArray = getAllFields(superClass, totalFields);
		} else {
			finalFieldsArray = totalFields;
		}

		return finalFieldsArray;
	}

	/**
	 * Gets the id of the persisted object
	 * @param obj the object to get the id from
	 * @return object Id
	 */
	private Serializable getObjectId(Object obj) {
		Class objectClass = obj.getClass();
		Method[] methods = objectClass.getMethods();

		Serializable persistedObjectId = null;
		for (int ii = 0; ii < methods.length; ii++) {
			// If the method name equals 'getId' then invoke it to get the id of the object.
			if (methods[ii].getName().equals("getEntityId")) {
				try {
					persistedObjectId = (Serializable)methods[ii].invoke(obj, null);
					break;      
				} catch (Exception e) {
					log.warn("Audit Log Failed - Could not get persisted object id: " + e.getMessage());
				}
			}
		}
		return persistedObjectId;
	}


	private String getUserName()
	{

		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext == null) {
			return "anonymousUser";
		}
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null)
		{
			String s = null;
			if(authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userdetails = (UserDetails)authentication.getPrincipal();
				s = userdetails.getUsername();
			} else {
				s = authentication.getPrincipal().toString();
			}
			if(s == null || s.equals(""))
				return "anonymousUser";
			else
				return s;
		} else
		{
			return "anonymousUser";
		}
	}

}