package com.nodlify.bootstrap.infrastructure.converter;

import com.nodlify.shared.domain.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Converter(autoApply = true)
@RequiredArgsConstructor
class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null
                ? Email.EMPTY
                : Email.of(dbData);
    }
}
