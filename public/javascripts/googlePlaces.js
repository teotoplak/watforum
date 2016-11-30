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

            // Clear out the old markers.
            //     markers.forEach(function(marker) {
            //         marker.setMap(null);
            //     });
            //     markers = [];

        // For each place, get the icon, name and location.
            // var bounds = new google.maps.LatLngBounds();
        var num = 0;
        var finalPlace;
        places.forEach(function(place) {
            // console.log(JSON.stringify(place))

                // if (!place.geometry) {
                //     console.log("Returned place contains no geometry");
                //     return;
                // }
                // var icon = {
                //     url: place.icon,
                //     size: new google.maps.Size(71, 71),
                //     origin: new google.maps.Point(0, 0),
                //     anchor: new google.maps.Point(17, 34),
                //     scaledSize: new google.maps.Size(25, 25)
                // };

            // Create a marker for each place.
                // markers.push(new google.maps.Marker({
                //     map: map,
                //     icon: icon,
                //     title: place.name,
                //     position: place.geometry.location
                // }));

                // if (place.geometry.viewport) {
                //     // Only geocodes have viewport.
                //     bounds.union(place.geometry.viewport);
                // } else {
                //     bounds.extend(place.geometry.location);
                // }
            finalPlace = place;
            num++;
        });
        // map.fitBounds(bounds);
        if(num>1) {
            alert("You have selected " + num + "places. Select just one!")
        } else {
            xhr = new XMLHttpRequest();
            var url = "http://localhost:9000/search";
            xhr.open("POST", url, false);
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var gogo = xhr.responseText;
                    ReplaceContent(gogo)
                }
            }
            var data = JSON.stringify(finalPlace);
            xhr.send(data);

            // var form = document.createElement('form');
            // form.type = 'application/json';
            // form.method = 'post';
            // form.action = 'http://localhost:9000/search';
            // form.value = JSON.stringify(finalPlace);
            // form.submit();

            // $.post({
            //     url: 'http://localhost:9000/search',
            //     dataType: 'json',
            //     contentType: "application/json; charset=utf-8",
            //     data: JSON.stringify(finalPlace),
            //     type: 'POST'
            // }, function(response){
            //     /*Callback*/
            //     alert(response);
            //     document.location='http://localhost:9000/search';
            // });
            function ReplaceContent(myString) {
                var newDoc = document.open("text/html", "replace");
                newDoc.write(myString);
                newDoc.close();
            }

        }
    });
}