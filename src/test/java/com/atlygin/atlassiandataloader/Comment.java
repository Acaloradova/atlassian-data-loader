package com.atlygin.atlassiandataloader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@SuppressWarnings({"WeakerAccess", "unused"})
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
public class Comment {
    String type = "comment";
    Body body;
    Container container;
    static Comment random(PageDto page) {
        Comment result = new Comment();
        result.body = Body.random();
        result.container = new Container(page);
        return result;
    }
    @JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE)
    static class Container {
        String type = "page";
        long id;
        Container(PageDto page) {
            this.id = Long.parseLong(page.id);
        }
    }
}
