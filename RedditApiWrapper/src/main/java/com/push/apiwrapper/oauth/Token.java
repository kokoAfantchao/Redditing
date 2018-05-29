package com.push.apiwrapper.oauth;

public class Token {
    /** Token type parameter. */
    public static final String PARAM_TOKEN_TYPE = "token_type";

    /** Access token. */
    private String accessToken;

    /** Refresh token. */
    private String refreshToken;

    /** Manager of the scopes that this token applies to. */
    private String  scopes;

    /** Token type. Only value possible (15-06-2015): bearer */
    private String tokenType;

    /** Time at which the token expires (seconds since UNIX epoch). */
    private long expiration;

    /** How long the token was valid starting from its creation (in seconds). */
    private long expirationSpan;
}
