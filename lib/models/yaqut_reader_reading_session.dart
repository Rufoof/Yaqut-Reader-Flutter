import 'package:yaqut_reader_plugin/constants/constants.dart';

class YaqutReaderReadingSession {
  int bookId;
  int bookFileId;
  int pagesRead;
  int startOffset;
  int endOffset;
  List<int> coveredOffset;
  List<int> coveredLength;
  double startTime;
  double endTime;
  String? md5;
  String? uuid;

  YaqutReaderReadingSession({
    required this.bookId,
    required this.bookFileId,
    required this.pagesRead,
    required this.startOffset,
    required this.endOffset,
    required this.coveredOffset,
    required this.coveredLength,
    required this.startTime,
    required this.endTime,
    required this.md5,
    required this.uuid,
  });

  Map<String, dynamic> toJson() {
    return {
      constBookId: bookId,
      constBookFileId: bookFileId,
      constPagesRead: pagesRead,
      constStartOffset: startOffset,
      constEndOffset: endOffset,
      constCoveredOffset: coveredOffset,
      constCoveredLength: coveredLength,
      constStartTime: startTime,
      constEndTime: endTime,
      constMd5: md5,
      constUuid: uuid,
    };
  }

  factory YaqutReaderReadingSession.fromJson(Map<String, dynamic> json) {
    return YaqutReaderReadingSession(
      bookId: json[constBookId] as int,
      bookFileId: json[constBookFileId] as int,
      pagesRead: json[constPagesRead] as int,
      startOffset: json[constStartOffset] as int,
      endOffset: json[constEndOffset] as int,
      coveredOffset: json[constCoveredOffset] as List<int>,
      coveredLength: json[constCoveredLength] as List<int>,
      startTime: json[constStartTime] as double,
      endTime: json[constEndTime] as double,
      md5: json[constMd5] as String? ?? json["MD5"] as String? ?? "",
      uuid: json[constUuid] as String? ?? json["UUID"] as String? ?? "",
    );
  }
}
