package org.vmasc.hla.rti;

import java.util.Calendar;

import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotKnown;
import hla.rti.CouldNotDiscover;
import hla.rti.CouldNotRestore;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateInternalError;
import hla.rti.FederateNotExecutionMember;
import hla.rti.FederateOwnsAttributes;
import hla.rti.InteractionClassNotKnown;
import hla.rti.InteractionParameterNotKnown;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectClassNotKnown;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.SpecifiedSaveLabelDoesNotExist;
import hla.rti.UnableToPerformSave;
import hla.rti.jlc.NullFederateAmbassador;

public class FederateAmbassador extends NullFederateAmbassador
{
	private RTIambassador rtiAmb;
	private FederateCallback federateCallback;
	private int clientIdAttrIndex = -1;

	FederateAmbassador(RTIambassador rtiAmb, FederateCallback federateCallback)
	{
		this.rtiAmb = rtiAmb;
		this.federateCallback = federateCallback;
	}

	public void discoverObjectInstance(int theObject, // supplied C1
			int theObjectClass, // supplied C1
			String theObjectName) // supplied C4
			throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Discovered object: " + theObjectName + " of class: " + theObjectClass + " with handle: " + theObject);
		System.out.println(".................................");
	}

	public void federationNotRestored() throws FederateInternalError
	{
		
		System.out.println(".................................");
		System.out.println("federationNotRestored ");
		System.out.println(".................................");
	}

	public void federationNotSaved() throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("federationNotSaved: ");
		System.out.println(".................................");
	}

	public void federationRestoreBegun() throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("federationRestoreBegun");
		System.out.println(".................................");
	}

	public void federationRestored() throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("federationRestored");
		System.out.println(".................................");
	}

	public void federationSaved() throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("federationSaved ");
		System.out.println(".................................");
	}

	public void initiateFederateRestore(String label, int federateHandle) throws SpecifiedSaveLabelDoesNotExist, CouldNotRestore, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("initiateFederateRestore " + label + " " + federateHandle);
		System.out.println(".................................");
	}

	public void initiateFederateSave(String label) throws UnableToPerformSave, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("initiateFederateSave: " + label);
		System.out.println(".................................");
	}

	public void printAttributeHandleValueMap(ReflectedAttributes theAttributes)
	{
		try
		{
			for (int i = 0; i < theAttributes.size(); i++)
			{
				int handle = theAttributes.getAttributeHandle(i);
				byte[] value = theAttributes.getValue(i);
				String valueString = new String(value);

				System.out.println("Handle: " + handle);
				System.out.println("Value: " + valueString);
			}
		}
		catch (ArrayIndexOutOfBounds ex)
		{
			System.out.println("Array index out of bounds.");
		}
	}

	public void printParameterHandleValueMap(ReceivedInteraction theParameters)
	{
		try
		{
			for (int i = 0; i < theParameters.size(); i++)
			{
				int handle = theParameters.getParameterHandle(i);
				byte[] value = theParameters.getValue(i);
				String valueString = new String(value);

				System.out.println("Handle: " + handle);
				System.out.println("Value: " + valueString);
			}
		}
		catch (ArrayIndexOutOfBounds ex)
		{
			System.out.println("Array index out of bounds.");
		}
	}
	
	private int getClientIdAttrIndex(int theObject, ReflectedAttributes theAttributes)
	{
		int index = -1;
		if(clientIdAttrIndex == -1)
		{
			try
			{
				int classHandle = rtiAmb.getObjectClass(theObject);
				for (int i = 0; i < theAttributes.size(); i++)
				{
					int attrHandle = theAttributes.getAttributeHandle(i);
					String attrName = rtiAmb.getAttributeName(attrHandle, classHandle);
					if(attrName.equals(FomConstants.BASEENTITY_ATTRIBUTE_CLIENTID))
					{
						index = i;
						break;
					}
				}
			}
			catch (ArrayIndexOutOfBounds ex)
			{
				System.out.println("Array index out of bounds.");
			}
			catch (ObjectClassNotDefined e)
			{
				e.printStackTrace();
			}
			catch (FederateNotExecutionMember e)
			{
				e.printStackTrace();
			}
			catch (RTIinternalError e)
			{
				e.printStackTrace();
			}
			catch (AttributeNotDefined e)
			{
				e.printStackTrace();
			}
			catch (ObjectNotKnown e)
			{
				e.printStackTrace();
			}
		}
		else
			index = clientIdAttrIndex;
		
		return index;
	}

	// 4.6
	public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag) throws InteractionClassNotKnown,
			InteractionParameterNotKnown, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Received Interaction: " + interactionClass);
		System.out.print("Tag Value:");
		String tagString = new String(userSuppliedTag);
		System.out.println(tagString);
		System.out.println("Parameter Data: ");
		printParameterHandleValueMap(theInteraction);
		System.out.println(".................................");
	}

	// 6.7
	public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag, LogicalTime theTime,
			EventRetractionHandle eventRetractionHandle) throws InteractionClassNotKnown, InteractionParameterNotKnown, InvalidFederationTime,
			FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Received Interaction: " + interactionClass);
		System.out.print("Tag Value:");
		String tagString = new String(userSuppliedTag);
		System.out.println(tagString);
		System.out.println("LogicalTime: " + theTime.toString());
		System.out.println("Parameter Data: ");
		printParameterHandleValueMap(theInteraction);
		System.out.println(".................................");
	}

	public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag) throws ObjectNotKnown, AttributeNotKnown,
			FederateOwnsAttributes, FederateInternalError
	{
		long receiveTime = Calendar.getInstance().getTimeInMillis();
		String strSendTime = new String(userSuppliedTag);
		long sendTime = Long.parseLong(strSendTime);
		
		int clientIndex = getClientIdAttrIndex(theObject, theAttributes);
		if(clientIndex != -1)
		{
			byte[] clientIdValue;
			try
			{
				clientIdValue = theAttributes.getValue(clientIndex);
				String clientId = new String(clientIdValue);
				//System.out.println("federateCallback");
				federateCallback.receivedReport(sendTime, receiveTime, clientId);
			}
			catch (ArrayIndexOutOfBounds e)
			{
				e.printStackTrace();
			}
		}
	}

	// 6.5
	public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag, LogicalTime theTime,
			EventRetractionHandle retractionHandle) throws ObjectNotKnown, AttributeNotKnown, FederateOwnsAttributes, InvalidFederationTime,
			FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Reflected Update for object: " + theObject);
		System.out.print("Tag Value:");
		String tagString = new String(userSuppliedTag);
		System.out.println(tagString);
		System.out.println("LogicalTime: " + theTime.toString());
		System.out.println("Attribute Data: ");
		printAttributeHandleValueMap(theAttributes);
		System.out.println("Retraction Handle: " + retractionHandle.toString());
		System.out.println(".................................");
		//processReflectedAttributes(theObject, theAttributes);
	}

	// 6.9
	public void removeObjectInstance(int theObject, byte[] userSuppliedTag) throws ObjectNotKnown, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Removed Object Instance: " + theObject);
		System.out.println("Tag Value: " + userSuppliedTag.toString());
		System.out.println(".................................");
	}
	
	// 6.9
	public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown,
			InvalidFederationTime, FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("Removed Object Instance: " + theObject);
		System.out.println("Tag Value: " + userSuppliedTag.toString());
		System.out.println("LogicalTime: " + theTime.toString());
		System.out.println("Retraction Handle: " + retractionHandle.toString());
		System.out.println(".................................");
	}
	
	public void requestFederationRestoreFailed(String label, String reason) throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("requestFederationRestoreFailed " + label);
		System.out.println(".................................");
	}

	public void requestFederationRestoreSucceeded(String label) throws FederateInternalError
	{
		System.out.println(".................................");
		System.out.println("requestFederationRestoreSucceeded " + label);
		System.out.println(".................................");
	}
}
