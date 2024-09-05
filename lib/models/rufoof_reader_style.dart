class RufoofReaderStyle {
  int readerColor;
  int textSize;
  bool isJustified;
  int lineSpacing;
  int font;

  RufoofReaderStyle(
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
