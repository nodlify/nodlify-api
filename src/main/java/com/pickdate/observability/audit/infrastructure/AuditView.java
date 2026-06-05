package com.pickdate.observability.audit.infrastructure;

import com.pickdate.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class AuditView {

    @GetMapping("/observability/audit")
    String auditPage() {
        return "audit";
    }
}
