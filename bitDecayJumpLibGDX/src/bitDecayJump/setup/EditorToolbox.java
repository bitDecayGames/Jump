package bitDecayJump.setup;

import java.awt.Image;
import java.util.*;

public class EditorToolbox {
	public Map<Image, Class<? extends EditorObject>> tiles = new HashMap<Image, Class<? extends EditorObject>>();
	public Map<Image, Class<? extends EditorObject>> placableObject = new HashMap<Image, Class<? extends EditorObject>>();
	public Map<Image, Class<? extends EditorObject>> pathedObject = new HashMap<Image, Class<? extends EditorObject>>();

	public void add(EditorObjectType type, Image icon, Class<? extends EditorObject> class1) {
		switch (type) {
		case GROUND_TILE:
			tiles.put(icon, class1);
			break;
		case PATHED_OBJECT:
			pathedObject.put(icon, class1);
			break;
		case PLACEABLE_OBJECT:
			placableObject.put(icon, class1);
			break;
		default:
			break;

		}
	}
}
