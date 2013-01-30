/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.rest;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Collection;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.convert.JsonConversionServiceImpl;
import com.cumulocity.me.rest.convert.alarm.AlarmCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.alarm.AlarmRepresentationConverter;
import com.cumulocity.me.rest.convert.alarm.AlarmsApiRepresentationConverter;
import com.cumulocity.me.rest.convert.audit.AuditRecordCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.audit.AuditRecordRepresentationConverter;
import com.cumulocity.me.rest.convert.audit.AuditRecordsRepresentationConverter;
import com.cumulocity.me.rest.convert.audit.ChangeConverter;
import com.cumulocity.me.rest.convert.base.PageStatisticsRepresentationConverter;
import com.cumulocity.me.rest.convert.event.EventCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.event.EventRepresentationConverter;
import com.cumulocity.me.rest.convert.event.EventsApiRepresentationConverter;
import com.cumulocity.me.rest.convert.identity.ExternalIDCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.identity.ExternalIDRepresentationConverter;
import com.cumulocity.me.rest.convert.identity.IdentityRepresentationConverter;
import com.cumulocity.me.rest.convert.inventory.InventoryRepresentationConverter;
import com.cumulocity.me.rest.convert.inventory.ManagedObjectCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceRepresentationConverter;
import com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter;
import com.cumulocity.me.rest.convert.measurement.MeasurementCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.measurement.MeasurementRepresentationConverter;
import com.cumulocity.me.rest.convert.measurement.MeasurementsApiRepresentationConverter;
import com.cumulocity.me.rest.convert.operation.DeviceControlRepresentationConverter;
import com.cumulocity.me.rest.convert.operation.OperationCollectionRepresentationConverter;
import com.cumulocity.me.rest.convert.operation.OperationRepresentationConverter;
import com.cumulocity.me.rest.convert.platform.PlatformApiRepresentationConverter;
import com.cumulocity.me.rest.validate.RepresentationValidationService;
import com.cumulocity.me.rest.validate.RepresentationValidationServiceImpl;

public class RepresentationServicesFactory {

    private static RepresentationServicesFactory INSTANCE = new RepresentationServicesFactory();
    
    private JsonConversionService conversionService;
    private RepresentationValidationService validationService;
    private Collection converters;
    
    public static RepresentationServicesFactory getInstance() {
        return INSTANCE;
    }
    
    public JsonConversionService getConversionService() {
        if (conversionService == null) {
            conversionService = new JsonConversionServiceImpl(getConverters());
        }
        return conversionService;
    }
    
    public RepresentationValidationService getValidationService() {
        if (validationService == null) {
            validationService = new RepresentationValidationServiceImpl(getConverters());
        }
        return validationService;
    }

    private Collection getConverters() {
        if (converters == null) {
            converters = new ArrayList();
            converters.add(new PageStatisticsRepresentationConverter());
            converters.add(new PlatformApiRepresentationConverter());
            
            converters.add(new InventoryRepresentationConverter());
            converters.add(new ManagedObjectRepresentationConverter());
            converters.add(new ManagedObjectReferenceRepresentationConverter());
            converters.add(new ManagedObjectCollectionRepresentationConverter());
            converters.add(new ManagedObjectReferenceCollectionRepresentationConverter());
            
            converters.add(new AlarmsApiRepresentationConverter());
            converters.add(new AlarmRepresentationConverter());
            converters.add(new AlarmCollectionRepresentationConverter());
            converters.add(new ChangeConverter());
            
            converters.add(new AuditRecordCollectionRepresentationConverter());
            converters.add(new AuditRecordRepresentationConverter());
            converters.add(new AuditRecordsRepresentationConverter());
            
            converters.add(new EventsApiRepresentationConverter());
            converters.add(new EventCollectionRepresentationConverter());
            converters.add(new EventRepresentationConverter());

            converters.add(new IdentityRepresentationConverter());
            converters.add(new ExternalIDRepresentationConverter());
            converters.add(new ExternalIDCollectionRepresentationConverter());
            
            converters.add(new DeviceControlRepresentationConverter());
            converters.add(new OperationCollectionRepresentationConverter());
            converters.add(new OperationRepresentationConverter());
            
            converters.add(new MeasurementsApiRepresentationConverter());
            converters.add(new MeasurementRepresentationConverter());
            converters.add(new MeasurementCollectionRepresentationConverter());
//            converters.add(new FragmentOneConverter());
            
            // FIXME add all converters !!!
        }
        return converters;
    }
}
