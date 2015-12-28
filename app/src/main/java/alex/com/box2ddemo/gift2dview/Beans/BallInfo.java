package alex.com.box2ddemo.gift2dview.Beans;

/**
 * @author AleXQ
 * @Date 15/12/26
 * @Description: 小球信息
 */

public class BallInfo {

	private float m_runtimes = 0;
	private float m_aplhascale = 1f;

//	private Texture m_ballTexture;
//
//	public BallInfo() {
//
//		m_ballTexture = new Texture(Gdx.files.internal("star.png"));
//
//	}

//	public Texture getBallTexture() {
//		return m_ballTexture;
//	}

	public float getRuntimes() {
		synchronized (BallInfo.class) {
			return m_runtimes;
		}
	}

	public void setRuntimes(float runtimes) {
		synchronized (BallInfo.class) {
			m_runtimes = runtimes;
		}
	}

	public float getAplhascale() {
		return m_aplhascale;
	}

	public void setAplhascale(float aplhascale) {
		m_aplhascale = aplhascale;
	}
}
