package org.vmasc.hla.rti;

import java.util.HashMap;
import java.util.Map;

public class RtiDataBean
{
	private Map<String, Object> attributeMap = new HashMap<String, Object>();
	private String className = "";
	
	/**
	 * @return the attributeMap
	 */
	public Map<String, Object> getAttributeMap()
	{
		return attributeMap;
	}
	
	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}
	
	/**
	 * @param attributeMap the attributeMap to set
	 */
	public void setAttributeMap(Map<String, Object> attributeMap)
	{
		this.attributeMap = attributeMap;
	}
	
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}
}