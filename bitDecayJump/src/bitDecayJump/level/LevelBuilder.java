package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.*;

import com.google.gson.*;

public class LevelBuilder {
	public Level level;

	public List<LevelObject> selection;

	public LevelBuilder(Level level) {
		this.level = level;
		selection = new ArrayList<LevelObject>();
	}

	public void createObject(BitPointInt startPoint, BitPointInt endPoint) {
		LevelObject obj = new LevelObject();
		obj.rect = GeomUtils.makeRect(startPoint, endPoint);
		level.objects.add(obj);
	}

	public void selectObjects(BitRectangle selectionArea, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : level.objects) {
			if (selectionArea.contains(object.rect)) {
				selection.add(object);
			}
		}
	}

	public void selectObject(BitPointInt startPoint, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : level.objects) {
			if (object.rect.contains(startPoint)) {
				selection.add(object);
				return;
			}
		}
	}

	public void deleteSelected() {
		level.objects.removeAll(selection);
		selection.clear();
	}

	public void toJson() {
		Gson gson = new GsonBuilder().create();
		gson.toJson(level, System.out);
	}
}
