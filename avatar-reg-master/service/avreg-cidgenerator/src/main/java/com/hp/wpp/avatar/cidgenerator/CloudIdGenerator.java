package com.hp.wpp.avatar.cidgenerator;

/**
 * Interface for generating a Cloud Id based on provided pod id and provided entity type
 * @author sanket
 * @since 30/10/2015
 */
public interface CloudIdGenerator {

    /**
     * Generates a newCloudId for a entity within the given pod and of a particular type
     * @param podId the provided pod
     * @param entityType the provided type
     * @return the newly generated cloud id
     */
    String newCloudID(short podId, byte entityType);

    /**
     * Parses a provided Cloud Id and gets the details from the same
     * @param cloudId the provided cloud id
     * @return the parsed cloud id
     * @throws InvalidCloudIdException This exception is thrown while parsing the String based CPID
     */
    ParsedCloudID parse(String cloudId) throws InvalidCloudIdException;
}
