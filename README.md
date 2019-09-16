example url:
jdbc:sheets:{{appName}}/{{sheetId}}?sheetsCredentialsFilePath={{}}&sheetsTokensDirectoryPath={{}}&driveCredentialsFilePath={{}}&driveTokensDirectoryPath={{}}&schemas={{}}

compliant with datagrip if above url is provided as parameters in Advanced Section of Data Source Settings (add jar from releases as custom driver)
-> if schemas provided, sheetId will be ignored

## tested queries

#### update specific cells;
UPDATE `spreadsheet name`.`sheet name` SET c1 = '', b1 = '';

#### append after last written line (that is what google does);
INSERT INTO `spreadsheet name`.`sheet name` (A,B,C) VALUES(1,2,3);

#### insert from line 40 and upwards;
INSERT INTO `spreadsheet name`.`sheet name` (A40,B40,C40) VALUES(1,2,3),(1,2,3);

#### specific cells;
SELECT A40,B40,C40 FROM `spreadsheet name`.`sheet name`;

#### all;
SELECT * FROM `spreadsheet name`.`sheet name`;

