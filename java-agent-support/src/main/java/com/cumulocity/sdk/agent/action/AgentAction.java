/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

/**
 * <p>
 * Defines an action that will be executed by an Agent.
 * </p><p>
 * An action is an operation or command that the platform instructs the agent to perform. These actions can range from
 * setting a set of configuration parameters over manipulating a control (such as a switch) to uploading a new firmware.
 * Changes are idempotent, i.e., they can be repeated multiple times with the same result. This is an important property
 * in error situations: For example, if an agent is not sure if a request for changing a parameter actually reached the
 * device, it can safely retry that request. A command may be, for example, asking a security camera to take a picture.
 * This is not idempotent; the command can be repeated later but the result is likely to be different.
 * </p>
 */
public interface AgentAction extends Runnable {

    /**
     * This method will be called when the action is executed by the agent.
     */
    @Override
    void run();
}
