package com.cumulocity.microservice.customencoders.api.model;

import com.cumulocity.rest.representation.BaseResourceRepresentation;
import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EncoderInputData extends BaseResourceRepresentation implements Serializable {

    private String commandName;

    private String commandData;

    private String sourceDeviceId;

    private Map<String, String> args;

    private ExecutionEventStatus status = ExecutionEventStatus.PENDING;

    public static EncoderInputData fromMap(Map<String, Object> map) {
        EncoderInputData data = new EncoderInputData();
        data.setArgs((Map<String, String>) map.get("args"));
        String status = (String) map.get("status");
        if (status != null) {
            data.setStatus(ExecutionEventStatus.valueOf(status));
        }
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncoderInputData that = (EncoderInputData) o;
        return Objects.equals(commandName, that.commandName) &&
                Objects.equals(commandData, that.commandData) &&
                Objects.equals(args, that.args) &&
                Objects.equals(sourceDeviceId, that.sourceDeviceId) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, commandData, args, sourceDeviceId, status);
    }
}
