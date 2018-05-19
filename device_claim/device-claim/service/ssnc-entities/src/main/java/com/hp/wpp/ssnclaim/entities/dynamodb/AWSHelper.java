package com.hp.wpp.ssnclaim.entities.dynamodb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nallussi on 1/11/2018.
 */
public class AWSHelper {
    @Autowired
    private DynamicPropertyFactory dynamicPropertyFactory;

    private Region region;

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(AWSHelper.class);

    public AWSHelper() {
        setRegion();
    }

    private void setRegion(){
        region = Regions.getCurrentRegion();
    }

    public String getEndpoint() {

        String endpoint=DynamicPropertyFactory.getInstance()
                .getStringProperty("dynamodb.endpoint", "dynamodb.%s.amazonaws.com").getValue();

        if(region != null && !StringUtils.isBlank(region.getName())) {
            endpoint = endpoint.replace("%s", region.getName());
        }else {
            String regionName=DynamicPropertyFactory.getInstance()
                    .getStringProperty("dynamodb.default.region", "us-west-2").getValue();
            endpoint =endpoint.replace("%s", regionName);
            LOG.debug("Failed to fetch region. Setting dynamodb endpoint to default:",endpoint);
        }
        return endpoint;
    }
}
