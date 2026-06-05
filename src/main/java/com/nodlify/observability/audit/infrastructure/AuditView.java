package com.nodlify.observability.audit.infrastructure;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class AuditView {

    @GetMapping("/observability/audit")
    String auditPage() {
        return "audit";
    }
}
