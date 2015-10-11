package com.bitdecay.jump.geom;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Monday on 9/7/2015.
 */
public interface Projectable {
    @JsonIgnore
    BitPoint[] getProjectionPoints();
}
