package name.raev.kaloyan.android.mede8er.remote;

import android.view.KeyEvent;

public enum Command {
	
	ENTER("enter", R.id.btnEnter),
	UP("up", R.id.btnUp),
	RIGHT("right", R.id.btnRight),
	DOWN("down", R.id.btnDown),
	LEFT("left", R.id.btnLeft),
	HOME("guide", R.id.btnHome),
	RETURN("return", R.id.btnReturn),
	INFO("info", R.id.btnInfo),
	MENU("menu", R.id.btnMenu), 
	PLAY("play", R.id.btnPlay), 
	STOP("stop", R.id.btnStop),
	PREV("prev", R.id.btnPrev),
	NEXT("next", R.id.btnNext),
	REPLAY("instantreplay", R.id.btnReplay),
	SKIP("cmskip", R.id.btnSkip),
	VOLUME_UP("vol+", KeyEvent.KEYCODE_VOLUME_UP),
	VOLUME_DOWN("vol-", KeyEvent.KEYCODE_VOLUME_DOWN);
	
	private String name;
	private int buttonId;
	
	private Command(String name, int buttonId) {
		this.name = name;
		this.buttonId = buttonId;
	}

	public String getName() {
		return name;
	}

	public static Command getCommandForButtonId(int buttonId)
			throws IllegalArgumentException {
		for (Command command : Command.values()) {
			if (command.buttonId == buttonId) {
				return command;
			}
		}
		throw new IllegalArgumentException(
				"no command available for button with id " + buttonId);
	}

}
