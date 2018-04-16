package org.develnext.jphp.ext.git.support;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.ReflogEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.URIish;
import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;

import java.util.Collection;
import java.util.List;

public class GitUtils {

    public static Memory valueOf(AnyObjectId value) {
        return value == null ? Memory.NULL : StringMemory.valueOf(value.getName());
    }

    public static ArrayMemory valueOf(Ref.Storage storage) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("loose").assign(storage.isLoose());
        memory.refOfIndex("packed").assign(storage.isPacked());
        return memory;
    }

    public static ArrayMemory valueOf(Ref ref) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("name").assign(ref.getName());
        memory.refOfIndex("peeled").assign(ref.isPeeled());
        memory.refOfIndex("symbolic").assign(ref.isSymbolic());
        memory.refOfIndex("objectId").assign(valueOf(ref.getObjectId()));
        memory.refOfIndex("storage").assign(valueOf(ref.getStorage()));
        return memory;
    }

    public static ArrayMemory valueOfRevCommits(Iterable<RevCommit> commits) {
        ArrayMemory memory = new ArrayMemory();

        for (RevCommit ref : commits) {
            memory.add(valueOf(ref));
        }

        return memory;
    }
    public static ArrayMemory valueOfRefs(Iterable<Ref> refs) {
        ArrayMemory memory = new ArrayMemory();

        for (Ref ref : refs) {
            memory.add(valueOf(ref));
        }

        return memory;
    }

    public static ArrayMemory valueOf(OperationResult value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("advertisedRefs").assign(valueOfRefs(value.getAdvertisedRefs()));
        memory.refOfIndex("messages").assign(value.getMessages());
        memory.refOfIndex("peerUserAgent").assign(value.getPeerUserAgent());
        memory.refOfIndex("uri").assign(value.getURI().toString());
        return memory;
    }

    public static ArrayMemory valueOf(RemoteRefUpdate value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("message").assign(value.getMessage());
        memory.refOfIndex("remoteName").assign(value.getRemoteName());
        memory.refOfIndex("srcRef").assign(value.getSrcRef());
        memory.refOfIndex("expectedOldObjectId").assign(valueOf(value.getExpectedOldObjectId()));
        memory.refOfIndex("newObjectId").assign(valueOf(value.getNewObjectId()));
        memory.refOfIndex("delete").assign(value.isDelete());
        memory.refOfIndex("fastForward").assign(value.isFastForward());
        memory.refOfIndex("forceUpdate").assign(value.isForceUpdate());
        return memory;
    }

    public static ArrayMemory valueOfRemoteRefUpdates(Iterable<RemoteRefUpdate> values) {
        ArrayMemory memory = new ArrayMemory();

        for (RemoteRefUpdate value : values) {
            memory.add(valueOf(value));
        }

        return memory;
    }

    public static ArrayMemory valueOf(PushResult value) {
        ArrayMemory memory = valueOf((OperationResult) value);
        memory.refOfIndex("remoteUpdates").assign(valueOfRemoteRefUpdates(value.getRemoteUpdates()));
        return memory;
    }

    public static ArrayMemory valueOfPushResults(Iterable<PushResult> values) {
        ArrayMemory memory = new ArrayMemory();

        for (PushResult value : values) {
            memory.add(valueOf(value));
        }

        return memory;
    }

    public static ArrayMemory valueOf(RevObject value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("id").assign(valueOf(value.getId()));
        memory.refOfIndex("type").assign(value.getType());
        return memory;
    }

    public static ArrayMemory valueOf(PersonIdent value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("name").assign(value.getName());
        memory.refOfIndex("email").assign(value.getEmailAddress());
        memory.refOfIndex("when").assign(value.getWhen() == null ? Memory.NULL : LongMemory.valueOf(value.getWhen().getTime()));
        memory.refOfIndex("timeZone").assign(
                value.getTimeZone() == null ? Memory.NULL : StringMemory.valueOf(value.getTimeZone().getID())
        );
        return memory;
    }

    public static ArrayMemory valueOf(RevCommit value) {
        ArrayMemory memory = valueOf((RevObject) value);

        memory.refOfIndex("commitTime").assign(value.getCommitTime());
        memory.refOfIndex("encoding").assign(value.getEncodingName());
        memory.refOfIndex("shortMessage").assign(value.getShortMessage());
        memory.refOfIndex("fullMessage").assign(value.getFullMessage());
        
        ArrayMemory parents = new ArrayMemory();
        for (RevCommit revCommit : value.getParents()) {
            parents.add(valueOf((RevObject)revCommit));
        }

        memory.refOfIndex("parents").assign(parents);

        PersonIdent authorIdent = value.getAuthorIdent();
        memory.refOfIndex("author").assign(authorIdent == null ? Memory.NULL : valueOf(authorIdent));

        PersonIdent committerIdent = value.getCommitterIdent();
        memory.refOfIndex("committer").assign(committerIdent == null ? Memory.NULL : valueOf(committerIdent));

        return memory;
    }

    public static ArrayMemory valueOf(Status value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("added").assign(ArrayMemory.ofStringCollection(value.getAdded()));
        memory.refOfIndex("changed").assign(ArrayMemory.ofStringCollection(value.getChanged()));
        memory.refOfIndex("conflicting").assign(ArrayMemory.ofStringCollection(value.getConflicting()));
        memory.refOfIndex("ignoredNotInIndex").assign(ArrayMemory.ofStringCollection(value.getIgnoredNotInIndex()));
        memory.refOfIndex("missing").assign(ArrayMemory.ofStringCollection(value.getMissing()));
        memory.refOfIndex("modified").assign(ArrayMemory.ofStringCollection(value.getModified()));
        memory.refOfIndex("removed").assign(ArrayMemory.ofStringCollection(value.getRemoved()));
        memory.refOfIndex("uncommittedChanges").assign(ArrayMemory.ofStringCollection(value.getUncommittedChanges()));
        memory.refOfIndex("untracked").assign(ArrayMemory.ofStringCollection(value.getUntracked()));
        memory.refOfIndex("untrackedFolders").assign(ArrayMemory.ofStringCollection(value.getUntrackedFolders()));

        return memory;
    }

    public static ArrayMemory valueOf(RemoteConfig value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("name").assign(value.getName());
        memory.refOfIndex("receivePack").assign(value.getReceivePack());
        memory.refOfIndex("uploadPack").assign(value.getUploadPack());
        memory.refOfIndex("mirror").assign(value.isMirror());
        memory.refOfIndex("timeout").assign(value.getTimeout());
        memory.refOfIndex("tagOpt").assign(value.getTagOpt().name());
        ArrayMemory uris = ArrayMemory.createListed(value.getURIs().size());

        for (URIish urIish : value.getURIs()) {
            uris.add(urIish.toPrivateString());
        }

        memory.refOfIndex("uris").assign(uris);

        return memory;
    }


    public static ArrayMemory valueOfRemoteConfigs(Iterable<RemoteConfig> values) {
        ArrayMemory memory = new ArrayMemory();

        for (RemoteConfig value : values) {
            memory.add(valueOf(value));
        }

        return memory;
    }

    public static ArrayMemory valueOf(PullResult call) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("success").assign(call.isSuccessful());
        memory.refOfIndex("fetchedFrom").assign(call.getFetchedFrom());
        memory.refOfIndex("fetch").assign(valueOf(call.getFetchResult()));
        memory.refOfIndex("merge").assign(call.getMergeResult() == null ? Memory.NULL : valueOf(call.getMergeResult()));

        return memory;
    }

    public static ArrayMemory valueOf(MergeResult call) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("base").assign(valueOf(call.getBase()));
        memory.refOfIndex("newHead").assign(valueOf(call.getNewHead()));

        memory.refOfIndex("status").assign(call.getMergeStatus().name());
        memory.refOfIndex("success").assign(call.getMergeStatus().isSuccessful());

        memory.refOfIndex("checkoutConflicts").assign(ArrayMemory.ofStringCollection(call.getCheckoutConflicts()));

        return memory;
    }

    public static ArrayMemory valueOf(ReflogEntry value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("newId").assign(valueOf(value.getNewId()));
        memory.refOfIndex("oldId").assign(valueOf(value.getOldId()));
        memory.refOfIndex("who").assign(value.getWho() == null ? Memory.NULL : valueOf(value.getWho()));
        memory.refOfIndex("comment").assign(value.getComment());
        return memory;
    }

    public static ArrayMemory valueOfReflogEntries(Iterable<ReflogEntry> values) {
        ArrayMemory memory = new ArrayMemory();

        for (ReflogEntry value : values) {
            memory.add(valueOf(value));
        }

        return memory;
    }

    public static ArrayMemory valueOf(DiffEntry value) {
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("oldId").assign(value.getOldId() == null ? Memory.NULL : valueOf(value.getOldId().toObjectId()));
        memory.refOfIndex("oldPath").assign(value.getOldPath());
        memory.refOfIndex("oldMode").assign(value.getOldMode() == null ? Memory.NULL : StringMemory.valueOf(value.getOldMode().toString()));

        memory.refOfIndex("newId").assign(value.getNewId() == null ? Memory.NULL : valueOf(value.getNewId().toObjectId()));
        memory.refOfIndex("newPath").assign(value.getNewPath());
        memory.refOfIndex("newMode").assign(value.getNewMode() == null ? Memory.NULL : StringMemory.valueOf(value.getNewMode().toString()));

        memory.refOfIndex("score").assign(value.getScore());
        memory.refOfIndex("changeType").assign(value.getChangeType().name());

        return memory;
    }

    public static Memory valueOfDiffEntries(Iterable<DiffEntry> values) {
        ArrayMemory memory = new ArrayMemory();

        for (DiffEntry value : values) {
            memory.add(valueOf(value));
        }

        return memory;
    }
}
