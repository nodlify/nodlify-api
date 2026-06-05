package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Description;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class DescriptionConverter implements AttributeConverter<Description, String> {

    @Override
    public String convertToDatabaseColumn(Description attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Description convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Description.of(dbData);
    }
}
