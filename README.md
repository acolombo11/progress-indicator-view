# progress-indicator-view
Animated view showing determinate progress in a fun stupid way, mixing together PageIndicatorView and ProgressBar.

![big parking lot](docs/gifs/Progress_Indicator_View_Example_2019-07-01-14-57-22.gif)
![small parking lot](docs/gifs/Progress_Indicator_View_Example_2019-07-01-14-58-38.gif)

## Download &nbsp; [![Release](https://jitpack.io/v/eu.acolombo/progress-indicator-view.svg)](https://jitpack.io/#eu.acolombo/progress-indicator-view)

Add the dependency in your app  `build.gradle` with the current version number: 

```gradle
implementation 'eu.acolombo:progress-indicator-view:0.3.5'
```

If you haven't done so already for other libraries, you also have to add JitPack in your root `build.gradle`:
```gradle
allprojects {
    repositories {
        ..
        maven { url 'https://jitpack.io' }
    }
}
```

This library is AndroidX only, if you are still using Support libraries you can either migrate your app to AndroidX or you can contribute by downgrading the dependencies and subitting a pull-request, which will be merged in a different branch.
