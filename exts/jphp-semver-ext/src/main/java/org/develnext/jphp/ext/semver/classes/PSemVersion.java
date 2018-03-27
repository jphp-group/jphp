package org.develnext.jphp.ext.semver.classes;

import com.github.zafarkhaja.semver.Version;
import org.develnext.jphp.ext.semver.SemverExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("SemVersion")
@Reflection.Namespace(SemverExtension.NS)
public class PSemVersion extends BaseObject implements IComparableObject<PSemVersion> {
    protected Version value;

    public PSemVersion(Environment env, Version value) {
        super(env);
        this.value = value;
    }

    public PSemVersion(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Version getValue() {
        return value;
    }

    @Signature
    public void __construct(String version) {
        Version.valueOf(version);
    }

    @Signature
    public int getMajorNum() {
        return getValue().getMajorVersion();
    }

    @Signature
    public int getMinorNum() {
        return getValue().getMinorVersion();
    }

    @Signature
    public int getPatchNum() {
        return getValue().getPatchVersion();
    }

    @Signature
    public String getBuildString() {
        return getValue().getBuildMetadata();
    }

    @Signature
    public String toNormal() {
        return getValue().getNormalVersion();
    }

    @Signature
    public String __toString() {
        return getValue().toString();
    }

    @Signature
    public PSemVersion incMajorNum(Environment env) {
        return new PSemVersion(env, getValue().incrementMajorVersion());
    }

    @Signature
    public PSemVersion incMajorNum(Environment env, String preRelease) {
        return new PSemVersion(env, getValue().incrementMajorVersion(preRelease));
    }

    @Signature
    public PSemVersion incMinorNum(Environment env) {
        return new PSemVersion(env, getValue().incrementMinorVersion());
    }

    @Signature
    public PSemVersion incMinorNum(Environment env, String preRelease) {
        return new PSemVersion(env, getValue().incrementMinorVersion(preRelease));
    }

    @Signature
    public PSemVersion incPatchNum(Environment env) {
        return new PSemVersion(env, getValue().incrementPatchVersion());
    }

    @Signature
    public PSemVersion incPatchNum(Environment env, String preRelease) {
        return new PSemVersion(env, getValue().incrementPatchVersion(preRelease));
    }

    @Signature
    public PSemVersion incPreRelease(Environment env) {
        return new PSemVersion(env, getValue().incrementPreReleaseVersion());
    }

    @Signature
    public PSemVersion incBuildString(Environment env) {
        return new PSemVersion(env, getValue().incrementBuildMetadata());
    }

    @Signature
    public boolean satisfies(String expr) {
        return getValue().satisfies(expr);
    }

    @Override
    public boolean __equal(PSemVersion iObject) {
        return value.equals(iObject.value);
    }

    @Override
    public boolean __identical(PSemVersion iObject) {
        return value == iObject.value;
    }

    @Override
    public boolean __greater(PSemVersion iObject) {
        return value.greaterThan(iObject.value);
    }

    @Override
    public boolean __greaterEq(PSemVersion iObject) {
        return value.greaterThanOrEqualTo(iObject.value);
    }

    @Override
    public boolean __smaller(PSemVersion iObject) {
        return value.lessThan(iObject.value);
    }

    @Override
    public boolean __smallerEq(PSemVersion iObject) {
        return value.lessThanOrEqualTo(iObject.value);
    }
}
