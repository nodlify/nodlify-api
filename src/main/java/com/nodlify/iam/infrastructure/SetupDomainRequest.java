package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.DomainUrl;

record SetupDomainRequest(
        String domain
) {

    public DomainUrl toDomainUrl() {
        return DomainUrl.of(domain);
    }
}
