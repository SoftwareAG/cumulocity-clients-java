package com.cumulocity.me.agent.push.impl;

import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import java.util.Vector;

public class FixedXidCombinedEvaluator extends CombinedEvaluator{

	private final CombinedEvaluator delegate;
	private final String xId;
	
	public FixedXidCombinedEvaluator(CombinedEvaluator delegate, String xId) {
		this.delegate = delegate;
		this.xId = xId;
	}
	
	public void evaluate(SmartResponse response) {
		SmartRow[] rows = response.getDataRows();
		Vector currentRows = new Vector();
		for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
			delegate.callEvaluator(xId, new SmartRow[]{row});
        }
	}
}
