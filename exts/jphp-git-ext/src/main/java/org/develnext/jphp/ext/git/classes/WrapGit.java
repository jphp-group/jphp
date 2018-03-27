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
                    Memory timeout = settings.valueOfIndex("timeout");
                    if (timeout != null) {
                        transport.setTimeout(timeout.toInteger());
                    }

                    transport.setDryRun(settings.valueOfIndex("dryRun").toBoolean());
                    transport.setCheckFetchedObjects(settings.valueOfIndex("checkFetchedObjects").toBoolean());
                    transport.setFetchThin(settings.valueOfIndex("fetchThin").toBoolean());
                    transport.setPushAtomic(settings.valueOfIndex("pushAtomic").toBoolean());
                    transport.setPushThin(settings.valueOfIndex("pushThin").toBoolean());
                    transport.setRemoveDeletedRefs(settings.valueOfIndex("removeDeletedRefs").toBoolean());

                    if (transport instanceof SshTransport) {
                        ((SshTransport) transport).setSshSessionFactory(new JschConfigSessionFactory() {
                            @Override
                            protected void configure(OpenSshConfig.Host hc, Session session) {
                                Memory sshPassword = settings.valueOfIndex("sshPassword");
                                if (sshPassword.isNotNull()) {
                                    session.setPassword(sshPassword.toString());
                                }

                                Memory sshHost = settings.valueOfIndex("sshHost");
                                if (sshHost.isNotNull()) {
                                    session.setHost(sshHost.toString());
                                }

                                Memory sshPort = settings.valueOfIndex("sshPort");
                                if (sshHost.isNotNull()) {
                                    session.setPort(sshPort.toInteger());
                                }
                            }

                            @Override
                            protected JSch createDefaultJSch(FS fs) throws JSchException {
                                JSch jSch = super.createDefaultJSch(fs);
                                Memory sshIdentity = settings.valueOfIndex("sshIdentity");

                                if (sshIdentity.isNotNull()) {
                                    String passphrase = null;

                                    Memory passphraseMem = sshIdentity.valueOfIndex("passphrase");
                                    if (passphraseMem.isNotNull()) {
                                        passphrase = passphraseMem.toString();
                                    }

                                    Memory publicKey = sshIdentity.valueOfIndex("publicKey");
                                    String privateKey = sshIdentity.valueOfIndex("privateKey").toString();

                                    if (publicKey.isNull()) {
                                        jSch.addIdentity(privateKey, passphrase);
                                    } else {
                                        jSch.addIdentity(privateKey, publicKey.toString(), passphrase == null ? null : passphrase.getBytes());
                                    }
                                }

                                Memory knownHosts = settings.valueOfIndex("sshKnownHosts");
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
            Memory listMode = settings.valueOfIndex("listMode");
            if (listMode.isNotNull()) {
                command.setListMode(ListBranchCommand.ListMode.valueOf(listMode.toString()));
            }

            Memory contains = settings.valueOfIndex("contains");
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
            Memory startPoint = settings.valueOfIndex("startPoint");
            if (startPoint.isNotNull()) {
                command.setStartPoint(startPoint.toString());
            }

            command.setForce(settings.valueOfIndex("force").toBoolean());

            Memory upstreamMode = settings.valueOfIndex("upstreamMode");
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
    public Memory status() throws GitAPIException {
        return status(null);
    }

    @Signature
    public Memory status(@Nullable Set<String> paths) throws GitAPIException {
        return status(paths, null);
    }

    @Signature
    public Memory status(@Nullable Set<String> paths, ArrayMemory settings) throws GitAPIException {
        StatusCommand status = getWrappedObject().status();

        if (paths != null) {
            for (String path : paths) {
                status.addPath(path);
            }
        }

        if (settings != null && settings.isNotNull()) {
            if (settings.containsKey("ignoreSubmoduleMode")) {
                status.setIgnoreSubmodules(SubmoduleWalk.IgnoreSubmoduleMode.valueOf(settings.valueOfIndex("ignoreSubmoduleMode").toString()));
            }
        }

        Status call = status.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public void push(String remote) throws GitAPIException {
        push(remote, null);
    }

    @Signature
    public Memory push(String remote, ArrayMemory settings) throws GitAPIException {
        PushCommand push = getWrappedObject().push();

        push.setRemote(remote);

        push.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            push.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null && settings.isNotNull()) {
            push.setAtomic(settings.valueOfIndex("atomic").toBoolean());
            push.setDryRun(settings.valueOfIndex("dryRun").toBoolean());
            push.setForce(settings.valueOfIndex("force").toBoolean());

            if (settings.valueOfIndex("all").toBoolean()) {
                push.setPushAll();
            }

            if (settings.valueOfIndex("tags").toBoolean()) {
                push.setPushTags();
            }

            Memory receivePack = settings.valueOfIndex("receivePack");
            if (receivePack.isNotNull()) {
                push.setReceivePack(receivePack.toString());
            }

            push.setThin(settings.valueOfIndex("thin").toBoolean());

            Memory timeout = settings.valueOfIndex("timeout");
            if (timeout.isNotNull()) {
                push.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex("credentials");
            if (credentials.isNotNull()) {
                push.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex("username").toString(),
                        credentials.valueOfIndex("password").toString()
                ));
            }
        }

        return GitUtils.valueOfPushResults(push.call());
    }

    @Signature
    public Memory commit(String message) throws GitAPIException {
        return commit(message, null);
    }

    @Signature
    public Memory commit(String message, ArrayMemory settings) throws GitAPIException {
        CommitCommand commit = getWrappedObject().commit();
        commit.setMessage(message);

        if (settings != null && settings.isNotNull()) {
            commit.setAll(settings.valueOfIndex("all").toBoolean());
            commit.setAllowEmpty(settings.valueOfIndex("allowEmpty").toBoolean());
            commit.setAmend(settings.valueOfIndex("amend").toBoolean());

            commit.setInsertChangeId(settings.valueOfIndex("insertChangeId").toBoolean());
            commit.setNoVerify(settings.valueOfIndex("noVerify").toBoolean());

            Memory author = settings.valueOfIndex("author");
            if (author.isArray()) {
                commit.setAuthor(author.valueOfIndex("name").toString(), author.valueOfIndex("email").toString());
            }

            Memory committer = settings.valueOfIndex("committer");
            if (committer.isArray()) {
                commit.setCommitter(committer.valueOfIndex("name").toString(), committer.valueOfIndex("email").toString());
            }

            if (settings.containsKey("only")) {
                commit.setOnly(settings.valueOfIndex("only").toString());
            }

            if (settings.containsKey("reflogComment")) {
                commit.setReflogComment(settings.valueOfIndex("reflogComment").toString());
            }
        }

        RevCommit call = commit.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public void add(String filePattern) throws GitAPIException {
        add(filePattern, ArrayMemory.createHashed());
    }

    @Signature
    public void add(String filePattern, ArrayMemory settings) throws GitAPIException {
        AddCommand addCommand = getWrappedObject().add();
        addCommand.addFilepattern(filePattern);

        if (settings != null && settings.isNotNull()) {
            addCommand.setUpdate(settings.valueOfIndex("update").toBoolean());
        }

        DirCache dirCache = addCommand.call();
    }

    @Signature
    public Set<String> clean(Environment env) throws GitAPIException {
        return clean(env, null);
    }

    @Signature
    public Set<String> clean(Environment env, ArrayMemory settings) throws GitAPIException {
        CleanCommand cleanCommand = getWrappedObject().clean();

        if (settings != null && settings.isNotNull()) {
            if (settings.containsKey("cleanDirectories"))
                cleanCommand.setCleanDirectories(settings.valueOfIndex("cleanDirectories").toBoolean());

            if (settings.containsKey("dryRun"))
                cleanCommand.setDryRun(settings.valueOfIndex("dryRun").toBoolean());

            cleanCommand.setForce(settings.valueOfIndex("force").toBoolean());
            cleanCommand.setIgnore(settings.valueOfIndex("ignore").toBoolean());

            if (settings.containsKey("paths")) {
                Set<String> paths = new LinkedHashSet<>();

                ForeachIterator iterator = settings.valueOfIndex("paths").getNewIterator(env);

                if (iterator == null) {
                    paths.add(settings.valueOfIndex("paths").toString());
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
    public Memory fetch() throws GitAPIException {
        return fetch(null);
    }

    @Signature
    public Memory fetch(ArrayMemory settings) throws GitAPIException {
        FetchCommand command = getWrappedObject().fetch();

        command.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null) {
            command.setCheckFetchedObjects(settings.valueOfIndex("checkFetchedObjects").toBoolean());
            command.setDryRun(settings.valueOfIndex("dryRun").toBoolean());
            command.setRemoveDeletedRefs(settings.valueOfIndex("removeDeletedRefs").toBoolean());
            command.setThin(settings.valueOfIndex("thin").toBoolean());

            Memory remote = settings.valueOfIndex("remote");
            if (remote.isNotNull()) {
                command.setRemote(remote.toString());
            }

            Memory recurseSubmodules = settings.valueOfIndex("recurseSubmodules");
            if (recurseSubmodules.isNotNull()) {
                command.setRecurseSubmodules(SubmoduleConfig.FetchRecurseSubmodulesMode.valueOf(recurseSubmodules.toString()));
            }

            Memory tagOpt = settings.valueOfIndex("tagOpt");
            if (tagOpt.isNotNull()) {
                command.setTagOpt(TagOpt.valueOf(tagOpt.toString()));
            }

            Memory timeout = settings.valueOfIndex("timeout");
            if (timeout.isNotNull()) {
                command.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex("credentials");
            if (credentials.isNotNull()) {
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex("username").toString(),
                        credentials.valueOfIndex("password").toString()
                ));
            }
        }

        FetchResult call = command.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public Memory pull(String remote) throws GitAPIException {
        return pull(remote, ArrayMemory.createHashed());
    }

    @Signature
    public Memory pull(String remote, ArrayMemory settings) throws GitAPIException {
        PullCommand command = getWrappedObject().pull();

        command.setRemote(remote);

        command.setTransportConfigCallback(transportConfigCallback);
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }

        if (settings != null) {
            command.setRebase(settings.valueOfIndex("rebase").toBoolean());

            Memory branch = settings.valueOfIndex("branch");
            if (branch.isNotNull()) {
                command.setRemoteBranchName(branch.toString());
            }

            Memory strategy = settings.valueOfIndex("strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }

            Memory tagOpt = settings.valueOfIndex("tagOpt");
            if (tagOpt.isNotNull()) {
                command.setTagOpt(TagOpt.valueOf(tagOpt.toString()));
            }

            Memory timeout = settings.valueOfIndex("timeout");
            if (timeout.isNotNull()) {
                command.setTimeout(timeout.toInteger());
            }

            Memory credentials = settings.valueOfIndex("credentials");
            if (credentials.isNotNull()) {
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        credentials.valueOfIndex("username").toString(),
                        credentials.valueOfIndex("password").toString()
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
    public Memory diff() throws GitAPIException {
        return diff(null);
    }

    @Signature
    public Memory diff(ArrayMemory settings) throws GitAPIException {
        DiffCommand command = getWrappedObject().diff();

        if (settings != null) {
            command.setCached(settings.valueOfIndex("cached").toBoolean());

            Memory contextLines = settings.valueOfIndex("contextLines");
            if (contextLines.isNotNull()) {
                command.setContextLines(contextLines.toInteger());
            }

            Memory destPrefix = settings.valueOfIndex("destPrefix");
            if (destPrefix.isNotNull()) {
                command.setDestinationPrefix(destPrefix.toString());
            }

            Memory sourcePrefix = settings.valueOfIndex("sourcePrefix");
            if (sourcePrefix.isNotNull()) {
                command.setSourcePrefix(sourcePrefix.toString());
            }

            command.setShowNameAndStatusOnly(settings.valueOfIndex("showNameAndStatusOnly").toBoolean());

            Memory pathFilter = settings.valueOfIndex("pathFilter");
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
    public Memory log(Environment env) throws Throwable {
        return log(env, null);
    }

    @Signature
    public Memory log(Environment env, ArrayMemory settings) throws Throwable {
        LogCommand log = getWrappedObject().log();

        if (settings != null) {
            Repository repository = getWrappedObject().getRepository();

            Memory maxCount = settings.valueOfIndex("maxCount");

            if (maxCount.isNotNull()) {
                log.setMaxCount(maxCount.toInteger());
            }

            Memory skip = settings.valueOfIndex("skip");
            if (skip.isNotNull()) {
                log.setSkip(skip.toInteger());
            }

            if (settings.valueOfIndex("all").toBoolean()) {
                log.all();
            } else {
                Memory range = settings.valueOfIndex("range");

                if (range.isArray()) {
                    Memory first = range.valueOfIndex(0);
                    Memory second = range.valueOfIndex(1);

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

            Memory paths = settings.valueOfIndex("paths");
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
    public Memory merge(String[] refs, ArrayMemory settings) throws IOException, GitAPIException {
        MergeCommand command = getWrappedObject().merge();

        for (String ref : refs) {
            Repository repository = getWrappedObject().getRepository();
            ObjectId objectId = repository.resolve(ref);
            command.include(objectId);
        }

        if (settings != null) {
            command.setCommit(settings.valueOfIndex("commit").toBoolean());
            command.setMessage(settings.valueOfIndex("message").toString());
            command.setSquash(settings.valueOfIndex("squash").toBoolean());

            Memory fastForward = settings.valueOfIndex("fastForward");
            
            if (fastForward.isNumber()) {
                command.setFastForward(MergeCommand.FastForwardMode.valueOf(fastForward.toString()));
            }

            Memory strategy = settings.valueOfIndex("strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }
        }

        MergeResult call = command.call();
        return GitUtils.valueOf(call);
    }

    @Signature
    public Memory reset(Environment env, ArrayMemory settings) throws GitAPIException {
        ResetCommand reset = getWrappedObject().reset();

        Memory mode = settings.valueOfIndex("mode");
        if (mode.isNotNull()) {
            reset.setMode(ResetCommand.ResetType.valueOf(mode.toString()));
        }

        Memory ref = settings.valueOfIndex("ref");
        if (ref.isNotNull()) {
            reset.setRef(ref.toString());
        }

        reset.disableRefLog(settings.valueOfIndex("disableRefLog").toBoolean());

        Memory paths = settings.valueOfIndex("paths");
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
    public Memory stashCreate() throws GitAPIException {
        return stashCreate(null);
    }

    @Signature
    public Memory stashCreate(ArrayMemory settings) throws GitAPIException {
        StashCreateCommand command = getWrappedObject().stashCreate();

        if (settings != null) {
            command.setIncludeUntracked(settings.valueOfIndex("includeUntracked").toBoolean());

            Memory indexMessage = settings.valueOfIndex("indexMessage");
            if (indexMessage.isNotNull()) {
                command.setIndexMessage(indexMessage.toString());
            }

            Memory ref = settings.valueOfIndex("ref");
            if (ref.isNotNull()) {
                command.setRef(ref.toString());
            }

            Memory workingDirectoryMessage = settings.valueOfIndex("workingDirectoryMessage");
            if (workingDirectoryMessage.isNotNull()) {
                command.setWorkingDirectoryMessage(workingDirectoryMessage.toString());
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory stashApply() throws GitAPIException {
        return stashApply(null);
    }

    @Signature
    public Memory stashApply(ArrayMemory settings) throws GitAPIException {
        StashApplyCommand command = getWrappedObject().stashApply();

        if (settings != null) {
            command.setApplyIndex(settings.valueOfIndex("applyIndex").toBoolean());
            command.setApplyUntracked(settings.valueOfIndex("applyUntracked").toBoolean());

            Memory stashRef = settings.valueOfIndex("stashRef");
            if (stashRef.isNotNull()) {
                command.setStashRef(stashRef.toString());
            }

            Memory strategy = settings.valueOfIndex("strategy");
            if (strategy.isNotNull()) {
                command.setStrategy(MergeStrategy.get(strategy.toString()));
            }
        }

        return GitUtils.valueOf(command.call());
    }

    @Signature
    public Memory stashDrop() throws GitAPIException {
        return stashDrop(null);
    }

    @Signature
    public Memory stashDrop(ArrayMemory settings) throws GitAPIException {
        StashDropCommand command = getWrappedObject().stashDrop();

        if (settings != null) {
            command.setAll(settings.valueOfIndex("all").toBoolean());

            Memory stashRef = settings.valueOfIndex("stashRef");
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
    public Memory checkout(Environment env, ArrayMemory settings) throws GitAPIException {
        CheckoutCommand command = getWrappedObject().checkout();

        command.setAllPaths(settings.valueOfIndex("allPaths").toBoolean());
        command.setCreateBranch(settings.valueOfIndex("createBranch").toBoolean());
        command.setForce(settings.valueOfIndex("force").toBoolean());
        command.setOrphan(settings.valueOfIndex("orphan").toBoolean());

        Memory name = settings.valueOfIndex("name");
        if (name.isNotNull()) {
            command.setName(name.toString());
        }

        Memory stage = settings.valueOfIndex("stage");
        if (stage.isNotNull()) {
            command.setStage(CheckoutCommand.Stage.valueOf(stage.toString()));
        }

        Memory startPoint = settings.valueOfIndex("startPoint");
        if (startPoint.isNotNull()) {
            command.setStartPoint(startPoint.toString());
        }

        Memory upstreamMode = settings.valueOfIndex("upstreamMode");
        if (upstreamMode.isNotNull()) {
            command.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.valueOf(upstreamMode.toString()));
        }

        Memory paths = settings.valueOfIndex("paths");
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
