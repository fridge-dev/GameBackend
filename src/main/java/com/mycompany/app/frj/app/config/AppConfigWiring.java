package com.mycompany.app.frj.app.config;

import com.mycompany.app.frj.app.password.PasswordModule;
import com.mycompany.app.frj.dal.interfaces.DataAccessorProvider;

/**
 * TODO
 *
 * @author alecva
 */
public class AppConfigWiring extends AppConfig {

    public AppConfigWiring(final DataAccessorProvider dataAccessorProvider) {
        super(
                dataAccessorProvider.userAccessor(),
                new PasswordModule().passwordHasher()
        );
    }
}
