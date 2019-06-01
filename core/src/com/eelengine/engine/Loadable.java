package com.eelengine.engine;

import java.io.Externalizable;
import java.io.Serializable;

public interface Loadable extends Serializable {
    boolean isLoaded=false;
    public void build(String asset);
}
