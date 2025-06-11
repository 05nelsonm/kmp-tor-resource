# resource-compilation-lib-tor

Contains **only** the `libtor.so` compilations for Android, packaged in `jniLibs`. This is such 
that Android Native consumers can add to their application so it is available at runtime. Otherwise, 
this is a transitive dependency and should not be directly consumed.
