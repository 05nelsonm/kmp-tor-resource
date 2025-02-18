# diff-cli

A command line tool for creating `diff` files which can be applied at a later date

### WTF is a diff file?

It is a byte for byte comparison of 2 files, whereby the differences (or "diffs") are 
recorded. This is especially useful when codesigning reproducibly built software, as 
the signature can be "stripped" to a `.diff` file and applied later to the unsigned 
binaries for verification purposes.

Here's what one looks like

**FileA contents:**
```
The cat walked along the path
with its owner.
```

**FileB contents:**
```
The dog walked along the path
with its owner.
```

**FileA.diff:**
```
# Diff Schema: v1
# Created At: 2023-03-22T17:40:40.827701908Z
# Created For File: FileA
# Created For Hash: 225c10aa2f68ee67e36e456589134ad5fefd55757d57bd5102317278f53838cf
# Created From Hash: 45691d3c5c79895d03cda232bea12d524ea5cd2a914845ed75c48272a1eb1867
# i:4
ZG9n
# i:46

# END: f2252a8c04bbe48f4846d7e40452b22f36b16f6f3516833e630c778e392b8754
```

It basically says that to turn `FileA` into `FileB`, `FileA` at bytes starting with 
index `4` need to be changed to `ZG9n`.

The `i:46` with an empty difference below it simply states that between index 
`(4 + number of modified bytes)` and index `46`, there were no changes and to
use `FileA`'s current data.

Applying the diff will change it's sha256 hash from `225c10aa2f68ee67e36e456589134ad5fefd55757d57bd5102317278f53838cf`,
to `45691d3c5c79895d03cda232bea12d524ea5cd2a914845ed75c48272a1eb1867`

Will work on any file, for any signatures, anything... you can diff the `README.md` with
`settings.gradle.kts`, and then apply the diff to `README.md` to turn it exactly into what
`settings.gradle.kts` was when the diff was taken.

### Example Of producing a detached signature for codesigning

```shell
$ ./tooling diff-cli create --diff-ext-name ".signature" /tor-macos-arm64/unsigned/tor /tor-macos-arm64/signed/tor /tor-macos-arm64/signatures/
```

Will compare the **unsigned** `tor` binary with the **signed** `tor` binary,
and produce a `tor.signature` file. The file can be applied to the **unsigned**
binary later, to turn it into what it was created from (the **signed** tor binary).

```shell
$ ./tooling diff-cli apply /tor-macos-arm64/signatures/tor.signature /tor-macos-arm64/unsigned/tor
```

Will then apply the `.signature` to the file it is associated to.
