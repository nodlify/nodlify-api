package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.DomainUrl;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class DomainUrlAttributeConverter implements AttributeConverter<DomainUrl, String> {

    @Override
    public String convertToDatabaseColumn(DomainUrl domainUrl) {
        return domainUrl == null ? null : domainUrl.getValue();
    }

    @Override
    public DomainUrl convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isBlank() ? null : DomainUrl.of(dbData);
    }
}

