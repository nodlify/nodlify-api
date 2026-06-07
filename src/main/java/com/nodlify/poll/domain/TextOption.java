package com.nodlify.poll.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "text_options")
@NoArgsConstructor(access = PROTECTED)
public class TextOption extends Option {

    private Label label;

    private TextOption(Label label) {
        this.label = label;
    }

    public static TextOption of(Label label) {
        return new TextOption(label);
    }

    public static TextOption of(String label) {
        return new TextOption(Label.of(label));
    }
}
