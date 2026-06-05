package com.nodlify.iam.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;


@Data
@Entity
@Table(name = "config")
@NoArgsConstructor
public class ApplicationSetup {

    public static final String APP_CONFIG = "app_config";

    @Id
    private String id = APP_CONFIG;

    private DomainUrl domainUrl;

    @Enumerated(STRING)
    @Column(nullable = false)
    private SetupStatus setupStatus = SetupStatus.PENDING;

    public void completeSetup() {
        this.setupStatus = SetupStatus.COMPLETED;
    }

    public boolean isSetupCompleted() {
        return this.setupStatus == SetupStatus.COMPLETED;
    }

    enum SetupStatus {
        PENDING,
        COMPLETED
    }
}
