# Git

- **класс** `Git` (`git\Git`)
- **пакет** `git`
- **исходники** `git/Git.php`

**Описание**

Class Git

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _Git constructor._
- `->`[`init()`](#method-init)
- `->`[`resolve()`](#method-resolve) - _Parse a git revision string and return an object id._
- `->`[`resolveCommit()`](#method-resolvecommit)
- `->`[`getDirectory()`](#method-getdirectory)
- `->`[`getWorkTree()`](#method-getworktree)
- `->`[`getBranch()`](#method-getbranch)
- `->`[`getFullBranch()`](#method-getfullbranch)
- `->`[`getRemoteName()`](#method-getremotename)
- `->`[`findRef()`](#method-findref) - _Search for a ref by (possibly abbreviated) name._
- `->`[`exactRef()`](#method-exactref) - _Get a ref by name._
- `->`[`getState()`](#method-getstate) - _SAFE, MERGING, MERGING_RESOLVED, CHERRY_PICKING, CHERRY_PICKING_RESOLVED, REVERTING, REVERTING_RESOLVED, REBASING,_
- `->`[`isBare()`](#method-isbare)
- `->`[`isExists()`](#method-isexists)
- `->`[`getTags()`](#method-gettags)
- `->`[`setCredentials()`](#method-setcredentials)
- `->`[`setTransportConfig()`](#method-settransportconfig) - _Transport global config for push, fetch, pull, etc._
- `->`[`remoteList()`](#method-remotelist)
- `->`[`remoteAdd()`](#method-remoteadd)
- `->`[`remoteSetUrl()`](#method-remoteseturl)
- `->`[`remoteRemove()`](#method-remoteremove)
- `->`[`status()`](#method-status) - _Status git command, options:_
- `->`[`diff()`](#method-diff) - _Diff git command, options are:_
- `->`[`reflog()`](#method-reflog)
- `->`[`log()`](#method-log) - _Options are:_
- `->`[`branchList()`](#method-branchlist) - _Return branches, options are:_
- `->`[`branchCreate()`](#method-branchcreate) - _Create branch, options are:_
- `->`[`branchRename()`](#method-branchrename) - _Used to rename branches._
- `->`[`branchDelete()`](#method-branchdelete) - _Used to delete one or several branches._
- `->`[`add()`](#method-add) - _Add git command, options:_
- `->`[`commit()`](#method-commit) - _Add git command, options:_
- `->`[`push()`](#method-push) - _Push git command, options:_
- `->`[`fetch()`](#method-fetch) - _Fetch git command, options are:_
- `->`[`pull()`](#method-pull) - _Pull command, options:_
- `->`[`merge()`](#method-merge) - _Options:_
- `->`[`reset()`](#method-reset) - _Options are:_
- `->`[`clean()`](#method-clean) - _Clean git command, options:_
- `->`[`rm()`](#method-rm)
- `->`[`checkout()`](#method-checkout) - _Checkout git command, options are:_
- `->`[`stashCreate()`](#method-stashcreate) - _Stash create git command, options are:_
- `->`[`stashApply()`](#method-stashapply) - _Options are:_
- `->`[`stashDrop()`](#method-stashdrop) - _Options are:_
- `->`[`stashList()`](#method-stashlist) - _Stash list git command._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $directory, bool $createIfNotExists): void
```
Git constructor.

---

<a name="method-init"></a>

### init()
```php
init(bool $bare): void
```

---

<a name="method-resolve"></a>

### resolve()
```php
resolve(string $revstr): array
```
Parse a git revision string and return an object id.

* Combinations of these operators are supported:

HEAD, MERGE_HEAD, FETCH_HEAD
SHA-1: a complete or abbreviated SHA-1
refs/...: a complete reference name
short-name: a short reference name under {@code refs/heads},
{@code refs/tags}, or {@code refs/remotes} namespace
tag-NN-gABBREV: output from describe, parsed by treating
{@code ABBREV} as an abbreviated SHA-1.
id^: first parent of commit id, this is the same
as {@code id^1}
id^0: ensure id is a commit
id^n: n-th parent of commit id
id~n: n-th historical ancestor of id, by first
parent. {@code id~3} is equivalent to {@code id^1^1^1} or {@code id^^^}.
id:path: Lookup path under tree named by id
id^{commit}: ensure id is a commit
id^{tree}: ensure id is a tree
id^{tag}: ensure id is a tag
id^{blob}: ensure id is a blob

---

<a name="method-resolvecommit"></a>

### resolveCommit()
```php
resolveCommit(string $revstr): string
```

---

<a name="method-getdirectory"></a>

### getDirectory()
```php
getDirectory(): File
```

---

<a name="method-getworktree"></a>

### getWorkTree()
```php
getWorkTree(): File
```

---

<a name="method-getbranch"></a>

### getBranch()
```php
getBranch(): string
```

---

<a name="method-getfullbranch"></a>

### getFullBranch()
```php
getFullBranch(): string
```

---

<a name="method-getremotename"></a>

### getRemoteName()
```php
getRemoteName(string $name): string
```

---

<a name="method-findref"></a>

### findRef()
```php
findRef(string $name): array
```
Search for a ref by (possibly abbreviated) name.

---

<a name="method-exactref"></a>

### exactRef()
```php
exactRef(string $name): array
```
Get a ref by name.

---

<a name="method-getstate"></a>

### getState()
```php
getState(): string
```
SAFE, MERGING, MERGING_RESOLVED, CHERRY_PICKING, CHERRY_PICKING_RESOLVED, REVERTING, REVERTING_RESOLVED, REBASING,
REBASING_REBASING, APPLY, REBASING_MERGE, REBASING_INTERACTIVE, BISECTING

---

<a name="method-isbare"></a>

### isBare()
```php
isBare(): bool
```

---

<a name="method-isexists"></a>

### isExists()
```php
isExists(): bool
```

---

<a name="method-gettags"></a>

### getTags()
```php
getTags(): array[]
```

---

<a name="method-setcredentials"></a>

### setCredentials()
```php
setCredentials(string $username, string $password): void
```

---

<a name="method-settransportconfig"></a>

### setTransportConfig()
```php
setTransportConfig(array $config): void
```
Transport global config for push, fetch, pull, etc.

Config is an array:

timeout => int (seconds)
dryRun => bool
checkFetchedObjects => bool
fetchThin => bool
pushAtomic => bool
pushThin => bool
removeDeletedRefs => bool

sshPassword => string
sshHost => string
sshPort => int

sshIdentity => [privateKey => string, publicKey => string (optional), passphrase => string (optional)]
sshKnownHosts => string or Stream (path to file)

---

<a name="method-remotelist"></a>

### remoteList()
```php
remoteList(): array
```

---

<a name="method-remoteadd"></a>

### remoteAdd()
```php
remoteAdd(string $name, string $uri): array
```

---

<a name="method-remoteseturl"></a>

### remoteSetUrl()
```php
remoteSetUrl(string $name, string $uri): array
```

---

<a name="method-remoteremove"></a>

### remoteRemove()
```php
remoteRemove(string $name): array
```

---

<a name="method-status"></a>

### status()
```php
status(array|null $paths, array|null $options): array
```
Status git command, options:

ignoreSubmoduleMode => string (ALL|DIRTY|UNTRACKED|NONE)

Return array value:

added => [...]
changed => [...]
conflicting => [...]
ignoredNotInIndex => [...]
missing => [...]
modified => [...]
removed => [...]
uncommittedChanges => [...]
untracked => [...]
untrackedFolders => [...]

---

<a name="method-diff"></a>

### diff()
```php
diff(array $options): array
```
Diff git command, options are:

cached => bool
contextLines => int
destPrefix => string
sourcePrefix => string
showNameAndStatusOnly => bool
pathFilter => string

---

<a name="method-reflog"></a>

### reflog()
```php
reflog(string $ref): array
```

---

<a name="method-log"></a>

### log()
```php
log(array $options): array[]
```
Options are:

maxCount => int
skip => int
all => bool
paths => string[] or string

range => [fromId, toId (nullable)]

---

<a name="method-branchlist"></a>

### branchList()
```php
branchList(array $options): array
```
Return branches, options are:

listMode => string (ALL|REMOTE)
contains => string (a commit ID or ref name)

---

<a name="method-branchcreate"></a>

### branchCreate()
```php
branchCreate(string $name, array $options): array
```
Create branch, options are:

force => bool
startPoint => string (rev commit)
upstreamMode => string (TRACK|NOTRACK|SET_UPSTREAM)

---

<a name="method-branchrename"></a>

### branchRename()
```php
branchRename(string $newName, string $oldName): array
```
Used to rename branches.

---

<a name="method-branchdelete"></a>

### branchDelete()
```php
branchDelete(array $names, bool $force): string[]
```
Used to delete one or several branches.

The result of call is a list with the (full) names of the deleted
branches.

---

<a name="method-add"></a>

### add()
```php
add(string $filePattern, array $options): void
```
Add git command, options:

update => bool

---

<a name="method-commit"></a>

### commit()
```php
commit(string $message, array $options): array
```
Add git command, options:

all => bool
allowEmpty => bool
amend => bool
insertChangeId => bool
noVerify => bool
only => string
reflogComment => string

author => [name, email]
committer => [name, email]

---

<a name="method-push"></a>

### push()
```php
push(string $remote, array $options): array[]
```
Push git command, options:

atomic => bool
dryRun => bool
force => bool
all => bool
tags => bool
receivePack => string
thin => bool
timeout => int (millis)
credentials => [username, password]

---

<a name="method-fetch"></a>

### fetch()
```php
fetch(array $options): array
```
Fetch git command, options are:

checkFetchedObjects => bool
dryRun => bool
removeDeletedRefs => bool
thin => bool
remote => string
recurseSubmodules => string (YES|NO|ON_DEMAND)
tagOpt => string (AUTO_FOLLOW|NO_TAGS|FETCH_TAGS)
timeout => int (seconds)
credentials => [username, password]

---

<a name="method-pull"></a>

### pull()
```php
pull(string $remote, array $options): array
```
Pull command, options:

rebase => bool
branch => string
strategy => string (ours, theirs, resolve, recursive, simple-two-way-in-core)
timeout => int (seconds)
credentials => [username, password]

---

<a name="method-merge"></a>

### merge()
```php
merge(array $revs, array $options): array
```
Options:

commit => bool
message => string
squash => bool
fastForward => string (FF|NO_FF|FF_ONLY)
strategy => string (ours, theirs, resolve, recursive, simple-two-way-in-core)

---

<a name="method-reset"></a>

### reset()
```php
reset(array $options): array
```
Options are:

mode => string (SOFT|MIXED|HARD)
ref => string
disableRefLog => bool
paths => string[] or string

---

<a name="method-clean"></a>

### clean()
```php
clean(array $options): string[]
```
Clean git command, options:

cleanDirectories => bool
dryRun => bool
force => bool
ignore => bool
paths => string[]

---

<a name="method-rm"></a>

### rm()
```php
rm(string $filePattern, bool $cached): void
```

---

<a name="method-checkout"></a>

### checkout()
```php
checkout(array $options): array
```
Checkout git command, options are:

allPaths => bool
createBranch => bool
force => bool
orphan => bool
name => string
stage => string (BASE|OURS|THEIRS)
startPoint => string (a commit ID or ref name)
upstreamMode => string (TRACK|NOTRACK|SET_UPSTREAM)
paths => string[] or string

---

<a name="method-stashcreate"></a>

### stashCreate()
```php
stashCreate(array $options): array
```
Stash create git command, options are:

includeUntracked => bool
indexMessage => string
ref => string
workingDirectoryMessage => string

---

<a name="method-stashapply"></a>

### stashApply()
```php
stashApply(array $options): array
```
Options are:

applyIndex => bool
applyUntracked => bool
stashRef => string
strategy => string

---

<a name="method-stashdrop"></a>

### stashDrop()
```php
stashDrop(array $options): array
```
Options are:

all => bool
stashRef => int

---

<a name="method-stashlist"></a>

### stashList()
```php
stashList(): array
```
Stash list git command.