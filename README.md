# Paper Plugin Template
CraftersLifeの未来ある若者のために作った高機能テンプレート

右上の [use this template\] を押せばテンプレートを使用可能

## Gradle プラグイン

### [indra licenser spotless](https://github.com/KyoriPowered/indra/wiki/indra-licenser-spotless)

- **概要**: 全クラスのソースコードに一括でライセンスヘッダーを挿入
- **メリット**: IntelliJ IDEAなどのIDEで個別に設定する手間を省き、開発環境が変わっても一貫したライセンス管理が可能
  - 押したら俺のライセンスが全部消えるというすぐれもの！gitに残るから別にいいよ

### [gremlin](https://github.com/jpenilla/gremlin)

- **概要**: Paperプラグイン実行時に必要な依存関係を解決する
- **メリット**: [PluginLoader](https://docs.papermc.io/paper/dev/getting-started/paper-plugins#loaders) の仕組みを利用してプラグインの依存関係を動的に解決可能

### [Resource Factory](https://github.com/jpenilla/resource-factory)

- **概要**: `paper-plugin.yml` をビルドスクリプトで定義
- **メリット**: 設定ミスの削減と管理の簡略化

### [run-task](https://github.com/jpenilla/run-task)

- **概要**: IDE上で Paperサーバーを実行
- **メリット**:
  - サーバー起動だけでプラグインのテストが可能
  - 依存プラグインを自動インストール
  - ホットスワップでサーバー起動中にコードの変更が可能

### [check-style](https://docs.gradle.org/current/userguide/checkstyle_plugin.html)

- **概要**: コード品質を一定水準に保つ
- **メリット**: 設計上の問題検出、コードレイアウトやフォーマットがコーディングスタイルに準拠しているかのチェックが可能
  - 波括弧のインデントを揃える人間はストレスで病む

## Java ライブラリ

### [Paper API](https://docs.papermc.io/paper/dev/getting-started/paper-plugins)

- **概要**: 説明不要
- **メリット**:
  - サーバー起動前に設定やデータベースなどのリソースを初期化可能
    - Bukkitの一部API（例: `Server` や `ItemStack`）は、 `JavaPlugin#onEnable` 以降での呼び出しが必須であることに注意が必要
    - JavaPluginの具象クラスの初期化もコントロール可能
  - Mojangの[Brigadier](https://github.com/Mojang/brigadier)を基にしたコマンドの登録
  - レジストリの編集（現状はエンチャントのみ対応）
  - 実行時に必要なランタイムを自動ダウンロード
  - カラーコードを駆逐せよ

### [Configurate](https://github.com/SpongePowered/Configurate?tab=readme-ov-file)

- **概要**: SpongePoweredが開発した設定ライブラリ
- **メリット**:
  - Javaオブジェクトと設定ファイルの相互変換
  - カスタムType Serializerによる文字列変換

### [MiniPlaceholders](https://github.com/MiniPlaceholders/MiniPlaceholders)

- **概要**: MiniMessageのプレースホルダーを拡張するライブラリ 
- **メリット**:
  - 他プラグインが提供するタグを利用可能
  - 他プラグインに提供するタグを登録可能
  - [PlaceholderAPI拡張](https://modrinth.com/plugin/miniplaceholders-placeholderapi-expansion)もあるよ

### [Doburoku](https://github.com/NamiUni/doburoku)

- **概要**: 翻訳をDRYに管理できるライブラリ
- **メリット**:
    - インターフェースを定義するだけでリソースバンドルを生成可能
    - 引数をComponentLikeに解決する機能
    - 生成されたTranslatableComponentを戻り値に応じて操作してから返却する機能

## 使い方

`ResourceContainer`でリソースを管理し、`ServiceContainer`でビジネスロジックを管理する。イベントリスナーやコマンド内にビジネスロジックを直接書かない。

- 設定は`PrimaryConfig`にコンポーネントを追加する
  - 追加したコンポーネントは生成済みの設定ファイルには自動適用されないため、設定ファイルに反映したい場合は一度削除する必要あり
  - コンポーネントは[このページ](https://github.com/SpongePowered/Configurate/wiki/Type-Serializers)に存在する型であればシリアライズ可能。なければシリアライザーを自分で登録する必要あり
- 翻訳は`MessageService`か`LogggingService`に追加する
  - `MessageService`はプレイヤーなどに送信するメッセージ
  - `LoggingService`はログに記録するメッセージ
  - メソッドの引数は`ComponentLike`でなければ`Object#toString`を呼び出したあとに`Component.text(String)`でラップされる。不都合があればリゾルバ―を自分で登録する

## おわりに

このREADMEが、CraftersLifeのPaperプラグイン開発の参考として役立つことを願っています。
不明点や改善案などがあればフィードバックをくれ～
