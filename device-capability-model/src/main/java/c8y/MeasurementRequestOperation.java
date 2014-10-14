package c8y;

import java.io.Serializable;

public class MeasurementRequestOperation implements Serializable {

	private static final long serialVersionUID = -2731997499381254447L;

	private String requestName;

	public MeasurementRequestOperation() {
	}

	public MeasurementRequestOperation(String requestName) {
		this.requestName = requestName;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	@Override
	public String toString() {
		return String.format("MeasurementRequestOperation [requestName=%s]", requestName);
	}
}
