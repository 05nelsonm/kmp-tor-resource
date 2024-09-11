module io.matthewnelson.kmp.tor.resource.statik.tor {
    requires transitive io.matthewnelson.kmp.tor.common.api;
    requires io.matthewnelson.kmp.tor.common.core;
    requires io.matthewnelson.kmp.tor.resource.shared.geoip;
    requires io.matthewnelson.kmp.tor.resource.shared.tor;

    exports io.matthewnelson.kmp.tor.resource.statik.tor;
}
