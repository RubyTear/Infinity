using System;

// クラスの前にはそのクラスの説明を書いたほうがいい。

//「///」から始まるコメントはC#では特別な意味を持つ。
// 詳しくは「XML Documentation」で説明する。

/// <summary>
/// コメント付けのサンプルプログラム。
/// ここでは例として配列で与えられたデータ列の平均値と分散を求めて表示する。
/// </summary>
class CommentSample
{
  static void Main()
  {
    // 変数名の後に変数の説明を書いたりすることも。
    // ほんとは、コメントが無くても意味の分かる変数名を付けるべき。

    var dataSource = new[] {
      455,  58,   8,   7, 987,  56,   2,  64, 698,  79,
       98,  79,  45, 465, 167,  97,  94, 657, 237, 587,
      687, 654, 647,   4, 654, 984,   8, 489,   7,  22 }; // データ列
    double mean; // 平均値
    double variance;  // 分散

    // 処理の区切りごとに、処理の内容の簡単な説明を書いたり。
    // これも、できれば、コメントなんて書かなくても分かりやすい簡潔な処理を書く方がいい。
    // (「関数の前にだけ説明があれば十分」と言うのが理想。
    //   要するに、処理の区切りごとに関数に分かれてる方がいい。
    // コメントが必要そうな処理の区切りがあったら、そこを関数化する。)

    // データ列 dataSource の平均値と分散を求める
    CalcMean(dataSource, out mean, out variance);

    // 結果の表示
    Console.WriteLine("平均 : {0}, 分散 : {1}", mean, variance);
  }

  // 関数の前にはその関数の説明を書く。

  /// <summary>
  /// 配列に入ったデータの平均値と分散を求める
  /// <param name="data">与えられたデータ列</param>
  /// <param name="mean">dataの平均値(出力)</param>
  /// <param name="variance">dataの分散(出力)</param>
  /// </summary>
  static void CalcMean(int[] data, out double mean, out double variance)
  {
    int sum = 0;     // 合計
    int sq_sum = 0;  // 二乗の合計

    // データ列の合計と二乗の合計を求める
    foreach(int n in data)
    {
      sum += n;
      sq_sum += n*n;
    }
    // 平均値と分散を計算
    mean = (double)sum / data.Length;
    variance = (double)sq_sum / data.Length - mean*mean;
  }
}
