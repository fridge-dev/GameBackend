package com.mycompany.app.frj.dal.interfaces;

/**
 * Config interface for Data Access Layer (DAL).
 *
 * @author alecva
 */
public interface DataAccessorProvider {

    UserAccessor userAccessor();

    UserSessionAccessor userSessionAccessor();

}
