package com.frjgames.rest;

import com.frjgames.app.api.config.ApiHandlerModule;
import com.frjgames.dal.config.DataAccessLayerModule;
import com.frjgames.dal.config.DataAccessLayerModuleFactory;
import com.frjgames.dal.ddb.items.DdbItem;
import com.frjgames.dal.ddb.items.GameBoardDdbItem;
import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The top level bean-wiring module grandfather.
 *
 * TODO migrate this to Guice/Spring.
 *
 * @author fridge
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiHandlerModuleSingleton {

    private static final Set<Class<? extends DdbItem>> DDB_TABLES = ImmutableSet.of(
            UserDdbItem.class,
            UserSessionDdbItem.class,
            GameDdbItem.class,
            GameBoardDdbItem.class
    );

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final DataAccessLayerModule dal = DataAccessLayerModuleFactory.getModuleLocal(DDB_TABLES);

    @Getter(lazy = true)
    private static final ApiHandlerModule instance = ApiHandlerModule.instantiateModule(getDal());
}
