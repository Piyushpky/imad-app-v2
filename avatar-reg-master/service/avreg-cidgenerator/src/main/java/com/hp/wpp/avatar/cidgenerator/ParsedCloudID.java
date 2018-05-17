package com.hp.wpp.avatar.cidgenerator;

/**
 * DataType representing a parsed Cloud Id
 * @author sanket
 * @since 30/10/2015
 */
public class ParsedCloudID {

    private final short podid;
    private final byte entitytype;
    private final long timestamp;
    private final long randomsalt;
    private final byte algorithmVersion;

    /**
     * The default ctor for being able to create an instance of the ParsedCloudID class Instance
     * @param podid the passed podid
     * @param entitytype the passed entitytype
     * @param timestamp the passed timestamp
     * @param randomsalt the passed randomsalt
     * @param algorithmVersion the passed algorithm version
     */
    public ParsedCloudID(short podid, byte entitytype, long timestamp, long randomsalt, byte algorithmVersion) {
        this.podid = podid;
        this.entitytype = entitytype;
        this.timestamp = timestamp;
        this.randomsalt = randomsalt;
        this.algorithmVersion = algorithmVersion;
    }

    /**
     * Gets the pod Id from the Cloud Id
     * @return the Pod Id
     */
    public short getPodid() {
        return podid;
    }

    /**
     * Gets the Entity Type from the Cloud Id
     * @return the Entity Type
     */
    public byte getEntitytype() {
        return entitytype;
    }

    /**
     * The TimeStamp that was used in the Cloud Id
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the RandomSalt used to generate the Cloud Id
     * @return the randomsalt
     */
    public long getRandomsalt() {
        return randomsalt;
    }

    /**
     * Gets the algorithm version used to generate the cloud id
     * @return the algorithm version
     */
    public byte getAlgorithmVersion(){
       return algorithmVersion;
    }
}
