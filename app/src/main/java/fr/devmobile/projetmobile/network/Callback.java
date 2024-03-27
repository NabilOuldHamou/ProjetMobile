package fr.devmobile.projetmobile.network;

public interface Callback {
    void onResponse(Object data);
    void onError(Object data);
}
