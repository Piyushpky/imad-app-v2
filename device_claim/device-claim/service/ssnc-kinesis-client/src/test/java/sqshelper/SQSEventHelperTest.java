package sqshelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.dataproducer.exception.ConnectionException;
import com.hp.wpp.dataproducer.exception.InvalidParamException;
import com.hp.wpp.dataproducer.exception.QueueException;
import com.hp.wpp.dataproducer.exception.QueueNameResolutionException;
import com.hp.wpp.dataproducer.producer.SQSMessageProducer;
import com.hp.wpp.dataproducer.util.Operation;
import com.hp.wpp.ssnclaim.kinesis.client.model.RegNotificationPayload;
import com.hp.wpp.ssnclaim.sqshelper.SQSEventHelper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Timestamp;

/**
 * Created by karanam on 1/25/2018.
 */
public class SQSEventHelperTest {
    @InjectMocks
    private SQSEventHelper sqsEventHelper;

    @Mock
    private SQSMessageProducer sqsmessageproducer;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqsEventHelper = new SQSEventHelper(sqsmessageproducer);
    }

    @Test
    public void testNotifyDCSValid() throws ConnectionException, InvalidParamException, QueueNameResolutionException, QueueException, JsonProcessingException {
        Mockito.doReturn("messageId").when(sqsmessageproducer).put(Mockito.anyString(),(Operation) Mockito.any(),Mockito.anyString());
        RegNotificationPayload regPayload=getRegPayload();
        sqsEventHelper.notifyDCS(regPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.verify(sqsmessageproducer,Mockito.times(1)).put(objectMapper.writeValueAsString(regPayload),Operation.CREATE,null);
    }

    private RegNotificationPayload getRegPayload(){
        RegNotificationPayload regNotificationPayload = new RegNotificationPayload();
        regNotificationPayload.setCloudId("mockCloudId");
        regNotificationPayload.setSnkey("mockSnKey");
        regNotificationPayload.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return regNotificationPayload;
    }

    @DataProvider(name = "dataset-notifydcs-exception")
    public static Object[][] getDataSet_notifydcs_exception() throws Exception {
        return new Object[][] {
                {new InvalidParamException("InvalidParam") },
                {new ConnectionException("ConnectException") },
                {new QueueException("QueueException") },
                {new QueueNameResolutionException("Queue Name unresolved") }
        };
    }

    @Test(dataProvider = "dataset-notifydcs-exception")
    public void testNotifyDCSError(Exception e) throws ConnectionException, InvalidParamException, QueueNameResolutionException, QueueException, JsonProcessingException {
        Mockito.doThrow(e).when(sqsmessageproducer).put(Mockito.anyString(),(Operation) Mockito.any(),Mockito.anyString());
        RegNotificationPayload regPayload=getRegPayload();
        sqsEventHelper.notifyDCS(regPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.verify(sqsmessageproducer,Mockito.times(1)).put(objectMapper.writeValueAsString(regPayload),Operation.CREATE,null);
    }

}
