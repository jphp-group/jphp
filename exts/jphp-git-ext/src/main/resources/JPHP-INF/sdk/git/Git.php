<?php
namespace git;

use php\io\File;
use php\io\IOException;
use php\lib\arr;

/**
 * Class Git
 * @package git
 *
 * @packages git
 */
class Git
{
    /**
     * Git constructor.
     * @param string $directory
     * @param bool $createIfNotExists
     *
     * @throws IOException
     */
    function __construct($directory, bool $createIfNotExists = true)
    {
    }

    /**
     * @param bool $bare
     */
    function init($bare = false)
    {
    }


    /**
     * Parse a git revision string and return an object id.
     *
     * * Combinations of these operators are supported:
     * 
     * HEAD, MERGE_HEAD, FETCH_HEAD
     *  SHA-1: a complete or abbreviated SHA-1
     *  refs/...: a complete reference name
     *  short-name: a short reference name under {@code refs/heads},
     *  {@code refs/tags}, or {@code refs/remotes} namespace
     *  tag-NN-gABBREV: output from describe, parsed by treating
     *  {@code ABBREV} as an abbreviated SHA-1.
     *  id^: first parent of commit id, this is the same
     *  as {@code id^1}
     *  id^0: ensure id is a commit
     *  id^n: n-th parent of commit id
     *  id~n: n-th historical ancestor of id, by first
     *  parent. {@code id~3} is equivalent to {@code id^1^1^1} or {@code id^^^}.
     *  id:path: Lookup path under tree named by id
     *  id^{commit}: ensure id is a commit
     *  id^{tree}: ensure id is a tree
     *  id^{tag}: ensure id is a tag
     *  id^{blob}: ensure id is a blob
     * 
     *
     * @param string $revstr
     * @return array
     */
    function resolve($revstr)
    {
    }

    /**
     * @param string $revstr
     * @return string
     */
    function resolveCommit($revstr)
    {
    }

    /**
     * @return File
     */
    function getDirectory()
    {
    }

    /**
     * @return File
     */
    function getWorkTree()
    {
    }

    /**
     * @return string
     */
    function getBranch()
    {
    }

    /**
     * @return string
     */
    function getFullBranch()
    {
    }

    /**
     * @param string $name
     * @return string the remote name part of refName, i.e. without the
     *         refs/remotes/&lt;remote&gt; prefix, if
     *         refName represents a remote tracking branch;
     */
    function getRemoteName($name)
    {
    }

    /**
     * Search for a ref by (possibly abbreviated) name.
     * 
     * @param string $name
     * @return array
     * @throws IOException
     */
    function findRef($name)
    {
    }


    /**
     * Get a ref by name.
     *
     * @param string $name
     * @return array
     * @throws IOException
     */
    function exactRef($name)
    {
    }

    

    /**
     * SAFE, MERGING, MERGING_RESOLVED, CHERRY_PICKING, CHERRY_PICKING_RESOLVED, REVERTING, REVERTING_RESOLVED, REBASING,
     * REBASING_REBASING, APPLY, REBASING_MERGE, REBASING_INTERACTIVE, BISECTING
     * 
     * @return string
     */
    function getState()
    {
    }

    /**
     * @return bool
     */
    function isBare()
    {
    }

    /**
     * @return bool
     * @throws IOException
     */
    function isExists()
    {
    }

    /**
     * @return array[]
     */
    function getTags()
    {
    }

    /**
     * @param string $username
     * @param string $password
     */
    function setCredentials($username, $password)
    {
    }

    /**
     * Transport global config for push, fetch, pull, etc.
     *
     * Config is an array:
     *
     *      timeout => int (seconds)
     *      dryRun => bool
     *      checkFetchedObjects => bool
     *      fetchThin => bool
     *      pushAtomic => bool
     *      pushThin => bool
     *      removeDeletedRefs => bool
     *
     *      sshPassword => string
     *      sshHost => string
     *      sshPort => int
     *
     *      sshIdentity => [privateKey => string, publicKey => string (optional), passphrase => string (optional)]
     *      sshKnownHosts => string or Stream (path to file)
     *
     * @param array $config
     */
    function setTransportConfig(array $config)
    {
    }

    /**
     * @return array
     * @throws GitAPIException
     */
    function remoteList()
    {
    }

    /**
     * @param string $name
     * @param string $uri
     * @return array
     * @throws GitAPIException
     */
    function remoteAdd($name, $uri)
    {
    }

    /**
     * @param string $name
     * @param string $uri
     * @return array
     * @throws GitAPIException
     */
    function remoteSetUrl($name, $uri)
    {
    }

    /**
     * @param string $name
     * @return array
     * @throws GitAPIException
     */
    function remoteRemove($name)
    {
    }

    /**
     * Status git command, options:
     *
     *      ignoreSubmoduleMode => string (ALL|DIRTY|UNTRACKED|NONE)
     *
     * Return array value:
     *
     *      added => [...]
     *      changed => [...]
     *      conflicting => [...]
     *      ignoredNotInIndex => [...]
     *      missing => [...]
     *      modified => [...]
     *      removed => [...]
     *      uncommittedChanges => [...]
     *      untracked => [...]
     *      untrackedFolders => [...]
     *
     * @param array|null $paths
     * @param array|null $options
     * @return array
     * @throws GitAPIException
     */
    function status(array $paths = null, array $options = [])
    {
    }

    /**
     * Diff git command, options are:
     *
     *      cached => bool
     *      contextLines => int
     *      destPrefix => string
     *      sourcePrefix => string
     *      showNameAndStatusOnly => bool
     *      pathFilter => string
     *
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function diff(array $options = [])
    {
    }

    /**
     * @param string $ref
     * @return array
     * @throws GitAPIException
     */
    function reflog($ref = null)
    {
    }

    /**
     * Options are:
     *
     *      maxCount => int
     *      skip => int
     *      all => bool
     *      paths => string[] or string
     *
     *      range => [fromId, toId (nullable)]
     *      
     *
     * @param array $options
     * @return array[]
     */
    function log($options = [])
    {
    }

    /**
     * Return branches, options are:
     *
     *      listMode => string (ALL|REMOTE)
     *      contains => string (a commit ID or ref name)
     *
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function branchList(array $options = [])
    {
    }

    /**
     * Create branch, options are:
     *
     *      force => bool
     *      startPoint => string (rev commit)
     *      upstreamMode => string (TRACK|NOTRACK|SET_UPSTREAM)
     *
     * @param string $name
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function branchCreate($name, array $options = [])
    {
    }

    /**
     * Used to rename branches.
     * 
     * @param string $newName
     * @param string $oldName the name of the branch to rename; if not set, the currently
     *            checked out branch (if any) will be renamed
     *
     * @return array
     * @throws GitAPIException
     */
    function branchRename($newName, $oldName = null)
    {
    }

    /**
     * Used to delete one or several branches.
     *
     * The result of call is a list with the (full) names of the deleted
     * branches.
     *
     * @param array $names
     * @param bool $force
     * @return string[]
     * @throws GitAPIException
     */
    function branchDelete(array $names, $force = false)
    {
    }

    /**
     * Add git command, options:
     *
     *      update => bool
     *
     * @param string $filePattern
     * @param array $options
     * @throws GitAPIException
     */
    function add($filePattern, array $options = [])
    {
    }

    /**
     * Add git command, options:
     *
     *      all => bool
     *      allowEmpty => bool
     *      amend => bool
     *      insertChangeId => bool
     *      noVerify => bool
     *      only => string
     *      reflogComment => string
     *
     *      author => [name, email]
     *      committer => [name, email]
     *
     * @param string $message
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function commit($message, array $options = [])
    {
    }

    /**
     * Push git command, options:
     *
     *      atomic => bool
     *      dryRun => bool
     *      force => bool
     *      all => bool
     *      tags => bool
     *      receivePack => string
     *      thin => bool
     *      timeout => int (millis)
     *      credentials => [username, password]
     *
     * @param string $remote
     * @param array $options
     *
     * @return array[]
     * @throws GitAPIException
     */
    function push($remote, array $options = [])
    {
    }

    /**
     * Fetch git command, options are:
     *
     *      checkFetchedObjects => bool
     *      dryRun => bool
     *      removeDeletedRefs => bool
     *      thin => bool
     *      remote => string
     *      recurseSubmodules => string (YES|NO|ON_DEMAND)
     *      tagOpt => string (AUTO_FOLLOW|NO_TAGS|FETCH_TAGS)
     *      timeout => int (seconds)
     *      credentials => [username, password]
     *
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function fetch(array $options = [])
    {
    }

    /**
     * Pull command, options:
     *
     *      rebase => bool
     *      branch => string
     *      strategy => string (ours, theirs, resolve, recursive, simple-two-way-in-core)
     *      timeout => int (seconds)
     *      credentials => [username, password]
     *
     * @param string $remote
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function pull($remote, array $options = [])
    {
    }

    /**
     * Options:
     *
     *      commit => bool
     *      message => string
     *      squash => bool
     *      fastForward => string (FF|NO_FF|FF_ONLY)
     *      strategy => string (ours, theirs, resolve, recursive, simple-two-way-in-core)
     *
     * @param array $revs id commits, branch names, etc.
     * @param array $options
     * @return array
     * @throws GitAPIException, IOException
     */
    function merge(array $revs, array $options = [])
    {
    }

    /**
     * Options are:
     *
     *      mode => string (SOFT|MIXED|HARD)
     *      ref => string
     *      disableRefLog => bool
     *      paths => string[] or string
     *
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function reset(array $options)
    {
    }

    /**
     * Clean git command, options:
     * 
     *      cleanDirectories => bool
     *      dryRun => bool
     *      force => bool
     *      ignore => bool
     *      paths => string[]
     *
     * @param array $options
     * @return string[] a set of strings representing each file cleaned.
     *
     * @throws GitAPIException
     */
    function clean(array $options = [])
    {
    }

    /**
     * @param string $filePattern
     * @param bool $cached
     */
    function rm($filePattern, $cached = false)
    {
    }

    /**
     * Checkout git command, options are:
     *
     *      allPaths => bool
     *      createBranch => bool
     *      force => bool
     *      orphan => bool
     *      name => string
     *      stage => string (BASE|OURS|THEIRS)
     *      startPoint => string (a commit ID or ref name)
     *      upstreamMode => string (TRACK|NOTRACK|SET_UPSTREAM)
     *      paths => string[] or string
     *
     * @param array $options
     * @return array
     * @throws GitAPIException
     */
    function checkout(array $options)
    {
    }

    /**
     * Stash create git command, options are:
     *
     *      includeUntracked => bool
     *      indexMessage => string
     *      ref => string
     *      workingDirectoryMessage => string
     *
     * @param array $options
     * @return array rev commit
     */
    function stashCreate(array $options = [])
    {
    }

    /**
     * Options are:
     *
     *      applyIndex => bool
     *      applyUntracked => bool
     *      stashRef => string
     *      strategy => string
     *
     * @param array $options
     * @return array
     */
    function stashApply(array $options = [])
    {
    }

    /**
     * Options are:
     *
     *      all => bool
     *      stashRef => int
     *
     * @param array $options
     * @return array
     */
    function stashDrop(array $options = [])
    {
    }

    /**
     * Stash list git command.
     * @return array
     */
    function stashList()
    {
    }
}