### Objective:
A calculator with memory to show how much each person paid and spent and shows the net difference. If the net difference is positive, he paid more than he spent, hence owes that net amount. Another person may have a negative net difference, hence he needs to collect that amount from others.

### Constraint Layout
Use constraint layout as the views can be arranged relative to each other.
* Use Vertical Guidelines to create a universal offset of 8dp from either edge
* Use Horizontal Guidelines, defined by percentage, around midway of the screen. The area below the guideline will be the calculator area
* Use chains, horizontally and vertically so that the buttons space themselves out in the area. Button width and height should match constraint so they fill up the space.
* This creates a clean matrix of buttons, which would be scalable to different screen sizes

### ArrayMap
Define the array map with data types of the Key and Value. "int" is a primitive type, is not allowed. Use "Integer" instead.

### Logging
use Log.d, first param use a const string as a Tag, then build the debug string for the second param
* private static final String TAG = "MyApp";
* Log.d(TAG, "some string");

### Calculator
Two-line display. 0-9 keys. Plus, Minus, times, divide, equals, clear

### Scrollable "Person" window
This is the most interesting and tricky part. The container is called a "ViewPager" where user can swipe through "pages". Each page is defined using a Fragment. Fragments are managed by a FragmentStatePagerAdapter which has a FragmentManager. There's two different Fragments that's part of this view:
* Page to show the each person's Net amount
* Page to add a new person (always the most right i.e. last page)

When a new person is added using the button on the last page, a new Fragment is instantiated and added to the ViewPager via the manager and adapter. When user browses through the persons, the ViewPager's page change listener to capture the event. This event is used to load that person's paid/spent list into the column.