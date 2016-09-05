package dcda.core;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * @author Swapnil & Debjeet
 *
 */
public class DCDAnalyzer {

	private final static Logger logger = Logger.getLogger(DCDAnalyzer.class);

	final static Map<StmtInstance, Integer> stmtCountMap = new HashMap<StmtInstance, Integer>();

	private static int nextAssignableCC = 1;

	private static Stack<Integer> currentCC = new Stack<>();

	final static ControlDependenceStack CDS=new ControlDependenceStack();

	static {
		currentCC.add(0);
	}

	public static void branching(String stmt,String lineNo,String ipd,String ipdlineNo){
		StmtInstance instance=new StmtInstance(stmt.replaceFirst("(.*goto)(.*)", "$1..."),Integer.parseInt(lineNo));
		StmtInstance ipdInstance = null;
		boolean isDummy = ipdlineNo.equals("-1"); 
		if(!(isDummy && CDS.top() == null)){
			if(!isDummy) {
				ipdInstance = new StmtInstance(ipd.replaceFirst("(.*goto)(.*)", "$1..."),Integer.parseInt(ipdlineNo));
			} else if(CDS.top()!=null){
				ipdInstance = CDS.top().getIpdStatement();
			}
			logger.debug("branching instance ::" +stmt+instance);
			logger.debug("cds top before  ::" +CDS);
			instance.setInstanceId(getUnitInstanceID(instance));
			//Branching Code		

			if(CDS.top()!=null && CDS.top().getIpdStatement().equals(ipdInstance) && (isDummy || CDS.top().getCallingContextTimeStamp() == currentCC.peek())){
				CDS.top().setBpInstanse(instance);
				logger.debug("updating region new region");
			}
			else
			{
				logger.debug("pushing new region");
				CDS.push(new Region(instance,ipdInstance,currentCC.peek()));
			}
			logger.debug("cds top after  ::" +CDS);
			logger.debug("branching ipd ::" +ipd);
			
		}
	}

	public static void merging(String stmt,String lineNo){
		StmtInstance instance=new StmtInstance(stmt.replaceFirst("(.*goto)(.*)", "$1..."),Integer.parseInt(lineNo));
		//logger.debug("merging ipd ::" +stmt);
		logger.debug("CDS top before merging :: "+ CDS.top());
		if(CDS.top()!=null) {
			if(CDS.top().getIpdStatement().getStatement().equals(instance.getStatement()) && CDS.top().getCallingContextTimeStamp()== currentCC.peek()){
				CDS.pop();
				logger.debug("removing region");
			}
		}
		logger.debug("CDS top after merging :: "+ CDS.top());
	}

	private static int getUnitInstanceID(StmtInstance stmt){
		Integer unitCount =  stmtCountMap.get(stmt);		
		return unitCount;
	}

	private static int putUnit(StmtInstance stmt){
		Integer unitCount =  stmtCountMap.get(stmt);
		if(unitCount == null){
			unitCount=0;
		}
		stmtCountMap.put(stmt, ++unitCount);
		return unitCount;
	}

	/**
	 * 
	 * @param stmt
	 * @param lineNo
	 * @author Swapnil
	 */
	public static void logDcd(String stmt, String lineNo){
		StmtInstance instance=new StmtInstance(stmt.replaceFirst("(.*goto)(.*)", "$1..."),Integer.parseInt(lineNo));
		instance.setInstanceId(putUnit(instance) );
		if(CDS.top() != null)
		{
			logger.info(instance + " control dependes on  :" + CDS.top().getBpInstanse());		
		}else {
			logger.info(instance + " control independent");
		}
		//logger.debug("Stmt log :: " + stmt);
		//logger.debug("CDS top ::" +CDS.top());
		//logger.debug("Stmt count map :: " + stmtCountMap);
	}

	/**
	 * 
	 * 
	 * @author Swapnil
	 */
	public static void timeStampCallingContext(){

		currentCC.push(nextAssignableCC);
		nextAssignableCC++;
	}

	/**
	 * 
	 * 
	 * @author Swapnil
	 */
	public static void returnCall(){
		currentCC.pop();		
	}
}