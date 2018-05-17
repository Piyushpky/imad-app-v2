package com.hp.wpp.avatar.restapp.mock;

import com.hp.wpp.about.AboutResponse;
import com.hp.wpp.about.schema.AboutResponseCreation;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by parsh on 3/16/2017.
 */
@Component("generateResponse")
public class AboutMockResponse implements AboutResponse {
    @Override
    public AboutResponseCreation createResponse() throws IOException {
        return null;
    }
}
