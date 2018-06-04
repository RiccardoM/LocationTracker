# Location Tracker 
The main purpose of this application is to demonstrate how it's possible to track a user location
using a background service that runs even if the application is inside the background.

## Views
The application is composed of four different views.

### Main view
Inside the main view it's possibile to browse through the map view and the journeys list view.
It represents the main view of the application, and is responsible for asking the permission to use
the location when the app is first started.

By tapping on the floating button, the location will be activated and a new journey will start. If a
journey is already in progress, on the other hand, the current journey will be ended.

### Map view
Inside the map view (aka Journey view) it's possibile to see the status of the current journey if
the location tracking is enabled, or just the last known location if the location tracking is
disabled.

### Journey list view
Inside the journey list view, the user will be able to see the all the past journeys that he has
travelled. By clicking on a journey, he will be brought to the [details view](#journey-details-view)

### Journey details view
Inside this view the user has the opportunity to see the details of a single journey. The currently
shown details are
* the journey start date
* the journey end date
* the total time took by the journey
* the distance travelled
* the average speed
* a map containing all the locations that have been recorded during the journey


## Considerations about the project
### Code structure
The structure of the code follows the principles of the [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html).
I chose this pattern becasuse it's the one I'm most comfortable programming in and in the past I've
seen that it 's very good when it comes down to modify functionalities of the code or fix bugs.
The main objective of this pattern is to keep the code well structured and to separate the UI code
from the logic code.

To implement this pattern I didn't create the usecase classes just to avoid wasting some time on
them. The presents call directly the repositories interfaces whose imlementations lye on the
interface layer (the same of the presenters' themselves). The interfaces are used to permit the
dependency inversion principle, which is implemented also using the dependency injection technique
using Dagger 2.

### Data encryption
#### Database encryption
Currently the data that is saved inside the database is not encrypted. In order to encyrpt it and
make it more safe for the user, we could use [CWAC-SafeRoom](https://github.com/commonsguy/cwac-saferoom),
a library that implements the Room ability to work with other SQL databases by using [SQLCipher](https://www.zetetic.net/sqlcipher/sqlcipher-for-android/).
By using this library, we should be able to encrypt the database using the 256-bit AES which should
grant enough security for the data that we store.

#### Column encryption
Another approach in order to encrypt the data could be to encrypt individually the single cells tha are written inside the
database instead of ecnrypting the whole database at once. This could provide a safer way to
store the data, but could also lead to a overhead during data saving and loading. While this method provides a more atomic
way when it comes to choose what to ecnrypt and what to not, it often leads to a 5-6% overhead on common operations.

### Data persistence
Currently data is saved inside the Room database which is wipe when the user uninstalls the
application. In order to fix this problem, we have two kinds of solution.

#### 1. Online storage
The first soluction, which is the worst in my opinion, is to make the user create an account on
our server, and the upload the data that he collects to it so then we can retrieve them we we
want.

#### 2. Internal storage
The second option, which is the best in my opinion, it to implement a manual operation that takes
all the data that have been stored inside the database since that point in time, and then save
them to a second database which is stored inside the internal storage of the device. Doing so we
would allow the user to have full control over the saving of the data and we can also implement
an option to auto save those data on the backup database.
When the user will uninstall the application, the data will persist inside the device and will be
later possible to retrieve them once the user re-installs the app itself.

### Auto start the tracking
The last improvement we can implement is the auto tracking of the user based on his movements. This
means to start the location tracking when the user starts to move in a battery-efficient way.
To do so, we could act in different ways.

#### 1. Location tracking
The first way is to implement a service that constantly checks for the user location computing the
distance in meters from his last location. The check will be performed once in a while, without
consuming the battery too much.

#### 2. Accelerometer check
We could use the accelerometer in order to check the current speed at which the user starts moving.
If he starts moving very quickly, then we can assume that he is walking, running or else, and we
can start tracking his location.
Also, with the accelerometer, we could define what movements the phone is making and so determine
the action that the user is performing (e.g. walking, running, ...).


## Libraries used
* [Crashlytics](https://docs.fabric.io/android/crashlytics/overview.html) for production error logging
* [Dagger 2](https://github.com/google/dagger) to implement the dependecy injection technique
* [EventBus](https://github.com/greenrobot/EventBus) for event-oriented programming
* [Gson](https://github.com/google/gson) for easy object serialization and deserialization
* [Room](https://developer.android.com/topic/libraries/architecture/room) for data persistance
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) for the observable pattern
* [ThirtyInch](https://github.com/grandcentrix/ThirtyInch) to implement the view ↔ presenter
  comunication
* [Timber](https://github.com/JakeWharton/timber) for easier logging
