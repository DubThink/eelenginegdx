package com.eelengine.engine.eelgame;

import com.artemis.Component;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;

/**
 * An object that can be on fire
 */
public class CCharacterAnimation extends Component {
    String uSeq,dSeq,lSeq,rSeq;
    String idleSeq;

    public CCharacterAnimation setMovementSequences(String uSeq,String dSeq,String lSeq,String rSeq){
        this.uSeq=uSeq;
        this.dSeq=dSeq;
        this.lSeq=lSeq;
        this.rSeq=rSeq;
        return this;
    }

    public CCharacterAnimation setIdleSequence(String idleSeq){
        this.idleSeq=idleSeq;
        return this;
    }
}
