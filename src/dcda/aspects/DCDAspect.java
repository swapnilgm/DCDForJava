package dcda.aspects;

import dcda.core.DCDAnalyzer;

public aspect DCDAspect {
	
	pointcut branchingPoints() : 
		call(public static void dcda.core.DummyDCDAnalyzer.branching( *,*,*,* ));
	
	pointcut ipds() : 
		call(public static void dcda.core.DummyDCDAnalyzer.merging( *,* ));
	
	pointcut dcdLogging() : 
		call(public static void dcda.core.DummyDCDAnalyzer.logDcd( *,* ));
	
	pointcut functionCall() : 
		
		!branchingPoints() 
		&& !ipds() 
		&& !dcdLogging()
		&& !call(* dcda.*.*( .. ))
		&& call(* *.*.*( .. ));
	
	
	before() : branchingPoints() {
		
		Object[] args = thisJoinPoint.getArgs();
		String stmt = (String) args[0];
		String stmtLineNo = (String) args[1];
		String ipd = (String) args[2];
		String ipdLineNo = (String) args[3];
		DCDAnalyzer.branching(stmt, stmtLineNo , ipd, ipdLineNo);
	}
		
	before() : ipds() {
		//System.out.println("in merging from aspect");
		Object[] args = thisJoinPoint.getArgs();
		String stmt = (String) args[0];
		String stmtLineNo = (String) args[1];
		DCDAnalyzer.merging(stmt, stmtLineNo);
	}
	
	before() : dcdLogging() {
		//System.out.println("in logging from aspect");
		Object[] args = thisJoinPoint.getArgs();
		String stmt = (String) args[0];
		String stmtLineNo = (String) args[1];		
		DCDAnalyzer.logDcd(stmt, stmtLineNo);
	}
	
	before() : functionCall() {
		DCDAnalyzer.timeStampCallingContext();
	}
	
	
	after() returning : functionCall(){
		//System.out.println("Join point class :: "+thisJoinPoint.getClass().getSimpleName());
		DCDAnalyzer.returnCall();
	}
	
	
}
