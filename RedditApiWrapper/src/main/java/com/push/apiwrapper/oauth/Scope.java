package com.push.apiwrapper.oauth;
/*
Enumerator for the possible authorization scopes for reddit API.
This scope determine what actions the app can perfom with the generated token


*/

public enum Scope {

    IDENTITY("identity"),
    EDIT("edit"),
    FLAIR("flair"),
    HISTORY("history"),
    MODCONFIG("modconfig"),
    MODFLAIR("modflair"),
    MODLOG("modlog"),
    MODPOSTS("modposts"),
    MODWIKI("modwiki"),
    MYSUBREDDITS("mysubreddits"),
    PRIVATEMESSAGE("privatemessages"),
    READ("read"),
    REPORT("report"),
    SAVE("save"),
    SUBMIT("submit"),
    SUBSCRIBE("subscribe"),
    VOTE("vote"),
    WIKIEDIT("wikiedit"),
    WIKIREAD("wikiread") ;
    protected static final String SEPARATOR = ",";

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
