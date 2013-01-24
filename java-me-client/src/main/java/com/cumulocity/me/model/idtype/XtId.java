package com.cumulocity.me.model.idtype;

import com.cumulocity.me.model.ID;

public class XtId extends ID {

	public XtId(String id) {
		super(id);
	}
	
	// Default constructor is needed for parsing from Json
	public XtId()
	{
		super();
	}
}
