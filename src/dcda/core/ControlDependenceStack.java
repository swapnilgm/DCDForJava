package dcda.core;
import java.util.LinkedList;
/**
 * 
 * @author Debjeet
 *
 */

public class ControlDependenceStack {
	
	private LinkedList<Region> stack;

	public ControlDependenceStack() {
		stack=new LinkedList<Region>();
	}
	
	public Region top()
	{
		if(this.stack.isEmpty()){
			return null;
		}
		return this.stack.peekFirst();
	}
	
	public void push(Region stmtPair){
		this.stack.push(stmtPair);
	}
	
	public Region pop()
	{
		if(this.stack.isEmpty()){
			return null;
		}
		return this.stack.pop();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ControlDependenceStack [stack=" + stack + "]";
	}
	
}
