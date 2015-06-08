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

まず、AndroidManifest.xmlへ下記の1行を追加してください。

    <activity android:name="com.appvador.ads.AdActivity" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />

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

    AdView mAdManager = new AdManager(this, "be799a9af42fd94b851539335d3713ab");
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

高速再生モードの利用
--------------------

（インタースティシャル・ワイドビューにて共通）
通常、動画広告は呼び出されてから読込を開始するため、AdManager.showAd()から実際に動画が再生されるまで電波環境により数秒かかる可能性があります。
高速再生モードを有効にすると、AdManager.load()実行時に動画をキャッシュしておくことで、一瞬で再生を開始することができます。

        mAdManager.setPreloadType(AdManager.PreloadType.ALL);

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
    }

広告の読み込みに失敗した際は、failedToReceiveAdメソッドにエラーコードが渡されます。
エラーコードの内容はそれぞれ下記の通りです。

| 定数 | メッセージ | 内容 |
| ---- | ---------- | ---- |
| NO_AD | No ads found. | 配信可能な広告がありません。 |
| PUBLICATION_NOT_FOUND | Publication not found. Please check publication id. | 広告枠が見つかりません。広告枠IDを確認してください。 |
| NETWORK_ERROR | Unable to connect to server. | サーバーに接続できませんでした。ネットワーク環境に問題があります。 |
| SERVER_ERROR | Invalid response from server. | サーバーからのレスポンスが不正でした。広告リクエスト中に切断などの問題が発生しました。 |
| INTERNAL_ERROR | Unable to serve ad due to invalid internal state. | 広告の配信に失敗しました。 |

リリースノート
---------------


### 2015/06/08 3.0

bugfix

- onReadyToPlayが2回呼ばれる現象を修正
- ビデオのサイズ計算が特定の状態で動作しなくなる不具合を修正

### 2015/06/04 3.0

- 初回リリース

