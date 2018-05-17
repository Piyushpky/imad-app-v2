package com.hp.wpp.avatar.cidgenerator.impl;

import com.hp.wpp.avatar.cidgenerator.CloudIdConstants;
import com.hp.wpp.avatar.cidgenerator.CloudIdGenerator;
import com.hp.wpp.avatar.cidgenerator.InvalidCloudIdException;
import com.hp.wpp.avatar.cidgenerator.ParsedCloudID;
import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Initial Version of the CloudIDGenerator Algorithm
 * @author sanket naik
 * @since 22/11/2015
 */
public class CloudIdGeneratorImpl implements CloudIdGenerator {

    private final byte cloudalgorithmversion;
    private final Random randomGenerator;
    private final Base64 encoder;
    private final int BYTE_LENGTH_32 = 32;
    private final int BYTE_LENGTH_24 = 24;
    private final int BYTE_LENGTH_8 = 8;
    private final int BYTE_LENGTH_4 = 4;
    private final int BYTE_LENGTH_2 = 2;
    private final int BYTE_LENGTH_1 = 1;


    /**
     * The Default Ctor for the Algorithm
     */
    public CloudIdGeneratorImpl(){
        this((byte)1);
    }

    /**
     * The  Ctor taking a version number for the Algorithm
     * @param version the version of the algorithm
     */
    public CloudIdGeneratorImpl(byte version){

        this.cloudalgorithmversion = version;
        this.randomGenerator = new Random();
        this.encoder = new Base64(true);
    }

    @Override
    public String newCloudID(short podId, byte entityType) {
        byte[] cloudId = new byte[BYTE_LENGTH_24];
        ByteBuffer podBuffer = ByteBuffer.allocate(BYTE_LENGTH_2);
        ByteBuffer randomBuffer = ByteBuffer.allocate(BYTE_LENGTH_8);
        ByteBuffer timeStampBuffer = ByteBuffer.allocate(BYTE_LENGTH_8);
        ByteBuffer padByteBuffer = ByteBuffer.allocate(BYTE_LENGTH_4);

        // Initializing the parts
        byte[] pad = padByteBuffer.putInt(0).array();
        byte[] podid = podBuffer.putShort(podId).array();
        byte[] random = randomBuffer.putLong(randomGenerator.nextLong()).array();
        byte[] timestamp = timeStampBuffer.putLong(System.currentTimeMillis()).array();

        //Stitching the parts together to generate the id
        System.arraycopy(new byte[]{ cloudalgorithmversion}, 0, cloudId, 0, BYTE_LENGTH_1);
        System.arraycopy(new byte[]{ entityType}, 0, cloudId, 1, BYTE_LENGTH_1);
        System.arraycopy(timestamp, 0, cloudId, 2, BYTE_LENGTH_8);
        System.arraycopy(pad, 0, cloudId, 10, BYTE_LENGTH_4);
        System.arraycopy(podid, 0, cloudId, 14, BYTE_LENGTH_2);
        System.arraycopy(random, 0, cloudId, 16, BYTE_LENGTH_8);

        return encoder.encodeAsString(cloudId).trim();
    }

    @Override
    public ParsedCloudID parse(String cloudId) throws InvalidCloudIdException {
        if(cloudId.length() != BYTE_LENGTH_32)
            throw new InvalidCloudIdException(CloudIdConstants.CPID_LENGTH_MISMATCH);

        byte[] parsedRawByte;
        try {
            parsedRawByte =encoder.decode(cloudId);
        }
        catch (Exception e){
            throw new InvalidCloudIdException(CloudIdConstants.CPID_PARSING_FAILURE, e);
        }

        if(parsedRawByte.length != BYTE_LENGTH_24){
            throw new InvalidCloudIdException(CloudIdConstants.CPID_PARSING_FAILURE);
        }
        //Version Mismatch
        if(cloudalgorithmversion != parsedRawByte[0]){
            throw new InvalidCloudIdException(CloudIdConstants.CPID_VERSION_MISMATCH);
        }

        try {
            byte[] timestamp = new byte[BYTE_LENGTH_8];
            byte[] pod = new byte[BYTE_LENGTH_2];
            byte[] entityType = new byte[BYTE_LENGTH_1];
            byte[] version = new byte[BYTE_LENGTH_1];
            byte[] random = new byte[BYTE_LENGTH_8];
            System.arraycopy(parsedRawByte, 0, version, 0, BYTE_LENGTH_1);
            System.arraycopy(parsedRawByte, 1, entityType, 0, BYTE_LENGTH_1);
            System.arraycopy(parsedRawByte, 2, timestamp, 0, BYTE_LENGTH_8);
            System.arraycopy(parsedRawByte, 14, pod, 0, BYTE_LENGTH_2);
            System.arraycopy(parsedRawByte, 16, random, 0, BYTE_LENGTH_8);

            return new ParsedCloudID(ByteBuffer.wrap(pod).getShort(),
                    entityType[0], ByteBuffer.wrap(timestamp).getLong(), ByteBuffer.wrap(random).getLong(), version[0]);
        }
        catch (Exception e){
            throw new InvalidCloudIdException(CloudIdConstants.CPID_DATA_TYPES_PARSING_FAILED, e);
        }
    }
}
