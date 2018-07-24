package com.atlygin.atlassiandataloader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static io.qala.datagen.RandomShortApi.alphanumeric;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
class Plain {
    String value, representation = "plain";

    static Plain random() {
        Plain plain = new Plain();
        plain.value = alphanumeric(10, 100);
        return plain;
    }
}