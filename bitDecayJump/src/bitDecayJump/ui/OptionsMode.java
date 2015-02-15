package bitDecayJump.ui;

public enum OptionsMode {
	SELECT("Select", 0),
	CREATE("Create", 0),
	DELETE("Delete", 0),
	SET_TEST_PLAYER("Set Test Player", 1),
	SAVE_PLAYER("Save Player Props", 1),
	LOAD_PLAYER("Load Player Props", 1),
	SET_SPAWN("Set Spawn Point", 2),
	SAVE_LEVEL("Save Level", 3),
	LOAD_LEVEL("Load Level", 3);

	public final String label;
	public final int group;

	private OptionsMode(String label, int group) {
		this.label = label;
		this.group = group;
	}
}
