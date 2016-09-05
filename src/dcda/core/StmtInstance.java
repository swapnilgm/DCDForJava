package dcda.core;

public class StmtInstance {
	
	private String statement;
	
	private int lineNo;
	
	private int instanceId;
	
	public StmtInstance(String statement, int lineNo) {
		this.statement = statement;
		this.lineNo = lineNo;
		instanceId=0;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNo;
		result = prime * result
				+ ((statement == null) ? 0 : statement.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StmtInstance other = (StmtInstance) obj;
		if (lineNo != other.lineNo)
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		return true;
	}



	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "@L"+lineNo+"$"+instanceId;
	}
	
	

}
