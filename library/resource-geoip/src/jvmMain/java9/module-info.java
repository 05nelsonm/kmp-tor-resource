module io.matthewnelson.kmp.tor.resource.geoip {
    requires io.matthewnelson.kmp.tor.common.api;
    requires io.matthewnelson.kmp.tor.common.core;

    opens io.matthewnelson.kmp.tor.resource.geoip to io.matthewnelson.kmp.tor.common.core;
    exports io.matthewnelson.kmp.tor.resource.geoip;
}
