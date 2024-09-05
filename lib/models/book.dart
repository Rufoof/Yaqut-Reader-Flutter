class Book {
  final int bookId;
  final String title;
  final String? subtitle;
  final String? description;
  final String? language;
  final String? yearPublished;
  final String? coverThumbUrl;
  final double? price;
  final double? retailPrice;
  final double? rating;

  Book({
    required this.bookId,
    required this.title,
    this.subtitle,
    this.description,
    this.language,
    this.yearPublished,
    this.coverThumbUrl,
    this.price,
    this.retailPrice,
    this.rating,
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      bookId: json['bookId'] as int,
      title: json['title'] as String,
      subtitle: json['subtitle'] as String?,
      description: json['description'] as String?,
      language: json['language'] as String?,
      yearPublished: json['yearPublished'] as String?,
      coverThumbUrl: json['coverThumbUrl'] as String?,
      price: json['price'] as double?,
      retailPrice: json['retailPrice'] as double?,
      rating: json['rating'] as double?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'bookId': bookId,
      'title': title,
      'subtitle': subtitle,
      'description': description,
      'language': language,
      'yearPublished': yearPublished,
      'coverThumbUrl': coverThumbUrl,
      'price': price,
      'retailPrice': retailPrice,
      'rating': rating,
    };
  }
}
