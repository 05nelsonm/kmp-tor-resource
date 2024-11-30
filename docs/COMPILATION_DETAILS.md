# COMPILATION DETAILS

<!-- TODO -->

`tor` is compiled via the `external/task.sh` script using `Docker` in order to maintain 
reproducibility.

Detached code signatures are generated for Apple/Windows builds which are checked into 
`git`; this is so others wishing to verify reproducibility of the compiled output they 
are running (or providing to their users) can do so.

You can view the `help` output of `task.sh` by running `./external/task.sh` from the 
project's root directory.

```
$ git clone https://github.com/05nelsonm/kmp-tor-resource.git
$ cd kmp-tor-resource
$ ./external/task.sh
```

<details>
    <summary>Task Help Menu</summary>

![image](assets/task.sh.png)

</details>

### Packaging

The compiled output from `task.sh`'s `build` tasks are "packaged" for the given platforms and 
moved to their designated package module's resource directories (e.g. 
`external/build/package/resource-lib-tor/src/jvmMain/resources`).

Running `./external/task.sh package:all` after a `build` task will do the following.

- Android:
    - Move compilations to their respective `external/build/package/resource-{name}/src/androidMain/jniLibs/{ABI}` 
      directories.
- Jvm:
    - Apply detached code signatures (`macOS`, `Windows` only) via [diff-cli][path-diff-cli]
    - Gzip all compilations.
    - Move compilations to their respective `external/build/package/resource-{name}/src/jvmMain/resource` 
      directories.
- Native:
    - Apply detached code signatures (`macOS`, `iOS Simulator`, `Windows` only) via [diff-cli][path-diff-cli].
    - Gzip all compilations.
    - Convert compilations to `NativeResource` via [resource-cli][path-resource-cli].
    - Move Apple Framework compilations to `external/build/package/resource-frameworks-gradle-plugin/src/jvmMain/resources`
      directories.

After "packaging" all resources, an additional step for `Node.js` is performed. `geoip`, `geoip6`, and all 
compilations are published to `Npmjs` via the [npmjs][path-npmjs] module.

[path-diff-cli]: ../tools/diff-cli/README.md
[path-resource-cli]: ../tools/resource-cli/README.md
[path-npmjs]: ../library/npmjs/README.md
