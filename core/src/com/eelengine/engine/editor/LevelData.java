package com.eelengine.engine.editor;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<MailboxSrc> mailboxSrcs =new ArrayList<>();

    public LevelData() {
        mailboxSrcs =new ArrayList<>();
    }
}
