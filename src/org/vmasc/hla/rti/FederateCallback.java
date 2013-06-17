package org.vmasc.hla.rti;

public interface FederateCallback
{
	public void federationJoined();
	public void receivedReport(long sendTime, long receiveTime, String clientId);
}
