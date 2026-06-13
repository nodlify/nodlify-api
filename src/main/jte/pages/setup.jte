@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*

@template.layout.main(
title = message("setup.eyebrow"),
content = @`
    <div class="auth-page"
         x-data="setupWizard()">

        <div class="auth-form-side">
            <div class="auth-top">
                <a href="/"><span class="logo">nodlify<span class="dot">.</span></span></a>
            </div>

            <div class="auth-form-wrap">
                <div class="auth-head">
                    <span class="eyebrow">${message("setup.eyebrow")}</span>
                    <h1>${message("setup.heading")}</h1>
                    <p>${message("setup.subtitle")}</p>
                </div>

                <div class="setup-steps">
                    <template x-for="i in 3" :key="i">
                        <div class="setup-step">
                            <div class="setup-step-dot"
                                 :class="{ 'setup-step-dot--done': step > i, 'setup-step-dot--active': step === i }">
                                <img x-show="step > i" src="/icons/check.svg" class="setup-step-icon" alt="">
                                <span x-show="step <= i" x-text="i"></span>
                            </div>
                            <div x-show="i < 3" class="setup-step-line"
                                 :class="{ 'setup-step-line--done': step > i }"></div>
                        </div>
                    </template>
                </div>

                <div x-show="error" x-cloak class="setup-error">
                    <span class="setup-error-message" x-text="error"></span>
                    <button type="button" class="setup-error-dismiss" @click="error = null">✕</button>
                </div>

                <div x-show="step === 1"
                     x-transition:enter="transition ease-out duration-200"
                     x-transition:enter-start="opacity-0 translate-x-4"
                     x-transition:enter-end="opacity-100 translate-x-0">
                    <p class="setup-step-label">${message("setup.step1")}</p>

                    <form @submit.prevent="setupDomain()" novalidate class="auth-form">
                        <div class="field">
                            <label for="domain">${message("setup.domain")}</label>
                            <input type="text" id="domain" x-model="domain" required autocomplete="off"
                                   placeholder='${message("setup.domainPlaceholder")}'/>
                            <span class="hint">${message("setup.domainHint")}</span>
                        </div>

                        <button type="submit" class="btn auth-submit" :disabled="loading || !domain.trim()">
                            <img x-show="loading" x-cloak src="/icons/spinner.svg" class="setup-spinner" alt=""
                                 aria-hidden="true">
                            <span x-text="loading ? $store.i18n.message('setup.saving') : $store.i18n.message('common.continue')">Continue</span>
                            <span class="arrow" x-show="!loading">→</span>
                        </button>
                    </form>
                </div>

                <div x-show="step === 2"
                     x-transition:enter="transition ease-out duration-200"
                     x-transition:enter-start="opacity-0 translate-x-4"
                     x-transition:enter-end="opacity-100 translate-x-0">
                    <p class="setup-step-label">${message("setup.step2")}</p>

                    <form @submit.prevent="setupAdmin()" novalidate class="auth-form">
                        <div class="field">
                            <label for="email">${message("setup.email")}</label>
                            <input type="email" id="email" x-model="email" required autocomplete="email" autofocus
                                   placeholder="admin@yourdomain.com"/>
                        </div>

                        <div class="field">
                            <label for="password">${message("setup.password")}</label>
                            <div class="password-wrap">
                                <input :type="showPassword ? 'text' : 'password'" id="password" x-model="password"
                                       required autocomplete="new-password" placeholder="••••••••"/>
                                <button type="button" class="toggle-pw" @click="showPassword = !showPassword"
                                        x-text="showPassword ? $store.i18n.message('common.hide') : $store.i18n.message('common.show')"></button>
                            </div>
                        </div>

                        <button type="submit" class="btn auth-submit"
                                :disabled="loading || !email.trim() || !password.trim()">
                            <img x-show="loading" x-cloak src="/icons/spinner.svg" class="setup-spinner" alt=""
                                 aria-hidden="true">
                            <span x-text="loading ? $store.i18n.message('setup.creating') : $store.i18n.message('common.continue')">Continue</span>
                            <span class="arrow" x-show="!loading">→</span>
                        </button>
                    </form>
                </div>

                <div x-show="step === 3"
                     x-transition:enter="transition ease-out duration-200"
                     x-transition:enter-start="opacity-0 translate-x-4"
                     x-transition:enter-end="opacity-100 translate-x-0">
                    <p class="setup-step-label">${message("setup.step3")}</p>

                    <div class="setup-summary">
                        <div class="setup-summary-row">
                            <span class="setup-summary-label">${message("setup.summaryDomain")}</span>
                            <span class="setup-summary-value" x-text="domain"></span>
                        </div>
                        <div class="setup-summary-row">
                            <span class="setup-summary-label">${message("setup.summaryAdmin")}</span>
                            <span class="setup-summary-value" x-text="email"></span>
                        </div>
                    </div>

                    <button @click="completeSetup()" class="btn auth-submit" :disabled="loading">
                        <img x-show="loading" x-cloak src="/icons/spinner.svg" class="setup-spinner" alt=""
                             aria-hidden="true">
                        <span x-text="loading ? $store.i18n.message('setup.finalising') : $store.i18n.message('setup.complete')">Complete setup</span>
                        <span class="arrow" x-show="!loading">→</span>
                    </button>
                </div>
            </div>

            <div class="auth-bottom">
                <span>${message("footer.copyrightShort", currentYear())}</span>

                <span class="links">
                    <a href="#">${localize("common.privacy")}</a>
                    <a href="#">${localize("common.terms")}</a>
                    <a href="#">${localize("common.status")}</a>
                </span>
            </div>
        </div>
    </div>
`,
script = @`
    <script>
        function setupWizard() {
            return {
                step: 1,
                loading: false,
                error: null,
                showPassword: false,
                domain: '',
                email: '',
                password: '',

                async setupDomain() {
                    this.loading = true;
                    this.error = null;
                    try {
                        const res = await fetch('/api/v1/setup/domain', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({domain: this.domain.trim()})
                        });
                        if (!res.ok) throw new Error(await res.text().catch(() => null) || this.$store.i18n.message('setup.errorDomain'));
                        this.step = 2;
                    } catch (e) {
                        this.error = e.message;
                    } finally {
                        this.loading = false;
                    }
                },

                async setupAdmin() {
                    this.loading = true;
                    this.error = null;
                    try {
                        const res = await fetch('/api/v1/setup/admin', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({email: this.email.trim(), password: this.password})
                        });
                        if (!res.ok) throw new Error(await res.text().catch(() => null) || this.$store.i18n.message('setup.errorAdmin'));
                        this.step = 3;
                    } catch (e) {
                        this.error = e.message;
                    } finally {
                        this.loading = false;
                    }
                },

                async completeSetup() {
                    this.loading = true;
                    this.error = null;
                    try {
                        const res = await fetch('/api/v1/setup', {method: 'POST'});
                        if (!res.ok) throw new Error(await res.text().catch(() => null) || this.$store.i18n.message('setup.errorComplete'));
                        window.location.href = '/login';
                    } catch (e) {
                        this.error = e.message;
                        this.loading = false;
                    }
                }
            };
        }
    </script>
`
)
