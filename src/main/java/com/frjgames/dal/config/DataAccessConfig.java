package com.frjgames.dal.config;

import com.frjgames.dal.ddb.accessors.EverlastHighScoreDdbAccessor;
import com.frjgames.dal.ddb.accessors.UserDdbAccessor;
import com.frjgames.dal.ddb.accessors.UserSessionDdbAccessor;
import com.frjgames.dal.impl.EverlastHighScoreAccessorImpl;
import com.frjgames.dal.impl.UserAccessorImpl;
import com.frjgames.dal.impl.UserSessionAccessorImpl;
import com.frjgames.dal.interfaces.DataAccessorProvider;
import com.frjgames.dal.interfaces.EverlastHighScoreAccessor;
import com.frjgames.dal.interfaces.UserAccessor;
import com.frjgames.dal.interfaces.UserSessionAccessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;

/**
 * Configuration for the Data Access Layer.
 *
 * @author fridge
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

    @Override
    public EverlastHighScoreAccessor everlastHighScoreAccessor() {
        return new EverlastHighScoreAccessorImpl(new EverlastHighScoreDdbAccessor(dynamoDbConfig().dynamoDBMapper()));
    }

    @Synchronized
    private DynamoDbConfig dynamoDbConfig() {
        if (this.dynamoDbConfig == null) {
            this.dynamoDbConfig = new DynamoDbConfig(awsRegion, awsKey);
        }

        return this.dynamoDbConfig;
    }
}
