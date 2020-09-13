/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.karaf.examples.jsf.primefaces.internal;

import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.karaf.examples.jsf.api.Booking;
import com.example.karaf.examples.jsf.api.BookingService;

@ManagedBean(name = "helloWorld")
@SessionScoped
public class HelloWorldController {

    // properties
    static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);
    private String name;
    private Collection<Booking> bookings;

    /**
     * default empty constructor
     */
    public HelloWorldController() {
        BookingService bookingService = getServiceViaJndi(BookingService.class);
        bookings = bookingService.list();
    }

    // -------------------getter & setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that is backed to a submit button of a form.
     */
    public String send() {
        return "success?faces-redirect=true";
    }

    /**
     * Get a OSGi Service via JNDI. If no service is found a warning is logged
     * and null is returned
     * 
     * @param ctx
     *            JNDI Context
     * @param serviceInterfaceClass
     *            OSGi Service Class
     * @return OSGi Service implementing the serviceInterfaceClass or null
     */
    @SuppressWarnings("unchecked")
    private <T> T getServiceViaJndi(Context ctx, Class<T> serviceInterfaceClass) {
        logger.info("Class Loader = " + Thread.currentThread().getContextClassLoader().toString());

        T service = null;
        String serviceInterface = serviceInterfaceClass.getCanonicalName();
        try {
            service = (T) ctx.lookup("osgi:service/" + serviceInterface);

        } catch (NameNotFoundException e) {
            logger.warn("Not Found Service " + serviceInterface, e);
        } catch (NamingException e) {
            logger.warn("Exception Loading Service " + serviceInterface, e);
        }

        logger.info("Loaded Service {} = {}", serviceInterface, service);
        if (service != null) {
            logger.info("Service Class Loader = {}", service.getClass().getClassLoader());
            for (Class<?> interfaceClass : service.getClass().getInterfaces()) {
                logger.info("Implementing Class = {}", interfaceClass.getCanonicalName());
            }
        }
        return service;
    }

    /**
     * Get a OSGi Service via JNDI. If no service is found a warning is logged
     * and null is returned
     * 
     * @param serviceInterfaceClass
     *            OSGi Service Class
     * @return OSGi Service implementing the serviceInterfaceClass or null
     */
    protected <T> T getServiceViaJndi(Class<T> serviceInterfaceClass) {
        try {
            Context ctx = new InitialContext();
            return getServiceViaJndi(ctx, serviceInterfaceClass);
        } catch (NamingException e) {
            logger.error("Getting " + serviceInterfaceClass.getName(), e);
            return null;
        }
    }

    public Collection<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Collection<Booking> bookings) {
        this.bookings = bookings;
    }
}