package fr.devmobile.projetmobile.network;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import fr.devmobile.projetmobile.models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppHttpClient {

    public final static String API_BASE_URL = "https://oxyjen.io/api";

    private final OkHttpClient client;

    private String token;

    public AppHttpClient() {
        this.client = new OkHttpClient();
    }

    public AppHttpClient(String token) {
        this.client = new OkHttpClient();
        this.token = token;
    }

    public CompletableFuture<Boolean> validateToken() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Headers headers = new Headers.Builder()
                .add("Authorization", token)
                .build();

        Request request = new Request.Builder()
                .url(API_BASE_URL + "/validate")
                .headers(headers)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                future.complete(response.isSuccessful());
            }
        });

        return future;
    }

    public CompletableFuture<String> sendPostRequest(String endpoint, String body) {

        CompletableFuture<String> future = new CompletableFuture<>();

        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .build();

        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(API_BASE_URL + endpoint)
                .headers(headers)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                future.complete(data);
            }
        });

        return future;
    }

}
