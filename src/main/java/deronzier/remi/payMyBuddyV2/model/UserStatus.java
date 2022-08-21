package deronzier.remi.paymybuddyv2.model;

public enum UserStatus {
	ACTIVE("Active"),
	AWAY("Away");

	private final String label;

	private UserStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
