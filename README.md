### Objective:
To be able to note multiple entries of "paid" and "spent" for multiple instances of "person", and resolve the dues among one another

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