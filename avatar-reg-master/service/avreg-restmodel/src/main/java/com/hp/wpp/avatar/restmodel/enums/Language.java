package com.hp.wpp.avatar.restmodel.enums;

import java.util.HashMap;
import java.util.Map;

import com.hp.wpp.avatar.framework.exceptions.EnumMapException;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

public enum Language {

    Arabic("ar","Arabic"),
    Bulgarian("bg","Bulgarian"),
    Catalan("ca","Catalan"),
    Czech("cs","Czech"),
    Danish("da","Danish"),
    German("de","German"),
    Greek("el","Greek"),
    EnglishUS("en","English US"),
    EuropeanEnglish("en-EU","European English"),
    Spanish ("es","Spanish"),
    Estonian("et","Estonian"),
    EastEuro("eu","East euro"),
    Finnish ("fi","Finnish"),
    French("fr","French"),
    CanadianFrench("fr-CA","Canadian French"),
    Hebrew("he","Hebrew"),
    Croatian("hr","Croatian"),
    Hungarian("hu","Hungarian"),
    IndonesianBhasa("id","Indonesian Bhasa"),
    Italian("it","Italian"),
    Japanese("ja","Japanese"),
    Korean("ko","Korean"),
    Lithuvanian("lt","Lithuvanian"),
    Lativian("lv","Lativian"),
    Malay("ms","Malay"),
    Dutch("nl","Dutch"),
    Norwegian("no","Norwegian"),
    Polish("pl","Polish"),
    Portuguese("pt","Portuguese"),
    Romanian("ro","Romanian"),
    Russian("ru","Russian"),
    Slovak("sk","Slovak"),
    Slovenian("sl","Slovenian"),
    Swedish("sv","Swedish"),
    Thai("th","Thai"),
    Turkish("tr","Turkish"),
    Ukranian("uk","Ukranian"),
    SimplifiedChinese("zh-CN","Simplified Chinese"),
    TraditionalChinese("zh-TW","Traditional Chinese");
	

    private final String languageCode;
    private final String languageFullName;
    private static Map<String,Language> languageMap;
    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(Language.class);

    Language(String languageCode, String languageFullName) {
        this.languageCode = languageCode;
        this.languageFullName = languageFullName;
    }

    @Override
    public String toString(){
        return this.languageCode;
    }

    public String getValue(){
    	return this.languageCode;
    }

    public String getLanguageFullName(){ return this.languageFullName; }

    public static Language getLanguage(String languageCode){

        if(languageMap==null){
            synchronized (Language.class) {
                LOG.debug("Loading languageMap with language enumerations...");
                if(languageMap == null) {
                    languageMap = new HashMap<>();

                    for (Language l : Language.values()) {
                        languageMap.put(l.toString(), l);
                    }
                }
                LOG.debug("Language enum map loaded with size: {}",languageMap.entrySet().size());
            }
        }
        Language language = languageMap.get(languageCode);
        if(language == null)
            throw new EnumMapException("language-code["+languageCode+"] does not exist");

        return language;
    }
	
}
