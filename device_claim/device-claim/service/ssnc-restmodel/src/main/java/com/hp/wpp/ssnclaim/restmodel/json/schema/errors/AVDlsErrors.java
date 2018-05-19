/**
 *
 */
package com.hp.wpp.ssnclaim.restmodel.json.schema.errors;

import org.springframework.http.HttpStatus;

/**
 * @author mahammad
 *
 */
public enum AVDlsErrors {

	AVDLS100001("AVDLS100001") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS100001");
			error.setHttpCode(HttpStatus.SERVICE_UNAVAILABLE.toString());
			error.setDescription("DynamoDB down");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS100001");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000002("AVDLS000002") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000002");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Invalid Length of PrinterCode");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000002");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000003("AVDLS000003") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000003");
			error.setHttpCode(HttpStatus.NOT_FOUND.toString());
			error.setDescription("Invalid PrinterCode");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000003");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000102("AVDLS000102") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000102");
			error.setHttpCode(HttpStatus.NOT_FOUND.toString());
			error.setDescription("snKey not  found in databse");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000102");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000202("AVDLS000202") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000202");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Notification Paylod is invalid");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000202");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000302("AVDLS000302") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000302");
			error.setHttpCode(HttpStatus.NOT_FOUND.toString());
			error.setDescription("CloudId not found");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000302");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000303("AVDLS000303") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000303");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("claimCode not found");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000303");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000304("AVDLS000304") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000304");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("validaton data mismatch");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000304");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000501("AVDLS100501") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS100501");
			error.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			error.setDescription("Unalble to read the step3 event kinesis stream");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS100501");
			errors.getErrors().add(error);
			return errors;
		}
	}, AVDLS000601("AVDLS000601") {
		public Errors getWPPErrorModel() {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000601");
			error.setHttpCode(HttpStatus.NOT_FOUND.toString());
			error.setDescription("UUID not found");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000601");
			errors.getErrors().add(error);
			return errors;
		}
	},AVDLS000602("AVDLS000602") {
		public Errors getWPPErrorModel () {
			Errors errors = new Errors();
			Error error = new Error();
			error.setServiceName("AV Device LookUp Service");
			error.setVersion("1.0");
			error.setCode("AVDLS000602");
			error.setHttpCode(HttpStatus.BAD_REQUEST.toString());
			error.setDescription("Incorrect Payload");
			error.setDescriptionLink("https://vp.avatar.ext.hp.com/virtualprinter/v1/errorinfo?code=AVDLS000602");
			errors.getErrors().add(error);
			return errors;
		}
	};

	private String value;

	AVDlsErrors(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public abstract Errors getWPPErrorModel();

	public static AVDlsErrors getAVDlsErrors(String errorCode) {
		AVDlsErrors[] avErrors = AVDlsErrors.values();

		for (AVDlsErrors avError : avErrors) {
			if (avError.getValue().equalsIgnoreCase(errorCode)) {
				return avError;
			}
		}
		return null;
	}
}
