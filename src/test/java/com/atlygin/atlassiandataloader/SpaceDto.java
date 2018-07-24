package com.atlygin.atlassiandataloader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static io.qala.datagen.RandomShortApi.alphanumeric;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
class SpaceDto {
    String key, name;
    Description description;

    static SpaceDto random() {
        SpaceDto result = new SpaceDto();
        result.key = alphanumeric(10, 20);
        result.name = alphanumeric(1, 30);
        result.description = Description.random();
        return result;
    }
    @JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
    static class Description {
        Plain plain;
        static Description random() {
            Description description = new Description();
            description.plain = Plain.random();
            return description;
        }
    }

}
