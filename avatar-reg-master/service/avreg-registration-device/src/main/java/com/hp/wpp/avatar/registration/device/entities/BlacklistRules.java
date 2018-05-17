package com.hp.wpp.avatar.registration.device.entities;

import com.hp.wpp.avatar.registration.device.enums.BlacklistRuleType;

import javax.persistence.*;

@Entity
@Table(name="printer_blacklist_rules")
public class BlacklistRules {

	private static final long serialVersionUID = -2816246351310208954L;


	@Id
	@Column(name="ruleId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ruleId;

	@Column(name = "ruleType")
	@Enumerated(EnumType.STRING)
	private BlacklistRuleType ruleType;

	@Column(name = "ruleValue")
	private String ruleValue;


	@Column(name = "isActive")
	private boolean isActive;

	public Long getRuleId() {
		return ruleId;
	}

	public void setId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public BlacklistRuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(BlacklistRuleType ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}
