/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.util;

import java.util.Comparator;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

/**
 * A comparator for {@link OperationRepresentation} that considers two operations equal if their ID's are equal.
 */
public class OperationRepresentationByIdComparator implements Comparator<OperationRepresentation> {

	private static final Comparator<OperationRepresentation> INSTANCE =
			new OperationRepresentationByIdComparator();
	
	public static Comparator<OperationRepresentation> getInstance() {
		return INSTANCE;
	}
	
	protected OperationRepresentationByIdComparator() {
		// hidden constructor of this state-less object enforces using above singleton instance
	}
	
	@Override
	public int compare(OperationRepresentation o1, OperationRepresentation o2) {
		if (o1 == null) {
			return o2 == null ? 0 : -1;
		}
		if (o2 == null) {
			return o1 == null ? 0 : 1;
		}
		GId id1 = o1.getId();
		GId id2 = o2.getId();
		if (id1 == null) {
			return id2 == null ? 0 : -1;
		}
		if (id2 == null) {
			return id1 == null ? 0 : 1;
		}
		return id1.equals(id2) ? 0 : -1;
	}
}
