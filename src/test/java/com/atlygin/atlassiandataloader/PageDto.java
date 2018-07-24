package com.atlygin.atlassiandataloader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static io.qala.datagen.RandomShortApi.alphanumeric;

@SuppressWarnings({"WeakerAccess", "unused"})
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
class PageDto {
    String id;
    String type = "page", title = alphanumeric(10, 20);
    List<Ancestor> ancestors = new ArrayList<Ancestor>();
    Space space;
    Body body;

    static PageDto randomPage(SpaceDto space) {
        PageDto result = new PageDto();
        Space spaceKey = new Space();
        spaceKey.key = space.key;
        result.type = "page";
        result.space = spaceKey;
        result.body = Body.random();
        return result;
    }

    @JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Space {
        String key;
    }
    @JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Ancestor {
        long id;
        Ancestor(){}
        Ancestor(String id) {
            this.id = Long.parseLong(id);
        }
    }
}
