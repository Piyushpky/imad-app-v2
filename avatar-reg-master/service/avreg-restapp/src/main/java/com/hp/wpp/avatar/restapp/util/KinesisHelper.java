package com.hp.wpp.avatar.restapp.util;

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
public class KinesisHelper {
    @Autowired
    private DynamicPropertyFactory dynamicPropertyFactory;

    private Region region;

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(KinesisHelper.class);

    public KinesisHelper() {
        setRegion();
    }

    private void setRegion(){
        region = Regions.getCurrentRegion();
    }

    public String getRegion() {
        String regionName;
        if(region!=null && !StringUtils.isBlank(region.getName())){
            regionName= region.getName();
        }else {

            regionName = DynamicPropertyFactory.getInstance().getStringProperty("avatar-reg.event-notifier.region","us-west-2").getValue();
            LOG.debug("Failed to fetch region. Setting kinesis region to default:",regionName);
        }
        return regionName;
    }
}
