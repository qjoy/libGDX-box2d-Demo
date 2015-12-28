package alex.com.box2ddemo.testcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import alex.com.box2ddemo.R;

/**
 * @author AleXQ
 * @Date 15/12/5
 */

public class HalfScreenActivity extends Activity{
	public static void launch(Context context){
		Intent intent = new Intent();
		intent.setClass(context, HalfScreenActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_dialogstyle);
	}
}
