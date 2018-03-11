package org.develnext.jphp.ext.javafx.support.effect;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.Effect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ChainableEffect {
        private final Effect effect;
        private Method inputMethod;
        private final BooleanProperty disabled = new SimpleBooleanProperty(
                false
        );

        public ChainableEffect(Effect effect) {
            if (effect == null) {
                throw new IllegalArgumentException("Effect for chaining must not be null");
            }

            this.effect = effect;

            try {
                inputMethod = effect.getClass().getMethod(
                        "setInput",
                        Effect.class
                );
            } catch (NoSuchMethodException e) {
                try {
                    inputMethod = effect.getClass().getMethod("setContentInput", Effect.class);
                } catch (NoSuchMethodException e1) {
                    throw new IllegalArgumentException("Effect for chaining must implement the setInput method", e);
                }
            } catch (SecurityException e) {
                throw new IllegalStateException("Creating chainable effects requires a reflection capable security environment", e);
            }
        }

        public ChainableEffect setInput(ChainableEffect chainableEffect) {
            try {
                inputMethod.invoke(
                        this.getEffect(),
                        chainableEffect != null
                                ? chainableEffect.getEffect()
                                : null
                );

                return this;
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Chainable effect does not support access rights for setInput", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Unable to set the input for a chainable effect", e);
            }
        }

        public Effect getEffect() {
            return effect;
        }

        public boolean isDisabled() {
            return disabled.get();
        }

        public BooleanProperty disabledProperty() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled.set(disabled);
        }
    }