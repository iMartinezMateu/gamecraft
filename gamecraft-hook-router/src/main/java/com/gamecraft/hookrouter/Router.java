package com.gamecraft.hookrouter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import static spark.Spark.port;
import static spark.Spark.post;

public class Router {
        public static void main(String[] args) {
            port(9999);

            post("/execute/:id", ((request, response) -> {
                HttpClient client = HttpClientBuilder.create().build();
                HttpPost post;
                StringEntity entity = null;
                post = new HttpPost("http://0.0.0.0:8080/gamecraftpipelinemanager/api/pipelines/" + request.params(":id") + "/execute");
                post.setHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUzNzM5MDI3M30.O3d9sf1YPIcLtkH0h20_fZwcD5HS6qQVGcbWaUt0zRBEqaSzdcaxHJtcFbrvsKUrjXVaqVJIo0XH-GXFMc9YOA");
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                client.execute(post);
                return "Done";
            }));
    }
}
