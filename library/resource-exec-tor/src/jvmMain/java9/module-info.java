module io.matthewnelson.kmp.tor.resource.exec.tor {
    requires transitive io.matthewnelson.kmp.tor.common.api;
    requires io.matthewnelson.kmp.tor.common.core;
    requires io.matthewnelson.kmp.tor.resource.geoip;
    requires io.matthewnelson.kmp.tor.resource.lib.tor;

    exports io.matthewnelson.kmp.tor.resource.exec.tor;
}
