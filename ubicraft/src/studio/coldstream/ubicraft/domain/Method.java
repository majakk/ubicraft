package studio.coldstream.ubicraft.domain;

public enum Method {

	TELLSTICK_TURNON(1), TELLSTICK_TURNOFF(2), TELLSTICK_BELL(4), TELLSTICK_TOGGLE(8), TELLSTICK_DIM(16), TELLSTICK_LEARN(32);

	private final int method;

	private Method(int method) {
		this.method = method;

	}

	public int intValue() {
		return method;
	}

	public static Method fromInt(int method) {
		switch (method) {
		case 1:
			return TELLSTICK_TURNON;
		case 2:
			return TELLSTICK_TURNOFF;
		case 4:
			return TELLSTICK_BELL;
		case 8:
			return TELLSTICK_TOGGLE;
		case 16:
			return TELLSTICK_DIM;
		case 32:
			return TELLSTICK_LEARN;
		default:
			return null;
		}
	}
}
