package com.atlygin.atlassiandataloader;

import com.atlygin.atlassiandataloader.PageDto.Ancestor;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.unicode;
import static io.restassured.RestAssured.given;

public class Loader {
    private static final Map<String, String> createdUsers = new HashMap<String, String>();
    private static int SPACES = 100, PAGES_PER_LEVEL = 10, HIERARCHY_DEPTH = 20, ATTACHMENTS_PER_PAGE = 10, COMMENTS_PER_PAGE = 100;
    @BeforeClass
    public static void initRestAssured() {
        RestAssured.baseURI = "http://conf.docker1.almworks.com";
        RestAssured.authentication = RestAssured.preemptive().basic("admin", "admin");
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType("application/json").setAccept("application/json")
//                .addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter())
                .build();
    }

    @Test public void createUsers() throws SQLException {
        String user = english(5, 10), password = alphanumeric(10);
        createdUsers.put(user, password);
        given()
//                .header("JSESSIONID", "827AC2503374BA177BE438AFBE9ADA62")
//                .header("seraph.confluence", "491521%3A81536419fcc0b2179cc306120366220016974cce")
                .queryParam("atl_token", "28fc7c16e4cfec64af99d80450cf974912156fb1")
                .queryParam("username", user)
                .queryParam("fullName", english(5, 10))
                .queryParam("email", english(5, 10) + "@blah.com")
                .queryParam("password", password)
                .queryParam("confirm", password)
                .post("/admin/users/docreateuser.action")
                .then().assertThat().statusCode(200);
    }

    @Test public void createSpacesAndPages() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        long started = System.currentTimeMillis();
        for(int spaceIdx = 0; spaceIdx < SPACES; spaceIdx++) {
            final int spaceIndex = spaceIdx;
            pool.submit(new Runnable() {
                public void run() {
                    SpaceDto space = createSpace();
                    System.out.println("Creating space " + space.name);
                    PageDto lastCreatedPage = null;
                    for(int levelIdx = 0; levelIdx < HIERARCHY_DEPTH; levelIdx++) {
                        System.out.println("Creating level #" + levelIdx);
                        Ancestor ancestor = lastCreatedPage == null ? null : new Ancestor(lastCreatedPage.id);
                        for(int pageIdx = 0; pageIdx < PAGES_PER_LEVEL; pageIdx++) {
                            System.out.println("Creating page #" + pageIdx + " in space #" + spaceIndex);
                            lastCreatedPage = createPage(space, ancestor);
                            for(int attachIdx = 0; attachIdx < ATTACHMENTS_PER_PAGE; attachIdx++)
                                createAttachment(lastCreatedPage);
                            for(int commentIdx = 0; commentIdx < COMMENTS_PER_PAGE; commentIdx++)
                                createComment(lastCreatedPage);
                        }
                    }
                }

            });
        }
        pool.awaitTermination(2, TimeUnit.DAYS);
        System.out.println("Took: " + (System.currentTimeMillis() - started) / 1000 / 60 + " min");
    }

    private void createAttachment(PageDto lastCreatedPage) {
        given()
                .multiPart("file", alphanumeric(10)+".jpg", getClass().getResourceAsStream("/cat.jpg"))
                .header("X-Atlassian-Token", "no-check")
                .contentType("multipart/form-data")
                .post("/rest/api/content/{contentId}/child/attachment", lastCreatedPage.id)
                .then().assertThat().statusCode(200);
    }
    private void createComment(PageDto page) {
        given().body(Comment.random(page))
                .post("/rest/api/content")
                .then().assertThat().statusCode(200);
    }

    private PageDto createPage(SpaceDto space, Ancestor ancestor) {
        PageDto page = PageDto.randomPage(space);
        if(ancestor != null)
            page.ancestors.add(ancestor);
        return given().body(page)
                .post("/rest/api/content")
                .thenReturn().as(PageDto.class);
    }

    private SpaceDto createSpace() {
        SpaceDto space = SpaceDto.random();
        given().body(space)
                .post("/rest/api/space")
                .then().assertThat().statusCode(200);
        return space;
    }
}
