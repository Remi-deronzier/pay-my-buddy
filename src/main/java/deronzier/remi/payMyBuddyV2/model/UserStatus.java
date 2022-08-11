package deronzier.remi.payMyBuddyV2.model;

public enum UserStatus {
	ACTIVE("Active"),
	DO_NOT_DISTURB("Do not disturb"),
	AWAY("Away"),
	IDLE("Idle");

	private final String label;

	private UserStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
