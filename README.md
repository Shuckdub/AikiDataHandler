# AikiDataHandler

The AikiDataHandler converts the .JSON files Aiki generates when users interact with it into .csv files.

1) Put the original .JSON file(s) into the /remakeData/data folder.
2) Then run the Main class of the /remakeData folder. The output of the Main class comes in the /remakeData/output folder.
3) Move the output from the /remakeData/output folder to the /userFriendlyLog/data folder. 
4) Then run the Main class of the /userFriendlyLog. This Main class outputs the following:
- in /userFriendlyLog/output it outputs .csv files for each of the users in the data set. 
- in /userFriendlyLog/output2 it outputs one .csv file with the data points used in the report
- in /userFriendlyLog/userFriendlyLogData it outputs .csv files for each of the users in the data set. The data is just more reader-friendly than the ones in the other folder.
