@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*

@template.layout.main(
title = message("index.pageTitle"),
header = @`
    <header class="nav"
            x-data="{ scrolled: false }"
            x-init="window.addEventListener('scroll', () => scrolled = window.scrollY > 12, { passive: true })"
            :class="{ scrolled: scrolled }">
        <div class="wrap nav-inner">
            <a href="/"><span class="logo">nodlify<span class="dot">.</span></span></a>
            <div class="nav-cta">
                <a href="/login" class="small text-[var(--muted)]">${localize("register.signIn")}</a>
                <a href="/create-poll" class="btn btn-sm"><span>${localize("index.createPoll")}</span>
                    <span class="arrow">→</span>
                </a>
            </div>
        </div>
    </header>
`,
content = @`
    <main>
        <section id="top" class="hero">
            <div class="wrap">
                <div class="hero-grid">
                    <div class="hero-left">
                        <span class="eyebrow">${localize("index.hero.eyebrow")}</span>
                        <h1 class="display hero-headline">
                            <span>${localize("index.hero.line1")}</span>
                            <%-- TODO: remove br? --%>
                            <br/>
                            <span class="inline-block">${localize("index.hero.line2")}</span>
                            <span class="accent">${localize("index.hero.everyone")}</span>.
                        </h1>
                        <p class="lede">
                            ${localize("index.hero.lede")}
                        </p>
                        <div class="hero-cta flex-col items-start gap-2">
                            <a href="/create-poll" class="btn">
                                <span>${localize("index.hero.cta")}</span>
                                <span class="arrow">→</span>
                            </a>
                            <span class="small text-[12.5px] text-[var(--muted)]">
                                ${localize("index.hero.ctaNote")}
                            </span>
                        </div>
                    </div>

                    <div class="hero-right justify-end items-center">
                        <div class="poll-card floating w-full">
                            <div class="poll-head">
                                <h3 class="poll-title">Team dinner — November</h3>
                                <div class="poll-meta-row">
                                    <span>
                                        8 <span>${localize("index.demo.invited")}</span>
                                    </span>
                                    <span class="dot-sep"></span>
                                    <span>
                                        4 <span>${localize("home.options")}</span>
                                    </span>
                                    <span class="dot-sep"></span>
                                    <span class="mono text-[11px]">nodlify.com/p/4hT9k</span>
                                </div>
                            </div>
                            <div class="poll-grid">
                                <div class="poll-grid-header [grid-template-columns:1.3fr_1fr_1fr_1fr_1fr]">
                                    <div class="poll-cell-name header">
                                        ${localize("index.demo.who")}
                                    </div>
                                    <div class="poll-cell-opt">
                                        <div class="opt-date">Nov 18</div>
                                        <div class="opt-time">18:00</div>
                                    </div>
                                    <div class="poll-cell-opt">
                                        <div class="opt-date">Nov 19</div>
                                        <div class="opt-time">18:30</div>
                                    </div>
                                    <div class="poll-cell-opt">
                                        <div class="opt-date">Nov 20</div>
                                        <div class="opt-time">19:00</div>
                                    </div>
                                    <div class="poll-cell-opt">
                                        <div class="opt-date">Nov 21</div>
                                        <div class="opt-time">18:00</div>
                                    </div>
                                </div>
                                <div class="poll-grid-row [grid-template-columns:1.3fr_1fr_1fr_1fr_1fr]">
                                    <div class="poll-cell-name"><span
                                                class="avatar">AK</span><span>Ania</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip maybe">?</span></div>
                                </div>
                                <div class="poll-grid-row [grid-template-columns:1.3fr_1fr_1fr_1fr_1fr]">
                                    <div class="poll-cell-name"><span
                                                class="avatar">MD</span><span>Marek</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip no">✕</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                </div>
                                <div class="poll-grid-row [grid-template-columns:1.3fr_1fr_1fr_1fr_1fr]">
                                    <div class="poll-cell-name"><span
                                                class="avatar">TL</span><span>Tomek</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip maybe">?</span></div>
                                    <div class="poll-cell-vote best"><span class="vote-pip yes">✓</span></div>
                                    <div class="poll-cell-vote"><span class="vote-pip yes">✓</span></div>
                                </div>
                            </div>
                            <div class="poll-foot">
                                <div class="totals">
                                    <span>
                                        <b>14</b> <span>${localize("index.demo.yes")}</span>
                                    </span>
                                    <span>
                                        <b>3</b> <span>${localize("index.demo.maybe")}</span>
                                    </span>
                                    <span>
                                        <b>3</b> <span>${localize("index.demo.no")}</span>
                                    </span>
                                </div>
                                <span class="mono text-[11px] text-[var(--muted)]">
                                    63% <span>${localize("index.demo.responded")}</span>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="pricing">
            <div class="wrap">
                <div class="section-head">
                    <span class="eyebrow">
                        ${localize("iindex.pricing.eyebrow")}
                    </span>
                    <h2 class="h2">
                        <span>${localize("index.pricing.headingPre")}</span>
                        <i>Pro</i>
                        <span>${localize("index.pricing.headingPost")}</span>
                    </h2>
                </div>
                <div class="pricing-grid">
                    <div class="price-card">
                        <div>
                            <div class="name">${localize("index.price.free")}</div>
                            <h3 class="amount">€0<span class="per"> ${localize("index.price.free.per")}</span></h3>
                        </div>
                        <p class="summary">
                            ${localize("index.price.free.summary")}
                        </p>
                        <ul>
                            <li>${localize("index.price.free.f1")}</li>
                            <li>${localize("index.price.free.f2")}</li>
                            <li>${localize("index.price.free.f3")}</li>
                            <li>${localize("index.price.free.f4")}</li>
                            <li>${localize("index.price.free.f5")}</li>
                        </ul>
                        <div class="price-cta">
                            <a href="/create-poll" class="btn btn-ghost">
                                <span>
                                    ${localize("index.price.free.cta")}
                                </span>
                                <span class="arrow"> →</span>
                            </a>
                        </div>
                    </div>
                    <div class="price-card featured">
                        <span class="tag">
                            ${localize("index.price.tag")}
                        </span>
                        <div>
                            <div class="name">Pro</div>
                            <h3 class="amount">€6
                                <span class="per">
                                    ${localize("index.price.pro.per")}
                                </span>
                            </h3>
                        </div>
                        <p class="summary">
                            ${localize("index.price.pro.summary")}
                        </p>
                        <ul>
                            <li>${localize("index.price.pro.f1")}</li>
                            <li>${localize("index.price.pro.f2")}</li>
                            <li>${localize("index.price.pro.f3")}</li>
                            <li>${localize("index.price.pro.f4")}</li>
                            <li>${localize("index.price.pro.f5")}</li>
                            <li>${localize("index.price.pro.f6")}</li>
                        </ul>
                        <div class="price-cta">
                            <a href="/register" class="btn">
                                <span>
                                    ${localize("index.price.pro.cta")}
                                </span>
                                <span class="arrow"> →</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="cta-block">
            <div class="wrap">
                <span class="eyebrow mb-6 inline-flex">
                    ${localize("index.cta.eyebrow")}
                </span>
                <h2 class="h2 mt-4 text-[length:var(--hero-size)]">
                    <span>
                        ${localize("index.cta.headingPre")}
                    </span><br/>
                    <i class="text-[var(--yes)]">${localize("index.cta.headingAccent")}</i>
                </h2>
                <p class="lede mt-6">
                    ${localize("index.cta.lede")}
                </p>
                <div class="mt-8 flex flex-wrap justify-center gap-3">
                    <a href="/create-poll" class="btn">
                        <span>${localize("index.createPoll")}</span>
                        <span class="arrow"> →</span>
                    </a>
                    <a href="#" class="btn btn-ghost">${localize("index.cta.docs")}</a>
                </div>
                <p class="small mt-6">${localize("index.cta.foot")}</p>
            </div>
        </section>
    </main>
`,
footer = @`
    @template.components.footer()
`
)
