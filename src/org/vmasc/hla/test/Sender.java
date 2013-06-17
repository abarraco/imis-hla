package org.vmasc.hla.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.vmasc.hla.rti.FederateCallback;
import org.vmasc.hla.rti.RtiInterface;
import org.vmasc.hla.util.Constants;
import org.vmasc.hla.util.LogFile;
import org.vmasc.hla.util.Utility;

/**
 * 
 * 
 * @author abarraco
 */
public class Sender implements FederateCallback
{
	private int clientId;
	private int numReports;
	private int reportInterval;
	private String updateSize;
	
	private RtiInterface federate = null;
	private boolean federationJoined = false;
	
	private LogFile logFile;
	
	private List<Long> reportTimeHistory = new ArrayList<Long>();
	private List<Long> reportTimeIntervalHistory = new ArrayList<Long>();
	
	public Sender(int trialId, int clientId, int numReports, String updateSize, int reportInterval, int numSubscriptions)
	{
		this.clientId = clientId;
		this.numReports = numReports;
		this.reportInterval = reportInterval;
		this.updateSize = updateSize;
		
		logFile = Utility.initLogFile(trialId, clientId, numReports, updateSize, reportInterval, numSubscriptions);
		
		federate = new RtiInterface(this, reportInterval);
		federate.startFederate();
		waitForJoin();
	}
	
	@Override
	public synchronized void federationJoined()
	{
		federationJoined = true;
		this.notify();
	}
	
	private long findTimeSinceLastReport(long currentTime)
	{
		long timeSinceLastReport = -1;
		
		//get the last time entry in the history
		int size = reportTimeHistory.size();
		if(size > 0)
		{
			long lastReportTime = reportTimeHistory.get(size - 1).longValue();
			timeSinceLastReport = currentTime - lastReportTime;
			reportTimeIntervalHistory.add(timeSinceLastReport);
		}
		
		reportTimeHistory.add(currentTime);
		
		return timeSinceLastReport;
	}
	
	private void logClosingData()
	{
		logFile.logMessage("");
		
		double avgReportTimeHistory = Utility.calculateLongAverage(reportTimeIntervalHistory);
		if(avgReportTimeHistory > -1)
			logFile.logMessage(Constants.DISPLAY_STR_AVERAGE_UPDATE_INTERVAL + Double.toString(avgReportTimeHistory));
	}
	
	private void logReportSent(long reportedWhen)
	{	
		logFile.logMessage("");
		logFile.logMessage(Constants.DISPLAY_STR_TIME_SENT + Long.toString(reportedWhen));
		long timeSinceLastReport = findTimeSinceLastReport(reportedWhen);
		if(timeSinceLastReport != -1)
			logFile.logMessage(Constants.DISPLAY_STR_UPDATE_INTERVAL + Long.toString(timeSinceLastReport));
	}
	
	@Override
	public void receivedReport(long sendTime, long receiveTime, String clientId)
	{
	}

	public void sendReports()
	{
		for(int i = 0; i < numReports; i++)
		{
			long currentTime = Calendar.getInstance().getTimeInMillis();
			federate.updateAttributes(currentTime, clientId, updateSize);
			
			logReportSent(currentTime);
			try 
			{
				Thread.sleep(reportInterval);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				logFile.logMessage("InterruptedException");
			}
		}
		
		logClosingData();
		logFile.close();
	}

	private synchronized void waitForJoin()
	{
		try
		{
			this.wait(Constants.MAX_TIME_TO_WAIT);
			if(federationJoined)
			{
				federate.publish();
				federate.register();
				
				sendReports();
				
				federate.unregister();
				federate.unpublish();
				federate.callQuit();
				System.exit(0);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}