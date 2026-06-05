package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.domain.Action;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
class ActionAttributeConverter implements AttributeConverter<Action, String> {

    @Override
    public String convertToDatabaseColumn(Action attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public Action convertToEntityAttribute(String dbData) {
        return Action.ofNullable(dbData);
    }
}
