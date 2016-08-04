# Android Searchable Spinner
An android dropdown widget which allows to easily filter huge list of options

## Demo
![](/screencast.gif)

##Importing the library in Android Studio
First of all download the entire repository on to your computer. Then copy the library folder present in the repository and paste it in the root folder of your Android Studio project.

Then open the settings.gradle file in your project and add the following line.
```gradle
 include ':app',':library'
```
After this open the build.gradle(Module: app) file present in your project and add the following line under dependecies.
```gradle
 compile project(":library")
```
Just sync your project gradle files and that's it. The library is now included in your project.

## Usage
Add the `SearchableSpinner` widget to your main layout file and provide an `app:list` [string-array](/demo/src/main/res/values/strings.xml) reference.
 See [activity_main.xml](/demo/src/main/res/layout/activity_main.xml)

```xml
<com.rajasharan.widget.SearchableSpinner
    android:id="@+id/search"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:padding="5dp"
    app:list="@array/all_languages"
    />
```

#### Setup OnSelectionChangeListener

```java
public class MainActivity extends ActionBarActivity implements SearchableSpinner.OnSelectionChangeListener {

    private SearchableSpinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (SearchableSpinner) findViewById(R.id.search);
        mSpinner.setOnSelectionChangeListener(this);
    }

    @Override
    public void onSelectionChanged(String selection) {
        Toast.makeText(this, selection + " selected", Toast.LENGTH_SHORT).show();
    }
}
```

## [License](/LICENSE)
    The MIT License (MIT)
