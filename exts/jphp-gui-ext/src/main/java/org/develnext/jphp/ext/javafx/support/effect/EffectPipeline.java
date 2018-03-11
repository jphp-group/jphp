package org.develnext.jphp.ext.javafx.support.effect;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.effect.Effect;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;

import java.util.ArrayList;
import java.util.List;

public class EffectPipeline {
    private List<ChainableEffect> effects;

    private ReadOnlyObjectWrapper<Effect> chainedEffect = new ReadOnlyObjectWrapper<>();

    public EffectPipeline() {
        this.effects = new ArrayList<>();
    }

    public void clear() {
        effects.clear();
        refreshChainedEffect();
    }

    public void add(int i, Effect effect) {
        ChainableEffect chainableEffect = new ChainableEffect(effect);

        effects.add(i, chainableEffect);
        refreshChainedEffect();
    }

    public void add(Effect effect) {
        add(effects.size(), effect);
    }

    public void addAll(List<Effect> effects) {
        for (Effect effect : effects) {
            this.effects.add(new ChainableEffect(effect));
        }
        refreshChainedEffect();
    }

    public void remove(Effect effect) {
        ChainableEffect deleted = null;

        for (ChainableEffect chainableEffect : effects) {
            if (chainableEffect.getEffect().equals(effect)) {
                deleted = chainableEffect;
                break;
            }
        }

        if (deleted != null) {
            effects.remove(deleted);
        }

        refreshChainedEffect();
    }

    public boolean has(Effect effect) {
        for (ChainableEffect chainableEffect : effects) {
            if (chainableEffect.getEffect().equals(effect)) {
                return true;
            }
        }

        return false;
    }

    public void disable(Effect effect) {
        for (ChainableEffect chainableEffect : effects) {
            if (chainableEffect.getEffect().equals(effect)) {
                chainableEffect.setDisabled(true);
            }
        }

        refreshChainedEffect();
    }

    public void enable(Effect effect) {
        for (ChainableEffect chainableEffect : effects) {
            if (chainableEffect.getEffect().equals(effect)) {
                chainableEffect.setDisabled(false);
            }
        }

        refreshChainedEffect();
    }

    public boolean isEnabled(Effect effect) {
        for (ChainableEffect chainableEffect : effects) {
            if (chainableEffect.getEffect().equals(effect)) {
                return !chainableEffect.isDisabled();
            }
        }

        return false;
    }

    public void refreshChainedEffect() {
        ChainableEffect firstEffect = null, lastEffect = null;

        for (ChainableEffect nextEffect : effects) {
            nextEffect.setInput(null);
            if (nextEffect.isDisabled()) {
                continue;
            }

            if (firstEffect == null) {
                firstEffect = nextEffect;
                lastEffect = firstEffect;
                continue;
            }

            lastEffect.setInput(nextEffect);
            lastEffect = nextEffect;
        }

        chainedEffect.setValue(firstEffect == null ? null : firstEffect.getEffect());
    }

    public List<Effect> all() {
        List<Effect> result = new ArrayList<>();

        for (ChainableEffect effect : effects) {
            result.add(effect.getEffect());
        }

        return result;
    }

    public int count() {
        return effects.size();
    }

    public Effect getChainedEffect() {
        return chainedEffect.get();
    }

    public ReadOnlyObjectProperty<Effect> chainedEffectProperty() {
        return chainedEffect.getReadOnlyProperty();
    }
}