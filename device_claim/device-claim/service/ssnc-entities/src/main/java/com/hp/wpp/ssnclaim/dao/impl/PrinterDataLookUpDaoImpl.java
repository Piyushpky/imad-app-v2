package com.hp.wpp.ssnclaim.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.hp.wpp.ssnclaim.exception.AvDlsDynamoDBException;
import com.hp.wpp.ssnclaim.exception.UUIDNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.entities.dac.vo.PrinterDataLookUpVO;

/**
 * DynamoDBClaimServiceImpl is the implementation of ClaimService using Amazon's
 * DynamoDB.
 *
 * The configured / enabled implementation of the ClaimService should be
 * obtained using DACServiceProvider only. It's not advised to use, the
 * DynamoDBClaimServiceImpl directly, instead DACServiceProvider.getClaimService
 * should be used, even to get this instance. This provides an option to switch
 * the implementation without any impact.
 *
 */
public class PrinterDataLookUpDaoImpl {

	// The HashKey configured in the Claim table.
	static final String CLAIM_HASH_KEY = "snKey";
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PrinterDataLookUpDaoImpl.class);
	private final DynamoDBMapper dynamoDBMapper;
	private static final String ENVIRONMENT = "dls_environment";
	private static final String PRINTER_DATA_TABLE = "printer_data";

	private static final WPPLogger logger = WPPLoggerFactory
			.getLogger(PrinterDataLookUpDaoImpl.class);

	@Autowired
	public PrinterDataLookUpDaoImpl(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/**

	 * ConditionalCheckFailedException ProvisionedThroughputExceededException
	 * InternalServerErrorException ItemCollectionSizeLimitExceededException
	 * LimitExceededException ResourceInUseException ResourceNotFoundException
	 */

	public PrinterDataEntity createPrinterDataLookUp(PrinterDataEntity printerDataLookUpEntity) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		LOG.debug("Creating claim for {}" + printerDataLookUpEntity);
		ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue()
				.withComparisonOperator(ComparisonOperator.NULL);
		DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression().withExpectedEntry(CLAIM_HASH_KEY,
				expectedAttributeValue);

		try {
			dynamoDBMapper.save(printerDataLookUpEntity, dynamoDBSaveExpression, config);
			LOG.debug("Claim saved with SnKey {}", printerDataLookUpEntity.getSnKey());
		} catch (Exception e) {
			LOG.debug("Failed to create claim for {} {}", e, printerDataLookUpEntity);
			throw new AvDlsDynamoDBException(e.getMessage());
		}

		return printerDataLookUpEntity;
	}

	public PrinterDataEntity getPrinterDataLookUpEntity(String snKey) {
		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE +enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		PrinterDataEntity printerDataLookUpEntity = null;
		try {
			printerDataLookUpEntity = dynamoDBMapper.load(PrinterDataEntity.class, snKey, config);
			LOG.debug("Claim details for ClaimId {} is {}", snKey, printerDataLookUpEntity);
		} catch (Exception e) {
			LOG.debug("Failed to load claim for {} {}", e, snKey);
			throw new AvDlsDynamoDBException(e.getMessage());
		}
		return printerDataLookUpEntity;
	}

	/**
	 * Helper method to convert the ClaimVO to Claim (Dynamo's entity)
	 *
	 * @param claimVO
	 *            ClaimVO to convert
	 * @return Claim Converted Claim
	 */
	PrinterDataEntity convertToClaim(PrinterDataLookUpVO printerDataLookUpVO) {
		return new PrinterDataEntity(printerDataLookUpVO.getSnKey(), printerDataLookUpVO.getDomainIndex(),
				printerDataLookUpVO.getIsInkCapable(), printerDataLookUpVO.getPrinterId());
	}

	/**
	 * Helper method to convert Dynamo's entity, Claim to ClaimVO
	 *
	 * @param ssnPrinterClaim
	 *            Claim to convert
	 * @return ClaimVO Converted ClaimVO
	 */
	PrinterDataLookUpVO convertToClaimVO(PrinterDataEntity printerDataLookUpEntity) {
		return new PrinterDataLookUpVO(printerDataLookUpEntity.getSnKey(), printerDataLookUpEntity.getDomainIndex(),
				printerDataLookUpEntity.isInkCapable(), printerDataLookUpEntity.getCloudId(), null, 0, 0, null);
	}

	public void deletePrinterDataLookUpEntity(String snKey) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		PrinterDataEntity printerDataLookUpEntity = null;
		try {
			printerDataLookUpEntity = dynamoDBMapper.load(PrinterDataEntity.class, snKey, config);
			LOG.debug("Claim details for ClaimId {} is {}", snKey, printerDataLookUpEntity);
			dynamoDBMapper.delete(printerDataLookUpEntity);
		} catch (Exception exception) {
			LOG.debug("Failed to load claim for {}", exception, snKey);
		}
	}
	public PrinterDataEntity getPrinterDataLookUpEntityByCloudID(String cloudId) {
		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);
		PrinterDataEntity cloudIdEntity= new PrinterDataEntity();
		cloudIdEntity.setCloudId(cloudId);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);
		DynamoDBQueryExpression<PrinterDataEntity> expression = new DynamoDBQueryExpression<PrinterDataEntity>()
				.withIndexName("cloud_id-index")
				.withConsistentRead(false)
				.withHashKeyValues(cloudIdEntity);
		PaginatedQueryList<PrinterDataEntity> printerDataLookUpEntity = null;
		try {
			printerDataLookUpEntity = dynamoDBMapper.query(PrinterDataEntity.class,expression,config);
			LOG.debug("Claim details for ClaimId {} is {}", cloudId, printerDataLookUpEntity);
		} catch (Exception e) {
			LOG.debug("Failed to load claim for {} {}", e, cloudId);
			throw new AvDlsDynamoDBException(e.getMessage());
		}
		if(printerDataLookUpEntity==null || printerDataLookUpEntity.isEmpty())
			return null;
		return printerDataLookUpEntity.get(0);
	}

	public void deletePrinterDataLookUpEntity(PrinterDataEntity printerDataLookUpEntity) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);

		try {

			LOG.debug("Claim details for ClaimId {} is {}", printerDataLookUpEntity.getSnKey(), printerDataLookUpEntity);
			dynamoDBMapper.delete(printerDataLookUpEntity,config);
		} catch (Exception exception) {
			LOG.debug("Failed to delete claim for SnKey={},exception={}",printerDataLookUpEntity.getSnKey(), exception );
			throw exception;
		}
	}

	public PrinterDataEntity getPrinterDataLookUpEntityByUUID(String uuid) {

		String enviornment = System.getenv(ENVIRONMENT) == null ? "" : System.getenv(ENVIRONMENT);
		LOG.debug("Loaded environment : " + enviornment);
		PrinterDataEntity uuidEntity= new PrinterDataEntity();
		uuidEntity.setdeviceUUID(uuid);

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(
				PRINTER_DATA_TABLE + enviornment);
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(tableNameOverride);

		DynamoDBQueryExpression<PrinterDataEntity> expression = new DynamoDBQueryExpression<PrinterDataEntity>()
				.withIndexName("uuid-index")
				.withConsistentRead(false)
				.withHashKeyValues(uuidEntity);

		PaginatedQueryList<PrinterDataEntity> printerDataLookUpEntity = null;
		printerDataLookUpEntity = dynamoDBMapper.query(PrinterDataEntity.class,expression,config);
		if(printerDataLookUpEntity==null  || printerDataLookUpEntity.isEmpty()){
			logger.debug("UUID not found in database: {}", uuid);
			throw new UUIDNotFoundException();
		}
		return printerDataLookUpEntity.get(0);

	}
}
