package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.fieldbus.model.*;
import com.cumulocity.me.agent.util.Callback;
import com.cumulocity.me.smartrest.client.impl.SmartResponseImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.Assertions.assertThat;

public class FieldbusTypeEvaluatorTest {

    private FieldbusChild child = new FieldbusChild("MODBUS", "12345", "myName", 1, "/inventory/managedObjects/23456");
    private FieldbusDevice parsedDevice;

    @Before
    public void before(){
        FieldbusDeviceList list = new FieldbusDeviceList();
        Callback mockedCallback = Mockito.mock(Callback.class);
        FieldbusTypeEvaluator evaluator = new FieldbusTypeEvaluator(child, list, mockedCallback);
        evaluator.evaluate(new SmartResponseImpl(200, "OK", new SmartRow[]{
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_BASE_RESPONSE_MESSAGE_ID, 2, new String[]{"typeName", "true"}),

                new SmartRow(FieldbusTemplates.DEVICE_TYPE_COIL_RESPONSE_MESSAGE_ID, 2, new String[]{"ID1", "1", "coilName1", "false"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_COIL_STATUS_RESPONSE_MESSAGE_ID, 2, new String[]{"ID1", "read"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_COIL_ALARM_RESPONSE_MESSAGE_ID, 2, new String[]{"ID1", "300", "alarmTypeCoil1", "alarmTextCoil1", "MAJOR"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_COIL_EVENT_RESPONSE_MESSAGE_ID, 2, new String[]{"ID1", "301", "eventTypeCoil1", "eventTextCoil1"}),

                new SmartRow(FieldbusTemplates.DEVICE_TYPE_REGISTER_RESPONSE_MESSAGE_ID, 2, new String[]{"ID2", "1", "registerName1", "true", "false", "0", "16", "1", "2", "0"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_REGISTER_STATUS_RESPONSE_MESSAGE_ID, 2, new String[]{"ID2", "write"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_REGISTER_ALARM_RESPONSE_MESSAGE_ID, 2, new String[]{"ID2", "302", "alarmTypeRegister1", "alarmTextRegister1", "MINOR"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_REGISTER_EVENT_RESPONSE_MESSAGE_ID, 2, new String[]{"ID2", "303", "eventTypeRegister1", "eventTextRegister1"}),
                new SmartRow(FieldbusTemplates.DEVICE_TYPE_REGISTER_MEASUREMENT_RESPONSE_MESSAGE_ID, 2, new String[]{"ID2", "304", "measurementTypeRegister1", "measurementSeriesRegister1"}),
        }));
        assertThat(list.size()).isEqualTo(1);
        parsedDevice = list.elementAt(0);
    }

    @Test
    public void shouldParseDeviceParametersCorrectly() throws Exception {
        assertThat(parsedDevice.getAddress()).isEqualTo(1);
        assertThat(parsedDevice.getId()).isEqualTo("12345");
        assertThat(parsedDevice.getName()).isEqualTo("myName");






    }

    @Test
    public void shouldParseTypeParametersCorrectly(){
        assertThat(parsedDevice.getType()).isNotNull();
        FieldbusDeviceType parsedType = parsedDevice.getType();

        assertThat(parsedType.getType()).isEqualTo(child.getType());
        assertThat(parsedType.getName()).isEqualTo("typeName");
        assertThat(parsedType.isUseServerTime()).isTrue();
    }

    @Test
    public void shouldParseCoilParametersCorrectly(){
        FieldbusDeviceType parsedType = parsedDevice.getType();
        assertThat(parsedType.getCoils().length).isEqualTo(1);
        CoilDefinition parsedCoil = parsedType.getCoils()[0];
        assertThat(parsedCoil.getName()).isEqualTo("coilName1");
        assertThat(parsedCoil.getNumber()).isEqualTo(1);
        assertThat(parsedCoil.isInput()).isFalse();
    }

    @Test
    public void shouldParseCoilMappingsCorrectly(){
        CoilDefinition parsedCoil = parsedDevice.getType().getCoils()[0];
        assertThat(parsedCoil.getStatusMapping().getType()).isEqualTo(StatusMappingType.READ);

        assertThat(parsedCoil.getAlarmMapping().getType()).isEqualTo("alarmTypeCoil1");
        assertThat(parsedCoil.getAlarmMapping().getTemplate()).isEqualTo(300);
        assertThat(parsedCoil.getAlarmMapping().getText()).isEqualTo("alarmTextCoil1");
        assertThat(parsedCoil.getAlarmMapping().getSeverity()).isEqualTo(AlarmSeverity.MAJOR);

        assertThat(parsedCoil.getEventMapping().getType()).isEqualTo("eventTypeCoil1");
        assertThat(parsedCoil.getEventMapping().getTemplate()).isEqualTo(301);
        assertThat(parsedCoil.getEventMapping().getText()).isEqualTo("eventTextCoil1");
    }

    @Test
    public void shouldParseRegisterParametersCorrectly(){
        FieldbusDeviceType parsedType = parsedDevice.getType();
        assertThat(parsedType.getRegisters().length).isEqualTo(1);
        RegisterDefinition parsedRegister = parsedType.getRegisters()[0];
        assertThat(parsedRegister.getName()).isEqualTo("registerName1");
        assertThat(parsedRegister.getNumber()).isEqualTo(1);
        assertThat(parsedRegister.isInput()).isTrue();
        assertThat(parsedRegister.isSigned()).isFalse();
        assertThat(parsedRegister.getStartBit()).isEqualTo(0);
        assertThat(parsedRegister.getLength()).isEqualTo(16);
    }

    @Test
    public void shouldParseRegisterMappingsCorrectly(){
        RegisterDefinition parsedRegister = parsedDevice.getType().getRegisters()[0];
        assertThat(parsedRegister.getStatusMapping().getType()).isEqualTo(StatusMappingType.WRITE);

        assertThat(parsedRegister.getAlarmMapping().getType()).isEqualTo("alarmTypeRegister1");
        assertThat(parsedRegister.getAlarmMapping().getTemplate()).isEqualTo(302);
        assertThat(parsedRegister.getAlarmMapping().getText()).isEqualTo("alarmTextRegister1");
        assertThat(parsedRegister.getAlarmMapping().getSeverity()).isEqualTo(AlarmSeverity.MINOR);

        assertThat(parsedRegister.getEventMapping().getType()).isEqualTo("eventTypeRegister1");
        assertThat(parsedRegister.getEventMapping().getTemplate()).isEqualTo(303);
        assertThat(parsedRegister.getEventMapping().getText()).isEqualTo("eventTextRegister1");

        assertThat(parsedRegister.getMeasurementMapping().getTemplate()).isEqualTo(304);
        assertThat(parsedRegister.getMeasurementMapping().getType()).isEqualTo("measurementTypeRegister1");
        assertThat(parsedRegister.getMeasurementMapping().getSeries()).isEqualTo("measurementSeriesRegister1");
    }

    @Test
    public void shouldParseRegisterConversionCorrectly(){
        RegisterDefinition parsedRegister = parsedDevice.getType().getRegisters()[0];
        assertThat(parsedRegister.getConversion().getMultiplier()).isEqualTo(1);
        assertThat(parsedRegister.getConversion().getDivisor()).isEqualTo(2);
        assertThat(parsedRegister.getConversion().getDecimalPlaces()).isEqualTo(0);
    }
}