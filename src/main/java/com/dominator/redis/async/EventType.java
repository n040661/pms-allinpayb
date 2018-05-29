package com.dominator.redis.async;

public enum EventType {

	MAIL(0),
	MSG(1),
	ZAXMSG(2),
	CONSULT(3),
	;


	private int value;
	public int getValue(){
		return value;
	}

	EventType(int value){
		this.value = value;
	}
}
