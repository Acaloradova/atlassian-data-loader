package com.atlygin.atlassiandataloader;

import static io.qala.datagen.RandomShortApi.alphanumeric;

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

    static class Description {
        Plain plain;
        static Description random() {
            Description description = new Description();
            description.plain = Plain.random();
            return description;
        }
    }
    static class Plain {
        String value, representation = "plain";

        static Plain random() {
            Plain plain = new Plain();
            plain.value = alphanumeric(10, 100);
            return plain;
        }
    }
}
