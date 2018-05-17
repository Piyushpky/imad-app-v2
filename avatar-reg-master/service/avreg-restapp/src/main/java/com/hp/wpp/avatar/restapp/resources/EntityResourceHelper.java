package com.hp.wpp.avatar.restapp.resources;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.*;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.restapp.util.JSONUtility;
import com.hp.wpp.avatar.restmodel.errors.AVRegErrors;
import com.hp.wpp.avatar.restmodel.json.schema.EntityConfig;
import com.hp.wpp.avatar.restmodel.json.schema.EntityConfig.Link;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification.EntityAdditionalId;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification.EntityVersion;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.exception.*;
import com.hp.wpp.ssn.exception.InvalidSSNException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class EntityResourceHelper {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(EntityResourceHelper.class);
	
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	
    public static Response actOnRegistrationFailure(EntityIdentification entityIdentification, EntityConfigurationBO entityConfigurationBO, Throwable e) {
        Status httpStatusCode = null;
        Response response;
        String error=null;
        
            
    if (e instanceof CountryMismatchException) {
    	error=AVRegErrors.AVR000101.toString();
    	response=buildResponse(Status.CONFLICT,AVRegErrors.AVR000101);
	} else if (e instanceof InvalidSerialNumberException) {
		error=AVRegErrors.AVR000102.toString();
		response=buildResponse(Status.CONFLICT,AVRegErrors.AVR000102);
    } else if (e instanceof LanguageMismatchException) {
    	error=AVRegErrors.AVR000103.toString();
    	response=buildResponse(Status.CONFLICT,AVRegErrors.AVR000103);
    } else if (e instanceof InvalidEntityDomainIndexException) {
    	error=AVRegErrors.AVR000104.toString();
    	response=buildResponse(Status.CONFLICT,AVRegErrors.AVR000104);
    }else if (e instanceof InvalidRegistrationDataException || e.getCause() instanceof InvalidRegistrationDataException) {
    	error=AVRegErrors.AVR000105.toString();
    	response=buildResponse(Status.CONFLICT,AVRegErrors.AVR000105);
    }else if (e instanceof EmptyConfigUrlException) {
    	error=AVRegErrors.AVR000106.toString();
    	response=buildResponse(Status.INTERNAL_SERVER_ERROR,AVRegErrors.AVR000106);
    }else if (e instanceof ModellingException) {
    	error=AVRegErrors.AVR000107.toString();
    	response=buildResponse(Status.INTERNAL_SERVER_ERROR,AVRegErrors.AVR000107);
    }else if (e instanceof EnumMapException) {
    	error=AVRegErrors.AVR000108.toString();
    	response=buildResponse(Status.INTERNAL_SERVER_ERROR,AVRegErrors.AVR000108);
    } else if (e instanceof InvalidPublicKeyException) {
    	error=AVRegErrors.AVR000001.toString();
    	response=buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000001);
    } else if (e instanceof PostcardDeCompressionFailureException) {
    	error=AVRegErrors.AVR000002.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000002);
    } else if (e instanceof PostcardDecryptFailureException) {
    	error=AVRegErrors.AVR000003.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000003);
    } else if (e instanceof PostcardSignatureMismatchException) {
    	error=AVRegErrors.AVR000004.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000004);
    } else if (e instanceof PostcardCompressionMismatchException) {
    	error=AVRegErrors.AVR000005.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000005);
    } else if (e instanceof PostcardHashMismatchException) {
    	error=AVRegErrors.AVR000006.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000006);
    } else if (e instanceof PostcardJSONCorruptedException) {
    	error=AVRegErrors.AVR000007.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000007);
    } else if (e instanceof InitiateKeyNegotiationException|| e instanceof InvalidPostcardException) {
    	error=AVRegErrors.AVR000008.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000008);
    } else if (e instanceof UnsupportedPostcardException) {
    	error=AVRegErrors.AVR000009.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000009);
    } else if (e instanceof PostcardCompressionFailureException) {
    	error=AVRegErrors.AVR000010.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000010);
    }else if (e instanceof PostcardEncryptFailureException) {
    	error=AVRegErrors.AVR000011.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000011);
    }else if (e instanceof PostcardEntityNotFoundException) {
    	error=AVRegErrors.AVR000012.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000012);
    }else if (e instanceof KeyExistsException) {
    	error=AVRegErrors.AVR000013.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000013);
    }else if (e instanceof KeyCertificateException) {
    	error=AVRegErrors.AVR000014.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000014);
    }else if (e instanceof EmptyCipherInputsException) {
    	error=AVRegErrors.AVR000015.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000015);
    }else if (e instanceof InvalidPostcardCertificateException) {
    	error=AVRegErrors.AVR000016.toString();
    	response= buildResponse(Status.BAD_REQUEST,AVRegErrors.AVR000016);
    }else if(e.getCause() instanceof InvalidSSNException){
            httpStatusCode = Status.CONFLICT;
            response=Response.status(httpStatusCode).build();
    }else if(e instanceof PostcardNonRetriableException){
            httpStatusCode = Status.BAD_REQUEST;
            response=Response.status(httpStatusCode).build();
    }else if(e instanceof DeviceBlacklistException){
		error=AVRegErrors.AVR000017.toString();
		response= buildResponse(Status.FORBIDDEN,AVRegErrors.AVR000017);
	}
    else{
            httpStatusCode = Status.INTERNAL_SERVER_ERROR;
            response=Response.status(httpStatusCode).build();
			e.printStackTrace();
    }     
    	printRegistrationStatusOnFailure(entityIdentification, entityConfigurationBO, e, response,error);
        return response;
    }
    
    
    protected static Response buildResponse(Status httpStatus, AVRegErrors avregErrors) {
		if(avregErrors != null)
		{
			String error=JSONUtility.marshal(avregErrors.getWPPErrorModel());
			return Response.status(httpStatus).header("Internal-Error-Code",avregErrors.getWPPErrorModel().getErrors().get(0).getCode()).entity(error).build();
		}
		else
			return Response.status(httpStatus.getStatusCode()).build();
	}

	public static void printRegistrationStatusOnFailure(EntityIdentification entityIdentification, EntityConfigurationBO entityConfigurationBO, Throwable e, Response response, String error) {
        if (entityIdentification == null){
        	LOG.error("api=registerEntity; method=POST; executionType=SyncFlow; entityIdentificationObject=null; executionState=COMPLETED; status=FAILURE; statusCode={}; internalErrorCode={}; failureReason=\"{}\";",response.getStatus(),(error!=null)?error:"none",e.getMessage());
            return;
        }
			LOG.error("api=registerEntity; method=POST; executionType=SyncFlow; entityId=\"{}\"; entityType={}; entityModel={}; cloudId={}; entityVersion={}; countryAndRegion={}; language={}; printerUUID={}; executionState=COMPLETED; statusCode={}; internalErrorCode={}; status=FAILURE; failureReason=\"{}\";",getLoggableEntityId(entityIdentification),(entityIdentification!=null?entityIdentification.getEntityType():null),(entityIdentification!=null?entityIdentification.getEntityModel():null),(entityConfigurationBO!=null?entityConfigurationBO.getCloudId():null),getFrimwareVersion(entityIdentification),(entityIdentification!=null?entityIdentification.getCountryAndRegionName():null),(entityIdentification!=null?entityIdentification.getLanguage():null), (entityIdentification!=null?getPrinterUUID(entityIdentification):null),response.getStatus(),(error!=null)?error:"none", e.getMessage());
	}

	public static EntityIdentification.EntityVersion getFrimwareVersion(EntityIdentification entityIdentification) {
		
		EntityVersion entityVersion = (entityIdentification!=null?entityIdentification.getEntityVersion():null);
		return entityVersion !=null ? entityVersion : null;
	}

	public static String getLoggableEntityId(EntityIdentification entityIdentification) {
		
		String  entityId = (entityIdentification!=null?entityIdentification.getEntityId():null);
		if(entityId == null || entityId.isEmpty())
			return entityId;
		return generateSHA256AndBase64(entityId.getBytes());
	}
	
	public static String getPrinterUUID(EntityIdentification entityIdentification) {
		String uuid=null;
		
		try
		{
		 List<EntityAdditionalId>  additionalIds = (entityIdentification!=null?entityIdentification.getEntityAdditionalIds():null);
			 
		 for (EntityAdditionalId id : additionalIds)
         {
			 if(id.getIdType().equals("printer_uuid"))
			 {
				 uuid=id.getIdValue();
				 return uuid;
			 }
         }
		}	catch(NullPointerException e)
			{
				return null;
			}
		 return uuid;
		}

	public static EntityIdentification createAndValidateEntityIdentification(String regPayload) throws EntityRegistrationNonRetriableException{
		EntityIdentification entityIdentification = null;
		try {
			entityIdentification = (EntityIdentification) JSONUtility.unmarshal(EntityIdentification.class, regPayload);
			
			if(entityIdentification.getCountryAndRegionName()==null)
			{
				throw new CountryMismatchException("Country mismatch in registration payload");
			}
			if(entityIdentification.getLanguage()==null)
			{
				throw new LanguageMismatchException("Language mismatch in registration payload");
			}
			if(StringUtils.isBlank(entityIdentification.getEntityId()))
			{
				throw new InvalidSerialNumberException("Empty Serial Number in registration payload");
			}if(entityIdentification.getEntityType()== EntityType.devices)
			{
				if(entityIdentification.getEntityId().length()!=10)
				throw new InvalidSerialNumberException("Invalid Serial Number in registration payload");
			}
			if(StringUtils.isBlank(entityIdentification.getEntityDomain()))
			{
				throw new InvalidEntityDomainIndexException("Empty Domain in registration payload");
			}
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<EntityIdentification>> violations = validator.validate(entityIdentification);
			if (!violations.isEmpty()) {
				throw new InvalidRegistrationDataException("Registration payload validation failed");
			}
			EntityIdentification.EntityVersion entityVersion = entityIdentification.getEntityVersion();
			if(entityVersion==null ||(entityVersion.getRevision()==null && entityVersion.getDate() == null))
			{
				throw new InvalidRegistrationDataException("Registration payload firmaware field is null");
			}else if(entityVersion.getDate()!=null){
				validateDate(entityVersion.getDate());
			}
			
			if(entityIdentification.getEntityClassifier()==null)
				LOG.debug("entity_classifier attribute is missing in reg payload");
			
			if(entityIdentification.getEntityClassifier()!=null && (!entityIdentification.getEntityClassifier().getEntityType().equals(entityIdentification.getEntityType())))
				throw new InvalidRegistrationDataException("Registration payload entityType="+entityIdentification.getEntityType()+" & calssifier="+entityIdentification.getEntityClassifier()+" mismatch");
		} catch (Exception e) {
			
			if(e instanceof CountryMismatchException || e instanceof LanguageMismatchException || e instanceof InvalidSerialNumberException || e instanceof InvalidRegistrationDataException || e instanceof InvalidEntityDomainIndexException)
				throw e;
			else 
			throw new InvalidRegistrationDataException(e.getMessage());
		}
		return entityIdentification;
	}

	public static String createEntityConfigJsonPayload(EntityConfigurationBO entityConfigurationBO) {
		EntityConfig config = new EntityConfig();
		
		try {
			BeanUtils.copyProperties(config, entityConfigurationBO);
			config.setVersion(entityConfigurationBO.getSpecVersion());
			for(LinkBO linkBO: entityConfigurationBO.getConfigurations()){
				Link link = new Link();
				BeanUtils.copyProperties(link, linkBO);
				config.getConfigurations().add(link);
			}
		} catch (Exception e) {
			throw new ModellingException("Error in create EntityConfig json payload",e);
		}
		
		return JSONUtility.marshal(config);
	}
	
	public static Date validateDate(String date) throws InvalidRegistrationDataException {
		try {
			return new SimpleDateFormat(DATE_PATTERN).parse(date);
		} catch (Exception e) {
			LOG.debug("Date [{}] format Error",e,date);
			throw new InvalidRegistrationDataException("String to date conversion error");
		}
	}
	
	private static final String SHA256_ALGORITHM_NAME = "SHA-256";
	public static String generateSHA256AndBase64(byte[] serialNumberInBytes) {
		byte[] hash;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
			hash = md.digest(serialNumberInBytes);
		} catch (Exception e) {
			LOG.error("Exception while hashing entityId; failureReason={}",e);
            return null;
		}
		return new String(Base64.encodeBase64(hash));
	}

}
