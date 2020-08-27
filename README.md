# AikiDataHandler

The AikiDataHandler converts the .JSON files Aiki generates when users interact with it into .csv files.

- The original .JSON files needs to go into the /remakeData/data folder.
- Then run the Main class of the /remakeData folder. The output of the Main class comes in  the /remakeData/output folder.
- Move the output to the /userFriendlyLog/data. 
- Then run the Main class of the /userFriendlyLog. The Main class outputs the following:

- in /userFriendlyLog/output it outputs one .csv file for each of the users in the data set. 
- in /userFriendlyLog/output2 it outputs the data points used in the report
- in /userFriendlyLog/userFriendlyLogData it outputs .csv files for each of the users in the data set. The data is just more reader-friendly than the ones in the other folder.
