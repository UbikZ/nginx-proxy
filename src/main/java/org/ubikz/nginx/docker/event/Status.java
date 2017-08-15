package org.ubikz.nginx.docker.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    attach("attach"),
    commit("commit"),
    copy("copy"),
    create("create"),
    destroy("destroy"),
    detach("detach"),
    die("die"),
    exec_create("exec_create"),
    exec_detach("exec_detach"),
    exec_start("exec_start"),
    export("export"),
    health_status("health_status"),
    kill("kill"),
    oom("oom"),
    pause("pause"),
    rename("rename"),
    resize("resize"),
    restart("restart"),
    start("start"),
    stop("stop"),
    top("top"),
    unpause("unpause"),
    update("update");

    private final String name;

    @JsonCreator
    private Status(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}