module io.matthewnelson.kmp.tor.resource.noexec.tor {
    requires transitive io.matthewnelson.kmp.tor.common.api;
    requires io.matthewnelson.kmp.tor.common.core;
    requires io.matthewnelson.kmp.tor.resource.geoip;
    requires io.matthewnelson.kmp.tor.resource.lib.tor;

    opens io.matthewnelson.kmp.tor.resource.noexec.tor to io.matthewnelson.kmp.tor.common.core;
    exports io.matthewnelson.kmp.tor.resource.noexec.tor;
}
