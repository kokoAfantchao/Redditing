package com.push.apiwrapper.oauth;

/*
  Enumerator for the duration of tokens
  Have to option
  -Permanent : the token can be reflesh as many time the app deserve
  -Temporary : the token can't be reflesh and last for 60 minutes
*/

public enum Duration {
    PERMANENT("permanent"),
    TEMPORARY("temporary");

    private final String value;

    Duration(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
