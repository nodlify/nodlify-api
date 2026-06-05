package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Title;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class TitleConverter implements AttributeConverter<Title, String> {

    @Override
    public String convertToDatabaseColumn(Title attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Title convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Title.of(dbData);
    }
}
