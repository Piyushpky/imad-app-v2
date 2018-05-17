/**
 * 
 */
package com.hp.wpp.avatar.restmodel.errors;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author bnshr
 *
 */
public enum AVRegErrors {

	AVR000101("AVR000101") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000101");
			error.setHttpCode(HttpStatus.CONFLICT.toString());
			error.setDescription("Country Mismatch in Registration Payload");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000101");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000102("AVR000102") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000102");
			error.setHttpCode(HttpStatus.CONFLICT.toString());
			error.setDescription("Invalid Serial Number in Registraion Payload");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000102");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000103("AVR000103") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000103");
			error.setHttpCode(HttpStatus.CONFLICT.toString());
			error.setDescription("Language Mismatch in Registraion Payload");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000103");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000104("AVR000104") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000104");
			error.setHttpCode(HttpStatus.CONFLICT.toString());
			error.setDescription("Invalid Domain Index in Registraion Payload");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000104");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000105("AVR000105") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000105");
			error.setHttpCode(HttpStatus.CONFLICT.toString());
			error.setDescription("Invalid Registration Data");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000105");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000106("AVR000106") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000106");
			error.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			error.setDescription("Empty Config Urls for service");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000106");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000107("AVR000107") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000107");
			error.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			error.setDescription("Modelling and Validation Failure");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000107");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000108("AVR000108") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000108");
			error.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			error.setDescription("Mapping of enum failed");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000108");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000001("AVR000001") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000001");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Invalid Public Key. Public Key Not Found");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000001");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000002("AVR000002") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000002");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Decompression Failure");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000002");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000003("AVR000003") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000003");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Decrypt Failure");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000003");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000004("AVR000004") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000004");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Signature Mismatch");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000004");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000005("AVR000005") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000005");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Compression Mismatch");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000005");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000006("AVR000006") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000006");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Hash Mismatch");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000006");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000007("AVR000007") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000007");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard JSON Corrupted");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000007");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000008("AVR000008") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000008");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Invalid Postcard");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000008");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000009("AVR000009") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000009");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Unsupported Postcard");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000009");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000010("AVR000010") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000010");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Compression Failure");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000010");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000011("AVR000011") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000011");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Encrypt Failure");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000011");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000012("AVR000012") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000012");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Postcard Entity Not Found");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000012");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000013("AVR000013") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000013");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Key id in negotiation already exists");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000013");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000014("AVR000014") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000014");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Error in generating/retreiveing keys/certificate");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000014");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVR000015("AVR000015") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000015");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Empty cipher inputs");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000015");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000016("AVR000016") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000016");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Invalid Postcard Certificate");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000016");
			errors.getErrors().add(error);
			return errors;
		}
	},AVR000017("AVR000017") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("Avatar registration");
			error.setVersion("1.0");
			error.setCode("AVR000017");
			error.setHttpCode(HttpStatus.FORBIDDEN.toString());
			error.setDescription("Device has been blacklisted");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVR000017");
			errors.getErrors().add(error);
			return errors;
		}
	};

	private String value;

	AVRegErrors(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public abstract Errors getWPPErrorModel();
	
	public static AVRegErrors getAVRegErrors(String errorCode) {
		AVRegErrors[] avregErrors = AVRegErrors.values();

		for (AVRegErrors avregError : avregErrors) {
			if (avregError.getValue().equalsIgnoreCase(errorCode)) {
				return avregError;
			}
		}
		return null;
	}
}
