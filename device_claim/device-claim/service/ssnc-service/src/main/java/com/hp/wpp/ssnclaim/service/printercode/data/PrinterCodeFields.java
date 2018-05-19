package com.hp.wpp.ssnclaim.service.printercode.data;


public enum PrinterCodeFields {

	VERSION() {
		
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			int versionInInt = ((header[0] & 0xE0) >> 5);
			String version = Integer.toString(versionInInt);
			printerCodeEntity.setVersion(version);
		}
	},
	
	REGISTRATION_DOMAIN_INDEX()
	{
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			byte[] domainIndexInBytes = new byte[] {
					(byte) (header[0] & 0x1F),
					(byte) ((header[1] & 0xF0) >> 4) };
			int domainIndex = (((int)domainIndexInBytes[0]) << 4)
					| (domainIndexInBytes[1]);
			printerCodeEntity.setDomainIndex(domainIndex);
		}
	},
	
	INSTANT_INK_BIT(){
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			printerCodeEntity.setInstantInkFlag(((header[1] & 0x08) != 0));
		}
	},
	
	OWNERSHIP_COUNTER()
	{
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			int count = ((header[2] & 0xC0) >> 6);
			printerCodeEntity.setOwnership(count);
		}
	},
	
	OVERRUN_BIT()
	{
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			printerCodeEntity.setOverrunBit(((header[2] & 0x20) != 0));
		}
	},
	ISSUANCE_COUNTER(){
		@Override
		public void extract(byte[] header, PrinterCodeData printerCodeEntity) {
			int issuanceCount = (header[2] & 0x1F);
			printerCodeEntity.setIssuanceCounter(issuanceCount);
		}
	};
	
	 public abstract void extract(byte[] header, PrinterCodeData ssnEntity);
}
