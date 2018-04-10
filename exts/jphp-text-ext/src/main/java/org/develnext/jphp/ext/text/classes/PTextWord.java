package org.develnext.jphp.ext.text.classes;

import java.util.Locale;
import java.util.Objects;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.HammingDistance;
import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.develnext.jphp.ext.text.TextExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name("TextWord")
@Namespace(TextExtension.NS)
public class PTextWord extends BaseObject implements ICloneableObject<PTextWord>, IComparableObject<PTextWord> {
    private String text;

    public PTextWord(Environment env, String text) {
        super(env);
        this.text = text;
    }

    public PTextWord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String text) {
        this.text = text;
    }

    @Signature
    public Memory __debugInfo() {
        return ArrayMemory.ofPair("*text", text);
    }

    @Signature
    public String __toString() {
        return this.text;
    }

    @Signature
    public int length() {
        return this.text.length();
    }

    @Signature
    public PTextWord capitalize(Environment env, @Optional(type = HintType.STRING) String delimiters) {
        return new PTextWord(env, WordUtils.capitalize(text, delimiters.isEmpty() ? null : delimiters.toCharArray()));
    }

    @Signature
    public PTextWord capitalizeFully(Environment env, @Optional(type = HintType.STRING) String delimiters) {
        return new PTextWord(env, WordUtils.capitalizeFully(text, delimiters.isEmpty() ? null : delimiters.toCharArray()));
    }

    @Signature
    public PTextWord uncapitalize(Environment env, @Optional(type = HintType.STRING) String delimiters) {
        return new PTextWord(env, WordUtils.uncapitalize(text, delimiters.isEmpty() ? null : delimiters.toCharArray()));
    }

    @Signature
    public PTextWord wrap(Environment env, int lineLength,
                          @Optional("null") Memory newLineString,
                          @Optional("false") boolean wrapLongWords) {
        return new PTextWord(env, WordUtils.wrap(
                text, lineLength, newLineString.isNull() ? null : newLineString.toString(), wrapLongWords
        ));
    }

    @Signature
    public PTextWord swapCase(Environment env) {
        return new PTextWord(env, WordUtils.swapCase(text));
    }

    @Signature
    public PTextWord initials(Environment env, @Optional(type = HintType.STRING) String delimiters) {
        return new PTextWord(env, WordUtils.initials(text, delimiters.isEmpty() ? null : delimiters.toCharArray()));
    }

    @Override
    public PTextWord __clone(Environment env, TraceInfo trace) {
        return new PTextWord(env, text);
    }

    @Override
    public boolean __equal(PTextWord iObject) {
        return Objects.equals(iObject.text, iObject.text);
    }

    @Override
    public boolean __identical(PTextWord iObject) {
        return this == iObject;
    }

    @Override
    public boolean __greater(PTextWord iObject) {
        return text.compareTo(iObject.text) > 0;
    }

    @Override
    public boolean __greaterEq(PTextWord iObject) {
        return text.compareTo(iObject.text) >= 0;
    }

    @Override
    public boolean __smaller(PTextWord iObject) {
        return text.compareTo(iObject.text) < 0;
    }

    @Override
    public boolean __smallerEq(PTextWord iObject) {
        return text.compareTo(iObject.text) <= 0;
    }


    @Signature
    public Integer levenshteinDistance(Environment env, Memory other, @Optional("null") Memory threshold) {
        LevenshteinDistance distance = new LevenshteinDistance(
                threshold.isNull() ? null : threshold.toInteger()
        );
        return distance.apply(text, other.toString());
    }

    @Signature
    public Double jaroWinklerDistance(Environment env, Memory other) {
        JaroWinklerDistance distance = new JaroWinklerDistance();
        return distance.apply(text, other.toString());
    }

    @Signature
    public Double jaccardDistance(Environment env, Memory other) {
        JaccardDistance distance = new JaccardDistance();
        return distance.apply(text, other.toString());
    }

    @Signature
    public Integer hammingDistance(Environment env, Memory other) {
        HammingDistance distance = new HammingDistance();
        return distance.apply(text, other.toString());
    }

    @Signature
    public Double cosineDistance(Environment env, Memory other) {
        CosineDistance cosineDistance = new CosineDistance();
        return cosineDistance.apply(text, other.toString());
    }

    @Signature
    public Integer fuzzyScore(Environment env, Memory query) {
        return fuzzyScore(env, query, null);
    }

    @Signature
    public Integer fuzzyScore(Environment env, Memory query, @Nullable Locale locale) {
        FuzzyScore cosineDistance = new FuzzyScore(locale != null ? locale : env.getLocale());
        return cosineDistance.fuzzyScore(text, query.toString());
    }
}
