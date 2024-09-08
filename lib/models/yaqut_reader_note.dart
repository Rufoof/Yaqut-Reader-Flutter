import 'package:yaqut_reader_plugin/constants/constants.dart';

class YaqutReaderNote {
  final int bookId;
  final int fromOffset;
  final int toOffset;
  final int markColor;
  final int type;
  final String? displayText;
  final int? deleted;
  final int? local;

  YaqutReaderNote({
    required this.bookId,
    required this.fromOffset,
    required this.toOffset,
    required this.markColor,
    required this.type,
    this.displayText,
    this.deleted,
    this.local,
  });

  factory YaqutReaderNote.fromJson(Map<String, dynamic> json) {
    return YaqutReaderNote(
      bookId: json[constBookId] as int,
      fromOffset: json[constFromOffset] as int,
      toOffset: json[constToOffset] as int,
      markColor: json[constMarkColor] as int,
      type: json[constType] as int,
      displayText: json[constDisplayText] as String?,
      deleted: json[constDeleted] as int?,
      local: json[constLocal] as int?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      constBookId: bookId,
      constFromOffset: fromOffset,
      constToOffset: toOffset,
      constMarkColor: markColor,
      constType: type,
      constDisplayText: displayText,
      constDeleted: deleted,
      constLocal: local,
    };
  }
}
