package com.idega.block.creditcard2.business;

public class RRN {
	private Object LOCK = new Object() {
	};
	private Long lastAuth = null;

	public Object getLOCK() {
		return LOCK;
	}

	public void setLOCK(Object lOCK) {
		LOCK = lOCK;
	}

	public Long getLastAuth() {
		return lastAuth;
	}

	public void setLastAuth(Long lastAuth) {
		this.lastAuth = lastAuth;
	}

	public Long getNextAuth() {
		this.lastAuth++;
		return lastAuth;
	}
}
