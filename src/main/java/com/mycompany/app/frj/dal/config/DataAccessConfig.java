package com.mycompany.app.frj.dal.config;

import com.mycompany.app.frj.dal.config.ddb.DynamoDbConfig;
import com.mycompany.app.frj.dal.config.ddb.LocalDynamoDbConfig;

/**
 * TODO
 *
 * @author alecva
 */
public class DataAccessConfig extends DataAccessInternalConfig {

    /**
     * Config using real DDB
     */
    public DataAccessConfig(
            final String awsRegion,
            final String awsKey,
            final String passwordSalt
    ) {
        super(
                new DynamoDbConfig(awsRegion, awsKey),
                passwordSalt
        );
    }

    /**
     * Config using local DDB
     */
    public DataAccessConfig(
            final String passwordSalt
    ) {
        super(
                new LocalDynamoDbConfig(),
                passwordSalt
        );
    }
}
