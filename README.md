
Gradle Template Project
=======================

Hello, you're reading this because you want to start a new Android project
with Gradle. Or at least, I hope you are.

All you need to do is run rename some packages and you're off.

_(It might also be nice idea to check you're using the latest versions of
everything & delete the stuff you don't want before you start)_


Lorem Ipsum
-----------

The classes starting with Lorem are there to show you how to use some of the
3rd party libraries and Android features that are included in here.


Features & Libraries
--------------------

### Fragments, Loaders & AppCompat

Your UI should be implemented in support library `Fragments` and these should
be controlled by an `ActionBarActivity`.

Even if you find yourself writing an `Activity` that holds a single `Fragment`
or doesn't use the `ActionBar`, stick to using these for consistency and to
make your life easier when someone changes their mind later on.

`Loaders` are our favourite way to offload work to the background and get the
result back. They can survive config changes and are pretty easy to use. Extend
`BaseLoader<T>` and create a static inner class that implements `LoaderCallbacks`
for 99% of cases. See `LoremLoader`.

### Splash Screen

Nobody likes them, but we always have them. Add whatever you want in there. Be
sure to use the splash screen theme for background colors etc. so the the app
launch is as nice as possible.

### DrawerLayout

All our apps seem to need this these days. Have a look at `MenuFragment` and
change appropriately. All the messy `DrawerLayout` lifecycle stuff is in
`AbstractMenuActivity`.

### OkHttp & Retrofit

Retro fit is our current favorite REST library. Try and keep a single instance
as it's thread safe and can do caching and cookies if you let it.

OkHttp should be used automatically by Retrofit, so don't worry too much about that.

### Picasso

Easy image loading.

### Butterknife & Dagger

A nice pair of injection libraries. See how `TemplateApplication` has implemented
injection. You can use it in your `Activities` and `Fragments` to add dependencies
in a nice modular way.

Be careful not to make a mess, as it's very easy to do.

Butterknife's similar, but for `Views`.


Releases
--------

When logging, try and remember to use `Timber.d()`, `.e()`, etc. as these will be hidden
from release builds.

For logging method calls, add `@DebugLog` to the method and Hugo will add log it for you.
These are automatically removed from release builds too.


TODO
----

I'm in the proccess of writing a do-da to upload to Testfairy automatically, I'll let you
know when it's ready.
