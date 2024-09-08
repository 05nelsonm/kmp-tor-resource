# resource-shared-tor

**NOTE:** This module is not meant for public consumtion. It is a transitive dependency
utilized by other `:library:resource-` modules.

Android/Jvm `tor` binaries are compiled to work with both the `exec-tor` and `statik-tor` 
dependencies. This module is strictly for ensuring the resources live in a single aar/jar 
for de-duplication purposes.
