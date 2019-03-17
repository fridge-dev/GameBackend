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
 * TODO
 *
 * @author TODO
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModuleContainer {

    private static final Set<Class<? extends DdbItem>> DDB_TABLES = ImmutableSet.of(
            UserDdbItem.class,
            UserSessionDdbItem.class,
            GameDdbItem.class,
            GameBoardDdbItem.class
    );

    @Getter(lazy = true)
    private static final ModuleContainer instance = new ModuleContainer();

    private final DataAccessLayerModule dal = DataAccessLayerModuleFactory.getModuleLocal(DDB_TABLES);

    @Getter
    private final ApiHandlerModule api = ApiHandlerModule.instantiateModule(dal);
}
