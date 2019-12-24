package org.develnext.jphp.ext.git.classes;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.develnext.jphp.ext.git.GitExtension;
import org.develnext.jphp.ext.git.support.GitUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.submodule.SubmoduleWalk;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.FS;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Name("Git")
@Namespace(GitExtension.NS)
public class WrapGit extends BaseWrapper<Git> {
    private CredentialsProvider credentialsProvider;
    private TransportConfigCallback transportConfigCallback;

    public WrapGit(Environment env, Git wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapGit(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(File directory) throws IOException, GitAPIException {
        __construct(directory, true);
    }

    @Signature
    public void __construct(File directory, boolean create) throws IOException, GitAPIException {
        try {
            __wrappedObject = Git.open(directory, FS.DETECTED);
        } catch (RepositoryNotFoundException e) {
            if (create) {
                Git.init().setBare(false).setDirectory(directory).call();
                __wrappedObject = Git.open(directory, FS.DETECTED);
            }
        }
    }

    @Signature
    public void init() throws IOException {
        init(false);
    }

    @Signature
    public void init(boolean bare) throws IOException {
        getWrappedObject().getRepository().create(bare);
    }

    @Signature
    public ArrayMemory getTags() {
        Map<String, Ref> tags = getWrappedObject().getRepository().getTags();
        ArrayMemory memory = new ArrayMemory();

        for (Map.Entry<String, Ref> entry : tags.entrySet()) {
            memory.put(entry.getKey(), GitUtils.valueOf(entry.getValue()));
        }

        return memory;
    }

    @Signature
    public String getBranch() throws IOException {
        return getWrappedObject().getRepository().getBranch();
    }

    @Signature
    public String getFullBranch() throws IOException {
        return getWrappedObject().getRepository().getFullBranch();
    }

    @Signature
    public File getDirectory() {
        return getWrappedObject().getRepository().getDirectory();
    }

    @Signature
    public RepositoryState getState() {
        return getWrappedObject().getRepository().getRepositoryState();
    }

    @Signature
    public String getRemoteName(String name) {
        return getWrappedObject().getRepository().getRemoteName(name);
    }

    @Signature
    public boolean isExists() throws IOException {
        return getWrappedObject().getRepository().getObjectDatabase().exists();
    }

    @Signature
    public boolean isBare() {
        return getWrappedObject().getRepository().isBare();
    }

    @Signature
    public Memory findRef(String name) throws IOException {
        Ref ref = getWrappedObject().getRepository().findRef(name);
        return ref == null ? Memory.NULL : GitUtils.valueOf(ref);
    }

    @Signature
    public Memory exactRef(String name) throws IOException {
        Ref ref = getWrappedObject().getRepository().exactRef(name);
        return ref == null ? Memory.NULL : GitUtils.valueOf(ref);
    }

    @Signature
    public File getWorkTree() throws IOException {
        return getWrappedObject().getRepository().getWorkTree();
    }

    @Signature
    public Memory resolve(String revstr) throws IOException {
        return GitUtils.valueOf(getWrappedObject().getRepository().resolve(revstr));
    }

    @Signature
    public Memory resolveCommit(String revstr) throws IOException, GitAPIException {
        ObjectId objectId = getWrappedObject().getRepository().resolve(revstr);

        if (objectId == null) {
            return Memory.NULL;
        }

        LogCommand command = getWrappedObject()
                .log()
                .add(objectId)
                .setMaxCount(1);

        Iterable<RevCommit> call = command.call();

        for (RevCommit revCommit : call) {
            return GitUtils.valueOf(revCommit);
        }

        return Memory.NULL;
    }

    @Signature
    public void setCredentials(String userName, String password) {
        credentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
    }

    @Signature
    public void setTransportConfig(final Environment env, final ArrayMemory settings) {
        if (settings.size() == 0) {
            transportConfigCallback = null;
        } else {
            transportConfigCallback = new TransportConfigCallback() {
                @Override
                public void configure(Transport transport) {
                    Memory timeout = settings.valueOfIndex(env, "timeout");
                    if (timeout != null) {
                        transport.setTimeout(timeout.toInteger());
                    }

                    transport.setDryRun(settings.valueOfIndex(env, "dryRun").toBoolean());
                    transport.setCheckFetchedObjects(settings.valueOfIndex(env, "checkFetchedObjects").toBoolean());
                    transport.setFetchThin(settings.valueOfIndex(env, "fetchThin").toBoolean());
                    transport.setPushAtomic(settings.valueOfIndex(env, "pushAtomic").toBoolean());
                    transport.setPushThin(settings.valueOfIndex(env, "pushThin").toBoolean());
                    transport.setRemoveDeletedRefs(settings.valueOfIndex(env, "removeDeletedRefs").toBoolean());

                    if (transport instanceof SshTransport) {
                        ((SshTransport) transport).setSshSessionFactory(new JschConfigSessionFactory() {
                            @Override
                            protected void configure(OpenSshConfig.Host hc, Session session) {
                                Memory sshPassword = settings.valueOfIndex(env, "sshPassword");
                                if (sshPassword.isNotNull()) {
                                    session.setPassword(sshPassword.toString());
                                }

                                Memory sshHost = settings.valueOfIndex(env, "sshHost");
                                if (sshHost.isNotNull()) {
                                    session.setHost(sshHost.toString());
                                }

                                Memory sshPort = settings.valueOfIndex(env, "sshPort");
                                if (sshHost.isNotNull()) {
                                    session.setPort(sshPort.toInteger());
                                }
                            }

                            @Override
                            protected JSch createDefaultJSch(FS fs) throws JSchException {
                                JSch jSch = super.createDefaultJSch(fs);
                                Memory sshIdentity = settings.valueOfIndex(env, "sshIdentity");

                                if (sshIdentity.isNotNull()) {
                                    String passphrase = null;

                                    Memory passphraseMem = sshIdentity.valueOfIndex(env, "passphrase");
                                    if (passphraseMem.isNotNull()) {
                                        passphrase = passphraseMem.toString();
                                    }

                                    Memory publicKey = sshIdentity.valueOfIndex(env, "publicKey");
                                    String privateKey = sshIdentity.valueOfIndex(env, "privateKey").toString();

                                    if (publicKey.isNull()) {
                                        jSch.addIdentity(privateKey, passphrase);
                                    } else {
                                        jSch.addIdentity(privateKey, publicKey.toString(), passphrase == null ? null : passphrase.getBytes());
                                    }
                                }

                                Memory knownHosts = settings.valueOfIndex(env, "sshKnownHosts");
                                if (knownHosts.isNotNull()) {
                                    if (knownHosts.instanceOf(Stream.class)) {
                                        jSch.setKnownHosts(Stream.getInputStream(env, knownHosts));
                                    } else {
                                        jSch.setKnownHosts(knownHosts.toString());
                                    }
                                }

                                return jSch;
                            }
                        });
                    }
                }
            };
        }
    }

    @Signature
    public Memory branchList() throws GitAPIException {
        return branchList(null);
    }

    @Signature
    public Memory branchList(ArrayMemory settings) throws GitAPIException {
        ListBranchCommand command = getWrappedObject().branchList();

        if (settings != null) {
            Memory listMode = settings.valueOfIndex(getEnvironment(), "listMode");
            if (listMode.isNotNull()) {
                command.setListMode(ListBranchCommand.ListMode.valueOf(listMode.toString()));
            }

            Memory contains = settings.valueOfIndex(getEnvironment(), "contains");
            if (contains.isNotNull()) {
                command.setContains(contains.toString());
            }
        }
        return GitUtils.valueOfRefs(command.call());
    }

    @Signature
    public List<String> branchDelete(String[] name) throws GitAPIException {
        return branchDelete(name, false);
    }

    @Signature
    public List<String> branchDelete(String[] names, boolean force) throws GitAPIException {
        DeleteBranchCommand command = getWrappedObject().branchDelete();
        command.setBranchNames(names);

        return command.call();
    }

    @Signature
    public Memory branchCreate(String name) throws GitAPIException {
        return branchCreate(name, null);
    }

    @Signature
    public Memory branchCreate(String name, ArrayMemory settings) throws GitAPIException {
        CreateBranchCommand command = getWrappedObject().branchCreate();
        command.setName(name);

        if (settings != null && settings.isNotNull()) {
            Memory startPoint = settings.valueOfIndex(getEnvironment(),"startPoint");
            if (startPoint.isNotNull()) {
                command.setStartPoint(startPoint.toString());
            }

            command.setForce(settings.valueOfIndex(getEnvironment(),"force").toBoolean());

            Memory upstreamMode = settings.valueOfIndex(getEnvironment(),"upstreamMode");
            if (upstreamMode.isNotNull()) {
                command.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.valueOf(upstreamMode.toString()));
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory branchRename(String newName) throws GitAPIException {
        return branchRename(newName, Memory.NULL);
    }

    @Signature
    public Memory branchRename(String newName, Memory oldName) throws GitAPIException {
        RenameBranchCommand command = getWrappedObject().branchRename();

        command.setNewName(newName);

        if (oldName.isNotNull()) {
            command.setOldName(oldName.toString());
        }

        return GitUtils.valueOf(command.call());
    }


    @Signature
    public Memory status(Environment env, TraceInfo trace) throws GitAPIException {
        return status(env, trace, null);
    }

    @Signature
    public Memory status(Environment env, TraceInfo trace, @Nullable Set<String> paths) throws GitAPIException {
        return status(env, trace, paths, null);
    }

    @Signature
    public Memory status(Environment env, TraceInfo trace, @Nullable Set<String> paths, ArrayMemory settings) throws GitAPIException {
        StatusCommand status = getWrappedObject().status();

        if (paths != null) {
            for (String path : paths) {
                status.addPath(path);
            }
        }

        if (settings != null && settings.isNotNull()) {
            if (settings.containsKey("ignoreSubmoduleMode")) {
                status.setIgnoreSubmodules(SubmoduleWalk.IgnoreSubmoduleMode.valueOf(settings.valueOfIndex(env, trace, "ignoreSubmoduleMode").toString()));
            }
        }

        Status call = status.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public void push(Environment env, TraceInfo trace, String remote) throws GitAPIException {
        push(env, trace, remote, null);
    }

    @Signature
    public Memory push(Environment env, TraceInfo trace, String remote, ArrayMemory settings) throws GitAPIException {
        PushCommand push = getWrappedObject().push();

        push.setRemote(remote);

        push.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            push.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null && settings.isNotNull()) {
            push.setAtomic(settings.valueOfIndex(env, trace, "atomic").toBoolean());
            push.setDryRun(settings.valueOfIndex(env, trace, "dryRun").toBoolean());
            push.setForce(settings.valueOfIndex(env, trace, "force").toBoolean());

            if (settings.valueOfIndex(env, trace, "all").toBoolean()) {
                push.setPushAll();
            }

            if (settings.valueOfIndex(env, trace, "tags").toBoolean()) {
                push.setPushTags();
            }

            Memory receivePack = settings.valueOfIndex(env, trace, "receivePack");
            if (receivePack.isNotNull()) {
                push.setReceivePack(receivePack.toString());
            }

            push.setThin(settings.valueOfIndex(env, trace, "thin").toBoolean());

            Memory timeout = settings.valueOfIndex(env, trace, "timeout");
            if (timeout.isNotNull()) {
                push.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex(env, trace, "credentials");
            if (credentials.isNotNull()) {
                push.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex(env, trace, "username").toString(),
                        credentials.valueOfIndex(env, trace, "password").toString()
                ));
            }
        }

        return GitUtils.valueOfPushResults(push.call());
    }

    @Signature
    public Memory commit(Environment env, TraceInfo trace, String message) throws GitAPIException {
        return commit(env, trace, message, null);
    }

    @Signature
    public Memory commit(Environment env, TraceInfo trace, String message, ArrayMemory settings) throws GitAPIException {
        CommitCommand commit = getWrappedObject().commit();
        commit.setMessage(message);

        if (settings != null && settings.isNotNull()) {
            commit.setAll(settings.valueOfIndex(env, trace, "all").toBoolean());
            commit.setAllowEmpty(settings.valueOfIndex(env, trace, "allowEmpty").toBoolean());
            commit.setAmend(settings.valueOfIndex(env, trace, "amend").toBoolean());

            commit.setInsertChangeId(settings.valueOfIndex(env, trace, "insertChangeId").toBoolean());
            commit.setNoVerify(settings.valueOfIndex(env, trace, "noVerify").toBoolean());

            Memory author = settings.valueOfIndex(env, trace, "author");
            if (author.isArray()) {
                commit.setAuthor(author.valueOfIndex(env, trace, "name").toString(), author.valueOfIndex(env, trace, "email").toString());
            }

            Memory committer = settings.valueOfIndex(env, trace, "committer");
            if (committer.isArray()) {
                commit.setCommitter(committer.valueOfIndex(env, trace, "name").toString(), committer.valueOfIndex(env, trace, "email").toString());
            }

            if (settings.containsKey("only")) {
                commit.setOnly(settings.valueOfIndex(env, trace, "only").toString());
            }

            if (settings.containsKey("reflogComment")) {
                commit.setReflogComment(settings.valueOfIndex(env, trace, "reflogComment").toString());
            }
        }

        RevCommit call = commit.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public void add(Environment env, TraceInfo trace, String filePattern) throws GitAPIException {
        add(env, trace, filePattern, ArrayMemory.createHashed());
    }

    @Signature
    public void add(Environment env, TraceInfo trace, String filePattern, ArrayMemory settings) throws GitAPIException {
        AddCommand addCommand = getWrappedObject().add();
        addCommand.addFilepattern(filePattern);

        if (settings != null && settings.isNotNull()) {
            addCommand.setUpdate(settings.valueOfIndex(env, trace, "update").toBoolean());
        }

        DirCache dirCache = addCommand.call();
    }

    @Signature
    public Set<String> clean(Environment env, TraceInfo trace) throws GitAPIException {
        return clean(env, trace, null);
    }

    @Signature
    public Set<String> clean(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        CleanCommand cleanCommand = getWrappedObject().clean();

        if (settings != null && settings.isNotNull()) {
            if (settings.containsKey("cleanDirectories"))
                cleanCommand.setCleanDirectories(settings.valueOfIndex(env, trace, "cleanDirectories").toBoolean());

            if (settings.containsKey("dryRun"))
                cleanCommand.setDryRun(settings.valueOfIndex(env, trace, "dryRun").toBoolean());

            cleanCommand.setForce(settings.valueOfIndex(env, trace, "force").toBoolean());
            cleanCommand.setIgnore(settings.valueOfIndex(env, trace, "ignore").toBoolean());

            if (settings.containsKey("paths")) {
                Set<String> paths = new LinkedHashSet<>();

                ForeachIterator iterator = settings.valueOfIndex(env, trace, "paths").getNewIterator(env);

                if (iterator == null) {
                    paths.add(settings.valueOfIndex(env, trace, "paths").toString());
                } else {
                    while (iterator.next()) {
                        paths.add(iterator.getValue().toString());
                    }
                }

                cleanCommand.setPaths(paths);
            }
        }

        return cleanCommand.call();
    }

    @Signature
    public Memory fetch(Environment env, TraceInfo trace) throws GitAPIException {
        return fetch(env, trace, null);
    }

    @Signature
    public Memory fetch(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        FetchCommand command = getWrappedObject().fetch();

        command.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null) {
            command.setCheckFetchedObjects(settings.valueOfIndex(env, trace, "checkFetchedObjects").toBoolean());
            command.setDryRun(settings.valueOfIndex(env, trace, "dryRun").toBoolean());
            command.setRemoveDeletedRefs(settings.valueOfIndex(env, trace, "removeDeletedRefs").toBoolean());
            command.setThin(settings.valueOfIndex(env, trace, "thin").toBoolean());

            Memory remote = settings.valueOfIndex(env, trace, "remote");
            if (remote.isNotNull()) {
                command.setRemote(remote.toString());
            }

            Memory recurseSubmodules = settings.valueOfIndex(env, trace, "recurseSubmodules");
            if (recurseSubmodules.isNotNull()) {
                command.setRecurseSubmodules(SubmoduleConfig.FetchRecurseSubmodulesMode.valueOf(recurseSubmodules.toString()));
            }

            Memory tagOpt = settings.valueOfIndex(env, trace, "tagOpt");
            if (tagOpt.isNotNull()) {
                command.setTagOpt(TagOpt.valueOf(tagOpt.toString()));
            }

            Memory timeout = settings.valueOfIndex(env, trace,"timeout");
            if (timeout.isNotNull()) {
                command.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex(env, trace,"credentials");
            if (credentials.isNotNull()) {
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex(env, trace,"username").toString(),
                        credentials.valueOfIndex(env, trace,"password").toString()
                ));
            }
        }

        FetchResult call = command.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public Memory pull(Environment env, TraceInfo trace, String remote) throws GitAPIException {
        return pull(env, trace, remote, ArrayMemory.createHashed());
    }

    @Signature
    public Memory pull(Environment env, TraceInfo trace, String remote, ArrayMemory settings) throws GitAPIException {
        PullCommand command = getWrappedObject().pull();

        command.setRemote(remote);

        command.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null) {
            command.setRebase(settings.valueOfIndex(env, trace, "rebase").toBoolean());

            Memory branch = settings.valueOfIndex(env, trace, "branch");
            if (branch.isNotNull()) {
                command.setRemoteBranchName(branch.toString());
            }

            Memory strategy = settings.valueOfIndex(env, trace, "strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }

            Memory tagOpt = settings.valueOfIndex(env, trace, "tagOpt");
            if (tagOpt.isNotNull()) {
                command.setTagOpt(TagOpt.valueOf(tagOpt.toString()));
            }

            Memory timeout = settings.valueOfIndex(env, trace, "timeout");
            if (timeout.isNotNull()) {
                command.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex(env, trace, "credentials");
            if (credentials.isNotNull()) {
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex(env, trace, "username").toString(),
                        credentials.valueOfIndex(env, trace, "password").toString()
                ));
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory remoteList() throws GitAPIException {
        RemoteListCommand command = getWrappedObject().remoteList();
        List<RemoteConfig> call = command.call();

        return GitUtils.valueOfRemoteConfigs(call);
    }

    @Signature
    public Memory remoteAdd(String name, String uri) throws URISyntaxException, GitAPIException {
        RemoteAddCommand command = getWrappedObject().remoteAdd();
        command.setName(name);
        command.setUri(new URIish(uri));
        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory remoteSetUrl(String name, String uri) throws URISyntaxException, GitAPIException {
        return remoteSetUrl(name, uri, false);
    }

    @Signature
    public Memory remoteSetUrl(String name, String uri, boolean push) throws URISyntaxException, GitAPIException {
        RemoteSetUrlCommand command = getWrappedObject().remoteSetUrl();
        command.setName(name);
        command.setUri(new URIish(uri));
        command.setPush(push);
        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory remoteRemove(String name) throws URISyntaxException, GitAPIException {
        RemoteRemoveCommand command = getWrappedObject().remoteRemove();
        command.setName(name);
        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory diff(Environment env, TraceInfo trace) throws GitAPIException {
        return diff(env, trace, null);
    }

    @Signature
    public Memory diff(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        DiffCommand command = getWrappedObject().diff();

        if (settings != null) {
            command.setCached(settings.valueOfIndex(env, trace, "cached").toBoolean());

            Memory contextLines = settings.valueOfIndex(env, trace, "contextLines");
            if (contextLines.isNotNull()) {
                command.setContextLines(contextLines.toInteger());
            }

            Memory destPrefix = settings.valueOfIndex(env, trace, "destPrefix");
            if (destPrefix.isNotNull()) {
                command.setDestinationPrefix(destPrefix.toString());
            }

            Memory sourcePrefix = settings.valueOfIndex(env, trace, "sourcePrefix");
            if (sourcePrefix.isNotNull()) {
                command.setSourcePrefix(sourcePrefix.toString());
            }

            command.setShowNameAndStatusOnly(settings.valueOfIndex(env, trace, "showNameAndStatusOnly").toBoolean());

            Memory pathFilter = settings.valueOfIndex(env, trace, "pathFilter");
            if (pathFilter.isNotNull()) {
                command.setPathFilter(PathFilter.create(pathFilter.toString()));
            }
        }

        List<DiffEntry> call = command.call();

        return GitUtils.valueOfDiffEntries(call);
    }

    @Signature
    public Memory reflog() throws GitAPIException {
        return reflog(Memory.NULL);
    }

    @Signature
    public Memory reflog(Memory ref) throws GitAPIException {
        ReflogCommand command = getWrappedObject().reflog();

        if (ref.isNotNull()) {
            command.setRef(ref.toString());
        }

        Collection<ReflogEntry> call = command.call();
        return GitUtils.valueOfReflogEntries(call);
    }

    @Signature
    public Memory log(Environment env, TraceInfo trace) throws Throwable {
        return log(env, trace, null);
    }

    @Signature
    public Memory log(Environment env, TraceInfo trace, ArrayMemory settings) throws Throwable {
        LogCommand log = getWrappedObject().log();

        if (settings != null) {
            Repository repository = getWrappedObject().getRepository();

            Memory maxCount = settings.valueOfIndex(env, trace, "maxCount");

            if (maxCount.isNotNull()) {
                log.setMaxCount(maxCount.toInteger());
            }

            Memory skip = settings.valueOfIndex(env, trace, "skip");
            if (skip.isNotNull()) {
                log.setSkip(skip.toInteger());
            }

            if (settings.valueOfIndex(env, trace, "all").toBoolean()) {
                log.all();
            } else {
                Memory range = settings.valueOfIndex(env, trace, "range");

                if (range.isArray()) {
                    Memory first = range.valueOfIndex(env, TraceInfo.UNKNOWN, 0);
                    Memory second = range.valueOfIndex(env, TraceInfo.UNKNOWN, 1);

                    ObjectId fId;

                    if (first.isNull()) {
                        fId = repository.resolve("HEAD");
                    } else {
                        fId = repository.resolve(first.toString());
                    }

                    if (fId == null) {
                        throw new IllegalArgumentException("Range 'from' objectId (" + first.toString() + ") is not found");
                    }
                    
                    if (second.isNotNull()) {
                        ObjectId sId = repository.resolve(second.toString());

                        if (sId == null) {
                            throw new IllegalArgumentException("Range 'to' objectId (" + second.toString() + ") is not found");
                        }

                        log.addRange(sId, fId);
                    } else {
                        log.add(fId);
                    }
                }
            }

            Memory paths = settings.valueOfIndex(env, trace, "paths");
            if (paths.isNotNull()) {
                if (paths.isTraversable()) {
                    ForeachIterator iterator = paths.getNewIterator(env);
                    while (iterator.next()) {
                        log.addPath(iterator.getValue().toString());
                    }
                } else {
                    log.addPath(paths.toString());
                }
            }
        }

        return GitUtils.valueOfRevCommits(log.call());
    }

    @Signature
    public void rm(String filePattern) throws GitAPIException {
        rm(filePattern, false);
    }

    @Signature
    public void rm(String filePattern, boolean cached) throws GitAPIException {
        getWrappedObject()
                .rm()
                .addFilepattern(filePattern)
                .setCached(cached)
                .call();
    }

    @Signature
    public Memory merge(Environment env, String[] refs, ArrayMemory settings) throws IOException, GitAPIException {
        MergeCommand command = getWrappedObject().merge();

        for (String ref : refs) {
            Repository repository = getWrappedObject().getRepository();
            ObjectId objectId = repository.resolve(ref);
            command.include(objectId);
        }

        if (settings != null) {
            command.setCommit(settings.valueOfIndex(env, "commit").toBoolean());
            command.setMessage(settings.valueOfIndex(env, "message").toString());
            command.setSquash(settings.valueOfIndex(env, "squash").toBoolean());

            Memory fastForward = settings.valueOfIndex(env, "fastForward");
            
            if (fastForward.isNumber()) {
                command.setFastForward(MergeCommand.FastForwardMode.valueOf(fastForward.toString()));
            }

            Memory strategy = settings.valueOfIndex(env, "strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }
        }

        MergeResult call = command.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public Memory reset(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        ResetCommand reset = getWrappedObject().reset();

        Memory mode = settings.valueOfIndex(env, "mode");
        if (mode.isNotNull()) {
            reset.setMode(ResetCommand.ResetType.valueOf(mode.toString()));
        }

        Memory ref = settings.valueOfIndex(env, "ref");
        if (ref.isNotNull()) {
            reset.setRef(ref.toString());
        }

        reset.disableRefLog(settings.valueOfIndex(env, trace, "disableRefLog").toBoolean());

        Memory paths = settings.valueOfIndex(env, trace, "paths");
        if (paths.isNotNull()) {
            ForeachIterator iterator = paths.getNewIterator(env);

            if (iterator != null) {
                while (iterator.next()) {
                    reset.addPath(iterator.getValue().toString());
                }
            } else {
                reset.addPath(paths.toString());
            }
        }

        Ref call = reset.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public Memory stashCreate(Environment env, TraceInfo trace) throws GitAPIException {
        return stashCreate(env, trace, null);
    }

    @Signature
    public Memory stashCreate(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        StashCreateCommand command = getWrappedObject().stashCreate();

        if (settings != null) {
            command.setIncludeUntracked(settings.valueOfIndex(env, trace, "includeUntracked").toBoolean());

            Memory indexMessage = settings.valueOfIndex(env, trace, "indexMessage");
            if (indexMessage.isNotNull()) {
                command.setIndexMessage(indexMessage.toString());
            }

            Memory ref = settings.valueOfIndex(env, trace, "ref");
            if (ref.isNotNull()) {
                command.setRef(ref.toString());
            }

            Memory workingDirectoryMessage = settings.valueOfIndex(env, trace, "workingDirectoryMessage");
            if (workingDirectoryMessage.isNotNull()) {
                command.setWorkingDirectoryMessage(workingDirectoryMessage.toString());
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory stashApply(Environment env, TraceInfo trace) throws GitAPIException {
        return stashApply(env, trace, null);
    }

    @Signature
    public Memory stashApply(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        StashApplyCommand command = getWrappedObject().stashApply();

        if (settings != null) {
            command.setApplyIndex(settings.valueOfIndex(env, trace, "applyIndex").toBoolean());
            command.setApplyUntracked(settings.valueOfIndex(env, trace, "applyUntracked").toBoolean());

            Memory stashRef = settings.valueOfIndex(env, trace, "stashRef");
            if (stashRef.isNotNull()) {
                command.setStashRef(stashRef.toString());
            }

            Memory strategy = settings.valueOfIndex(env, trace, "strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory stashDrop(Environment env, TraceInfo trace) throws GitAPIException {
        return stashDrop(env, trace, null);
    }

    @Signature
    public Memory stashDrop(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        StashDropCommand command = getWrappedObject().stashDrop();

        if (settings != null) {
            command.setAll(settings.valueOfIndex(env, trace, "all").toBoolean());

            Memory stashRef = settings.valueOfIndex(env, trace, "stashRef");
            if (stashRef.isNotNull()) {
                command.setStashRef(stashRef.toInteger());
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory stashList() throws GitAPIException {
        StashListCommand command = getWrappedObject().stashList();
        return GitUtils.valueOfRevCommits(command.call());
    }

    @Signature
    public Memory checkout(Environment env, TraceInfo trace, ArrayMemory settings) throws GitAPIException {
        CheckoutCommand command = getWrappedObject().checkout();

        command.setAllPaths(settings.valueOfIndex(env, trace, "allPaths").toBoolean());
        command.setCreateBranch(settings.valueOfIndex(env, trace, "createBranch").toBoolean());
        command.setForce(settings.valueOfIndex(env, trace, "force").toBoolean());
        command.setOrphan(settings.valueOfIndex(env, trace, "orphan").toBoolean());

        Memory name = settings.valueOfIndex(env, trace, "name");
        if (name.isNotNull()) {
            command.setName(name.toString());
        }

        Memory stage = settings.valueOfIndex(env, trace, "stage");
        if (stage.isNotNull()) {
            command.setStage(CheckoutCommand.Stage.valueOf(stage.toString()));
        }

        Memory startPoint = settings.valueOfIndex(env, trace, "startPoint");
        if (startPoint.isNotNull()) {
            command.setStartPoint(startPoint.toString());
        }

        Memory upstreamMode = settings.valueOfIndex(env, trace, "upstreamMode");
        if (upstreamMode.isNotNull()) {
            command.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.valueOf(upstreamMode.toString()));
        }

        Memory paths = settings.valueOfIndex(env, trace, "paths");
        if (paths.isNotNull()) {
            ForeachIterator iterator = paths.getNewIterator(env);

            if (iterator != null) {
                while (iterator.next()) {
                    command.addPath(iterator.getValue().toString());
                }
            } else {
                command.addPath(paths.toString());
            }
        }

        return GitUtils.valueOf(command.call());
    }
}
