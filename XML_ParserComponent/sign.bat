"C:\Users\Felix\.jdks\openjdk-17.0.2\bin\jarsigner" -keystore keystore.jks -storepass dhbw2023* build\libs\XML_Parser-1.0.jar server
"C:\Users\Felix\.jdks\openjdk-17.0.2\bin\jarsigner" -keystore keystore.jks -storepass dhbw2023* jar\XML_Parser-1.0.jar server

"C:\Users\Felix\.jdks\openjdk-17.0.2\bin\jarsigner" -verify -keystore keystore.jks -storepass dhbw2023* build\libs\XML_Parser-1.0.jar server
"C:\Users\Felix\.jdks\openjdk-17.0.2\bin\jarsigner" -verify -keystore keystore.jks -storepass dhbw2023* jar\XML_Parser-1.0.jar server
pause