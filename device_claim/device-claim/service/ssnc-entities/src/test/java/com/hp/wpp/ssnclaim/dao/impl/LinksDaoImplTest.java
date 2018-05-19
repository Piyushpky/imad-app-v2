package com.hp.wpp.ssnclaim.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hp.wpp.mock.dynamodb.http.DynamoDBHttpMockService;
import com.hp.wpp.ssnclaim.entities.LinksEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kumaniti on 12/8/2017.
 */
@ContextConfiguration(locations =
        {
                "classpath:/dynamodb-mock-applicationContext-test.xml"
        })
public class LinksDaoImplTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private DynamoDBHttpMockService dynamoDBHttpMockService;

    @Autowired
    private LinksDaoImpl linksDaoImpl;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @BeforeClass
    public void setup() {
        dynamoDBHttpMockService.start();
        List<Class> classes = new ArrayList<>();
        classes.add(LinksEntity.class);
        dynamoDBHttpMockService.createTable(classes);
    }

    @AfterClass
    public void teardown() {
        //Stop mock service
        dynamoDBHttpMockService.stop();
    }

    @Test
    public void testGetLinkUrl() {
        LinksEntity entity = new LinksEntity();
        entity.setUrlType("mockurltype");
        entity.setUrlValue("mockvalue");
        linksDaoImpl.createLinkUrl(entity);
        entity = linksDaoImpl.getLinkUrl("mockurltype");
        Assert.assertEquals(entity.getUrlValue(), "mockvalue");
    }

    @Test
    public void testGetLinkUrlReturnsNull() {
        LinksEntity entity = linksDaoImpl.getLinkUrl("mockurltype1");
        Assert.assertNull(entity);
    }
}
