package com.pandora.api.service;

public interface StorageService {

    void store(String id, byte[] content);

    byte[] get(String id);

    void remove(String id);
}
