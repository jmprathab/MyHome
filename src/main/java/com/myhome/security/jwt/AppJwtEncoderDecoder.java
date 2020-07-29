package com.myhome.security.jwt;

public interface AppJwtEncoderDecoder {
  AppJwt decode(String encodedJwt, String secret);

  String encode(AppJwt jwt, String secret);
}
