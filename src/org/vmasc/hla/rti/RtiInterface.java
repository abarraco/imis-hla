/*
 * RtiInterface.java
 *
 * Created on Janurary 18, 2011, 1:29 PM
 *
 */

package org.vmasc.hla.rti;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.vmasc.hla.util.Params;

import hla.rti.AttributeHandleSet;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.ResignAction;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.RtiFactory;
import hla.rti.jlc.RtiFactoryFactory;

public class RtiInterface
{
	public class FederateRunnable implements Runnable
	{
		public void run()
		{
			//System.out.println("Running in thread");
			try
			{
				// Create the federation
				createFedExecution();

				// Join the federation
				joinFedEx();

				while (true)
				{
					rtiAmb.tick();
					if (callQuit)
					{
						break;
					}

					Thread.sleep(10);
				}

				// Resign and destroy federation
				resignAndDestroy();
			}
			catch (RTIexception ex)
			{
				System.out.println("RTI Exception (main loop): " + ex.getClass().toString() + " " + ex.toString());
			}
			catch (Exception ex)
			{
				System.out.println("UNKNOWN EXCEPTION:");
				System.out.println("Caught: " + ex.getClass().toString() + " : " + ex.toString());
			}

			return;
		}
		
	}
	
	final int MAX_VALUE_SIZE = 25000; //the maximum size in bytes of an update value
	final int BASE_SIZE = 154;  //the number in bytes of the BaseEntity class without adding extra values
	private final int MAX_JOIN_ATTEMPTS = 10;
	private final String FEDERATION_TYPE = Params.getString("FEDERATION_TYPE");
	private final String FEDERATION_NAME = Params.getString("FEDERATION_NAME");
	private final String FDD_FILE_NAME = Params.getString("FDD_FILE_NAME");
	
	private boolean callQuit = false;
	private RtiFactory rtiFactory;
	private FederateAmbassador federateAmb;
	private FederateAmbassadorData federateAmbData;
	private RTIambassador rtiAmb;
	private SuppliedAttributes attrValues = null;
	
	private FederateCallback federateCallback = null;

	public RtiInterface(FederateCallback federateCallback, int reportInterval)
	{
		this.federateCallback = federateCallback;
		federateAmbData = new FederateAmbassadorData();

		//System.out.println("Running Federate:");

		try
		{
			rtiFactory = RtiFactoryFactory.getRtiFactory();
			rtiAmb = rtiFactory.createRtiAmbassador();
			federateAmb = new FederateAmbassador(rtiAmb, federateCallback);
		}
		catch (RTIexception ex)
		{
			System.out.println("Exception caught getting RTI Factory / creating RTI Ambassador");
			ex.printStackTrace();
			return;
		}
	}

	public void callQuit()
	{
		callQuit = true;
	}

	private void createBaseEntityAttrValues(int clientId, String updateSize)
	{
		int targetValueSize = Integer.parseInt(updateSize) - BASE_SIZE;
		
		attrValues = rtiFactory.createSuppliedAttributes();
		HashMap<String, Integer> attrHandleMap = federateAmbData.getBaseEntityAttrNameHandleMap();
		if(attrHandleMap != null)
		{
			for (Iterator<String> it = attrHandleMap.keySet().iterator(); it.hasNext();)
			{
				Object name = it.next();
				Object handleobj = attrHandleMap.get(name);
				int handle = Integer.parseInt(handleobj.toString());
				
				String nameString = (String) name;
				byte[] nameByteArray;
				if(nameString.equals(FomConstants.BASEENTITY_ATTRIBUTE_CLIENTID))
				{
					nameByteArray = Integer.toString(clientId).getBytes();
				}
				else if(targetValueSize > 0)
				{
					int numBytes =  targetValueSize > MAX_VALUE_SIZE ? MAX_VALUE_SIZE : targetValueSize;
					nameByteArray = new byte[numBytes];
					targetValueSize -= numBytes;
					//System.out.println("nameByteArray.length:" + nameByteArray.length);
				}
				else
				{
					nameByteArray = nameString.getBytes();
				}
				
				attrValues.add(handle, nameByteArray);
			}
		}
		else
		{
			System.out.println("BaseEntityAttrNameHandleMap == null");
		}
	}
	
	private void createFedExecution() throws RTIexception, java.net.MalformedURLException
	{
		//System.out.println("createFederationExecution " + FEDERATION_NAME + " " + FDD_FILE_NAME);

		try
		{
			String theURL = "file:" + FDD_FILE_NAME;
			rtiAmb.createFederationExecution(FEDERATION_NAME, new URL(theURL));
		}
		catch (FederationExecutionAlreadyExists e)
		{
			System.out.println("Could not create Federation Execution: FederationExecutionAlreadyExists: " + e.toString());
		}
		catch (RTIexception ex)
		{
			System.out.println("Could not create Federation: " + ex.getClass().toString() + " : " + ex.toString());
			throw ex;
		}

		// Tick
		rtiAmb.tick();

		//System.out.println("Federation Created.");
		//System.out.println("------------------------------------");
	}
	
	private AttributeHandleSet getAttributeHandleForBaseEntity(int baseEntityClassHandle) 
	{
		AttributeHandleSet attrHandleSet = null;
		if(federateAmbData.getBaseEntityAttributeHandleSet() != null) 
		{
			attrHandleSet = federateAmbData.getBaseEntityAttributeHandleSet();
		}
		else
		{
			try
			{
				int attributeHandleValue = 0;
				attrHandleSet = rtiFactory.createAttributeHandleSet();
				HashMap<String, Integer> attrNameHandleMap = new HashMap<String, Integer>();
				for(String attrName: FomConstants.BASEENTITY_ATTRIBUTE_ARRAY)
				{
					attributeHandleValue = rtiAmb.getAttributeHandle(attrName, baseEntityClassHandle);
					attrNameHandleMap.put(attrName, attributeHandleValue);
					attrHandleSet.add(attributeHandleValue);
				}
				
				federateAmbData.setBaseEntityAttrNameHandleMap(attrNameHandleMap);
				federateAmbData.setBaseEntityAttributeHandleSet(attrHandleSet);
			}
			catch (RTIexception ex)
			{
				System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
				ex.printStackTrace();
			}
		}
		
		return attrHandleSet;
	}
	
	private int getClassHandleForBaseEntity()
	{
		int objectHandle = -1;
		if (federateAmbData.getBaseEntityClassHandle() != -1) 
		{
			objectHandle = federateAmbData.getBaseEntityClassHandle();
		}
		else
		{
			// Get the object class handle
			try 
			{
				objectHandle = rtiAmb.getObjectClassHandle(FomConstants.BASEENTITY_CLASS_NAME);
				federateAmbData.setBaseEntityClassHandle(objectHandle);
			} 
			catch (RTIexception ex) 
			{
				System.out.println("RTI Exception: " + ex.getClass().toString() + " : " + ex.toString());
				System.out.println("Could not get object class handle: " + FomConstants.BASEENTITY_CLASS_NAME);
				ex.printStackTrace();
			}
		}
		
		return objectHandle;
	}
	
	private void joinFedEx() throws RTIexception, java.net.MalformedURLException
	{
		boolean joined = false;
		
		int numTries = 0;
		//System.out.println("joinFederationExecution " + FEDERATION_TYPE + " " + FEDERATION_NAME);

		while (!joined && (numTries++ < MAX_JOIN_ATTEMPTS))
		{
			try
			{
				rtiAmb.joinFederationExecution(FEDERATION_TYPE, FEDERATION_NAME, federateAmb);
				joined = true;
			}
			catch (FederationExecutionDoesNotExist ex)
			{
				System.out.println("FederationExecutionDoesNotExist, try " + numTries + " out of " + MAX_JOIN_ATTEMPTS);
				continue;
			}
			catch (RTIexception ex)
			{
				System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
				throw ex;
			}

			rtiAmb.tick();
		}

		if (joined)
		{
			//System.out.println("Joined Federation.");
			federateCallback.federationJoined();
		}
		else
		{
			System.out.println("Giving up.");
			rtiAmb.destroyFederationExecution(FEDERATION_NAME);
			return;
		}
	}
	
	public void publish()
	{
		publishBaseEntity();
	}
	
	private boolean publishBaseEntity() 
	{
		boolean success = false;
		int baseEntityClassHandle = this.getClassHandleForBaseEntity();
		AttributeHandleSet attributeHandleSet = this.getAttributeHandleForBaseEntity(baseEntityClassHandle);
		if(attributeHandleSet != null)
		{
			try
			{
				rtiAmb.publishObjectClass(baseEntityClassHandle, attributeHandleSet);
				rtiAmb.tick();
				//System.out.println("Publishing BaseEntity");
				success = true;
			}
			catch (RTIexception ex)
			{
				System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
				ex.printStackTrace();
			}
		}
		return success;
	}
	
	public void register()
	{
		registerBaseEntity();
	}

	private boolean registerBaseEntity() 
	{
		boolean success = false;
		try
		{
			int instanceHandle = rtiAmb.registerObjectInstance(federateAmbData.getBaseEntityClassHandle());
			federateAmbData.setBaseEntityInstanceHandle(instanceHandle);
			//System.out.println("BaseEntity Registered");
			rtiAmb.tick();
			success = true;
		}
		catch (RTIexception ex)
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
			ex.printStackTrace();
		}
		return success;
	}

	private void resignAndDestroy() throws RTIexception
	{
		rtiAmb.resignFederationExecution(ResignAction.DELETE_OBJECTS);
		rtiAmb.destroyFederationExecution(FEDERATION_NAME);
	}
	
	public void startFederate()
	{
		Thread federateThread = new Thread(new FederateRunnable());
		federateThread.start();
	}
	
	public void subscribe(int numSubscriptions)
	{
		subscribeBaseEntity();
		subscribeAdditional(numSubscriptions);
	}
	
	private void subscribeAdditional(int numSubscriptions)
	{
		try
		{
			//subscribe to any number of subscriptions beyond 1
			for(int i = 1; i < numSubscriptions; i++)
			{
				int objectHandle = rtiAmb.getObjectClassHandle(FomConstants.BASEENTITY_CLASS_NAME + Integer.toString(i));
				AttributeHandleSet attrHandleSet = rtiFactory.createAttributeHandleSet();
				String attrName = FomConstants.BASEENTITY_ATTRIBUTE_ACCELERATIONVECTOR + Integer.toString(i);
				int attributeHandleValue = rtiAmb.getAttributeHandle(attrName, objectHandle);
				attrHandleSet.add(attributeHandleValue);
				rtiAmb.subscribeObjectClassAttributes(objectHandle, attrHandleSet);	
			}
			
			rtiAmb.tick();
			//System.out.println("Subscribing to additional classes.");
		} 
		catch (RTIexception ex) 
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " : " + ex.toString());
			ex.printStackTrace();
		}
	}

	private boolean subscribeBaseEntity() 
	{
		boolean success = false;
		int baseEntityObjectClassHandle = this.getClassHandleForBaseEntity();
		AttributeHandleSet attributeHandleSet = this.getAttributeHandleForBaseEntity(baseEntityObjectClassHandle);
		if(attributeHandleSet != null)
		{
			try
			{
				rtiAmb.subscribeObjectClassAttributes(baseEntityObjectClassHandle, attributeHandleSet);
				rtiAmb.tick();
				//System.out.println("Subscribing BaseEntity");
				success = true;
			}
			catch (RTIexception ex)
			{
				System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
				ex.printStackTrace();
			}
		}
		return success;
	}
	
	public void unpublish()
	{
		unpublishBaseEntity();
	}
	
	private void unpublishBaseEntity() 
	{
		int classHandle = this.getClassHandleForBaseEntity();
		try
		{
			rtiAmb.unpublishObjectClass(classHandle);
			//System.out.println("Unpublishing BaseEntity");
		}
		catch (RTIexception ex)
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void unregister()
	{
		unregisterBaseEntity();
	}
	
	private void unregisterBaseEntity() 
	{
		try
		{
			rtiAmb.deleteObjectInstance(federateAmbData.getBaseEntityInstanceHandle(), new String("Removing BaseEntity").getBytes());	
			//System.out.println("Unregistering BaseEntity");
		}
		catch (RTIexception ex)
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void unsubscribe()
	{
		unsubscribeBaseEntity();
	}
	
	private void unsubscribeBaseEntity() 
	{
		int classHandle = this.getClassHandleForBaseEntity();
		try
		{
			rtiAmb.unsubscribeObjectClass(classHandle);
			//System.out.println("Unsubscribing BaseEntity");
		}
		catch (RTIexception ex)
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void updateAttributes(long currentTime, int clientId, String updateSize)
	{
		// Update the object
		if(attrValues == null)
			createBaseEntityAttrValues(clientId, updateSize);
		
		try
		{
			String strCurrentTime = Long.toString(currentTime);
			rtiAmb.updateAttributeValues(federateAmbData.getBaseEntityInstanceHandle(), attrValues, strCurrentTime.getBytes());
		}
		catch (RTIexception ex)
		{
			System.out.println("RTI Exception: " + ex.getClass().toString() + " " + ex.toString());
			ex.printStackTrace();
		}
	}
}
