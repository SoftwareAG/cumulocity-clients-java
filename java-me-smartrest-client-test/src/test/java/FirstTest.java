
import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.sdk.SmartPlatform;
import com.cumulocity.me.sdk.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class FirstTest {
    
    private static String TENANT = "management";
    private static String USERNAME = "admin";
    private static String PASSWORD = "";
    
    private static String XID = "j2me_test_template8";
    
    private static String templateString =  "10,100,GET,/inventory/managedObjects/&&,,,&&,,\n" +
                                              "10,101,GET,/inventory/managedObjects/,,,,,\n" +
                                              "11,200,,,\"$.name\"\n" +
                                              "11,201,managedObjects,,\"$.name\",\"$.id\"";
    
    private SmartPlatform platform;
    
    
    
    @Before
    public void createPlatform() {
        platform = new SmartPlatform("http://mtmtest:8181", XID, TENANT, USERNAME, PASSWORD);
        platform.registerTemplates(templateString);
    }
    
    @Test
    public void test() {
        String body = "101";
        SmartRequest request = new SmartRequestImpl(body.getBytes());
        SmartResponse resp = platform.request(request);
        SmartRow[] rows = resp.getDataRows();
        for (SmartRow row : rows) {
            smartRowLogger(row);
        }
    }
    
    @Test
    public void testAsync() throws InterruptedException {
        SmartResponseEvaluator eval = new SmartResponseEvaluator() {
            public void evaluate(SmartResponse response) {
                SmartRow[] rows = response.getDataRows();
                for (SmartRow row : rows) {
                    smartRowLogger(row);
                }
            }
        };
        String body = "101";
        SmartRequest request = new SmartRequestImpl(body.getBytes());
        platform.requestAsync(request, eval);
        Thread.sleep(2000);
    }
    
    private void smartRowLogger(SmartRow row) {
        System.out.println("message id: " + row.getMessageId() + "\n" +
                "row number: " + row.getRowNumber() + "\n" +
                "data: " + joinArray(row.getData()));
    }
    
    private String joinArray(String[] array) {
        String result = "";
        for(String part : array) {
            result = result.concat(part);
            result = result.concat(",");
        }
        return result.substring(0, result.length()-1);
    }

}
