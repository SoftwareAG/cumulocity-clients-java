package com.cumulocity.model.idtype;

import com.cumulocity.model.ID;

public class XtId extends ID {

	public XtId(String value) {
		super(value);
	}

	public XtId(String type, String value) {
		super(type, value);
	}
	
	// Default constructor is needed for parsing from Json
	public XtId()
	{
		super();
	}
}
