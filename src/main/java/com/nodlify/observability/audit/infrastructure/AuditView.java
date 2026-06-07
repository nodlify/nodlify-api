package com.nodlify.observability.audit.infrastructure;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class AuditView {

    @GetMapping("/audit")
    String auditPage() {
        return "audit";
    }
}
