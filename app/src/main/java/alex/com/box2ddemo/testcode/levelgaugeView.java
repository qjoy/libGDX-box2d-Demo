package alex.com.box2ddemo.testcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author AleXQ
 * @Date 16/1/8
 * @Description: 水平仪
 */

public class LevelGaugeView extends View{

	private SensorEventListener sel;
	private SensorManager sm;
	private Sensor sensor;

	private int linewidth = 80;
	private int lineheight = 6;
	Point pleft = new Point();
	Point pright = new Point();
	Point pcenter = new Point(90,240);

	// 创建画笔
	Paint m_paint0 = new Paint();
	Paint m_paint1 = new Paint();

	public LevelGaugeView(Context context) {
		super(context);
		init();
	}

	public LevelGaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		initPaint();
		//获得重力感应硬件控制器
		sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//添加重力感应侦听，并实现其方法，
		sel = new SensorEventListener() {
			public void onSensorChanged(SensorEvent se) {

				float x = se.values[SensorManager.DATA_X];
				float y = se.values[SensorManager.DATA_Y];
//				Log.d(TAG, "x:" + x + ",y:" + y);
				float m = x*90f/9.8f + 90;
				float angle = (float)(m *  Math.PI / 180f);
				calcPoints(angle);
			}

			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}
		};

		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	private void initPaint(){
		m_paint0.setColor(0x8066CDAA);
		m_paint0.setStrokeWidth(lineheight);
		m_paint0.setAntiAlias(true);

		m_paint1.setColor(0x80FA8072);
		m_paint1.setStrokeWidth(lineheight);
		m_paint1.setAntiAlias(true);
	}

	public void release() {
		sm.unregisterListener(sel);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawCircle(pcenter.x, pcenter.y, linewidth / 2f + 5, m_paint0);

		canvas.drawLine(pleft.x, pleft.y, pright.x, pright.y, m_paint1);// 画线

		canvas.drawCircle(pright.x-2, pright.y-2, 3, m_paint1);
	}

	private void calcPoints(float angle){

//		Log.d("levelgaugeView", "angle:" + angle);
		pleft.x = (int) (pcenter.x +  linewidth / 2 * Math.sin(-angle) );
		pleft.y = (int) (pcenter.y -  linewidth / 2 * Math.cos(-angle) );

		pright.x = (int) (pcenter.x +  linewidth / 2 * Math.sin(angle) );
		pright.y = (int) (pcenter.y +  linewidth / 2 * Math.cos(angle) );

//		Log.d("levelgaugeView", "pleft.x:" + pleft.x + ",  pleft.y:" + pleft.y);
//		Log.d("levelgaugeView", "pright.x:" + pright.x + ",  pright.y:" + pright.y);

		invalidate();
	}
}
