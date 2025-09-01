# ZenCodeGame IAP Android SDK 对接文档

## 版本
| 版本 | 日期 | 作者 |更新|
|:-------:|:-------:|:-------:|:-------:|
| v1.1 | 2025年8月15日 | BIN |完善小米的IAP SDK接入|


## 简介
本文档描述了Android游戏如何接入ZenCodeGame IAP Android SDK，以便于我们更好的运营和推广游戏。此SDK的目的是集成厂商的IAP SDK，将厂商的IAP SDK的不同隐藏起来，为CP对接厂商IAP功能提供统一的对接SDK，让CP只需要接入一次SDK后，就可以完成不同厂商的IAP功能的接入。

## SDK下载
说明：为了减少CP在构建不同厂商的包的时候，不将其他厂商的代码也打包到里面，所以我们采用了不同的厂商的ZenCodeGame IAP的SDK分为不同的aar包，具体的厂商aar包下载地址如下

OPPO厂商 ZenCodeGame IAP Android SDK 下载路径如下：
- gameiapsdk-oppo-release.aar-v1.1.aar 下载地址：[ZenCodeGame IAP Android SDK OPPO](https://docs.zencodegame.com/aar/gameiapsdk-oppo-release-v1.1.aar)

小米厂商 ZenCodeGame IAP Android SDK 下载路径如下：
- gameiapsdk-xiaomi-release-v1.1.aar  下载地址：[ZenCodeGame IAP Android SDK XIAOMI](https://docs.zencodegame.com/aar/gameiapsdk-xiaomi-release-v1.1.aar)

目前支持厂商暂时只有OPPO、小米，后续将会实现对华为、传音等厂商的支持

## SDK引入并初始化
1. 下载SDK后，将下载好的aar文件复制到Android项目工程的游戏Module目录下的libs文件夹下，然后在Android项目工程的游戏Module目录下的build.gradle或者build.gradle.kts文件加入如下配置
```
dependencies {
    // ... existing code ...
    //引入com.squareup.okhttp3，ZenCodeGame iap sdk需要依赖这个库 必须添加
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //引入gameiapsdk-oppo-release-v1.0.aar包 构建OPPO渠道包的时候需要依赖
    implementation files('libs/gameiapsdk-oppo-release-v1.0.aar') 

    //引入gameiapsdk-xiaomi-release-v1.0.aar 构建xiaomi渠道包的时候需要依赖
    implementation files('libs/gameiapsdk-xiaomi-release-v1.0.aar') 
    //构建小米渠道包需要的依赖
    implementation("com.xiaomi.billingclient:billing:1.1.9")
}
```
备注：渠道集成SDK对minSdk需要大于23
```
//build.gradle.kts文件中的配置
adnroid{
    defaultConfig {
        //minSdk必须在23以上
        minSdk = 23 
        //targetSdk必须在30以上
        targetSdk = 35
    }
}
或者
//build.gradle文件中的配置：
android {
    defaultConfig {
        //minSdkVersion必须在23以上
        minSdkVersion 23
        //targetSdkVersion必须在30以上
        targetSdkVersion 35
    }
}
```

2. SDK初始化，代码如下：
```
//在游戏的启动入口Application文件中，添加初始化代码
public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //游戏appId="10031" 游戏appId请联系对接人员获得
        //游戏appSecret="8e692268fa354fcc9c6469f076fe6218" 游戏appId请联系对接人员获得 此参数是OPPO需要的参数，如果不接入OPPO渠道的话，此参数为""
        String appSecret = "8e692268fa354fcc9c6469f076fe6218";
        String appId = "10031";
        boolean debug = false; //开启debug的话，会答应更多有关IAP SDK的日志
        GameIAPSDK.getInstance().init(this, appId,appSecret,debug);
    }
}
```

3. AndroidManifest.xml的新增配置
```
<application>
    <meta-data
        android:name="app_key" //需要寻找相关对接人员获取 这个配置只有在构建OPPO渠道包的时候需要 如果不接入OPPO渠道的话可以不配置
        android:value="5afa05530ae846049e31006a7ab88e9e" />
</application>
```

## SDK用户发起支付
### 1. 在需要获取支付链接的地方调用如下代码
```
private void pay(){
    String orderId = System.currentTimeMillis() + "18";
        String amount = input;
        PayOrderInfo orderInfo = new PayOrderInfo(orderId, amount); //订单ID，CP确保唯一，订单金额
        orderInfo.setProductName("金币1000"); //商品名称
        orderInfo.setProductDesc("金币1000"); //商品描述
        orderInfo.setExtInfo("test_0001");//扩展字段 支付结果回调的时候回原封不动的返回
        orderInfo.setCurrency("CNY");//游戏当前的币种：支持INR,IDR,TWD,VND,THB,PHP,MYR,USD,RUB,CNY
        orderInfo.setCallbackUrl("https://api.zencodegame.com/payment/callback"); //通知回调接口网址，支付结果会通过这个地址回调通知给服务端
        orderInfo.setUserId("190879"); //游戏的用户ID
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
```
用户操作收银台完成支付后，支付结果将由平台服务端通过订单中的支付结果通知地址回调给游戏的服务端，服务端需要做好相关结果的处理，并返回正式的处理结果，

备注说明：目前支付接口支持沙盒模式，开启沙盒模式需要向对接人员提出开启沙盒请求，游戏上线前会将沙盒模式关闭

## Android客户端构建渠道包说明

### 1. 构建OPPO渠道的要求如下：
- 1. 包名需要是以.nearme.gamecenter结尾的包名或使用google play同名的包
- 2. 构建包的时候如果开启了混淆，需要在proguard-rules.pro文件中添加如下配置：
```
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepclassmembers class okhttp3.** {
    public *;
}
-keep class com.android.game.oppo.** { *; }
-keep interface com.android.game.oppo.** { *; }
-keepclassmembers class com.android.game.oppo.** {
    public *;
}
-keep class com.android.game.iap.** { *; }
-keep interface com.android.game.iap.** { *; }
-keepclassmembers class com.android.game.iap.** {
    public *;
}
-keep class com.nearme.** { *; }
-keep interface com.nearme.** { *; }
-keepclassmembers class com.nearme.** {
    public *;
}
```

### 2. 构建XIAOMI渠道的要求如下：
- 1. 构建的版本名称不应该以“_mitest”命名结尾
- 2. 构建包的时候如果开启了混淆，需要在proguard-rules.pro文件中添加如下配置：
```
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepclassmembers class okhttp3.** {
    public *;
}
-keep class com.android.game.xiaomi.** { *; }
-keep interface com.android.game.xiaomi.** { *; }
-keepclassmembers class com.android.game.xiaomi.** {
    public *;
}
-keep class com.android.game.iap.** { *; }
-keep interface com.android.game.iap.** { *; }
-keepclassmembers class com.android.game.iap.** {
    public *;
}
-keep class com.nearme.** { *; }
-keep interface com.nearme.** { *; }
-keepclassmembers class com.nearme.** {
    public *;
}
```

## 渠道测试环境要求

### 1. OPPO渠道调试条件要求
- 1. 需要一部安装了OPPO安全支付插件app的手机,OPPO安全支付插件下载地址：https://docs.zencodegame.com/apks/KeKePay6.7.5.apk
- 2. 需要将手机语言地区设置成需要测试支付的地区：目前只支持：印度-IN，印度尼西亚-ID，台湾-TW， 越南-VN， 泰国-TH， 菲律宾-PH，  马来西亚-MY。目前支持以上七个地区

### 1. XIAOMI渠道调试条件要求
- 1. 需要一部小米国际版系统手机
- 2. 需要安装GetApp应用商店App 版本需要高于25.0.0，下载地址：https://docs.zencodegame.com/apks/com.xiaomi.mipicks.apk
- 3. 需要将手机语言地区设置成需要测试支付的地区：目前IAP服务支持印尼、俄罗斯、马来西亚、法国、德国、西班牙、巴西、土耳其、菲律宾地区。后续将陆续开通更多国家/地区）
- 4. 需要注册一个小米帐号，并将小米帐号ID提供给我们，我们将你们的小米帐号ID添加许可测试人员
- 5. 需要将小米帐号在手机上登录
- 6. 需要将你们的apk的版本名设置为以“_mitest”命名结尾，如“1.4.1_mitest”；


### 2. 服务端回调接口
    当用户完成支付后，支付订单状体将通过上面设置的支付回调地址通知CP服务端，服务端需要接收并处理支付回调结果，接口说明如下：
- 请求方式：POST
- 接口地址：例如（https://api.zencodegame.com/payment/callback）
- 接口参数：

| 参数名 | 类型 | 是否必须 |备注
|:-------:|:-------:|:-------:|:-------:|
| userId | string | 是 |用户ID CP游戏订单用户ID
| payOrderId|string|是|订单ID CP游戏订单ID
| currency| string|是|货币（USD、CNY），CP游戏订单货币
| amount | string | 是 |订单金额，CP游戏订单金额
| extra | string | 是 |订单扩展信息，CP游戏订单的扩展信息
| tradeNo | string | 是 |平台订单号
| status | string | 是 |支付状态，PAID-支付成功，FAIL-支付失败，REFUND-退款
| sign | string | 否 | 签名，为了安全

- 接口返回值：

| 参数名 | 类型 | 是否必须 |备注｜
|:-------:|:-------:|:-------:|:-------:|
| code | int | 是 |返回码，0表示成功，非0表示失败|
| message | string | 是 |返回消息|

- 备注：返回的数据需要进行签名校验，服务端会使用到签名校验会用到一个key，此key需要联系对接人员获取

- 参考
```
//请求
curl -X POST "https://api.zencodegame.com/payment/callback" \
-H "Content-Type: application/json" \
-d '{
    "userId": "190879",
    "payOrderId": "175144870291918",
    "tradeNo": "250702093140253702",
    "amount": "5",
    "currency": "USD",
    "extra": "test_0001",
    "status": "PAID",
    "sign": "c1f935e949d9174fd127af68e593e827e27089dfdda00631aeebac8814208737"
}'
//返回结果 0表示正常处理 通知回调服务器收到code为0，则不在回调，如果code不为0，则继续尝试数次回调
{"code":0,"message":"ok"}
```
- 签名机制
    获取签名字符串：
    请求参数json中的业务信息，按照key的Unicode进行排序。然后将参数以 key1=value1&key2=value2&key3=value3 的形式拼接，
    最后将应用key拼接至字符串最后，key值为key。我们暂且称拼接好的字符串为originStr
    将originStr转化为UTF-8的byte数组后，使用sha256算法进行编码，再将编码后的数组转码为16进制的字符串。
    伪代码如下：
    Hex.encodeHexString(SHACoder.encodeSHA256(originStr.getBytes("UTF-8")));
- 备注
    在拼接字符串时，会过滤掉key或value为空的字符串的参数，此类参数不参与签名前的字符串拼接。
    在请求参数与返回参数的签名中，出现extInfo字段时，签名时需要将extInfo 的值转换为Map 集合，按照key的Unicode进行排序。
    然后将Map中参数以`key1=value1&key2=value2&key3=value3` 的形式拼接为新的字符串作为extInfo的值， 
    如右侧示例，extInfo参与签名时，参数值应为payerEmail=example@163.com&payerMobile=081666866866&payerName=example，
    那么extInfo参与签名时的字符串为extInfo=payerEmail=example@163.com&payerMobile=081666866866&payerName=example

-例如：
```
//回调通知参数：
{
    "userId": "190879",
    "payOrderId": "175144870291918",
    "tradeNo": "250702093140253702",
    "amount": "5",
    "currency": "USD",
    "extra": "test_0001",
    "status": "PAID",
    "sign": "c1f935e949d9174fd127af68e593e827e27089dfdda00631aeebac8814208737"
}
//参数拼接后的到如下字符串：
amount=5&currency=USD&extra=test_0001&payOrderId=175144870291918&status=PAID&tradeNo=250702093140253702&userId=190879
//签名的key为：fe68e63bea35f8edeae04daec0ecb728
//拼接后的到字符串
originStr="amount=5&currency=USD&extra=test_0001&payOrderId=175144870291918&status=PAID&tradeNo=250702093140253702&userId=190879&key=fe68e63bea35f8edeae04daec0ecb728"
//然后签名(伪代码)
sign=Hex.encodeHexString(SHACoder.encodeSHA256(originStr.getBytes("UTF-8")));
//获得签名
sign=c1f935e949d9174fd127af68e593e827e27089dfdda00631aeebac8814208737
```

## 参考 Demo
### 1. android demo 工程：[Demo](https://github.com/ZenCodeGame/ZenCodeGameDemo)
Demo一个Project,clone下来后直接用Android studio打开运行，在Demo的代码中，增加了同时构建多个渠道包的配置，如果想一次性构建多个渠道，可以参考相应的配置：
```
//增加配置
android{
    ....原来的代码
    // 添加厂商维度
    flavorDimensions += "vendor"
    // 定义不同厂商的构建变体
    productFlavors {
        create("xiaomi") {
            dimension = "vendor"
            // 小米特定的配置
            manifestPlaceholders["VENDOR_NAME"] = "xiaomi"
            applicationId = "com.sqrush.merge"
            versionCode=100
            versionName="1.0.0_mitest"
        }
        create("oppo") {
            dimension = "vendor"
            // OPPO特定的配置
            manifestPlaceholders["VENDOR_NAME"] = "oppo"
            applicationId = "com.sqrush.merge.gamecenter"
            versionCode=100
            versionName="1.0.0"
        }
    }
}
//调整sdk依赖方式
dependencies {

    implementation libs.appcompat
    implementation libs.material
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //构建OPPO渠道包需要的依赖
    "oppoImplementation" files('libs/gameiapsdk-oppo-release-v1.1.aar')

    //构建小米渠道包需要的依赖
    "xiaomiImplementation" files('libs/gameiapsdk-xiaomi-release-v1.1.aar')
    //构建小米渠道包需要的依赖
    "xiaomiImplementation"("com.xiaomi.billingclient:billing:1.1.9")
}

```
调整完配置文件后，如果需要一次性打出多个渠道包，需要执行命令：
```
//构建多个渠道包
./gradlew assembleRelease
```
如果构建一个渠道包，需要执行命令：
```
//构建OPPO渠道包
./gradlew assembleOppoRelease
//构建Xiaomi渠道包
./gradlew assembleXiaomiRelease
//构建Huawei渠道包
./gradlew assembleHuaweiRelease
```

## 结束语
ZenCodeGame IAP SDK的接入文档说明，如有任何问题，请随时联系我们。祝生意兴隆！
