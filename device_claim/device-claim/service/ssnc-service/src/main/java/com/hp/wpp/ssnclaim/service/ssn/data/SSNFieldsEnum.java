package com.hp.wpp.ssnclaim.service.ssn.data;

public enum SSNFieldsEnum {

	VERSION() {
		@Override
		public void populate(byte[] header, SSNFields ssnEntity) {
			int versionInInt = ((header[0] & 0xE0) >> 5);
			String version = Integer.toString(versionInInt);

			ssnEntity.setVersion(version);
		}
	},

	REGISTRATION_DOMAIN_INDEX() {
		@Override
		public void populate(byte[] header, SSNFields ssnEntity) {
			byte[] domainIndexInBytes = new byte[] { (byte) (header[1] & 0xFF),
					(byte) ((header[2] & 0xC0) >> 6) };
			int domainIndex = ((domainIndexInBytes[0]) << 2)
					| (domainIndexInBytes[1]);

			ssnEntity.setDomainIndex(domainIndex);
		}
	},
	OVERRUN_BIT() {
		@Override
		public void populate(byte[] header, SSNFields ssnEntity) {
			ssnEntity.setOverrunBit((Boolean) ((header[3] & 0x80) != 0));
		}
	},
	ISSUANCE_COUNTER() {
		@Override
		public void populate(byte[] header, SSNFields ssnEntity) {
			byte[] countInBytes = new byte[] { (byte) (header[3] & 0x7F),
					(byte) ((header[4] & 0xE0) >> 5) };
			int count = (countInBytes[0] << 3) | (countInBytes[1]);

			ssnEntity.setIssuanceCounter(count);
		}
	},
	INSTANT_INK_BIT() {
		@Override
		public void populate(byte[] header, SSNFields ssnEntity) {
			ssnEntity.setInstantInkFlag((Boolean) ((header[4] & 0x10) != 0));
		}
	};
	public abstract  void populate(byte[] header, SSNFields ssnEntity);
}
