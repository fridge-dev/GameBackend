package com.mycompany.app.frj.dal.config;

import com.mycompany.app.frj.dal.config.ddb.DynamoDbConfig;
import com.mycompany.app.frj.dal.ddb.accessors.UserDdbAccessor;
import com.mycompany.app.frj.dal.impl.UserAccessorImpl;
import com.mycompany.app.frj.dal.interfaces.DataAccessorProvider;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.utils.SecureHashUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
/* pkg */ class DataAccessInternalConfig implements DataAccessorProvider {

    /**
     * Config params
     */
    private final DynamoDbConfig dynamoDbConfig;
    private final String passwordSalt;

    @Override
    public UserAccessor userAccessor() {
        return new UserAccessorImpl(
                new UserDdbAccessor(this.dynamoDbConfig.dynamoDBMapper()),
                new SecureHashUtil(passwordSalt)
        );
    }
}
