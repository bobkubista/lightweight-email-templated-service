package com.bobkubista.services.email.strategy;

/**
 * Interface for different email strategies
 *
 * @author bkubista
 *
 */
@FunctionalInterface
public interface EmailStrategy {

    /**
     * Send an email
     *
     * @return true if send
     */
    boolean send();
}
