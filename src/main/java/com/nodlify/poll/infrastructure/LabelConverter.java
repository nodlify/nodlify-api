package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Label;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class LabelConverter implements AttributeConverter<Label, String> {

    @Override
    public String convertToDatabaseColumn(Label attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Label convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Label.of(dbData);
    }
}
