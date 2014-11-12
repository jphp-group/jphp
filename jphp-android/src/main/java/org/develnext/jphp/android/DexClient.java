package org.develnext.jphp.android;

import com.android.dex.DexFormat;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.code.PositionList;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import php.runtime.exceptions.CriticalException;

import java.io.IOException;
import java.io.Writer;

public class DexClient {
    private static DexFile outputDex;
    private static DexOptions dexOptions = new DexOptions();

    static {
        dexOptions.targetApiLevel = DexFormat.API_NO_EXTENDED_OPCODES;
    }

    private final CfOptions cfOptions;

    public DexClient() {
        outputDex = new DexFile(dexOptions);

        cfOptions = new CfOptions();
        cfOptions.positionInfo = PositionList.LINES;
        cfOptions.localInfo = false;
        cfOptions.strictNameCheck = false;
        cfOptions.optimize = false;
        cfOptions.optimizeListFile = null;
        cfOptions.dontOptimizeListFile = null;
        cfOptions.statistics = false;
    }

    public void classToDex(String name, byte[] byteArray) {

        DirectClassFile directClassFile = new DirectClassFile(byteArray, name, false);
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);

        processClass(directClassFile, byteArray);
    }

    private boolean processClass(DirectClassFile classFile, byte[] bytes) {
        try {
            ClassDefItem clazz;
            clazz = CfTranslator.translate(classFile, bytes, cfOptions, dexOptions, outputDex);
            outputDex.add(clazz);
            return true;
        } catch (ParseException ex) {
            throw new CriticalException(ex);
        }
    }

    public void write(Writer writer) {
        try {
            outputDex.toDex(writer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
