package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;

/**
 * Created by Monday on 9/29/2015.
 */
public interface ContactListener {
    void contactStarted(BitBody other);
    void contactEnded(BitBody other);
}
