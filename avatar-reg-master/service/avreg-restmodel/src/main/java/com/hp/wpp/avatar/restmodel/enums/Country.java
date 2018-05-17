package com.hp.wpp.avatar.restmodel.enums;

public enum Country {
//"latin-america": enum name is as per spec and value is for readability
	africa("africa"),
	angola("angola"),
	argentina("argentina"),
	asiaPacific("asiaPacific"),
	australia("australia"),
	austria("austria"),
	bahrain("bahrain"),
	bangladesh("bangladesh"),
	belarus("belarus"),
	belgium("belgium"),
	brazil("brazil"),
	brunei("brunei"),
	bulgaria("bulgaria"),
	canada("canada"),
	caribbean("caribbean"),
	chile("chile"),
	china("china"),
	colombia("colombia"),
	costaRica("costaRica"),
	croatia("croatia"),
	czechRepublic("czechRepublic"),
	denmark("denmark"),
	ecuador("ecuador"),
	egypt("egypt"),
	estonia("estonia"),
	europe("europe"),
	finland("finland"),
	france("france"),
	germany("germany"),
	greece("greece"),
	guatemala("guatemala"),
	hongKongSAR("hongKongSAR"),
	hungary("hungary"),
	iceland("iceland"),
	india("india"),
	indonesia("indonesia"),
	iran("iran"),
	ireland("ireland"),
	israel("israel"),
	italy("italy"),
	japan("japan"),
	japanCISMEA("japanCISMEA"),
	jordan("jordan"),
	kazakhstan("kazakhstan"),
	korea("korea"),
	kuwait("kuwait"),
	latinAmerica("latinAmerica"),
	latvia("latvia"),
	lebanon("lebanon"),
	lithuania("lithuania"),
	luxembourg("luxembourg"),
	malaysia("malaysia"),
	malta("malta"),
	mexico("mexico"),
	middleEast("middleEast"),
	morocco("morocco"),
	mozambique("mozambique"),
	netherlandsThe("netherlandsThe"),
	newZealand("newZealand"),
	nordic("nordic"),
	northAmericaUSACanada("northAmericaUSACanada"),
	northWestAfrica("northWestAfrica"),
	norway("norway"),
	other("other"),
	pakistan("pakistan"),
	panama("panama"),
	paraguay("paraguay"),
	peru("peru"),
	philippines("philippines"),
	poland("poland"),
	portugal("portugal"),
	qatar("qatar"),
	romania("romania"),
	russia("russia"),
	saudiArabia("saudiArabia"),
	serbiaMontenegro("serbiaMontenegro"),
	singapore("singapore"),
	slovakia("slovakia"),
	slovenia("slovenia"),
	southAfrica("southAfrica"),
	southernAfrica("southernAfrica"),
	spain("spain"),
	sriLanka("sriLanka"),
	sweden("sweden"),
	swissFrench("swissFrench"),
	swissItalian("swissItalian"),
	switzerland("switzerland"),
	taiwan("taiwan"),
	thailand("thailand"),
	tunisia("tunisia"),
	turkey("turkey"),
	ukraine("ukraine"),
	unitedArabEmirates("unitedArabEmirates"),
	unitedKingdom("unitedKingdom"),
	unitedStates("unitedStates"),
	uruguay("uruguay"),
	venezuela("venezuela"),
	vietnam("vietnam"),
	yemen("yemen");

	
	private final String countryName;   

    Country(String value) {
      this.countryName = value;
    }
    
    public String getValue(){
    	return this.countryName;
    }

	}
