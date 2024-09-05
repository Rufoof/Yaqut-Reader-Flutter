class ReaderStyle {
  int readerColor;
  int textSize;
  bool isJustified;
  int lineSpacing;
  int font;

  ReaderStyle(
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
}
