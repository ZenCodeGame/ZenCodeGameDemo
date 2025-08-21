package com.android.zencodegame.demo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.game.iap.GameIAPSDK;
import com.android.game.iap.IAPCallback;
import com.android.game.iap.model.PayOrderInfo;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_pay).setOnClickListener(v -> doPay());
    }

    private void doPay() {
        // CP 支付参数
        EditText payInput = findViewById(R.id.test_pay_input);
        String input = payInput.getText().toString();

        if(TextUtils.isEmpty(input)){
            input="0.99";
        }
        String orderId = System.currentTimeMillis() + "18";
        String amount = input;
        PayOrderInfo orderInfo = new PayOrderInfo(orderId, amount); //订单ID，CP确保唯一，订单金额
        orderInfo.setProductName("金币1000"); //商品名称
        orderInfo.setProductDesc("金币1000"); //商品描述
        orderInfo.setExtInfo("test_0001");//扩展字段 支付结果回调的时候回原封不动的返回
        orderInfo.setCurrency("USD");//游戏当前的币种
        orderInfo.setCallbackUrl("https://api.gtest4tg.com/payment/callback"); //通知回调接口网址，支付结果会通过这个地址回调通知给服务端
        orderInfo.setUserId("11000214110"); //游戏的用户ID
        GameIAPSDK.getInstance().doPay(this, orderInfo, new IAPCallback() {
            @Override
            public void onSuccess(String s) {
                //支付完成
                Toast.makeText(MainActivity.this, "onSuccess:" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String s, int i) {
                //支付失败或者取消
                Toast.makeText(MainActivity.this, "onFailure:" + s + ":code" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}