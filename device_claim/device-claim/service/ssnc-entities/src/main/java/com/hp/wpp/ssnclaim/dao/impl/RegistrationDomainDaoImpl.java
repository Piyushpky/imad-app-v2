package com.hp.wpp.ssnclaim.dao.impl;

import java.util.List;

import com.hp.wpp.ssnclaim.exception.AvDlsDynamoDBException;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.entities.RegistrationDomainEntity;
import com.hp.wpp.ssnclaim.entities.dac.vo.RegDomainVO;

public class RegistrationDomainDaoImpl {

	// The HashKey configured in the Claim table.
	static final String CLAIM_HASH_KEY = "domainIndex";
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(RegistrationDomainDaoImpl.class);
	private final DynamoDBMapper dynamoDBMapper;
	private static final String ENVIRONMENT = "dls_environment";
	private static final String REGISTRATION_DOMAIN_ENTITY = "registration_domain";

	@Autowired
	public RegistrationDomainDaoImpl(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public RegistrationDomainEntity getRegDomainKey(int domainIndex) {
		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				REGISTRATION_DOMAIN_ENTITY + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		RegistrationDomainEntity regDomain = null;

		try {
			regDomain = dynamoDBMapper.load(RegistrationDomainEntity.class, domainIndex, config);
			LOG.debug("Domain Index passed is {}", domainIndex);
		} catch (Exception exception) {
			LOG.debug("Failed to load DomainKey for {}", exception, domainIndex);
			throw new AvDlsDynamoDBException("Failed to load DomainKey");
		}
		return regDomain;
	}

	/**
	 * Helper method to convert Dynamo's entity, Claim to ClaimVO
	 *
	 * @param ssnPrinterClaim
	 *            Claim to convert
	 * @return ClaimVO Converted ClaimVO
	 */
	RegDomainVO convertToClaimVO(RegistrationDomainEntity regDomain) {
		return new RegDomainVO(regDomain.getDomainIndex(), regDomain.getDomainKey());
	}
   // Used only in Test classes
	public void createRegistrationDomainEntity(RegistrationDomainEntity registrationDomainEntity) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				REGISTRATION_DOMAIN_ENTITY + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		LOG.debug("Creating domain index for {}" + registrationDomainEntity);
		ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue()
				.withComparisonOperator(ComparisonOperator.NULL);
		DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression().withExpectedEntry(CLAIM_HASH_KEY,
				expectedAttributeValue);

		try {
			dynamoDBMapper.save(registrationDomainEntity, dynamoDBSaveExpression, config);
			LOG.debug("Registration domain index key saved for domainIndex {}",
					registrationDomainEntity.getDomainIndex());
		} catch (Exception exception) {
			LOG.debug("Failed to create registration domain for {}", exception, registrationDomainEntity);
		}

	}

	public List<RegistrationDomainEntity> getRegistrationDomainEntity() {
		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				REGISTRATION_DOMAIN_ENTITY + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
		List<RegistrationDomainEntity> registrationDomainEntity = null;
		try {
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			registrationDomainEntity = dynamoDBMapper.scan(RegistrationDomainEntity.class, scanExpression, config);
			LOG.debug("RegistrationDomainEntity is   records fetched are {}", registrationDomainEntity.size());
		} catch (Exception exception) {
			LOG.debug("Failed to load url value ", exception);
		}
		return registrationDomainEntity;
	}

}
