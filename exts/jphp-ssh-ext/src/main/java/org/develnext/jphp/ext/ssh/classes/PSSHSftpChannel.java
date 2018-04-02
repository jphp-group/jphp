package org.develnext.jphp.ext.ssh.classes;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

@Reflection.Name("SSHSftpChannel")
@Reflection.Namespace(SSHExtension.NS)
public class PSSHSftpChannel extends PSSHChannel<ChannelSftp> {
    public enum Mode { OVERWRITE, RESUME, APPEND }

    interface WrappedInterface {
        @Reflection.Property String home();
        @Reflection.Property int serverVersion();
        @Reflection.Property int bulkRequests();

        String pwd();

        void cd(String path);
        void chgrp(int gid, String path);
        void chmod(int permissions, String path);
        void chown(int uid, String path);
        void lcd(String path);
        String lpwd();

        void hardlink(String oldpath, String newpath);
        void symlink(String oldpath, String newpath);
        void rename(String oldpath, String newpath);
        void mkdir(String path);
        void rmdir(String path);
        void rm(String path);

        String readlink(String path);
        String realpath(String path);

        String getExtension(String key);
    }

    public PSSHSftpChannel(Environment env, ChannelSftp wrappedObject) {
        super(env, wrappedObject);
    }

    public PSSHSftpChannel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public String getVersion() {
        return getWrappedObject().version();
    }

    @Signature
    public Memory stat(Environment env, String path) throws SftpException {
        try {
            SftpATTRS attrs = getWrappedObject().stat(path);
            return ArrayMemory.ofNullableBean(env, attrs);
        } catch (SftpException e) {
            if ("2: No such file".equals(e.toString())) {
                return Memory.NULL;
            }

            throw e;
        }
    }

    @Signature
    public Memory lstat(Environment env, String path) throws SftpException {
        try {
            SftpATTRS attrs = getWrappedObject().lstat(path);
            return ArrayMemory.ofNullableBean(env, attrs);
        } catch (SftpException e) {
            if ("2: No such file".equals(e.toString())) {
                return Memory.NULL;
            }

            throw e;
        }
    }

    @Signature
    public void d() {
        getWrappedObject().lpwd();
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory ls(Environment env, String path) throws SftpException {
        return ls(env, path, null);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory ls(Environment env, String path, @Nullable Invoker lsSelector) throws SftpException {
        Vector<ChannelSftp.LsEntry> ls = new Vector<>();

        ArrayMemory result = ArrayMemory.createListed(10);

        getWrappedObject().ls(path, new ChannelSftp.LsEntrySelector() {
            @Override
            public int select(ChannelSftp.LsEntry l) {
                ArrayMemory entry = ArrayMemory.createHashed();
                entry.put("name", l.getFilename());
                entry.put("longname", l.getLongname());
                entry.put("attrs", ArrayMemory.ofNullableBean(env, l.getAttrs()));

                if (lsSelector != null && lsSelector.callAny(entry).toBoolean()) {
                    return ChannelSftp.LsEntrySelector.BREAK;
                }

                result.add(entry);
                return ChannelSftp.LsEntrySelector.CONTINUE;
            }
        });

        return result.toConstant();
    }

    @Signature
    public InputStream get(String src) throws SftpException {
        return get(src, 0L);
    }

    @Signature
    public InputStream get(String src, long skip) throws SftpException {
        return get(src, skip, null);
    }

    @Signature
    public InputStream get(String src, long skip, @Nullable Invoker progressMonitor) throws SftpException {
        try {
            return getWrappedObject().get(src, new SftpProgressMonitor() {
                private long max;
                private long offset;
                private int op;

                @Override
                public void init(int op, String src, String dest, long max) {
                    this.offset = 0;
                    this.max = max;
                    this.op = op;
                }

                @Override
                public boolean count(long count) {
                    offset += count;

                    if (progressMonitor != null) {
                        if (progressMonitor.callAny(count, offset, max).toValue() == Memory.FALSE) {
                            return false;
                        }
                    }

                    return true;
                }

                @Override
                public void end() {
                    if (progressMonitor != null) {
                        progressMonitor.callAny(0, max, max);
                    }
                }
            }, skip);
        } catch (SftpException e) {
            if ("2: No such file".equals(e.toString())) {
                return null;
            }

            throw e;
        }
    }

    @Signature
    public OutputStream put(String src) throws SftpException {
        return put(src, Mode.OVERWRITE);
    }

    @Signature
    public OutputStream put(String src, Mode mode) throws SftpException {
        return put(src, mode, null);
    }

    @Signature
    public OutputStream put(String src, Mode mode, @Nullable Invoker progressMonitor) throws SftpException {
        return put(src, mode, progressMonitor, 0);
    }

    @Signature
    public OutputStream put(String src, Mode mode, @Nullable Invoker progressMonitor, int offset) throws SftpException {
        return getWrappedObject().put(src, new SftpProgressMonitor() {
            int op;
            long offset;
            long max;

            @Override
            public void init(int op, String src, String dest, long max) {
                this.max = max;
                this.offset = 0L;
                this.op = op;
            }

            @Override
            public boolean count(long count) {
                offset += count;

                if (progressMonitor != null) {
                    if (progressMonitor.callAny(count, offset, max).toValue() == Memory.FALSE) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void end() {
                if (progressMonitor != null) {
                    progressMonitor.callAny(0, max, max);
                }
            }
        }, mode.ordinal(), offset);
    }
}
