class YaqutReaderBook {
  final int bookId;
  final int bookFileId;
  final String title;
  final String? subtitle;
  final String? description;
  final String? language;
  final String? yearPublished;
  final String? coverThumbUrl;
  final double? price;
  final double? retailPrice;
  final double? rating;
  double? previewPercentage;
  int? position;
  List<Map<String, dynamic>>? notesAndMarks;

  YaqutReaderBook({
    required this.bookId,
    required this.bookFileId,
    required this.title,
    this.subtitle,
    this.description,
    this.language,
    this.yearPublished,
    this.coverThumbUrl,
    this.price,
    this.retailPrice,
    this.rating,
    this.previewPercentage,
    this.position,
    this.notesAndMarks,
  });

  factory YaqutReaderBook.fromJson(Map<String, dynamic> json) {
    return YaqutReaderBook(
      bookId: json['bookId'] as int,
      bookFileId: json['bookFileId'] as int,
      title: json['title'] as String,
      subtitle: json['subtitle'] as String?,
      description: json['description'] as String?,
      language: json['language'] as String?,
      yearPublished: json['yearPublished'] as String?,
      coverThumbUrl: json['coverThumbUrl'] as String?,
      price: json['price'] as double?,
      retailPrice: json['retailPrice'] as double?,
      rating: json['rating'] as double?,
      previewPercentage: json['previewPercentage'] as double?,
      position: json['position'] as int?,
      notesAndMarks: (json['notesAndMarks'] as List<dynamic>?)
          ?.map((item) => Map<String, dynamic>.from(item as Map))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'bookId': bookId,
      'bookFileId': bookFileId,
      'title': title,
      'subtitle': subtitle,
      'description': description,
      'language': language,
      'yearPublished': yearPublished,
      'coverThumbUrl': coverThumbUrl,
      'price': price,
      'retailPrice': retailPrice,
      'rating': rating,
      'previewPercentage': previewPercentage,
      'position': position,
      'notesAndMarks': notesAndMarks
          ?.map((item) => Map<String, dynamic>.from(item))
          .toList(),
    };
  }
}
