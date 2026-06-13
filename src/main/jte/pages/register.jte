@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*
@template.layout.main(
title = message("register.eyebrow"),
content = @`
    <%--  TODO: make better password strength check
    --%>
    <div class="auth-page"
         x-data="{
            showPassword: false,
            name: '',
            email: '',
            password: '',
            agree: false,
            get strength() {
             if (this.password.length === 0) return 0;
             if (this.password.length < 6)  return 1;
             if (this.password.length < 10) return 2;
             return 3;
            },
            get strengthLabel() { return ['', $store.i18n.message('register.strength.weak'), $store.i18n.message('register.strength.okay'), $store.i18n.message('register.strength.strong')][this.strength]; },
            get canSubmit() { return this.agree && this.name.trim().length >= 2 && this.password.length >= 8 && this.email.length > 0; }
        }">

        <div class="auth-form-side">
            <div class="auth-top">
                <a href="/"><span class="logo">nodlify<span class="dot">.</span></span></a>
                <span class="small">
                    <span>${localize("register.haveAccount")}</span>
                    <a href="/login">${localize("register.signIn")}</a>
                </span>
            </div>

            <div class="auth-form-wrap">
                <div class="auth-head">
                    <span class="eyebrow">${localize("register.eyebrow")}</span>
                    <h1>${localize("register.heading")}</h1>
                    <p>${localize("register.subtitle")}</p>
                </div>

                <form class="auth-form"
                      action="/register"
                      method="post"
                      @submit.prevent="canSubmit && $el.submit()">
<%--                    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>--%>

                    <div class="field">
                        <label for="name">${localize("register.name")}</label>
                        <input id="name" type="text" name="displayName"
                               placeholder="Sofia Reyes"
                               x-model="name"
                               autocomplete="name"
                               minlength="2"
                               maxlength="50"
                               required
                               autofocus/>
                    </div>

                    <div class="field">
                        <label for="email">${localize("register.email")}</label>
                        <input id="email" type="email" name="email"
                               placeholder="you@company.com"
                               x-model="email"
                               autocomplete="email"/>
                        <span class="hint">${localize("register.emailHint")}</span>
                    </div>

                    <div class="field">
                        <label for="password">
                            <span>${localize("register.password")}</span>
                            <span x-show="password.length > 0"
                                  class="font-mono text-[11px] uppercase tracking-[0.04em]"
                                  :class="{ 'text-[var(--no)]': strength === 1, 'text-[var(--maybe)]': strength === 2, 'text-[var(--yes)]': strength === 3 }"
                                  x-text="strengthLabel">
                            </span>
                        </label>
                        <div class="password-wrap">
                            <input id="password" :type="showPassword ? 'text' : 'password'" name="password"
                                   placeholder="${message("register.passwordPlaceholder")}"
                                   x-model="password"
                                   autocomplete="new-password"/>
                            <button type="button"
                                    class="toggle-pw"
                                    @click="showPassword = !showPassword"
                                    x-text="showPassword ? $store.i18n.message('common.hide') : $store.i18n.message('common.show')">
                            </button>
                        </div>
                        <div class="mt-1 flex gap-[3px]">
                            <template x-for="i in [1,2,3]" :key="i">
                        <span class="h-[3px] flex-1 rounded-xs transition-colors"
                              :class="strength >= i ? { 1: 'bg-[var(--no)]', 2: 'bg-[var(--maybe)]', 3: 'bg-[var(--yes)]' }[strength] : 'bg-[var(--line-2)]'"></span>
                            </template>
                        </div>
                    </div>

                    <label class="checkbox-row">
                        <input type="checkbox" x-model="agree" name="agree"/>
                        <span><span th:text="#{register.agreePre}">I agree to the</span>
                        <a href="#" class="border-b-[0.5px] border-[var(--line)] text-[var(--ink)]"
                           th:text="#{register.terms}">terms</a>
                        <span th:text="#{register.and}">and</span>
                        <a href="#" class="border-b-[0.5px] border-[var(--line)] text-[var(--ink)]"
                           th:text="#{register.privacyPolicy}">privacy policy</a>
                    </span>
                    </label>

                    <button type="submit"
                            class="btn auth-submit"
                            :disabled="!canSubmit"
                            :class="{ 'cursor-not-allowed opacity-40': !canSubmit }">
                        <span th:text="#{register.submit}">Create account</span> <span class="arrow">→</span>
                    </button>

                    <div class="auth-divider" th:text="#{common.continueWith}">or continue with</div>
                    <div class="grid grid-cols-2 gap-2.5">
                        <a href="/oauth2/authorization/google" class="oauth-btn">
                            <img src="/icons/google.svg" width="16" height="16" alt="" aria-hidden="true">
                            Google
                        </a>

                        <a href="/oauth2/authorization/github" class="oauth-btn">
                            <img src="/icons/github.svg" width="16" height="16" alt="" aria-hidden="true">
                            GitHub
                        </a>
                    </div>
                </form>

                <p class="auth-foot">${localize("register.foot")}</p>
            </div>
            <div class="auth-bottom">
                <%--  TODO: update font to something more readable
                --%>
                <span>${localize("footer.copyrightShort", currentYear())}</span>
                <span class="links">
                    <a href="#">${localize("common.privacy")}</a>
                    <a href="#">${localize("common.terms")}</a>
                    <a href="#">${localize("common.status")}</a>
                </span>
            </div>
        </div>
    </div>
`,
)
