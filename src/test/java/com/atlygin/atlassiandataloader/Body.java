package com.atlygin.atlassiandataloader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static io.qala.datagen.RandomValue.between;
import static io.qala.datagen.StringModifier.Impls.spaces;


@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
class Body {
    Plain storage;

    static Body random() {
        Body body = new Body();
        body.storage = Plain.random();
        body.storage.representation = "storage";
        body.storage.value = between(10, 200).with(spaces()).alphanumeric();
        return body;
    }
}