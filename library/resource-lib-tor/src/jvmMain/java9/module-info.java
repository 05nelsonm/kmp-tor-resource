module io.matthewnelson.kmp.tor.resource.lib.tor {
    requires io.matthewnelson.kmp.tor.common.api;
    requires io.matthewnelson.kmp.tor.common.core;

    opens io.matthewnelson.kmp.tor.resource.lib.tor to io.matthewnelson.kmp.tor.common.core;
    exports io.matthewnelson.kmp.tor.resource.lib.tor;
}
