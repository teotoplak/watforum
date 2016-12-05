/**
 * Created by teo on 11/29/16.
 */
// This example adds a search box to a map, using the Google Place Autocomplete
// feature. People can enter geographical searches. The search box will return a
// pick list containing a mix of places and predicted search terms.

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

function initAutocomplete() {
        // var map = new google.maps.Map(document.getElementById('map'), {
        //     center: {lat: 38, lng: 260},
        //     zoom: 5,
        //     mapTypeId: 'roadmap'
        // });

    // Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
        // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    // Bias the SearchBox results towards current map's viewport.
    // map.addListener('bounds_changed', function() {
    //     searchBox.setBounds(map.getBounds());
    // });

    var markers = [];
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();
        // window.location.replace("http://localhost:9000");
        $.ajax({
            type: "GET",
            url: "/register"
        });
        if (places.length == 0) {
            return;
        }
        var finalPlace;
        var num = 0;
        places.forEach(function(place) {
            finalPlace = place;
            num++;
        });
        // map.fitBounds(bounds);
        if(num>1) {
            alert("You have selected " + num + "places. Select just one!")
        } else {
            window.location.href = "http://localhost:9000/watplace/" +  finalPlace.place_id;
        };
});
}