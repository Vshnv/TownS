package com.rocketmail.vaishnavanil.towns.Messages;

public enum Format {
    CmdErrFrmt(new CmdError()),
    CmdInfoFrmt(new CmdInfo()),
    AlrtFrmt(new Alert());


    private Message frmt;

    Format(Message type) {
        this.frmt = type;
    }

    public Message use() {
        return frmt;
    }

}
