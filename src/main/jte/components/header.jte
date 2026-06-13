@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*

<header class="home-nav"
        x-data="{
            displayName: '',
            open: false,
            get initials() {
                const value = this.displayName.trim();
                if (!value) return '?';
                const parts = value.split(/\s+/);
                if (parts.length === 1) {
                    return parts[0].slice(0, 2).toUpperCase();
                }
                return parts.slice(0, 2)
                    .map(part => part.charAt(0).toUpperCase())
                    .join('');
            },
            async loadUserInfo() {
                try {
                    const response = await fetch('/api/v1/user', {
                        headers: { 'Accept': 'application/json' },
                        credentials: 'same-origin'
                    });
                    if (!response.ok) return;

                    const user = await response.json();
                    this.displayName = user.displayName || '';
                } catch (_) {
                    this.displayName = '';
                }
            }
        }"
        x-init="loadUserInfo()"
        @keydown.escape.window="open = false">

    <div class="wrap home-nav-inner">
        <a href="/"><span class="logo">nodlify<span class="dot">.</span></span></a>
        <nav class="home-nav-links">
            <a href="/home"
               :class="{ active: window.location.pathname.startsWith('/home') }">${localize("nav.myPolls")}</a>
        </nav>
        <div class="home-nav-right">
            <input class="search-input" type="search" placeholder="${message("nav.search")}"/>

            <div class="avatar-menu-wrap" @click.outside="open = false">
                <button class="avatar-button"
                        type="button"
                        aria-label="Profile"
                        :aria-expanded="open.toString()"
                        aria-haspopup="menu"
                        :title="displayName || 'Profile'"
                        @click="open = !open"
                        x-text="initials">?
                </button>

                <div class="avatar-menu"
                     x-show="open"
                     x-transition.origin.top.right
                     x-cloak
                     role="menu">
                    <div class="avatar-menu-name" x-text="displayName || 'Profile'"></div>
                    <a href="/settings"
                       class="avatar-menu-action"
                       role="menuitem">
                        ${localize("nav.settings")}
                    </a>

                    <form action="/logout" method="post" x-data="{ csrfToken: getCookie('XSRF-TOKEN') }">
                        <input type="hidden" name="_csrf" x-bind:value="csrfToken">
                        <button type="submit" class="avatar-menu-action" role="menuitem">
                            Log out
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</header>
