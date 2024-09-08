class YaqutReaderStyle {
  int readerColor;
  int textSize;
  bool isJustified;
  int lineSpacing;
  int font;

  YaqutReaderStyle(
      {required this.readerColor,
      required this.textSize,
      required this.isJustified,
      required this.lineSpacing,
      required this.font});

  Map<String, dynamic> toJson() {
    return {
      'readerColor': readerColor,
      'textSize': textSize,
      'isJustified': isJustified,
      'lineSpacing': lineSpacing,
      'font': font
    };
  }

  factory YaqutReaderStyle.fromJson(Map<String, dynamic> json) {
    return YaqutReaderStyle(
      readerColor: json['readerColor'] as int,
      textSize: json['textSize'] as int,
      isJustified: json['isJustified'] as bool,
      lineSpacing: json['lineSpacing'] as int,
      font: json['font'] as int,
    );
  }
}
