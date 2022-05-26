package com.cumulocity.lpwan.exception;

import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LpwanErrorMessageRepresentation extends ErrorMessageRepresentation {
    private String url;
}
