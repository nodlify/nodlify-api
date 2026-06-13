@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*
<footer class="footer-min" x-data="{}">
    <div class="wrap footer-min-inner">
        <nav class="footer-min-links">
            <a href="#">${localize("footer.privacy")}</a>
            <a href="#">${localize("footer.cookies")}</a>
            <a href="#">${localize("footer.terms")}</a>
        </nav>
        <div class="footer-min-right">
            <span class="footer-min-meta">${message("footer.copyrightShort", currentYear())}</span>
            <select class="lang-dropdown footer-min-lang" aria-label="Language"
                    @change="const params = new URLSearchParams(window.location.search); params.set('lang', $event.target.value); window.location.search = params.toString();">
                <option value="en" selected="${language().equalsIgnoreCase("en")}">English</option>
                <option value="pl" selected="${language().equalsIgnoreCase("pl")}">Polski</option>
            </select>
        </div>
    </div>
</footer>
