import org.junit.Test;

import com.cumulocity.me.sdk.SmartPlatform;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;



public class FirstTest {

    private static String XID = "j2me_test_template4";
    
    private static String templateString =  "10,100,GET,/inventory/managedObjects/&&,,,&&,,\n" +
                                              "10,101,GET,/inventory/managedObjects/,,,,,\n" +
                                              "11,200,,,\"$.name\"\n" +
                                              "11,201,managedObjects,,\"$.name\",\"$.id\"";     
    @Test
    public void test() {
        SmartPlatform platform = new SmartPlatform("http://mtmtest:8181", XID, "Basic bWFuYWdlbWVudC9hZG1pbjpQeWkxYm8xcg==");
        System.out.println(platform.registerTemplates(templateString));
        String body = "101";
        SmartRequest request = new SmartRequestImpl(body.getBytes());
        SmartResponse resp = platform.request(request);
        SmartRow[] rows = resp.getDataRows();
        for (SmartRow row : rows) {
            smartRowLogger(row);
        }
    }
    
    private void smartRowLogger(SmartRow row) {
        System.out.println("message id: " + row.getMessageId());
        System.out.println("row number: " + row.getRowNumber());
        System.out.println("data: " + joinArray(row.getData()));
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
