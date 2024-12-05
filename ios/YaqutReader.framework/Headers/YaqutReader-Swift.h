#if 0
#elif defined(__arm64__) && __arm64__
// Generated by Apple Swift version 6.0.2 effective-5.10 (swiftlang-6.0.2.1.2 clang-1600.0.26.4)
#ifndef YAQUTREADER_SWIFT_H
#define YAQUTREADER_SWIFT_H
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wgcc-compat"

#if !defined(__has_include)
# define __has_include(x) 0
#endif
#if !defined(__has_attribute)
# define __has_attribute(x) 0
#endif
#if !defined(__has_feature)
# define __has_feature(x) 0
#endif
#if !defined(__has_warning)
# define __has_warning(x) 0
#endif

#if __has_include(<swift/objc-prologue.h>)
# include <swift/objc-prologue.h>
#endif

#pragma clang diagnostic ignored "-Wauto-import"
#if defined(__OBJC__)
#include <Foundation/Foundation.h>
#endif
#if defined(__cplusplus)
#include <cstdint>
#include <cstddef>
#include <cstdbool>
#include <cstring>
#include <stdlib.h>
#include <new>
#include <type_traits>
#else
#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>
#include <string.h>
#endif
#if defined(__cplusplus)
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wnon-modular-include-in-framework-module"
#if defined(__arm64e__) && __has_include(<ptrauth.h>)
# include <ptrauth.h>
#else
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wreserved-macro-identifier"
# ifndef __ptrauth_swift_value_witness_function_pointer
#  define __ptrauth_swift_value_witness_function_pointer(x)
# endif
# ifndef __ptrauth_swift_class_method_pointer
#  define __ptrauth_swift_class_method_pointer(x)
# endif
#pragma clang diagnostic pop
#endif
#pragma clang diagnostic pop
#endif

#if !defined(SWIFT_TYPEDEFS)
# define SWIFT_TYPEDEFS 1
# if __has_include(<uchar.h>)
#  include <uchar.h>
# elif !defined(__cplusplus)
typedef uint_least16_t char16_t;
typedef uint_least32_t char32_t;
# endif
typedef float swift_float2  __attribute__((__ext_vector_type__(2)));
typedef float swift_float3  __attribute__((__ext_vector_type__(3)));
typedef float swift_float4  __attribute__((__ext_vector_type__(4)));
typedef double swift_double2  __attribute__((__ext_vector_type__(2)));
typedef double swift_double3  __attribute__((__ext_vector_type__(3)));
typedef double swift_double4  __attribute__((__ext_vector_type__(4)));
typedef int swift_int2  __attribute__((__ext_vector_type__(2)));
typedef int swift_int3  __attribute__((__ext_vector_type__(3)));
typedef int swift_int4  __attribute__((__ext_vector_type__(4)));
typedef unsigned int swift_uint2  __attribute__((__ext_vector_type__(2)));
typedef unsigned int swift_uint3  __attribute__((__ext_vector_type__(3)));
typedef unsigned int swift_uint4  __attribute__((__ext_vector_type__(4)));
#endif

#if !defined(SWIFT_PASTE)
# define SWIFT_PASTE_HELPER(x, y) x##y
# define SWIFT_PASTE(x, y) SWIFT_PASTE_HELPER(x, y)
#endif
#if !defined(SWIFT_METATYPE)
# define SWIFT_METATYPE(X) Class
#endif
#if !defined(SWIFT_CLASS_PROPERTY)
# if __has_feature(objc_class_property)
#  define SWIFT_CLASS_PROPERTY(...) __VA_ARGS__
# else
#  define SWIFT_CLASS_PROPERTY(...) 
# endif
#endif
#if !defined(SWIFT_RUNTIME_NAME)
# if __has_attribute(objc_runtime_name)
#  define SWIFT_RUNTIME_NAME(X) __attribute__((objc_runtime_name(X)))
# else
#  define SWIFT_RUNTIME_NAME(X) 
# endif
#endif
#if !defined(SWIFT_COMPILE_NAME)
# if __has_attribute(swift_name)
#  define SWIFT_COMPILE_NAME(X) __attribute__((swift_name(X)))
# else
#  define SWIFT_COMPILE_NAME(X) 
# endif
#endif
#if !defined(SWIFT_METHOD_FAMILY)
# if __has_attribute(objc_method_family)
#  define SWIFT_METHOD_FAMILY(X) __attribute__((objc_method_family(X)))
# else
#  define SWIFT_METHOD_FAMILY(X) 
# endif
#endif
#if !defined(SWIFT_NOESCAPE)
# if __has_attribute(noescape)
#  define SWIFT_NOESCAPE __attribute__((noescape))
# else
#  define SWIFT_NOESCAPE 
# endif
#endif
#if !defined(SWIFT_RELEASES_ARGUMENT)
# if __has_attribute(ns_consumed)
#  define SWIFT_RELEASES_ARGUMENT __attribute__((ns_consumed))
# else
#  define SWIFT_RELEASES_ARGUMENT 
# endif
#endif
#if !defined(SWIFT_WARN_UNUSED_RESULT)
# if __has_attribute(warn_unused_result)
#  define SWIFT_WARN_UNUSED_RESULT __attribute__((warn_unused_result))
# else
#  define SWIFT_WARN_UNUSED_RESULT 
# endif
#endif
#if !defined(SWIFT_NORETURN)
# if __has_attribute(noreturn)
#  define SWIFT_NORETURN __attribute__((noreturn))
# else
#  define SWIFT_NORETURN 
# endif
#endif
#if !defined(SWIFT_CLASS_EXTRA)
# define SWIFT_CLASS_EXTRA 
#endif
#if !defined(SWIFT_PROTOCOL_EXTRA)
# define SWIFT_PROTOCOL_EXTRA 
#endif
#if !defined(SWIFT_ENUM_EXTRA)
# define SWIFT_ENUM_EXTRA 
#endif
#if !defined(SWIFT_CLASS)
# if __has_attribute(objc_subclassing_restricted)
#  define SWIFT_CLASS(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) __attribute__((objc_subclassing_restricted)) SWIFT_CLASS_EXTRA
#  define SWIFT_CLASS_NAMED(SWIFT_NAME) __attribute__((objc_subclassing_restricted)) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
# else
#  define SWIFT_CLASS(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
#  define SWIFT_CLASS_NAMED(SWIFT_NAME) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
# endif
#endif
#if !defined(SWIFT_RESILIENT_CLASS)
# if __has_attribute(objc_class_stub)
#  define SWIFT_RESILIENT_CLASS(SWIFT_NAME) SWIFT_CLASS(SWIFT_NAME) __attribute__((objc_class_stub))
#  define SWIFT_RESILIENT_CLASS_NAMED(SWIFT_NAME) __attribute__((objc_class_stub)) SWIFT_CLASS_NAMED(SWIFT_NAME)
# else
#  define SWIFT_RESILIENT_CLASS(SWIFT_NAME) SWIFT_CLASS(SWIFT_NAME)
#  define SWIFT_RESILIENT_CLASS_NAMED(SWIFT_NAME) SWIFT_CLASS_NAMED(SWIFT_NAME)
# endif
#endif
#if !defined(SWIFT_PROTOCOL)
# define SWIFT_PROTOCOL(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) SWIFT_PROTOCOL_EXTRA
# define SWIFT_PROTOCOL_NAMED(SWIFT_NAME) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_PROTOCOL_EXTRA
#endif
#if !defined(SWIFT_EXTENSION)
# define SWIFT_EXTENSION(M) SWIFT_PASTE(M##_Swift_, __LINE__)
#endif
#if !defined(OBJC_DESIGNATED_INITIALIZER)
# if __has_attribute(objc_designated_initializer)
#  define OBJC_DESIGNATED_INITIALIZER __attribute__((objc_designated_initializer))
# else
#  define OBJC_DESIGNATED_INITIALIZER 
# endif
#endif
#if !defined(SWIFT_ENUM_ATTR)
# if __has_attribute(enum_extensibility)
#  define SWIFT_ENUM_ATTR(_extensibility) __attribute__((enum_extensibility(_extensibility)))
# else
#  define SWIFT_ENUM_ATTR(_extensibility) 
# endif
#endif
#if !defined(SWIFT_ENUM)
# define SWIFT_ENUM(_type, _name, _extensibility) enum _name : _type _name; enum SWIFT_ENUM_ATTR(_extensibility) SWIFT_ENUM_EXTRA _name : _type
# if __has_feature(generalized_swift_name)
#  define SWIFT_ENUM_NAMED(_type, _name, SWIFT_NAME, _extensibility) enum _name : _type _name SWIFT_COMPILE_NAME(SWIFT_NAME); enum SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_ENUM_ATTR(_extensibility) SWIFT_ENUM_EXTRA _name : _type
# else
#  define SWIFT_ENUM_NAMED(_type, _name, SWIFT_NAME, _extensibility) SWIFT_ENUM(_type, _name, _extensibility)
# endif
#endif
#if !defined(SWIFT_UNAVAILABLE)
# define SWIFT_UNAVAILABLE __attribute__((unavailable))
#endif
#if !defined(SWIFT_UNAVAILABLE_MSG)
# define SWIFT_UNAVAILABLE_MSG(msg) __attribute__((unavailable(msg)))
#endif
#if !defined(SWIFT_AVAILABILITY)
# define SWIFT_AVAILABILITY(plat, ...) __attribute__((availability(plat, __VA_ARGS__)))
#endif
#if !defined(SWIFT_WEAK_IMPORT)
# define SWIFT_WEAK_IMPORT __attribute__((weak_import))
#endif
#if !defined(SWIFT_DEPRECATED)
# define SWIFT_DEPRECATED __attribute__((deprecated))
#endif
#if !defined(SWIFT_DEPRECATED_MSG)
# define SWIFT_DEPRECATED_MSG(...) __attribute__((deprecated(__VA_ARGS__)))
#endif
#if !defined(SWIFT_DEPRECATED_OBJC)
# if __has_feature(attribute_diagnose_if_objc)
#  define SWIFT_DEPRECATED_OBJC(Msg) __attribute__((diagnose_if(1, Msg, "warning")))
# else
#  define SWIFT_DEPRECATED_OBJC(Msg) SWIFT_DEPRECATED_MSG(Msg)
# endif
#endif
#if defined(__OBJC__)
#if !defined(IBSegueAction)
# define IBSegueAction 
#endif
#endif
#if !defined(SWIFT_EXTERN)
# if defined(__cplusplus)
#  define SWIFT_EXTERN extern "C"
# else
#  define SWIFT_EXTERN extern
# endif
#endif
#if !defined(SWIFT_CALL)
# define SWIFT_CALL __attribute__((swiftcall))
#endif
#if !defined(SWIFT_INDIRECT_RESULT)
# define SWIFT_INDIRECT_RESULT __attribute__((swift_indirect_result))
#endif
#if !defined(SWIFT_CONTEXT)
# define SWIFT_CONTEXT __attribute__((swift_context))
#endif
#if !defined(SWIFT_ERROR_RESULT)
# define SWIFT_ERROR_RESULT __attribute__((swift_error_result))
#endif
#if defined(__cplusplus)
# define SWIFT_NOEXCEPT noexcept
#else
# define SWIFT_NOEXCEPT 
#endif
#if !defined(SWIFT_C_INLINE_THUNK)
# if __has_attribute(always_inline)
# if __has_attribute(nodebug)
#  define SWIFT_C_INLINE_THUNK inline __attribute__((always_inline)) __attribute__((nodebug))
# else
#  define SWIFT_C_INLINE_THUNK inline __attribute__((always_inline))
# endif
# else
#  define SWIFT_C_INLINE_THUNK inline
# endif
#endif
#if defined(_WIN32)
#if !defined(SWIFT_IMPORT_STDLIB_SYMBOL)
# define SWIFT_IMPORT_STDLIB_SYMBOL __declspec(dllimport)
#endif
#else
#if !defined(SWIFT_IMPORT_STDLIB_SYMBOL)
# define SWIFT_IMPORT_STDLIB_SYMBOL 
#endif
#endif
#if defined(__OBJC__)
#if __has_feature(objc_modules)
#if __has_warning("-Watimport-in-framework-header")
#pragma clang diagnostic ignored "-Watimport-in-framework-header"
#endif
@import CoreFoundation;
@import CoreGraphics;
@import Foundation;
@import ObjectiveC;
@import UIKit;
#endif

#endif
#pragma clang diagnostic ignored "-Wproperty-attribute-mismatch"
#pragma clang diagnostic ignored "-Wduplicate-method-arg"
#if __has_warning("-Wpragma-clang-attribute")
# pragma clang diagnostic ignored "-Wpragma-clang-attribute"
#endif
#pragma clang diagnostic ignored "-Wunknown-pragmas"
#pragma clang diagnostic ignored "-Wnullability"
#pragma clang diagnostic ignored "-Wdollar-in-identifier-extension"
#pragma clang diagnostic ignored "-Wunsafe-buffer-usage"

#if __has_attribute(external_source_symbol)
# pragma push_macro("any")
# undef any
# pragma clang attribute push(__attribute__((external_source_symbol(language="Swift", defined_in="YaqutReader",generated_declaration))), apply_to=any(function,enum,objc_interface,objc_category,objc_protocol))
# pragma pop_macro("any")
#endif

#if defined(__OBJC__)
@class UICollectionViewLayoutAttributes;
@class NSCoder;

/// A <code>UICollectionViewFlowLayout</code> subclass enables custom transitions between cells.
SWIFT_CLASS("_TtC11YaqutReader28AnimatedCollectionViewLayout")
@interface AnimatedCollectionViewLayout : UICollectionViewFlowLayout
/// Overrided so that we can store extra information in the layout attributes.
SWIFT_CLASS_PROPERTY(@property (nonatomic, class, readonly) Class _Nonnull layoutAttributesClass;)
+ (Class _Nonnull)layoutAttributesClass SWIFT_WARN_UNUSED_RESULT;
- (NSArray<UICollectionViewLayoutAttributes *> * _Nullable)layoutAttributesForElementsInRect:(CGRect)rect SWIFT_WARN_UNUSED_RESULT;
- (BOOL)shouldInvalidateLayoutForBoundsChange:(CGRect)newBounds SWIFT_WARN_UNUSED_RESULT;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)coder OBJC_DESIGNATED_INITIALIZER;
@end


@interface AnimatedCollectionViewLayout (SWIFT_EXTENSION(YaqutReader))
@property (nonatomic, readonly) BOOL flipsHorizontallyInOppositeLayoutDirection;
@property (nonatomic, readonly) UIUserInterfaceLayoutDirection developmentLayoutDirection;
@end


/// A custom layout attributes that contains extra information.
SWIFT_CLASS("_TtC11YaqutReader38AnimatedCollectionViewLayoutAttributes")
@interface AnimatedCollectionViewLayoutAttributes : UICollectionViewLayoutAttributes
- (id _Nonnull)copyWithZone:(struct _NSZone * _Nullable)zone SWIFT_WARN_UNUSED_RESULT;
- (BOOL)isEqual:(id _Nullable)object SWIFT_WARN_UNUSED_RESULT;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end

@class NSString;
@class NSData;
@class CIImage;

SWIFT_CLASS("_TtC11YaqutReader13AnimatedImage")
@interface AnimatedImage : UIImage
- (nullable instancetype)initWithContentsOfFile:(NSString * _Nonnull)path OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithData:(NSData * _Nonnull)data OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithData:(NSData * _Nonnull)data scale:(CGFloat)scale OBJC_DESIGNATED_INITIALIZER SWIFT_AVAILABILITY(ios,introduced=6.0);
- (nonnull instancetype)initWithCGImage:(CGImageRef _Nonnull)cgImage OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithCGImage:(CGImageRef _Nonnull)cgImage scale:(CGFloat)scale orientation:(UIImageOrientation)orientation OBJC_DESIGNATED_INITIALIZER SWIFT_AVAILABILITY(ios,introduced=4.0);
- (nonnull instancetype)initWithCIImage:(CIImage * _Nonnull)ciImage OBJC_DESIGNATED_INITIALIZER SWIFT_AVAILABILITY(ios,introduced=5.0);
- (nonnull instancetype)initWithCIImage:(CIImage * _Nonnull)ciImage scale:(CGFloat)scale orientation:(UIImageOrientation)orientation OBJC_DESIGNATED_INITIALIZER SWIFT_AVAILABILITY(ios,introduced=6.0);
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)coder OBJC_DESIGNATED_INITIALIZER;
@end



@class CALayer;

SWIFT_CLASS("_TtC11YaqutReader17AnimatedImageView")
@interface AnimatedImageView : UIImageView
/// Overrides Properties
@property (nonatomic, readonly, getter=isAnimating) BOOL animating;
@property (nonatomic, strong) UIImage * _Nullable image;
@property (nonatomic, strong) UIImage * _Nullable highlightedImage;
@property (nonatomic, copy) NSArray<UIImage *> * _Nullable animationImages;
@property (nonatomic, copy) NSArray<UIImage *> * _Nullable highlightedAnimationImages;
- (nonnull instancetype)initWithFrame:(CGRect)frame OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithImage:(UIImage * _Nullable)image OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithImage:(UIImage * _Nullable)image highlightedImage:(UIImage * _Nullable)highlightedImage OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder OBJC_DESIGNATED_INITIALIZER;
- (void)encodeWithCoder:(NSCoder * _Nonnull)aCoder;
- (void)didMoveToWindow;
- (void)didMoveToSuperview;
- (void)stopAnimating;
- (void)startAnimating;
- (void)displayLayer:(CALayer * _Nonnull)layer;
@end







/// It allows asynchronous tasks, has a pause and resume states,
/// can be easily added to a queue and can be created with a block.
SWIFT_CLASS("_TtC11YaqutReader19ConcurrentOperation")
@interface ConcurrentOperation : NSOperation
/// Set the <code>Operation</code> as asynchronous.
@property (nonatomic, readonly, getter=isAsynchronous) BOOL asynchronous;
/// Set if the <code>Operation</code> is executing.
@property (nonatomic, readonly, getter=isExecuting) BOOL executing;
/// Set if the <code>Operation</code> is finished.
@property (nonatomic, readonly, getter=isFinished) BOOL finished;
/// Start the <code>Operation</code>.
- (void)start;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end







/// A Image Download Operation runned in URLSession
SWIFT_CLASS("_TtC11YaqutReader22ImageDownloadOperation")
@interface ImageDownloadOperation : NSObject
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end

@class NSURLSession;
@class NSURLSessionTask;

@interface ImageDownloadOperation (SWIFT_EXTENSION(YaqutReader)) <NSURLSessionTaskDelegate>
- (void)URLSession:(NSURLSession * _Nonnull)session task:(NSURLSessionTask * _Nonnull)task didCompleteWithError:(NSError * _Nullable)error;
@end

@class NSURLSessionDataTask;
@class NSURLResponse;

@interface ImageDownloadOperation (SWIFT_EXTENSION(YaqutReader)) <NSURLSessionDataDelegate>
- (void)URLSession:(NSURLSession * _Nonnull)session dataTask:(NSURLSessionDataTask * _Nonnull)dataTask didReceiveResponse:(NSURLResponse * _Nonnull)response completionHandler:(void (^ _Nonnull)(NSURLSessionResponseDisposition))completionHandler;
- (void)URLSession:(NSURLSession * _Nonnull)session dataTask:(NSURLSessionDataTask * _Nonnull)dataTask didReceiveData:(NSData * _Nonnull)data;
@end


/// ImageLoadTask defines an image loading task
SWIFT_CLASS("_TtC11YaqutReader13ImageLoadTask")
@interface ImageLoadTask : NSObject
@property (nonatomic, readonly) NSUInteger hash;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end


SWIFT_CLASS("_TtC11YaqutReader7Jukebox")
@interface Jukebox : NSObject
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end



SWIFT_CLASS("_TtC11YaqutReader11JukeboxItem")
@interface JukeboxItem : NSObject
- (void)observeValueForKeyPath:(NSString * _Nullable)keyPath ofObject:(id _Nullable)object change:(NSDictionary<NSKeyValueChangeKey, id> * _Nullable)change context:(void * _Nullable)context;
@property (nonatomic, readonly, copy) NSString * _Nonnull description;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end


SWIFT_CLASS("_TtC11YaqutReader17LonginusWeakProxy")
@interface LonginusWeakProxy : NSObject
- (BOOL)respondsToSelector:(SEL _Null_unspecified)aSelector SWIFT_WARN_UNUSED_RESULT;
- (id _Nullable)forwardingTargetForSelector:(SEL _Null_unspecified)aSelector SWIFT_WARN_UNUSED_RESULT;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end


SWIFT_CLASS("_TtC11YaqutReader10MarkSlider")
@interface MarkSlider : UISlider
@property (nonatomic) float value;
- (nonnull instancetype)initWithFrame:(CGRect)frame OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)coder OBJC_DESIGNATED_INITIALIZER;
- (void)drawRect:(CGRect)rect;
@end

@class UITouch;
@class UIEvent;

@interface MarkSlider (SWIFT_EXTENSION(YaqutReader))
- (BOOL)beginTrackingWithTouch:(UITouch * _Nonnull)touch withEvent:(UIEvent * _Nullable)event SWIFT_WARN_UNUSED_RESULT;
@end

@class UICollectionView;
@class UIStoryboardSegue;
@protocol UIViewControllerTransitionCoordinator;
@class NSBundle;

/// Controller that is able to interact and navigate through pages of a <code>PDFDocument</code>
SWIFT_CLASS("_TtC11YaqutReader17PDFViewController")
@interface PDFViewController : UIViewController
/// Collection veiw where all the pdf pages are rendered
@property (nonatomic, strong) IBOutlet UICollectionView * _Null_unspecified collectionView;
- (void)viewDidLoad;
- (void)viewDidLayoutSubviews;
@property (nonatomic, readonly) BOOL prefersStatusBarHidden;
@property (nonatomic, readonly) UIStatusBarAnimation preferredStatusBarUpdateAnimation;
- (BOOL)shouldPerformSegueWithIdentifier:(NSString * _Nonnull)identifier sender:(id _Nullable)sender SWIFT_WARN_UNUSED_RESULT;
- (void)prepareForSegue:(UIStoryboardSegue * _Nonnull)segue sender:(id _Nullable)sender;
- (void)viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id <UIViewControllerTransitionCoordinator> _Nonnull)coordinator;
- (nonnull instancetype)initWithNibName:(NSString * _Nullable)nibNameOrNil bundle:(NSBundle * _Nullable)nibBundleOrNil OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)coder OBJC_DESIGNATED_INITIALIZER;
@end


@class UICollectionViewLayout;
@class NSIndexPath;

@interface PDFViewController (SWIFT_EXTENSION(YaqutReader)) <UICollectionViewDelegateFlowLayout>
- (CGSize)collectionView:(UICollectionView * _Nonnull)collectionView layout:(UICollectionViewLayout * _Nonnull)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath * _Nonnull)indexPath SWIFT_WARN_UNUSED_RESULT;
@end

@class UICollectionViewCell;

@interface PDFViewController (SWIFT_EXTENSION(YaqutReader)) <UICollectionViewDataSource>
- (NSInteger)collectionView:(UICollectionView * _Nonnull)collectionView numberOfItemsInSection:(NSInteger)section SWIFT_WARN_UNUSED_RESULT;
- (UICollectionViewCell * _Nonnull)collectionView:(UICollectionView * _Nonnull)collectionView cellForItemAtIndexPath:(NSIndexPath * _Nonnull)indexPath SWIFT_WARN_UNUSED_RESULT;
@end


@class UIScrollView;

@interface PDFViewController (SWIFT_EXTENSION(YaqutReader)) <UIScrollViewDelegate>
- (void)scrollViewDidEndDragging:(UIScrollView * _Nonnull)scrollView willDecelerate:(BOOL)decelerate;
- (void)scrollViewDidEndDecelerating:(UIScrollView * _Nonnull)scrollView;
@end


SWIFT_CLASS("_TtC11YaqutReader13RRAudioPlayer")
@interface RRAudioPlayer : UIView
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
@end




/// A dim view for use as an overlay over content you want dimmed.
SWIFT_CLASS("_TtC11YaqutReader12RRDimmedView")
@interface RRDimmedView : UIView
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
@end


SWIFT_CLASS("_TtC11YaqutReader17RRMiniAudioPlayer")
@interface RRMiniAudioPlayer : UIView
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
@end


/// A view wrapper around the presented view in a PanModal transition.
/// This allows us to make modifications to the presented view without
/// having to do those changes directly on the view
SWIFT_CLASS("_TtC11YaqutReader18RRPanContainerView")
@interface RRPanContainerView : UIView
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
@end


/// Handles the animation of the presentedViewController as it is presented or dismissed.
/// This is a vertical animation that
/// <ul>
///   <li>
///     Animates up from the bottom of the screen
///   </li>
///   <li>
///     Dismisses from the top to the bottom of the screen
///   </li>
/// </ul>
/// This can be used as a standalone object for transition animation,
/// but is primarily used in the PanModalPresentationDelegate for handling pan modal transitions.
/// note:
/// The presentedViewController can conform to RRPanModalPresentable to adjust
/// it’s starting position through manipulating the shortFormHeight
SWIFT_CLASS("_TtC11YaqutReader30RRPanModalPresentationAnimator")
@interface RRPanModalPresentationAnimator : NSObject
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end

@protocol UIViewControllerContextTransitioning;

@interface RRPanModalPresentationAnimator (SWIFT_EXTENSION(YaqutReader)) <UIViewControllerAnimatedTransitioning>
/// Returns the transition duration
- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning> _Nullable)transitionContext SWIFT_WARN_UNUSED_RESULT;
/// Performs the appropriate animation based on the transition style
- (void)animateTransition:(id <UIViewControllerContextTransitioning> _Nonnull)transitionContext;
@end


/// The PanModalPresentationController is the middle layer between the presentingViewController
/// and the presentedViewController.
/// It controls the coordination between the individual transition classes as well as
/// provides an abstraction over how the presented view is presented & displayed.
/// For example, we add a drag indicator view above the presented view and
/// a background overlay between the presenting & presented view.
/// The presented view’s layout configuration & presentation is defined using the RRPanModalPresentable.
/// By conforming to the RRPanModalPresentable protocol & overriding values
/// the presented view can define its layout configuration & presentation.
SWIFT_CLASS("_TtC11YaqutReader32RRPanModalPresentationController")
@interface RRPanModalPresentationController : UIPresentationController
/// Override presented view to return the pan container wrapper
@property (nonatomic, readonly, strong) UIView * _Nonnull presentedView;
- (void)containerViewWillLayoutSubviews;
- (void)presentationTransitionWillBegin;
- (void)presentationTransitionDidEnd:(BOOL)completed;
- (void)dismissalTransitionWillBegin;
- (void)dismissalTransitionDidEnd:(BOOL)completed;
/// Update presented view size in response to size class changes
- (void)viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id <UIViewControllerTransitionCoordinator> _Nonnull)coordinator;
- (nonnull instancetype)initWithPresentedViewController:(UIViewController * _Nonnull)presentedViewController presentingViewController:(UIViewController * _Nullable)presentingViewController OBJC_DESIGNATED_INITIALIZER;
@end


@class UIGestureRecognizer;

@interface RRPanModalPresentationController (SWIFT_EXTENSION(YaqutReader)) <UIGestureRecognizerDelegate>
/// Do not require any other gesture recognizers to fail
- (BOOL)gestureRecognizer:(UIGestureRecognizer * _Nonnull)gestureRecognizer shouldBeRequiredToFailByGestureRecognizer:(UIGestureRecognizer * _Nonnull)otherGestureRecognizer SWIFT_WARN_UNUSED_RESULT;
/// Allow simultaneous gesture recognizers only when the other gesture recognizer’s view
/// is the pan scrollable view
- (BOOL)gestureRecognizer:(UIGestureRecognizer * _Nonnull)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer * _Nonnull)otherGestureRecognizer SWIFT_WARN_UNUSED_RESULT;
@end






/// The PanModalPresentationDelegate conforms to the various transition delegates
/// and vends the appropriate object for each transition controller requested.
/// Usage:
/// \code
/// viewController.modalPresentationStyle = .custom
/// viewController.transitioningDelegate = PanModalPresentationDelegate.default
///
/// \endcode
SWIFT_CLASS("_TtC11YaqutReader30RRPanModalPresentationDelegate")
@interface RRPanModalPresentationDelegate : NSObject
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end

@class UITraitCollection;

@interface RRPanModalPresentationDelegate (SWIFT_EXTENSION(YaqutReader)) <UIPopoverPresentationControllerDelegate>
/// Dismisses the presented view controller
- (UIModalPresentationStyle)adaptivePresentationStyleForPresentationController:(UIPresentationController * _Nonnull)controller traitCollection:(UITraitCollection * _Nonnull)traitCollection SWIFT_WARN_UNUSED_RESULT;
@end


@interface RRPanModalPresentationDelegate (SWIFT_EXTENSION(YaqutReader)) <UIViewControllerTransitioningDelegate>
/// Returns a modal presentation animator configured for the presenting state
- (id <UIViewControllerAnimatedTransitioning> _Nullable)animationControllerForPresentedController:(UIViewController * _Nonnull)presented presentingController:(UIViewController * _Nonnull)presenting sourceController:(UIViewController * _Nonnull)source SWIFT_WARN_UNUSED_RESULT;
/// Returns a modal presentation animator configured for the dismissing state
- (id <UIViewControllerAnimatedTransitioning> _Nullable)animationControllerForDismissedController:(UIViewController * _Nonnull)dismissed SWIFT_WARN_UNUSED_RESULT;
/// Returns a modal presentation controller to coordinate the transition from the presenting
/// view controller to the presented view controller.
/// Changes in size class during presentation are handled via the adaptive presentation delegate
- (UIPresentationController * _Nullable)presentationControllerForPresentedViewController:(UIViewController * _Nonnull)presented presentingViewController:(UIViewController * _Nullable)presenting sourceViewController:(UIViewController * _Nonnull)source SWIFT_WARN_UNUSED_RESULT;
@end


SWIFT_CLASS("_TtC11YaqutReader12RRReaderView")
@interface RRReaderView : UIView
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
@end








@interface RRReaderView (SWIFT_EXTENSION(YaqutReader)) <UICollectionViewDelegate>
- (void)collectionView:(UICollectionView * _Nonnull)collectionView willDisplayCell:(UICollectionViewCell * _Nonnull)cell forItemAtIndexPath:(NSIndexPath * _Nonnull)indexPath;
- (void)scrollViewWillBeginDragging:(UIScrollView * _Nonnull)scrollView;
- (void)scrollViewDidEndDecelerating:(UIScrollView * _Nonnull)scrollView;
- (void)scrollViewDidEndScrollingAnimation:(UIScrollView * _Nonnull)scrollView;
@end




SWIFT_CLASS("_TtC11YaqutReader8SpeedBtn")
@interface SpeedBtn : UIButton
- (nonnull instancetype)initWithFrame:(CGRect)frame OBJC_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)coder OBJC_DESIGNATED_INITIALIZER;
@end


/// It allows synchronous tasks, has a pause and resume states, can be easily added to a queue and can be created with a block.
SWIFT_CLASS("_TtC11YaqutReader20SynchronousOperation")
@interface SynchronousOperation : ConcurrentOperation
/// Set the <code>Operation</code> as synchronous.
@property (nonatomic, readonly, getter=isAsynchronous) BOOL asynchronous;
/// Advises the <code>Operation</code> object that it should stop executing its task.
- (void)cancel;
@end


SWIFT_CLASS("_TtC11YaqutReader32TPKeyboardAvoidingCollectionView")
@interface TPKeyboardAvoidingCollectionView : UICollectionView <UITextViewDelegate>
@property (nonatomic) CGSize contentSize;
@property (nonatomic) CGRect frame;
- (nonnull instancetype)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout * _Nonnull)layout SWIFT_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
- (void)awakeFromNib;
- (void)willMoveToSuperview:(UIView * _Nullable)newSuperview;
- (void)touchesEnded:(NSSet<UITouch *> * _Nonnull)touches withEvent:(UIEvent * _Nullable)event;
- (void)layoutSubviews;
@end


@class UITextField;

SWIFT_CLASS("_TtC11YaqutReader28TPKeyboardAvoidingScrollView")
@interface TPKeyboardAvoidingScrollView : UIScrollView <UITextFieldDelegate, UITextViewDelegate>
@property (nonatomic) CGSize contentSize;
@property (nonatomic) CGRect frame;
- (nonnull instancetype)initWithFrame:(CGRect)frame SWIFT_UNAVAILABLE;
- (void)awakeFromNib;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
- (void)willMoveToSuperview:(UIView * _Nullable)newSuperview;
- (void)touchesEnded:(NSSet<UITouch *> * _Nonnull)touches withEvent:(UIEvent * _Nullable)event;
- (BOOL)textFieldShouldReturn:(UITextField * _Nonnull)textField SWIFT_WARN_UNUSED_RESULT;
- (void)layoutSubviews;
@end



SWIFT_CLASS("_TtC11YaqutReader27TPKeyboardAvoidingTableView")
@interface TPKeyboardAvoidingTableView : UITableView <UITextFieldDelegate, UITextViewDelegate>
@property (nonatomic) CGRect frame;
@property (nonatomic) CGSize contentSize;
- (nonnull instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style SWIFT_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder SWIFT_UNAVAILABLE;
- (void)awakeFromNib;
- (void)willMoveToSuperview:(UIView * _Nullable)newSuperview;
- (void)touchesEnded:(NSSet<UITouch *> * _Nonnull)touches withEvent:(UIEvent * _Nullable)event;
- (BOOL)textFieldShouldReturn:(UITextField * _Nonnull)textField SWIFT_WARN_UNUSED_RESULT;
- (void)layoutSubviews;
@end




























@class UIColor;

IB_DESIGNABLE
SWIFT_CLASS("_TtC11YaqutReader14VerticalSlider")
@interface VerticalSlider : UIControl
- (nullable instancetype)initWithCoder:(NSCoder * _Nonnull)aDecoder OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithFrame:(CGRect)frame OBJC_DESIGNATED_INITIALIZER;
@property (nonatomic) IBInspectable BOOL ascending;
- (void)layoutSubviews;
@property (nonatomic, readonly) CGSize intrinsicContentSize;
@property (nonatomic) IBInspectable float minimumValue;
@property (nonatomic) IBInspectable float maximumValue;
@property (nonatomic) IBInspectable float value;
@property (nonatomic, strong) IBInspectable UIColor * _Nullable thumbTintColor;
@property (nonatomic, strong) IBInspectable UIColor * _Nullable minimumTrackTintColor;
@property (nonatomic, strong) IBInspectable UIColor * _Nullable maximumTrackTintColor;
@property (nonatomic) IBInspectable BOOL isContinuous;
- (void)addTarget:(id _Nullable)target action:(SEL _Nonnull)action forControlEvents:(UIControlEvents)controlEvents;
- (void)removeTarget:(id _Nullable)target action:(SEL _Nullable)action forControlEvents:(UIControlEvents)controlEvents;
@end

#endif
#if __has_attribute(external_source_symbol)
# pragma clang attribute pop
#endif
#if defined(__cplusplus)
#endif
#pragma clang diagnostic pop
#endif

#else
#error unsupported Swift architecture
#endif
