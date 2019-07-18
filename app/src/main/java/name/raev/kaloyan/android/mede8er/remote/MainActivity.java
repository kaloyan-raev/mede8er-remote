package name.raev.kaloyan.android.mede8er.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private CommandQueue queue = new CommandQueue();
	
	private Object mPauseLock = new Object();
	private boolean mPause = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// inflate the activity content
		setContentView(R.layout.activity_main);
		
		// start the communication thread
		new Thread(new CommunicationThread()).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// pause the communication thread
		synchronized (mPauseLock) {
			mPause = false;
			mPauseLock.notifyAll();
		}
	}

	@Override
	protected void onPause() {
		// wake the commnication thread up
		synchronized (mPauseLock) {
			mPause = true;
		}
		
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
	
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
	        keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
    		// send volume event
			Command command = Command.getCommandForButtonId(keyCode);
			queue.add(command);
			// suppress next receiver
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
	        keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
	        // do nothing                                                       
	        return true;
	    }

	    return super.onKeyUp(keyCode, event);
	}

	/** Called when the user touches a button */
	public void sendCommand(View view) {
		Command command = Command.getCommandForButtonId(view.getId());
		queue.add(command);
		
		if (vibrateOnTouch()) {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(20);
		}
	}
	
	private String getIPAddress() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPref.getString(SettingsFragment.KEY_PREF_IP, getResources().getString(R.string.pref_ip_default));
	}
	
	private boolean vibrateOnTouch() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPref.getBoolean(SettingsFragment.KEY_PREF_VIBRATE, true);
	}

	class CommunicationThread implements Runnable {

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				synchronized (mPauseLock) {
					if (mPause) {
						try {
							mPauseLock.wait();
						} catch (InterruptedException e) {
							continue;
						}
					}
				}
				
				Command command = queue.poll();
				if (command == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// do nothing
					}
					continue;
				}

				try {
					Socket socket = new Socket(getIPAddress(), 1187);
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					writer.print("rc " + command.getName());
					writer.flush();
					writer.close();
					socket.close();
				} catch (UnknownHostException e) {
					showError(e);
					queue.clear();
				} catch (IOException e) {
					showError(e);
					queue.clear();
				}
			}
		}
		
		private void showError(final Exception e) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		}

	}

}
