package dcda.driver;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;
import dcda.core.DummyDCDAnalyzer;
import dcda.instrumentation.DcdInstrumentor;

/**
 * 
 */

/**
 * @author Debjeet
 * 
 */
public class Driver {

	final private static Logger logger = Logger.getLogger(Driver.class);
	/**
	 * @throws IOException
	 * @throws InterruptedException
	 * 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		configureSoot();
		String libPath = null;
		
	
		List<String> argsList = new ArrayList<>(Arrays.asList(args));
		String libPathFlag = "-libpath";
		if(argsList.contains(libPathFlag)){
			int index = argsList.indexOf(libPathFlag);
			if(index< argsList.size()){
				libPath = argsList.get(index+1);
				argsList.remove(libPath);
				argsList.remove(libPathFlag);
				argsList.add("--x");
				argsList.add("dcda");
				argsList.add("-pp");
				argsList.add("-d");
				argsList.add("sootOutput");
				String sootArgs [] = new String [argsList.size()];
				sootArgs = argsList.toArray(sootArgs);
				/*	}
			else{
				logger.error("Missing libPath");
			}
		}
		if(args.length > 2 ){
			ArrayList<String> sootArgsList = new ArrayList<String>(); 
			String sootArgs [] = new String [args.length-2];
			for (int i = 0; i < args.length-1; i++) {
				if(args[i].equals("-libpath")){
					libPath = args[i+1];
					i++;
				}
				else{
					sootArgs[count] = args[i];
					count++;

				}
			}*/
				instrumentDCD(sootArgs);
				executeDCD(libPath);
			}else {
				logger.error("Missing libPath");
			}
		}else {
			logger.error("Missing libPath");
		}


	}

	private static void instrumentDCD(String[] args){
		PackManager packManager = PackManager.v();
		Pack jtpPack = packManager.getPack("jtp");
		jtpPack.add(new Transform("jtp.JTransformer", new DcdInstrumentor()));		
		soot.Main.main(args);		
	}

	/**
	 * @author Swapnil
	 */
	private static void configureSoot(){
		if (logger.isDebugEnabled()) {
			logger.debug("Configuring soot ...");
		}
		Options.v().setPhaseOption("jb", "use-original-names:true"); // Use same
		// variable
		// names
		// as in
		// test
		// program
		Options.v().set_keep_line_number(true);

		Scene.v().addBasicClass(DummyDCDAnalyzer.class.getCanonicalName(),
				SootClass.SIGNATURES);

		if (logger.isDebugEnabled()) {
			logger.debug("Soot configuration complete.");
		}
	}

	/**
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @author Swapnil
	 */
	private static void executeDCD(String libpath) throws InterruptedException, IOException{
		//TODO actually should pass only class

		String dcdrtpath=null;
		try {
			dcdrtpath = Driver.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			logger.debug("THE path is :: " + dcdrtpath);
			String commmand = "java -cp "+dcdrtpath+";"+libpath+" org.aspectj.tools.ajc.Main -classpath "+dcdrtpath+";.;"+libpath
					+ " -inpath sootOutput;"+ dcdrtpath+" "
					+ "-aspectpath "+dcdrtpath+"  -source 1.7 -target 1.7 -d output";
			DCDExecutor dcdExecutor = new DCDExecutor(commmand);
			dcdExecutor.executeDCD();
			if(logger.isDebugEnabled()){			
				logger.debug("Completed execution of command");
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*	URL url = Driver.class.getClassLoader().getResource("");
		String[] args = {"-classpath ","lib/aspectjrt.jar;.;lib/soot-2.5.0.jar;","-sourceroots","src/dcda/core;src/dcda/instrumentation","-inpath", "sootOutput","src/dcda/aspects/*.aj", "-source", "1.7", "-target", "1.7" ,"-d", "output","-noExit"};
		MessageHandler m = new MessageHandler();
	    Main.main(args);
		IMessage[] ms = m.getMessages(null, true);
	    System.out.println("messages: " + Arrays.asList(ms));

		//String classPath = Driver.class.getClassLoader().getResource("").getPath();

		dcdExecutor = new DCDExecutor("java -cp "
				+classPath + "../lib/aspectjrt.jar;"
				+classPath+"../output  testsuite.TestSuite 1> "+classPath+"../output.txt 2>&1");
		//dcdExecutor.executeDCD();
		DCDExecutor dCDExecutor = new DCDExecutor("java -cp .;F:\\swapnil\\PG\\PAVT\\Project\\DCDWithAspect\\sootOutput;F:\\swapnil\\PG\\PAVT\\Project\\DCDWithAspect\\bin testsuite.TestSuite");
		dCDExecutor.executeDCD();*/
	}
}