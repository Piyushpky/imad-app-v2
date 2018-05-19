package com.hp.wpp.ssnclaim.entities.dynamodb;

/**
 * Helper class to provide the right Proxy settings depending on the proxy enabled.
 * If proxyEnabled is TRUE, then returns the values configured for proxyHost and proxyPort,
 *
 * Otherwise will return null for proxyHost and -1 for proxyPort.
 *
 * Created by Srinivas on 7/10/2015.
 */
public class DACProxyHelper {

    private final boolean proxyEnabled;
    private final String proxyHost;
    private final String proxyPort;

    public DACProxyHelper(boolean proxyEnabled, String proxyHost, String proxyPort) {
        this.proxyEnabled = proxyEnabled;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public String getProxyHost() {
        return proxyEnabled ? proxyHost : null;
    }

    public String getProxyPort() {
        return proxyEnabled ? proxyPort : "-1";
    }
}
