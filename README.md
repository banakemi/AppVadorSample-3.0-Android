AppVador SDK Version 3.0
========================

要件
----

ビルド設定は以下のとおりです。

Minimum SDK Version: 9
Target SDK Version: 22

Android 2.3以降にインストール可能ですが、内部でAndroid 4.0の機能を使用しているため、
動画広告はAndroid 4.0（SDK Version 14以降）でのみ正常に動作します。
それ以下のバージョンの端末では下記にある各種通知機能にて広告在庫切れとして通知され、機会損失が発生することはございませんのでご安心ください。

初期設定
--------

SDK(AppVadorSDK.jar)をプロジェクトに追加し、マニフェストに追記します。
追加するのは下記3点です。

まず、AndroidManifest.xmlへ下記の1行（フルスクリーンプレイヤーのアクティビティ）を追加してください。

    <activity android:name="com.appvador.ads.FullscreenActivity" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />

動画広告の再生には、Androidのハードウェアアクセラレーションが必要です。
アプリケーション全体で有効化するには下記の記述を追加ください。

    <application
        android:label="@string/app_name"
        android:theme="@style/AppTheme"
        ...
        android:hardwareAccelerated="true"> // この行を追加。falseになっている場合はtrueにする

次に、アプリケーションに下記のパーミッションを付与してください。

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

最後に、Google play servicesを適用するため、下記を追加ください。

    <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />

￼
ワイドビュービデオ広告
----------------------

16:9サイズの動画広告を、フィード内や従来のレクタングル枠などに自由に設置できる広告フォーマットです。

必要なクラスをインポートします。

    import com.appvador.ads.*;

AdViewから使用されるAdListenerをimplementします。

    public class MyActivity extends Activity implements AdListener {

    }

実装・テスト用には下記の広告枠IDをご利用ください。
**本番実装時には必ず管理画面で発行した広告枠IDに差し替えてください。広告が正しく配信できません。**

    be799a9af42fd94b851539335d3713ab

onCreateメソッド等でAdManagerを作成し、広告の準備を行います。

    AdManager mAdManager = new AdManager(this, "be799a9af42fd94b851539335d3713ab");
    mAdManager.setAdListener(this);
    mAdManager.load();

広告の表示を行うには、表示するViewGroupを指定して下記のように実行します。

    FrameLayout adLayout = (FrameLayout) findViewById(R.id.adlayout);
    mAdManager.showAd(adLayout);

AdViewクラスには広告の準備が完了しているかどうかを確認するisReadyメソッドがあります。

    if (mAdManager.isReady()) {
        Log.d("DEBUG", "ad is ready.");
        } else {
        Log.d("DEBUG", "ad is not ready.");
    }

Activityの破棄の際にはAdViewも併せてクリアしてください。

    @Override
    public void onDestroy() {
        //広告を破棄
        mAdManager.destroy();
        super.onDestroy();
    }

<!--

インタースティシャル広告
-------------------------

16:9サイズの動画広告を全画面再生する広告フォーマットです。

AndroidManifest.xmlにアクティビティを追加してください。（既に追加してある場合は不要です）

    <activity android:name="com.appvador.ads.AdActivity" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />

必要なクラスをインポートします。

    import com.appvador.ads.*;

インタースティシャル広告から使用されるAdListenerをimplementします。

    public class MyActivity extends Activity implements AdListener {

    }

実装・テスト用には下記の広告枠IDをご利用ください。
**本番実装時には必ず管理画面で発行した広告枠IDに差し替えてください。広告が正しく配信できません。**

    be799a9af42fd94b851539335d3713ab

onCreateメソッド等でAdManagerを作成します。

    // init interstitial
    interstitial = new AdManager(this, "be799a9af42fd94b851539335d3713ab");
    interstitial.setPreloadType(AdManager.PreloadType.ALL);
    interstitial.setAdListener(this);
    interstitial.load();)

次のような実装で広告の表示を行うことができます。
表示タイミングは、画面遷移の途中、ゲーム終了時など、複数のタイミングで収益化が可能です。

    if (interstitial.isReady()) {
        interstitial.showInterstitial(self);
    }

-->

高速再生モードの利用
--------------------

（インタースティシャル・ワイドビューにて共通）
通常、動画広告は呼び出されてから読込を開始するため、AdManager.showAd()から実際に動画が再生されるまで電波環境により数秒かかる可能性があります。
高速再生モードを有効にすると、AdManager.load()実行時に動画をキャッシュしておくことで、一瞬で再生を開始することができます。

        mAdManager.setPreloadType(AdManager.PreloadType.ALL);

背景色の変更
--------------------

広告枠の背景色を任意に設定できます。

        mAdManager.setBackgroundColor(int color);



広告の読込失敗、成功など各種通知の取得
--------------------------------------

（インタースティシャル・ワイドビューにて共通）
AdListenerをimplementsし下記メソッドを実装してください。
（インタースティシャル・ワイドビューにて共通）

    public class AppVadorSample extends Activity implements AdListener {

        @Override
        public void onReadyToPlayAd() {
            // AdManager.load()により読み込んだ動画広告の再生準備が完了した際に呼ばれます。
            // このタイミングでAdManager.show()することで、シームレスな再生開始が可能です。
        }

        @Override
        public void onPlayingAd() {
            // 実際に動画広告の再生が開始した際に呼ばれます。
        }

        @Override
        public void onFailedToPlayAd(ErrorCode errorCode) {
            // 何らかの理由で動画広告の読込または再生に失敗した場合に呼ばれます。
            // 他アドネットワーク等の広告表示をここに実装することで、フィラーのように動作させることが可能です。
        }

        @Override
        public void onCompletionAd() {
            // 動画広告の再生が完了した際に呼ばれます。
        }

        @Override
        public void onClickAd() {
            // 広告枠がクリックされた際に呼ばれます。
            // 通常、アプリ外で標準のブラウザへ遷移します。
        }

        @Override
        public void onUnmuteAd() {
            // 動画広告の音声がオンになった際に呼ばれます。
        }

        @Override
        public void onMuteAd() {
            // 動画広告の音声がオフになった際に呼ばれます。
        }

        @Override
        public void onCloseAd() {
            // インタースティシャルなどの閉じるボタンのある広告フォーマットにて、広告枠が閉じられた際に呼ばれます。
        }

        @Override
        public void onReplayAd() {
            // 動画広告が再生完了後にリプレイされた場合に呼ばれます。
        }
    }

proguard
--------

proguardを使用してビルドする場合、proguard-rule.pro等の設定ファイルに以下を記述ください。

    -keepclassmembers class com.appvador.** { public *; }
    -keep public class com.appvador.**
    -keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

リリースノート
---------------

### 2015/11/26 3.08

- インタースティシャル広告で一部の通知が動作しないことがある問題を修正

### 2015/10/19 3.06

- 軽微なバグフィックス

### 2015/09/01 3.05

- ローカルキャッシュの効率化のためアルゴリズムを調整


### 2015/08/19 3.04

- PreloadType.METAの場合もローカルキャッシュを行うように修正

### 2015/08/07 3.03

- 動画広告枠のInView検知ロジックを修正

### 2015/07/21 3.02

- ハードウェアアクセラレーションの有効チェック機能を追加
- 全画面プレイヤーにおけるハードウェアアクセラレーション有効化の追加

### 2015/07/21 3.01

- リプレイ通知機能を追加

### 2015/07/08 3.0

- 再生完了後の表示内容を調整

### 2015/06/25 3.0

- メモリリークが発生する不具合を修正
- Androidの一部端末でMediaPlayerクラスのバグにより動画が再生されない不具合に対策を追加

### 2015/06/15 3.0

- 再生中の画面遷移により例外が発生する不具合を修正
- 広告停止後にNullPointerExceptionが発生する不具合を修正
- 広告枠の背景が設定できるsetBackgroundColor()を追加

### 2015/06/08 3.0

- onReadyToPlayが2回呼ばれる現象を修正
- ビデオのサイズ計算が特定の状態で動作しなくなる不具合を修正
- AdManager.destroy()でAdViewを破棄するように変更

### 2015/06/04 3.0

- 初回リリース

