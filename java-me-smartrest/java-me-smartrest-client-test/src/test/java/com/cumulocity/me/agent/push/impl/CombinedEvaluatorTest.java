package com.cumulocity.me.agent.push.impl;

import com.cumulocity.me.agent.smartrest.model.MessageId;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartResponseImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CombinedEvaluatorTest {

    @Test
    public void shouldEvaluateSingleRowCorrectly() throws Exception {
        CombinedEvaluator testEvaluator = new CombinedEvaluator();
        SmartResponseEvaluator mockedCallback = mock(SmartResponseEvaluator.class);
        testEvaluator.registerOperation("myXId", 100, mockedCallback);

        testEvaluator.evaluate(new SmartResponseImpl(200, "OK", new SmartRow[]{
                new SmartRow(MessageId.SET_XID_RESPONSE.getValue(), 1, new String[]{
                        "myXId"
                }),
                new SmartRow(100, 2, new String[]{
                        "test"
                })
        }));
        sleep(1000);
        verify(mockedCallback).evaluate(any(SmartResponse.class));
    }

    @Test
    public void shouldEvaluateDifferentCallsCorrectly() throws Exception {
        CombinedEvaluator testEvaluator = new CombinedEvaluator();
        SmartResponseEvaluator mockedCallback = mock(SmartResponseEvaluator.class);
        testEvaluator.registerOperation("myXId", 100, mockedCallback);

        testEvaluator.evaluate(new SmartResponseImpl(200, "OK", new SmartRow[]{
                new SmartRow(MessageId.SET_XID_RESPONSE.getValue(), 1, new String[]{
                "myXId"
                }),
                new SmartRow(101, 2, new String[]{
                        "test"
                }),
                new SmartRow(MessageId.SET_XID_RESPONSE.getValue(), 1, new String[]{
                        "myXId1"
                }),
                new SmartRow(100, 2, new String[]{
                        "test"
                })
        }));
        sleep(1000);
        verify(mockedCallback, Mockito.never()).evaluate(any(SmartResponse.class));
    }
    @Test

    public void shouldEvaluateMultipleRowsCorrectly() throws Exception {
        CombinedEvaluator testEvaluator = new CombinedEvaluator();
        SmartResponseEvaluator mockedCallback = mock(SmartResponseEvaluator.class);
        testEvaluator.registerOperation("myXId", 100, mockedCallback);

        testEvaluator.evaluate(new SmartResponseImpl(200, "OK", new SmartRow[]{
                new SmartRow(MessageId.SET_XID_RESPONSE.getValue(), 7, new String[]{
                        "myXId"
                }),
                new SmartRow(100, 2, new String[]{
                        "test"
                }),
                new SmartRow(100, 2, new String[]{
                        "test"
                }),
                new SmartRow(100, 2, new String[]{
                        "test"
                }),
                new SmartRow(100, 5, new String[]{
                        "test"
                }),
                new SmartRow(100, 6, new String[]{
                        "test"
                }),
                new SmartRow(101, 6, new String[]{
                        "test"
                }),
                new SmartRow(101, 7, new String[]{
                        "test"
                })

        }));
        sleep(1000);
        verify(mockedCallback, times(3)).evaluate(any(SmartResponse.class));
    }
}