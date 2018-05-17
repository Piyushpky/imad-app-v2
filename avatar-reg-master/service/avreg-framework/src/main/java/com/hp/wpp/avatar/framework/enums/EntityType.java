package com.hp.wpp.avatar.framework.enums;

import com.hp.wpp.avatar.framework.exceptions.EnumMapException;

public enum EntityType {
	
	devices(0),
	services(1);
	
	private int value;
	
	private EntityType(int value) {
		this.value = value ;
	}

	public int getValue() {
		return value;
	}
	
	public static EntityType getEntityTypeByValue(int value){
		for(EntityType type: EntityType.values()){
			if(type.value == value)
				return type;
		}
		throw new EnumMapException("Invalid entity value="+value+" recieved to identify entitytype ");
	}
}
