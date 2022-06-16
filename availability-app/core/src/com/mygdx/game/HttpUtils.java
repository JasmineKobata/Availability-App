package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Net.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HttpUtils {
    public static ServerResponse Request(String url, Map parameters) {
        ServerResponse response = new ServerResponse();
        HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
        String host = "availabilityapp.noip.me";
        httpGet.setUrl("http://" + host + "/" + url + "?" + HttpParametersUtils.convertHttpParameters(parameters));


        Gdx.net.sendHttpRequest(httpGet, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                response.statusCode = httpResponse.getStatus().getStatusCode();
                response.message = httpResponse.getResultAsString();
                Gdx.app.log("Status code ", "" + response.statusCode);
                Gdx.app.log("Result ", response.message);
            }

            @Override
            public void failed(Throwable t) {
                response.statusCode = 500;
                response.message = "Error: " + t.getMessage();
                t.printStackTrace();
                Gdx.app.log("Failed", t.getMessage());
            }

            @Override
            public void cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled");
            }
        });

        while (response.message.isEmpty()) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                break;
            }
        }

        return response;
    }
}
