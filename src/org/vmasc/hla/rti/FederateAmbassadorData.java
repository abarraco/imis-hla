/**
 * Simple data structure to provide communication between the main() and the
 * FederateAmbassador.
 */
package org.vmasc.hla.rti;

import hla.rti.AttributeHandleSet;

import java.util.HashMap;

public class FederateAmbassadorData
{	
	//
	//BaseEntity object
	//
	private int baseEntityClassHandle = -1;
	private int baseEntityInstanceHandle = -1;
	private AttributeHandleSet baseEntityAttributeHandleSet = null; 
	private HashMap<String, Integer> baseEntityAttrNameHandleMap;
	
	
	//
	//WeaponFire interaction
	//
	private int weaponFireInterClassHandle = -1;
	private HashMap<String, Integer> weaponFireParamNameHandleMap;
	
	public FederateAmbassadorData()
	{
	}

	/**
	 * @return the baseEntityAttributeHandleSet
	 */
	public AttributeHandleSet getBaseEntityAttributeHandleSet()
	{
		return baseEntityAttributeHandleSet;
	}

	/**
	 * @return the baseEntityAttrNameHandleMap
	 */
	public HashMap<String, Integer> getBaseEntityAttrNameHandleMap()
	{
		return baseEntityAttrNameHandleMap;
	}

	/**
	 * @return the baseEntityObjectHandle
	 */
	public int getBaseEntityClassHandle()
	{
		return baseEntityClassHandle;
	}

	/**
	 * @return the baseEntityInstanceHandle
	 */
	public int getBaseEntityInstanceHandle()
	{
		return baseEntityInstanceHandle;
	}

	/**
	 * @return the weaponFireInterClassHandle
	 */
	public int getWeaponFireInterClassHandle()
	{
		return weaponFireInterClassHandle;
	}

	/**
	 * @return the weaponFireParamNameHandleMap
	 */
	public HashMap<String, Integer> getWeaponFireParamNameHandleMap()
	{
		return weaponFireParamNameHandleMap;
	}

	/**
	 * @param baseEntityAttributeHandleSet the baseEntityAttributeHandleSet to set
	 */
	public void setBaseEntityAttributeHandleSet(AttributeHandleSet baseEntityAttributeHandleSet)
	{
		this.baseEntityAttributeHandleSet = baseEntityAttributeHandleSet;
	}

	/**
	 * @param baseEntityAttrNameHandleMap the baseEntityAttrNameHandleMap to set
	 */
	public void setBaseEntityAttrNameHandleMap(HashMap<String, Integer> baseEntityAttrNameHandleMap)
	{
		this.baseEntityAttrNameHandleMap = baseEntityAttrNameHandleMap;
	}

	/**
	 * @param baseEntityClassHandle the baseEntityClasstHandle to set
	 */
	public void setBaseEntityClassHandle(int baseEntityClassHandle)
	{
		this.baseEntityClassHandle = baseEntityClassHandle;
	}

	/**
	 * @param baseEntityInstanceHandle the baseEntityInstanceHandle to set
	 */
	public void setBaseEntityInstanceHandle(int baseEntityInstanceHandle)
	{
		this.baseEntityInstanceHandle = baseEntityInstanceHandle;
	}

	/**
	 * @param weaponFireInterClassHandle the weaponFireInterClassHandle to set
	 */
	public void setWeaponFireInterClassHandle(int weaponFireInterClassHandle)
	{
		this.weaponFireInterClassHandle = weaponFireInterClassHandle;
	}

	/**
	 * @param weaponFireParamNameHandleMap the weaponFireParamNameHandleMap to set
	 */
	public void setWeaponFireParamNameHandleMap(HashMap<String, Integer> weaponFireParamNameHandleMap)
	{
		this.weaponFireParamNameHandleMap = weaponFireParamNameHandleMap;
	}	
}
