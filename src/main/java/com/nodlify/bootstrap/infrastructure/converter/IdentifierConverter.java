package com.nodlify.bootstrap.infrastructure.converter;

import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class IdentifierConverter implements AttributeConverter<Identifier, String> {

    @Override
    public String convertToDatabaseColumn(Identifier identifier) {
        return identifier == null ? null : identifier.getValue();
    }

    @Override
    public Identifier convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Identifier.of(dbData);
    }
}
