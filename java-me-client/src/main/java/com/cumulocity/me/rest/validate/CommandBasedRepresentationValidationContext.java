package com.cumulocity.me.rest.validate;

public class CommandBasedRepresentationValidationContext implements RepresentationValidationContext {

    public static final CommandBasedRepresentationValidationContext GET = new CommandBasedRepresentationValidationContext();
    public static final CommandBasedRepresentationValidationContext CREATE = new CommandBasedRepresentationValidationContext();
    public static final CommandBasedRepresentationValidationContext UPDATE = new CommandBasedRepresentationValidationContext();
    public static final CommandBasedRepresentationValidationContext DELETE = new CommandBasedRepresentationValidationContext();
    
    private CommandBasedRepresentationValidationContext() {
    }
}
