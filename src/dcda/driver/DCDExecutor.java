/**
 * 
 */
package dcda.driver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * @author Swapnil
 *
 */
public class DCDExecutor {

	final private static Logger logger = Logger.getLogger(DCDExecutor.class);
	
	private String command;
	/**
	 * 
	 */
	public DCDExecutor(String cmd) {
		this.command = cmd;
	}

	public void executeDCD() throws InterruptedException{
		try {

			//TODO remove this hardcoding using argument or stdInput
			logger.info("Executing command ::\n" + this.command);
			Process proc = Runtime.getRuntime().exec(this.command);
			
	
			logger.info("Error message :: ");
			embedOutput(proc.getErrorStream());
			
			logger.info("Output message :: ");
			embedOutput(proc.getInputStream());
		} catch (IOException e) {
			logger.error("IOException raised: " + e.getMessage());
		}
	}
	
	private void embedOutput(InputStream inStream){
		//TODO apply threading over this afterwords
		BufferedInputStream bufferStream = new BufferedInputStream(inStream);

		BufferedReader commandOutput = new BufferedReader(
				new InputStreamReader(bufferStream));
		String line = null;
		try {
			while ((line = commandOutput.readLine()) != null) {
				logger.info(line);
			}// end while
			commandOutput.close();

		} catch (IOException e) {
			// log and/or handle it
			logger.error("IOException raised: " +e.getLocalizedMessage());
		}		
	}

}
