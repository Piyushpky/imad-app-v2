package com.hp.wpp.ssnclaim.dao.impl;

import com.hp.wpp.ssnclaim.exception.AvDlsDynamoDBException;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import com.hp.wpp.ssnclaim.entities.dac.vo.LinksVO;

public class LinksDaoImpl {
	
	static final String URL_TYPE = "urlType";
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(LinksDaoImpl.class);
	private final DynamoDBMapper dynamoDBMapper;
	private static final String ENVIRONMENT = "dls_environment";
	private static final String LINK_TABLE = "ssn_reference_url";

	@Autowired
	public LinksDaoImpl(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;

	}

	public LinksEntity getLinkUrl(String urlType) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				LINK_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		LinksEntity linksEntity = null;
		try {
			linksEntity = dynamoDBMapper.load(LinksEntity.class, urlType, config);
			LOG.debug("Url type passed is {} , fetched value is {}", urlType, linksEntity);
		} catch (Exception exception) {
			LOG.debug("Failed to load url value for {}", exception, urlType);
			throw new AvDlsDynamoDBException("failed to load links ,"+ exception.getMessage());
		}
		return linksEntity;
	}

	/**
	 * Helper method to convert Dynamo's entity, Claim to ClaimVO
	 *
	 * @param ssnPrinterClaim
	 *            Claim to convert
	 * @return ClaimVO Converted ClaimVO
	 */
	LinksVO convertToClaimVO(LinksEntity linkEntity) {
		return new LinksVO(linkEntity.getUrlType(), linkEntity.getUrlValue());
	}
		// createLinkUrl used only in Test Classes
	public void createLinkUrl(LinksEntity linksEntity) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				LINK_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		LOG.debug("Creating link url for {}" + linksEntity.getUrlType());
		ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue()
				.withComparisonOperator(ComparisonOperator.NULL);
		DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression().withExpectedEntry(URL_TYPE,
				expectedAttributeValue);

		try {
			dynamoDBMapper.save(linksEntity, dynamoDBSaveExpression, config);

		} catch (Exception exception) {
			LOG.debug("Failed to create registration domain for {}", exception, linksEntity);
		}

	}

}
