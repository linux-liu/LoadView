# LoadView
一个带进度圆形进度条
效果图如下:
<br>
![效果图](https://github.com/linux-liu/LoadView/blob/master/gif/loading.gif)
<br>
###用法 
直接调用setPercent方法设置进度即可，如下代码模拟下载
```java
 public void downLoad(View view) {

   new TimeDown(12*1000,1000).start();

    }

    private class TimeDown extends CountDownTimer{

        private float i=0;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            i=0;
            loadView.setPercent(0);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("lx","onTick"+millisUntilFinished);
            loadView.setPercent(i);
            i=i+0.083333f;
        }

        @Override
        public void onFinish() {
           loadView.setPercent(1f);
        }
    }
```
支持自定义参数如下:
```xml
    <declare-styleable name="LoadView">
        <!-- 已经load的颜色值-->
        <attr name="HasLoadColor" format="color" />
        <!-- 没有load的颜色值-->
        <attr name="NormalLoadColor" format="color" />
        <!-- 小长方形的宽度-->
        <attr name="RectangleWidth" format="dimension" />
        <!-- 小长方形的高度-->
        <attr name="RectangleHeight" format="dimension" />
        <!-- 小长方形的个数-->
        <attr name="RectangleNum" format="integer" />
        <!-- 内半径大小-->
        <attr name="InnerRadius" format="dimension" />
        <!-- 文字大小-->
        <attr name="TextSize" format="dimension" />
        <!-- 小长方形圆角度-->
        <attr name="RectangleRadius" format="dimension" />
    </declare-styleable>
```
###其他
<br>
[带进度的圆形进度条的实现](https://blog.csdn.net/u014795729/article/details/80144147)
