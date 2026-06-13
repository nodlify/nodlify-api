@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*

<footer class="footer" x-data="{}">
    <div class="wrap">
        <div class="footer-grid">
            <div class="footer-brand">
                <span class="logo">nodlify<span class="dot">.</span></span>
                <p class="small mt-1.5">${localize("footer.tagline")}</p>
            </div>
            <div class="footer-col">
                <h4>${localize("footer.developers")}</h4>
                <ul>
                    <li><a href="#">${localize("footer.github")}</a></li>
                    <li><a href="#">${localize("footer.documentation")}</a></li>
                    <li><a href="#">${localize("footer.selfHosted")}</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>${localize("footer.legal")}</h4>
                <ul>
                    <li><a href="#">${localize("footer.privacy")}</a></li>
                    <li><a href="#">${localize("footer.cookies")}</a></li>
                    <li><a href="#">${localize("footer.terms")}</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>${localize("footer.language")}</h4>
                <select class="lang-dropdown" aria-label="Language"
                        @change="const params = new URLSearchParams(window.location.search); params.set('lang', $event.target.value); window.location.search = params.toString();">
                    <option value="en" selected="${language() == "en"}">English</option>
                    <option value="pl" selected="${language() == "pl"}">Polski</option>
                </select>
            </div>
        </div>
        <div class="footer-bottom">
            <span>${localize("footer.copyright", currentYear())}</span>
            <span></span>
        </div>
    </div>
</footer>
