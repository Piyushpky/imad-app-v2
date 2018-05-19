
package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;
import org.springframework.beans.BeanUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by naikraj on 6/7/2016.
 */

public class BeanUtilsTest {

    public static final String SERIAL_NUMBER = "XXXX";

    @Test
    public void transform() {
        SSNFields ssnFields = new SSNFields();
        ssnFields.setSerialNumber(SERIAL_NUMBER);
        ssnFields.setDomainIndex(1);
        ssnFields.setSsn("SSN");
        ssnFields.setVersion("1.0");
        ssnFields.setInstantInkFlag(true);
        ssnFields.setIssuanceCounter(10);
        ssnFields.setOverrunBit(true);
        PrinterCodeData printerCodeData = new PrinterCodeData();

        BeanUtils.copyProperties(ssnFields, printerCodeData);

        Assert.assertEquals(printerCodeData.getSerialNumber(), SERIAL_NUMBER);
        Assert.assertEquals(printerCodeData.getDomainIndex(), 1);
        //Assert.assertEquals(printerCodeData.getprinterCode(), "SSN");
        Assert.assertEquals(printerCodeData.getVersion(), "1.0");
        Assert.assertEquals(printerCodeData.getInstantInkFlag().booleanValue(), true);
        Assert.assertEquals(printerCodeData.getIssuanceCounter(), 10);
        Assert.assertEquals(printerCodeData.getOverrunBit().booleanValue(), true);
    }
}

