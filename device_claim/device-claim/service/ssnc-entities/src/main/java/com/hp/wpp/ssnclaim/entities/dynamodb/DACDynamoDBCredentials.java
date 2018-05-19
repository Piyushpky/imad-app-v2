package com.hp.wpp.ssnclaim.entities.dynamodb;

import com.amazonaws.auth.AWSCredentials;


/**
 * DynamoDB Credentials provider.
 *
 * DynamoDB expects the client credentials to be provided for all the API. This includes the accessKeyId and the
 * secretKey.
 *
 * The current approach is to read the credentials from the properties file using spring configuration way.
 *
 * Created by Srinivas on 7/7/2015.
 */
public class DACDynamoDBCredentials implements AWSCredentials {

    private final String accessKeyId;
    private final String secretKey;

    public DACDynamoDBCredentials(String accessKeyId, String secretKey) {
        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
    }

    public String getAWSAccessKeyId() {
        return accessKeyId;
    }

    public String getAWSSecretKey() {
        return secretKey;
    }
}
