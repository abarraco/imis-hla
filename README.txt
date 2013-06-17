
Run IMIS
========

Execute “run.bat” found in the “dest” folder. This starts the GUI for entering input values.

	Number of Updates: number of updates sent during a test run
	Number of Senders: number of federates publishing updates
	Number of Receivers: number of federates subscribing to updates
	Number of Subscriptions: number of objects a receiver subscribes to
	Update Interval(ms): number of milliseconds between each publication
	Update Size(B): amount of data in an update in Bytes

To enter a range of values to be tested, set the "End" field to the max value to be tested and "Increment" to the increment of values between tests.  To not do a range of values, set "Increment" to 0. For example, the following entries would result in tests of 1KB, 3KB and 5KB update sizes: 

	Update Size(B): 1024  Increment: 2048  End: 5120 

Once the input values are entered, click "Run" to start the tests.  The "Output" field displays the status of the runs.

Output
======

A log file is written to the "dest" folder by every federate.  These logs contain the test input values and the times recorded.

If a range of variables is used, a summary of the results is written to an Excel file.  Results of the tests are charted for the following variables:

	Average Time To Send(ms): the time in milliseconds for an update to travel from sender to receiver
	Average Update Interval(ms): the time in milliseconds between updates received by the receiver
	Not Received(%): the percent of dropped updates 

	



