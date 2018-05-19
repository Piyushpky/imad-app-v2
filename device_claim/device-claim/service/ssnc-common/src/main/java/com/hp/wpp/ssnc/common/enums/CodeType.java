package com.hp.wpp.ssnc.common.enums;


public enum CodeType {
    PRINTERCODE(16),
    SSNCODE(18),
    NONE(-1);

    private final int length;

    private CodeType(int length) {
        this.length = length;
    }

    public static CodeType getCodeType(int length){
        for (CodeType codeType: CodeType.values()){
            if(codeType.length == length){
                return codeType;
            }
        }
        return CodeType.NONE;
    }
}

