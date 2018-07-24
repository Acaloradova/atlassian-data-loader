package com.atlygin.atlassiandataloader;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.jvm.hotspot.memory.Space;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Loader {
    private static final List<String> createdUsers = new ArrayList<String>();
    @BeforeClass
    public void initRestAssured() {
        RestAssured.baseURI = "/rest";
        RestAssured.preemptive().basic("", "");
    }

    @Test public void createUsers() throws SQLException {

    }

    @Test public void createSpaces() {
        given().body(SpaceDto.random())
                .post("space")
                .then().assertThat().statusCode(201);
    }
    @Test public void createPages() { }
}
