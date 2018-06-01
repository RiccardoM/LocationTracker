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


## Considerations about the code
The structure of the code follows the principles of the [Clean Architecture](
