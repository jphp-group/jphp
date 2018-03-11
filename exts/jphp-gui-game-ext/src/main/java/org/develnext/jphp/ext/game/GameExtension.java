package org.develnext.jphp.ext.game;

import org.develnext.jphp.ext.game.bind.Vec2dMemoryOperation;
import org.develnext.jphp.ext.game.classes.*;
import org.develnext.jphp.ext.game.classes.event.UXCollisionEvent;
import org.develnext.jphp.ext.game.support.*;
import org.develnext.jphp.ext.game.support.event.GamePaneEventProvider;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.env.CompileScope;

public class GameExtension extends JavaFXExtension {
    public static final String NS = "php\\game";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "game", "javafx" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerMemoryOperation(Vec2dMemoryOperation.class);

        registerWrapperClass(scope, Sprite.class, UXSprite.class);
        registerWrapperClass(scope, SpriteView.class, UXSpriteView.class);
        registerWrapperClass(scope, GameEntity2D.class, UXGameEntity.class);
        registerWrapperClass(scope, GameScene2D.class, UXGameScene.class);
        registerWrapperClass(scope, GamePane.class, UXGamePane.class);
        registerWrapperClass(scope, GameBackground.class, UXGameBackground.class);

        registerWrapperClass(scope, CollisionEvent.class, UXCollisionEvent.class);

        registerEventProvider(new GamePaneEventProvider());
        //registerEventProvider(new GameObjectEventProvider());
    }
}
