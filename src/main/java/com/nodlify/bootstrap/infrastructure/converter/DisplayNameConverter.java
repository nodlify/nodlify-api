package com.nodlify.bootstrap.infrastructure.converter;

import com.nodlify.shared.domain.DisplayName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class DisplayNameConverter implements AttributeConverter<DisplayName, String> {

    @Override
    public String convertToDatabaseColumn(DisplayName displayName) {
        return displayName == null ? null : displayName.getValue();
    }

    @Override
    public DisplayName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DisplayName.of(dbData);
    }
}
