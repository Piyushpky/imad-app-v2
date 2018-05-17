package com.hp.wpp.avatar.framework.enums;



public enum EntityClassifier {
	printer(EntityType.devices),
	jamc(EntityType.services);
	
	private EntityType entityType;
	
	private EntityClassifier(EntityType entityType) {
		this.entityType = entityType ;
	}

	public EntityType getEntityType() {
		return entityType;
	}
	
	/*public static EntityClassifier getEntityTypeByValue(EntityType entityType){
		for(EntityClassifier type: EntityClassifier.values()){
			if(type.entityType == entityType)
				return type;
		}
		throw new EnumMapException("Invalid entity value="+entityType.name()+" recieved to identify entitytype ");
	}*/
}
