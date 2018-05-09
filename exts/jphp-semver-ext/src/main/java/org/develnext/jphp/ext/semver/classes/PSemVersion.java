package org.develnext.jphp.ext.semver.classes;

import com.vdurmont.semver4j.Semver;
import org.develnext.jphp.ext.semver.SemverExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("SemVersion")
@Reflection.Namespace(SemverExtension.NS)
public class PSemVersion extends BaseObject implements IComparableObject<PSemVersion> {
    protected Semver value;

    public PSemVersion(Environment env, Semver value) {
        super(env);
        this.value = value;
    }

    public PSemVersion(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Semver getValue() {
        return value;
    }

    @Signature
    public void __construct(String version) {
        try {
            value = new Semver(version, Semver.SemverType.NPM);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Signature
    public Integer getMajorNum() {
        return getValue().getMajor();
    }

    @Signature
    public Integer getMinorNum() {
        return getValue().getMinor();
    }

    @Signature
    public Integer getPatchNum() {
        return getValue().getPatch();
    }

    @Signature
    public String getBuildString() {
        return getValue().getBuild();
    }

    @Signature
    public String getPreReleaseString() {
        return StringUtils.join(getValue().getSuffixTokens(), " ");
    }

    @Signature
    public String toNormal() {
        return getValue().withClearedSuffixAndBuild().getValue();
    }

    @Signature
    public boolean isStable() {
        return getValue().isStable();
    }

    @Signature
    public String __toString() {
        return getValue().toString();
    }

    @Signature
    public PSemVersion incMajorNum(Environment env) {
        return new PSemVersion(env, getValue().nextMajor());
    }

    @Signature
    public PSemVersion incMinorNum(Environment env) {
        return new PSemVersion(env, getValue().nextMinor());
    }

    @Signature
    public PSemVersion incPatchNum(Environment env) {
        return new PSemVersion(env, getValue().nextPatch());
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
        return value.isGreaterThan(iObject.value);
    }

    @Override
    public boolean __greaterEq(PSemVersion iObject) {
        return value.isGreaterThanOrEqualTo(iObject.value);
    }

    @Override
    public boolean __smaller(PSemVersion iObject) {
        return value.isLowerThanOrEqualTo(iObject.value);
    }

    @Override
    public boolean __smallerEq(PSemVersion iObject) {
        return value.isLowerThanOrEqualTo(iObject.value);
    }
}
