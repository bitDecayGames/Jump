package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;

public class TileBody extends BitBody {
	public int nValue = 0;

	public TileBody(){
		super();
	}

	public TileBody(TileBody other){
		super(other);
		this.nValue = other.nValue;
	}
}
