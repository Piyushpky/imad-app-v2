package com.hp.wpp.ssnc.restapp.resources;

import com.hp.wpp.ssnclaim.restapp.resources.PrinterGenericInfoResource;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

/**
 * Created by kumaniti on 12/5/2017.
 */
public class PrinterGenricInfoResourceTest {
    @InjectMocks
    PrinterGenericInfoResource info;

    @BeforeClass
    public void setUp(){
        info=new PrinterGenericInfoResource();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public  void  testGenericInfo(){
        Response response=info.getPrinterGenericInfo("7");
        Assert.assertEquals(response.getStatus(),200);
        }
    @Test
    public  void  testPrinterInfo(){
        Response response=info.getPrinterInfo("7");
        Assert.assertEquals(response.getStatus(),200);
    }


}
