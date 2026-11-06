package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.request.*;
import model.result.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var request = buildRequest("POST", "/user", new UserData(username,password,email));
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var request = buildRequest("POST", "/session", new LoginRequest(username,password));
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String auth) throws Exception {
        var request = buildRequest("DELETE", "/session", null, auth);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void createGame(String name, String auth) throws Exception {
        var request = buildRequest("POST", "/game", new CreateGameRequest(name), auth);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public List<GameData> listGames(String auth) throws Exception {
        var request = buildRequest("GET", "/game", null, auth);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class).games();
    }

    public void joinGame(int gameID, String color, String auth) throws Exception {
        var request = buildRequest("PUT", "/game", new JoinGameRequest(color,gameID), auth);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        return buildRequest(method,path,body,null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String auth) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (auth != null) {
            request.setHeader("authorization", auth);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception(body);
            }

            throw new Exception("other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}