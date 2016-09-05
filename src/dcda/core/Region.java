package dcda.core;


public class Region {
	
	private StmtInstance bpInstanse;
	
	private StmtInstance ipdStatement;
	
	private int callingContextTimeStamp;
	
	public Region(StmtInstance stmt, StmtInstance stmtIPD, int ccTimeStamp) {
		this.bpInstanse = stmt;
		this.ipdStatement = stmtIPD;
		this.callingContextTimeStamp = ccTimeStamp;
	}

	/**
	 * @return the bpInstanse
	 */
	protected StmtInstance getBpInstanse() {
		return bpInstanse;
	}

	/**
	 * @param bpInstanse the bpInstanse to set
	 */
	protected void setBpInstanse(StmtInstance bpInstanse) {
		this.bpInstanse = bpInstanse;
	}

	/**
	 * @return the ipdStatement
	 */
	protected StmtInstance getIpdStatement() {
		return ipdStatement;
	}

	/**
	 * @param ipdStatement the ipdStatement to set
	 */
	protected void setIpdStatement(StmtInstance ipdStatement) {
		this.ipdStatement = ipdStatement;
	}
	
	/**
	 * @return the callingContextTimeStamp
	 */
	protected int getCallingContextTimeStamp() {
		return callingContextTimeStamp;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Region [bpInstanse=" + bpInstanse + ", ipdStatement="
				+ ipdStatement + ", callingContextTimeStamp="
				+ callingContextTimeStamp + "]";
	}

	/**
	 * @param callingContextTimeStamp the callingContextTimeStamp to set
	 */
	protected void setCallingContextTimeStamp(int callingContextTimeStamp) {
		this.callingContextTimeStamp = callingContextTimeStamp;
	}
}
