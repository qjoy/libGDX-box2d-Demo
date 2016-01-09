#Android FrameWork 基于libGDX实现高性能动画特效（Box2D/物理碰撞 篇）
**by AlexQ （[email](alexq_andr@163.com) alexq_andr@163.com）**

![image](https://libgdx.badlogicgames.com/img/logo.png)![image](http://box2d.org/images/icon.gif)

**工程托管在此：[GitHub](https://github.com/xueqing325/libGDX-box2d-Demo.git)**

####前情提要
**之前写的那篇[Android FrameWork 基于libGDX实现高性能动画特效（烟花／粒子特效篇）](http://alexq.farbox.com/post/2015-12-11)最后提到了Box2D，很久之前我写过一个基于jbox2d库开发的一个碰撞特效，由于jbox2d性能所限制，同时出现20颗左右的物体碰撞时，就会出现卡顿掉帧的现象，便萌生了想用libGDX中提供的Box2D来实现一个更高效的版本，很高兴很快就完成了，我们今天就来继续分享一下这部分内容。（*[基于jbox2d版本demo工程地址](https://github.com/xueqing325/jbox2d-demo.git)*）**



*Gif较大，可能需要较长时间加载，或者直接查看[视频效果](http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-total.mp4)*

<img src="http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-total.gif" width = "200" height = "350" alt="图片名称" align=center />


####导语 
[libGDX](https://libgdx.badlogicgames.com)是一个小有名气的跨平台游戏开发引擎,[Box2D](http://box2d.org)则是大名鼎鼎模拟刚体运动特性的库，文章中我们会介绍如下几点：

1. 关于libGDX以及如何引入到App；
2. 学习Box2D
3. App FrameWork层使用libGDX－Box2D实现特效的那些细节；
4. 推荐


***
##回顾：libGDX入门学习以及如何引入到App
阅读此篇文章之前可能您需要对以下两个知识点做一些了解和铺垫，

* libGDX入门学习内容，请参考我的文章[libGDX 入门精要](http://alexq.farbox.com/post/libgdx)

* 如何才能完美引入到App中的详细分析，请参考我的另外一篇文章[Android FrameWork 基于libGDX实现高性能动画特效](http://alexq.farbox.com/post/2015-12-11)。

***
##学习Box2D
为了不分散我们这篇文章的主旨，关于Box2D入门学习内容，我特意编写在这篇文章中请移步到这里查看[Box2D 入门简要](http://alexq.farbox.com/post/box2d-ru-men-jian-yao)

当您初步掌握了Box2D中基本概念后会很容易的掌握下面的内容。

***
##App FrameWork层使用libGDX－Box2D实现特效的那些细节
1. 工程组织结构
2. 性能表现卓越
3. 利用libGDX中提供的Box2D实现特效
4. 添加重力感应逻辑

####1.工程组织结构三个部分
* libs中再增加libgdx提供的gdx-box2d.so以及gdx-box2d.jar包

<img src="http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-libs.png" width = "300" height = "200" alt="图片名称" align=center />

* 资源文件增加碰撞物体显示图片

<img src="http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-res.png" width = "200" height = "70" alt="图片名称" align=center border=0/>

* 核心代码文件，用来在app中使用libgdx－box2d实现带有重力感应的碰撞效果

<img src="http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-code.png" width = "200" height = "80" alt="图片名称" align=center border=0/>

####2.性能卓越
以下的对比很好的说明了左侧基于jbox2d的实现与右侧基于libgdx－box2d实现的两个版本之间的巨大性能差异。
运算方面一个是在java层进行的，另外一个则是在底层C上进行的，绘制方面则一个是用android通常的ondraw绘制，另一个用opengles绘制的。
如果对比还不够明显的话，你可以尝试将星星的上线调整到100左右，在你的真机上运行一下试试效果。*[基于jbox2d版本demo工程地址](https://github.com/xueqing325/jbox2d-demo.git)*

<table><tr>
<td><img src="http://7xox5k.com1.z0.glb.clouddn.com/Box2d-demo-old.gif" width = "200" height = "330" alt="图片名称" align=center />
<td><img src="http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-new.gif" width = "200" height = "330" alt="图片名称" align=center />
</tr></table>



####3.利用libGDX中提供的Box2D实现特效
1. 注意坐标系
**首先有个重要的问题要说明，Box2D的坐标系原点是在你创建的Fragment的中心点，而libGDX的绘制坐标系是Fragment的左下，如果你发现你的debug视图是好的，而一旦按照Box2D返回的坐标贴图，造成显示位置偏差那就是没有注意坐标系关系造成的**

<img src="http://7xox5k.com1.z0.glb.clouddn.com/Box2d-zbx.png" width = "330" height = "240" alt="图片名称" align=center />

	
	工程中Tools目录下的Transform，就是为了转换坐标系用的，提供双向的转换：
	
	libGDX －> Box2D 以及 Box2D －> libGDX
	
	使用方法就是贴图绘制前，将你从box2d中拿到的坐标做一次转化之后再使用。
	<code><pre>
	Vector2 transformVect = Transform.mtp(box2d.x, box2d.y, new Vector2(w, h), PXTM);
	</pre></code>
	
2. “盖房子”，添加左、右、底三面墙
	建立用来限制小星星碰撞范围的围墙，三面围墙原理相同，只是位置不同罢了，可以通过打开“Open DebugDraw”，看到绿色的细线就是围墙，这里我们用staticBody这种刚体类型来表现他最合适不过。我们列举地面的代码：
	
	<code><pre>
	private void addground(){
        BodyDef groundBodyDef =new BodyDef();
        groundBodyDef.position.set(new Vector2(0, -camera.viewportHeight/2));
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 1.0f / PXTM);
        groundBody.createFixture(groundBox, 0.5f);
        groundBox.dispose();
    }
	</pre></code>
	
	这里面有2个点值得注意：
	
	* 首先是设置位置的时候，我们将地面的Y坐标设置为-camera.viewportHeight/2，是因为，我们之前提到了，Box2D的坐标系是中心点，所以，Y向下为负值，总高度的一半向下，自然就是最下面的边缘了。
	
	* 其次注意到PXTM这个常量了嘛，它的作用是单位换算，什么单位呢，Box2D用的距离单位都是米(m)，而我们屏幕的单位是像素，这两者有个转化比例就是PXTM = 30（1米等于30像素）;这个是除去坐标系转化之外，影响绘制位置的重要因素，请切记。		


3. render方法中的三件事情
	
	*预览代码:*
	<code><pre>
	 @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        synchronized (Box2dEffectView.class) {
            if (m_ballBodys.size() == 0)
                return;
            float deltatm = Gdx.app.getGraphics().getDeltaTime();
            world.step(deltatm, 6, 2);
            _ballUpdatasLogic(deltatm);
            if (m_isDebugRenderer)
                m_debugRenderer.render(world, camera.combined);
        }
    }
	</pre></code>
	
	<code><pre>
	private void _ballUpdatasLogic(float deltatm){
        for (int i=0; i<m_ballBodys.size(); i++){
            Body bd = m_ballBodys.get(i);
            BallInfo ballInfo = (BallInfo)bd.getUserData();
            float alphascale = 1f;
            float curruntm = ballInfo.getRuntimes();
            if (curruntm >= Box2dConstant.MaxBallLiferSP){
                alphascale = hidesmallBody(i);
            }
            //判断是否已经到了生命尽头
            if (curruntm >= Box2dConstant.MaxBallLifer){
                destoryBody(i);
                i--;
                continue;
            }
            //更新时间
            ballInfo.setRuntimes(curruntm + deltatm);
            Vector2 transformVect = Transform.mtp(bd.getPosition().x + 1f, bd.getPosition().y + 1f, new Vector2(2f, 2f), PXTM);
            //绘制
            m_spriteBatch.begin();
            m_spriteBatch.setColor(new Color(1,1,1,alphascale));
            m_spriteBatch.draw(TESTTEXTURE, transformVect.x+(1f-alphascale)*30, transformVect.y+(1f-alphascale)*30, alphascale*60f, alphascale*60f);
            m_spriteBatch.end();
        }
    }
	</pre></code>


 	* 模拟碰撞
 	
 		调用	world.step(deltatm, 6, 2);启动Box2D模拟运算。
 		
	在_ballUpdatasLogic函数里面遍历所有小星星的循环
 	* 控制物体生命周期
 	
 		响应业务逻辑，判断如果小星星展示时间超过一个我们定义阀值，启动摧毁这颗小星星代码逻辑；
 
 		
 	* 绘制
 		像我们在前一篇粒子特效中和libGDX入门中介绍的一样，最终我们要将Box2D模拟的数据结果绘制出来，通过调用m_spriteBatch.draw方法，将小星星的图片按照Box2D运算结果绘制到屏幕上。其中我们为了更好的视觉效果，在小星星的消失过程中设置了缩小以及渐隐，通过设置透明度以及调整贴图来达到，并不需要改变Box2D运算过程。
 		
	

	

####4.添加重力感应逻辑
如果您仔细观察最开始那个事例gif图片的话，在左上角有一个实时在动的像是水平仪的东西（带有指针的小圆圈），对，他就是个水平仪。我们为整个碰撞特效添加了重力感应的效果，当你左右晃动手机时，会发现小星星也跟着左右摆动起来，很有意思很互动，水平仪表现了手机摆动的情况。

首先为了能够直观的演示重力感应，我在工程的testcode目录里面实现了一个levelgaugeView，也就是你看到的这个小东西，帮助你看到自己的陀螺仪水平方向上的变化，这个demo表现了，我们将手机先是水平的，之后向右倾斜，在之后向左倾斜，最后恢复水平状态。

<table><tr>
<td><img src="http://7xox5k.com1.z0.glb.clouddn.com/Box2d-demo-shuipingyi.gif" width = "100" height = "130" alt="图片名称" align=center border=0/>
<td><img src="http://7xox5k.com1.z0.glb.clouddn.com/Box2d-demo-spyd.gif" width = "180" height = "130" alt="图片名称" align=center border=0/>
</tr></table>




<code><pre>
public Box2dSenserLogic(final World world, Context context) {
		m_world = world;
		//获得重力感应硬件控制器
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//添加重力感应侦听，并实现其方法，
		sel = new SensorEventListener() {
			public void onSensorChanged(SensorEvent se) {
				float x = se.values[SensorManager.DATA_X];
				float y = se.values[SensorManager.DATA_Y];
				float xSenser, ySenser;
				if (m_isPortrait) {
					xSenser = -5.0f * x;
					ySenser = (y >= -1) ? -30 : -y * 5.0f;
					if (y<-1){
						xSenser = -1 * xSenser;
						ySenser = -1 * ySenser;
					}
				}
				else{
					xSenser = 5.0f * y;
					ySenser = (x >= -1) ? -30 : -x * 5.0f;
					if (x<-1) {
						xSenser = -1 * xSenser;
						ySenser = -1 * ySenser;
					}
				}
				Vector2 vec2 = m_isPortrait ? new Vector2(xSenser, ySenser) : new Vector2(xSenser, ySenser);
				if (world != null)
					world.setGravity(vec2);
			}
			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}
		};
	}
</pre></code>

我们考虑了如果你开启屏幕方向变化的因素，横竖屏幕我们分开处理。其中能够达到效果的核心代码就是：“world.setGravity(vec2);”，根据陀螺仪传递的数据，我们设置好相应的新的重力方向，就能够达到们看到的效果了。别忘了退出时，关闭重力感应接收哦。

**以上便是用libgdx－Box2d实现碰撞特效的全部内容，如果您之前了解了libGDX入门内容＋libGDX如何引入到app中＋Box2D入门知识，掌握起来还是很简单的**

***
##推荐
####1.推荐书目
<img src="http://7xox5k.com1.z0.glb.clouddn.com/IMG_0781.JPG" width = "400" height = "500" alt="图片名称" align=center/>
作者: 陈文登 
出版社: 科学出版社
出版年: 2015-3-16
页数: 328
定价: 78
装帧: 平装
ISBN: 9787030434340
####2.推荐网址
* [Box2D org](http://box2d.org)

* [libGDX Box2D封装API](https://github.com/libgdx/libgdx/wiki/Box2d)


