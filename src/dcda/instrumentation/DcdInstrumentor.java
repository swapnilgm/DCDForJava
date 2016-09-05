/**
 * 
 */
package dcda.instrumentation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.internal.JIdentityStmt;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import dcda.core.DummyDCDAnalyzer;

/**
 * @author Swapnil
 *
 */
public class DcdInstrumentor extends BodyTransformer {

	final private static Logger logger = Logger.getLogger(DcdInstrumentor.class); 
			
	@Override
	protected void internalTransform(Body body, String phaseName, Map options) {
		// Control flow graph analysis
		SootMethod method = body.getMethod();
		
		logger.debug("\n\nAnalysis of  " + method.getName()
				+ "()  :"); // Print method name. Just User Friendly
		// way!
		// Body body = method.retrieveActiveBody();
		logger.debug("\n**********Start of the method*********");
		String className = method.getDeclaringClass().getName();
		logger.debug("\nProcessing Class :: "
				+ className);
		logger.debug("Processing method :: " + method.getName());
		
		UnitGraph unitGraph = new ExceptionalUnitGraph(body);

		String analyzer = DummyDCDAnalyzer.class.getCanonicalName();
		SootClass analyzerClass = Scene.v().loadClassAndSupport(analyzer); // load the class

		SootMethod toCallBranch = analyzerClass
				.getMethod("void branching(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
		SootMethod toCallMerge = analyzerClass
				.getMethod("void merging(java.lang.String,java.lang.String)");
		SootMethod toCallDCDLogger = analyzerClass
				.getMethod("void logDcd(java.lang.String,java.lang.String)");
	
		
		logger.debug(body.toString());
		PatchingChain<Unit> unitChain = body.getUnits();
		MHGPostDominatorsFinder pdomFinder=new MHGPostDominatorsFinder(unitGraph);
		Map<Unit, Unit> staticRegionCollection = collectStaticRegions(unitChain ,pdomFinder);
		for (Map.Entry<Unit, Unit> regionEntry : staticRegionCollection.entrySet()) {
			System.out.println("bp ::" + regionEntry.getKey() + "     ipd :: "+ regionEntry.getValue());
		}
		Set<Unit> originalUnits = new HashSet<Unit>(unitChain);
		//printControlFlowEdges(unitGraph);
		instrumentMergingCall(toCallMerge, new HashSet<Unit>(staticRegionCollection.values()), unitChain);
		//unitGraph = new ExceptionalUnitGraph(body);
		//printControlFlowEdges(unitGraph);
		instrumentDCDLoggerCall(toCallDCDLogger, originalUnits, unitChain);
		instrumentBranchingCall(toCallBranch, staticRegionCollection, unitChain);

		logger.debug(body.toString());
	}

/*	private void printControlFlowEdges(UnitGraph unitGraph){
		
		Iterator<Unit> blockGraphIterator = unitGraph.iterator();
		while(blockGraphIterator.hasNext()) {
			Unit currentBlock = blockGraphIterator.next();
			List<UnitBox> successorsBlockList = currentBlock.getBoxesPointingToThis();			
			String currentBlockIndex = currentBlock.toString();
			G.v().out.println("\n\n****************\n"+currentBlockIndex + "pointed by  :::" );
			for (UnitBox successorBlock : successorsBlockList) {
				G.v().out.println(successorBlock.getUnit().toString());
			}
			successorsBlockList = currentBlock.getUnitBoxes();
			G.v().out.println("\n"+currentBlockIndex + "points to  :::" );
			for (UnitBox successorBlock : successorsBlockList) {
				G.v().out.println(successorBlock.getUnit().toString());
			}
		}

	}*/

	private Map<Unit, Unit> collectStaticRegions(PatchingChain<Unit> unitChain, MHGPostDominatorsFinder pdomFinder){
		Map<Unit, Unit> staticRegionCollection = new HashMap<Unit, Unit>();

		for (Unit unit : unitChain) {
			if (unit instanceof IfStmt) {				
				Unit pd=(Unit)pdomFinder.getImmediateDominator(unit);
				staticRegionCollection.put(unit, pd);
			}
		}
		return staticRegionCollection;
	}

	private void instrumentBranchingCall(SootMethod toCallBranch, Map<Unit, Unit> staticRegionCollection, PatchingChain<Unit> unitChain){
		//instrument branching call
		for (Map.Entry<Unit, Unit> region : staticRegionCollection.entrySet()) {
			Unit bpUnit = region.getKey();
			Unit ipd= region.getValue();
			LineNumberTag tag1 = (LineNumberTag) bpUnit.getTag("LineNumberTag");
			StringConstant arg1 = StringConstant.v(bpUnit.toString());
			StringConstant arg2 = StringConstant.v(""+tag1.getLineNumber());
			LineNumberTag tag2 = null;
			StringConstant arg3 = StringConstant.v("dummy");
			StringConstant arg4 = StringConstant.v("-1");
			if(ipd != null) {
				tag2 = (LineNumberTag) ipd.getTag("LineNumberTag");
				arg3 = StringConstant.v(ipd.toString());
				arg4 = StringConstant.v(""+tag2.getLineNumber());
			}
			StaticInvokeExpr sie = Jimple.v().newStaticInvokeExpr(
					toCallBranch.makeRef(), arg1,arg2,arg3,arg4);
			
			/*Unit branchingUnit = Jimple.v().newInvokeStmt(sie);
			unitChain.insertBefore(branchingUnit, bpUnit);
			*/Unit branchingUnit1 = Jimple.v().newInvokeStmt(sie);
			unitChain.insertBefore(branchingUnit1, bpUnit);
			
			/*
			for (UnitBox unitBox : bpUnit.getBoxesPointingToThis()) {
				
				branchingUnit.addBoxPointingToThis(unitBox);
				bpUnit.removeBoxPointingToThis(unitBox);
			}*/
		}
	}

	private void instrumentMergingCall(SootMethod toCallMerge, Set<Unit> targetUnits, PatchingChain<Unit> unitChain){

		//instruments merging call
		for (Unit unit : targetUnits) {
			if(unit != null) {
				LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");

				StringConstant arg1 = StringConstant.v(unit.toString());
				StringConstant arg2 = StringConstant.v(tag.getLineNumber()+"");
				StaticInvokeExpr sie = Jimple.v().newStaticInvokeExpr(
						toCallMerge.makeRef(), arg1,arg2);
				unitChain.insertBefore(Jimple.v().newInvokeStmt(sie), unit);
			}
		}

	}

	private void instrumentDCDLoggerCall(SootMethod toCallDCDLogger, Set<Unit> targetUnits, PatchingChain<Unit> unitChain){
		//instruments dcdLogger call
		for (Unit unit : targetUnits) {
		/*	boolean thisRef = false;
			System.out.println(unit.getClass().getSimpleName() + " :: "+unit);
			for(ValueBox valBox : unit.getUseBoxes()){
				Value value = valBox.getValue();
				if( value instanceof ThisRef  ) //|| value instanceof CaughtExceptionRef
				{
					thisRef = true;
					return;
				}
			}
	*/
			LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");
			StringConstant arg1 = StringConstant.v(unit.toString());
			StringConstant arg2 = StringConstant.v(tag.getLineNumber()+"");
			StaticInvokeExpr sie = Jimple.v().newStaticInvokeExpr(
					toCallDCDLogger.makeRef(), arg1,arg2);
			
			if(unit instanceof JIdentityStmt ){
				continue;
			}else {
				Unit loggingUnit = Jimple.v().newInvokeStmt(sie);				
				unitChain.insertBefore(loggingUnit, unit);
			}
		}
	}

}
