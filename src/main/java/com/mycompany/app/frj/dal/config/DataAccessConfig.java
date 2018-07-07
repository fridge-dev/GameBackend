package com.mycompany.app.frj.dal.config;

import com.mycompany.app.frj.dal.ddb.accessors.UserSessionDdbAccessor;
import com.mycompany.app.frj.dal.ddb.accessors.UserDdbAccessor;
import com.mycompany.app.frj.dal.impl.UserSessionAccessorImpl;
import com.mycompany.app.frj.dal.impl.UserAccessorImpl;
import com.mycompany.app.frj.dal.interfaces.DataAccessorProvider;
import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class DataAccessConfig implements DataAccessorProvider {

    /**
     * Dependencies
     */
    private final String awsRegion;
    private final String awsKey;

    /**
     * Lazy singletons
     */
    @Setter(AccessLevel.PACKAGE)
    private DynamoDbConfig dynamoDbConfig;

    @Override
    public UserAccessor userAccessor() {
        return new UserAccessorImpl(new UserDdbAccessor(dynamoDbConfig().dynamoDBMapper()));
    }

    @Override
    public UserSessionAccessor userSessionAccessor() {
        return new UserSessionAccessorImpl(new UserSessionDdbAccessor(dynamoDbConfig().dynamoDBMapper()));
    }

    @Synchronized
    private DynamoDbConfig dynamoDbConfig() {
        if (this.dynamoDbConfig == null) {
            this.dynamoDbConfig = new DynamoDbConfig(awsRegion, awsKey);
        }

        return this.dynamoDbConfig;
    }
}
