/**
 * This class is the main entry point to the HLA scalability test.
 */

package org.vmasc.hla.test;

import org.vmasc.hla.gui.MainWindow;
import org.vmasc.hla.util.Constants;
import org.vmasc.hla.util.Params;
import org.vmasc.hla.util.Utility;

/**
 * This class is a test harness for the HLA scalability testing.
 * 
 * @author abarraco
 */
public class HlaScalabilityTest
{
	private TestVariableManager testVariableManager = new TestVariableManager();

	public HlaScalabilityTest()
	{
		initTestVariables();
		startGui();
	}
	
	/**
	 * Read the variable values from the properties file and initialize the variables manager.
	 */
	private void initTestVariables()
	{
		testVariableManager.addTestVariable(Constants.PROP_STR_NUM_UPDATES, Constants.DISPLAY_STR_NUMBER_OF_UPDATES, Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_UPDATES)));
		testVariableManager.addTestVariable(Constants.PROP_STR_UPDATE_INTERVAL, Constants.DISPLAY_STR_UPDATE_INTERVAL, Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_INTERVAL)), Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_INTERVAL_END)), Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_INTERVAL_INCREMENT)));
		testVariableManager.addTestVariable(Constants.PROP_STR_NUM_SENDERS, Constants.DISPLAY_STR_NUM_SENDERS, Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SENDERS)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SENDERS_END)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SENDERS_INCREMENT)));
		testVariableManager.addTestVariable(Constants.PROP_STR_NUM_RECEIVERS, Constants.DISPLAY_STR_NUM_RECEIVERS, Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_RECEIVERS)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_RECEIVERS_END)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_RECEIVERS_INCREMENT)));
		testVariableManager.addTestVariable(Constants.PROP_STR_UPDATE_SIZE, Constants.DISPLAY_STR_UPDATE_SIZE, Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_SIZE)), Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_SIZE_END)), Integer.parseInt(Params.getString(Constants.PROP_STR_UPDATE_SIZE_INCREMENT)));
		testVariableManager.addTestVariable(Constants.PROP_STR_NUM_SUBSCRIPTIONS, Constants.DISPLAY_STR_NUM_SUBSCRIPTIONS, Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SUBSCRIPTIONS)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SUBSCRIPTIONS_END)), Integer.parseInt(Params.getString(Constants.PROP_STR_NUM_SUBSCRIPTIONS_INCREMENT)));
	}

	private void startGui()
	{
		// Create the main window
		MainWindow wnd = new MainWindow(Constants.DISPLAY_STR_MAIN_TITLE_ABBR);
		wnd.setupWindow(testVariableManager);
		
		// Show it.
		wnd.setVisible(true);
	}
	
	public static void main(String args[])
	{
		int argsLength = args.length;
		if (argsLength == 0)
		{
			new HlaScalabilityTest();
		}
		else if(argsLength == 6)
		{
			int trialId = Integer.parseInt(args[0]);
			int clientId = Integer.parseInt(args[1]);
			int numReports = Integer.parseInt(args[2]);
			String updateSize = args[3];
			int reportInterval = Integer.parseInt(args[4]);
			int numSubscriptions = Integer.parseInt(args[5]);
			
			//if sender
			if(!Utility.isReceiverId(clientId))
			{
				System.out.println("Creating sender");
				new Sender(trialId, clientId, numReports, updateSize, reportInterval, numSubscriptions);
			}
			//else receiver
			else
			{
				System.out.println("Creating receiver");
				new Receiver(trialId, clientId, numReports, updateSize, reportInterval, numSubscriptions);
			}
		}
		else
			System.out.println("Incorrect number of arguments:" + Integer.toString(argsLength));
	}
}