package com.example.AlbumVerse.utils.constants;

public enum Authority {
    READ,
    WRITE,
    UPDATE,
    USER,//can update  delete self objects and read anything
    ADMIN//can manage all objects

}
