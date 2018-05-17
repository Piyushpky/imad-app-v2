package com.hp.wpp.avatar.restmodel.enums;


public enum Originator {

	front_panel("front_panel"),
	ews("ews"),
	sw("sw"),
	mobile_app("mobile_app"),
	wja("wja"),
	JAMC("JAMC"),
	other("other");
    
    private final String value;

    Originator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Originator fromValue(String v) {
        for (Originator c: Originator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
