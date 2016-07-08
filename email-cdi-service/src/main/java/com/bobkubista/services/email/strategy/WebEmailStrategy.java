package com.bobkubista.services.email.strategy;

@FunctionalInterface
public interface WebEmailStrategy {

    /**
     * Build a webversion of an email
     *
     * @return the web version
     */
    String getWebVersion();
}
