package com.mycompany.app.frj.dal.config;

import com.mycompany.app.frj.dal.ddb.testutils.TestUtilDynamoDbTableCreator;

/**
 * TODO
 *
 * @author alecva
 */
public class TestDataAccessConfig extends DataAccessConfig {

    private final LocalDynamoDbConfig localDdbConfig;

    public TestDataAccessConfig() {
        super(null, null);

        this.localDdbConfig = new LocalDynamoDbConfig();
        super.setDynamoDbConfig(localDdbConfig);
    }

    public void createTable(final Class<?> clazz) {
        TestUtilDynamoDbTableCreator.createTable(localDdbConfig.getAmazonDbLocal(), localDdbConfig.dynamoDBMapper(), clazz);
    }

}
