@import gg.jte.Content
@import static com.nodlify.bootstrap.infrastructure.config.JteContext.*

@param String title
@param Content content

@param Content header = null
@param Content footer = null
@param Content script = null

<!doctype html>
<html lang="${language()}">
<head x-data="{ lang: '${language()}' }" x-init="$store.i18n.load(lang)">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <meta name="description"
          content="Group scheduling, with consent. Propose times, collect yes/no/maybe in one tap, lock the best slot. Free, open source, no ads."/>
    <script>
        function getCsrfCookie() {
            const cookieName = "XSRF-TOKEN";
            const cookie = document.cookie.split("; ")
                    .find(value => value.startsWith(cookieName));
            return cookie
                    ? cookie.substring(cookieName.length + 1)
                    : '';
        }
        document.addEventListener("alpine:init", () => {
            Alpine.store("csrf", {
                token: getCsrfCookie()
            });
            Alpine.store('i18n', {
                messages: {},
                message(key) {
                    return this.messages[key] ?? key;
                },
                load(language) {
                    fetch("/i18n/" + language + ".json")
                            .then(response => response.ok ? response.json() : {})
                            .then(messages => {
                                this.messages = messages;
                            })
                            .catch(() => {
                            });
                }
            });
        });
    </script>
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3/dist/cdn.min.js"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Geist:wght@400;500;600;700&family=Geist+Mono:wght@400;500;600&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="/css/app.css"/>
</head>
<body>

@if(header != null)
    ${header}
@endif

${content}

@if(footer != null)
    ${footer}
@endif

@if(script != null)
    ${script}
@endif
<script>

</script>
</body>
</html>
