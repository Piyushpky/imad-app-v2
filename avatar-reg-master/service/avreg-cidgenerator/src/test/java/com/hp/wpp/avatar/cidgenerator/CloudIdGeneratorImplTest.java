package com.hp.wpp.avatar.cidgenerator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.cidgenerator.impl.CloudIdGeneratorImpl;

/**
 * Unit Tests for Cloud Id Generator
 */
public class CloudIdGeneratorImplTest {

    @Test
    public void ctorTest(){
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl((byte) 1);
        Assert.assertNotNull(cloudIdGenerator);
    }

    @Test
    public void newCloudIDTest(){
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl((byte) 1);
        String cloudId = cloudIdGenerator.newCloudID((short) 1, (byte)1);
        Assert.assertNotNull(cloudId);
        Assert.assertTrue(cloudId.length() == 32);
    }
    @Test
    public void parseTest() throws InvalidCloudIdException {
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl((byte) 1);
        String cloudId = cloudIdGenerator.newCloudID((short) 1, (byte)2);
        ParsedCloudID pCloudId = cloudIdGenerator.parse(cloudId);
        Assert.assertEquals(pCloudId.getAlgorithmVersion(), (byte)1);
        Assert.assertEquals(pCloudId.getEntitytype(), (byte)2);
        Assert.assertEquals(pCloudId.getPodid(), (short) 1);
        Assert.assertTrue(System.currentTimeMillis() - pCloudId.getTimestamp() <= 5);
        Assert.assertTrue(pCloudId.getRandomsalt() != 0);
    }

    @Test
    public void defaultAlgorithmVersionTest() throws InvalidCloudIdException {
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl();
        String cloudId = cloudIdGenerator.newCloudID((short) 1, (byte)2);
        ParsedCloudID pCloudId = cloudIdGenerator.parse(cloudId);
        Assert.assertEquals(pCloudId.getAlgorithmVersion(), (byte)1);
    }

    @Test(expectedExceptions = InvalidCloudIdException.class)
    public void caseSensitivityTest() throws InvalidCloudIdException{
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl();
        String cloudId = cloudIdGenerator.newCloudID((short) 1, (byte)2);
        ParsedCloudID pCloudId = cloudIdGenerator.parse(cloudId.toLowerCase());
    }

    @Test(expectedExceptions = InvalidCloudIdException.class)
    public void lengthTest() throws InvalidCloudIdException{
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl();
        String cloudId = cloudIdGenerator.newCloudID((short) 1, (byte)2);
        ParsedCloudID pCloudId = cloudIdGenerator.parse(cloudId + "abc");
    }

    @Test(expectedExceptions = InvalidCloudIdException.class)
    public void InvalidBase64Test_VersionMismatch() throws InvalidCloudIdException{
        CloudIdGenerator cloudIdGenerator = new CloudIdGeneratorImpl();
        ParsedCloudID pCloudId = cloudIdGenerator.parse("AAABABABABAAAABAAAABABABABAAAABA");
    }

    @Test
    public void multiThreadedCPIDGenerationScenario(){
        final ConcurrentHashMap<String, ParsedCloudID> cpidStore = new ConcurrentHashMap<>();
        final AtomicBoolean duplicateExists = new AtomicBoolean(false);
        Runnable cpIdGenerator = new Runnable() {
            @Override
            public void run() {
                int ctr = 1000;
                while(ctr > 0) {
                    CloudIdGenerator generator = new CloudIdGeneratorImpl();
                    String cloudId = generator.newCloudID((short) 1, (byte) 2);
                    try {
                        ParsedCloudID pCloudId = generator.parse(cloudId);
                        if (cpidStore.containsKey(cloudId)) {
                            duplicateExists.set(true);
                        } else {
                            cpidStore.put(cloudId, pCloudId);
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    } catch (InvalidCloudIdException e) {
                    }
                    ctr -= 1;
                }
            }
        };
        Thread t1 = new Thread(cpIdGenerator);
        Thread t2 = new Thread(cpIdGenerator);
        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(duplicateExists.get()){
            Assert.fail();
        }
    }


}
