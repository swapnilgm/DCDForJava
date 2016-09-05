#README.txt

#Author : Swapnil Mhamane && Debjeet Majumdar

#We have provided runnable packaged binary of our tool DCDrt.jar and its dependent libraries in directory lib
# to find out dynamic control dependence of your library follow following steps
#Command to instrument class
# java -jar DCDrt.jar --cp DCDrt.jar;<TestSuiteBinanryRoot> -process-dir <TestSuiteBinaryRoot> -libpath <libarypath to dependencies like aspectj, aspectjrt, log4j-1, soot + any dependencies for input class>
"%JRE_HOME%/bin/java"  -jar DCDrt.jar --cp .;DCDrt.jar;TestSuite\bin\testcase4 -process-dir TestSuite\bin\testcase4 -libpath lib/aspectj-1.8.7.jar;lib/aspectjrt.jar;lib/log4j-1.2.17.jar;lib/soot-2.5.0.jar lib/rt.jar

#This step consist of two phases internally 
#first phase will instrument input binaries with dummy merging, branching, logging etc. call.
#And generate output class file in directory .\sootOutput
#In second phase, instrumented binary from sootOutput and further processed by aspectj.
#Now finally you will get completely instrumented binary of your input llibrary for which you have to find out dynamic control dependence.
#Next step is simple to execute your propgram. 
#Before that since intrumention put dependency yout library on aspectj, log4j and soot, don't forget to mention those in classpath while execution

#To keep DCD output separate from defualt console output of input binaries we are logging out DCD in log file named dcd.log using log4j
#you need to copy log4j properties file to output directory generated in previous step 
copy TestSuite\src\log4j.properties output

#Command to find dynamic control dependence for each statment by executing instrumented binary
#java -cp lib\*;output <MAIN_CLASS OF TEST SUITE>
java -cp lib\*;output testsuite.TestSuite

#Now you can see your expected result i.e. showing dynamic control dependence in log file <dcd.log>{Default}.

####
#
#Dirrectory structure 
#src : source
#lib : dpendecies library
	1.soot-2.5.0.jar
	2.log4j-1.2.17.jar 
	3.aspectjrt.jar
	4.aspectjtools.jar
	5.aspectj-1.8.7.jar
#testcase : testSuite
#