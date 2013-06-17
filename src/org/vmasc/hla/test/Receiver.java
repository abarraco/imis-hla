package org.vmasc.hla.test;

import java.util.ArrayList;
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
public class Receiver implements FederateCallback
{
	private LogFile logFile;
	private int reportsReceived = 0;
	private int numReports;
	private int numSubscriptions;
	private boolean federationJoined = false;
	
	private List<Long> reportTimeHistory = new ArrayList<Long>();
	private List<Long> reportTimeIntervalHistory = new ArrayList<Long>();
	private List<Long> reportTimeToSendHistory = new ArrayList<Long>();
	
	private RtiInterface federate = null;
	
	public Receiver(int trialId, int clientId, int numReports, String updateSize, int reportInterval, int numSubscriptions)
	{
		this.numReports = numReports;
		this.numSubscriptions = numSubscriptions;
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
			
		double percentLoss = ((double)(numReports - reportsReceived)/(double)numReports) * 100;
		logFile.logMessage(Constants.DISPLAY_STR_REPORTS_SENT + numReports + "; " + Constants.DISPLAY_STR_REPORTS_RECEIVED + reportsReceived + "; " + Constants.DISPLAY_STR_PERCENT_LOSS + percentLoss);
		
		double avgReportTimeToSendHistory = Utility.calculateLongAverage(reportTimeToSendHistory);
		if(avgReportTimeToSendHistory > -1)
			logFile.logMessage(Constants.DISPLAY_STR_AVERAGE_TIME_TO_SEND + Double.toString(avgReportTimeToSendHistory));
		
		double avgReportTimeIntervalHistory = Utility.calculateLongAverage(reportTimeIntervalHistory);
		if(avgReportTimeIntervalHistory > -1)
			logFile.logMessage(Constants.DISPLAY_STR_AVERAGE_UPDATE_INTERVAL + Double.toString(avgReportTimeIntervalHistory));
	}
	
	private void logReportReceipt(long currentTime, String reporterWhoOid, long reportedWhen)
	{
		long timeToSend = currentTime - reportedWhen;

		logFile.logMessage("");
		logFile.logMessage(Constants.DISPLAY_STR_SENDER_ID + reporterWhoOid);
		logFile.logMessage(Constants.DISPLAY_STR_TIME_SENT + Long.toString(reportedWhen));
		logFile.logMessage(Constants.DISPLAY_STR_TIME_RECEIVED + Long.toString(currentTime));
		logFile.logMessage(Constants.DISPLAY_STR_TIME_TO_SEND + Long.toString(timeToSend));
		reportTimeToSendHistory.add(timeToSend);
		
		long timeSinceLastReport = findTimeSinceLastReport(currentTime);
		if(timeSinceLastReport != -1)
			logFile.logMessage(Constants.DISPLAY_STR_UPDATE_INTERVAL + Long.toString(timeSinceLastReport));
	}
	
	@Override
	public synchronized void receivedReport(long sendTime, long receiveTime, String clientId)
	{
		logReportReceipt(receiveTime, clientId, sendTime);

		reportsReceived++;
		this.notify();
	}

	private synchronized void waitForJoin()
	{
		try
		{
			this.wait(Constants.MAX_TIME_TO_WAIT);
			if(federationJoined)
			{
				federate.subscribe(numSubscriptions);
				
				waitForReports();
				
				federate.unsubscribe();
				federate.callQuit();
				System.exit(0);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void waitForReports()
	{
		int lastNumReportsReceived;
		do
		{
			try 
			{
				lastNumReportsReceived = reportsReceived;
				this.wait(Constants.MAX_TIME_TO_WAIT);
				if(lastNumReportsReceived == reportsReceived)
					break;
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
		} while(numReports > reportsReceived);
		
		logClosingData();
		logFile.close();
	}
}