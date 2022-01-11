# MyTaxi App

App consuming myTaxi api to show taxi vehicles list on google maps. The list should show all the vehicle data in the bounds of Hamburg (53.694865,
9.757589 & 53.394655, 10.099891).

Endpont to get the vehicles: 
https://fake-poi-api.mytaxi.com/?p1Lat={Latitude1}&p1Lon={Longitude1}&p2Lat={Latitude2}&p2Lon={Longitude2}

in this case, https://fake-poi-api.mytaxi.com/?p1Lat=53.694865&p1Lon=9.757589&p2Lat=53.394655&p2Lon=10.099891


#### Example response: 

```json
"poiList": [
        {
            "id": 802714,
            "coordinate": {
                "latitude": 53.66960373842644,
                "longitude": 9.770707237261918
            },
            "fleetType": "POOLING",
            "heading": 74.56956474495095
        },
        {
            "id": 219149,
            "coordinate": {
                "latitude": 53.39808833026627,
                "longitude": 9.800280946838797
            },
            "fleetType": "POOLING",
            "heading": 65.00681325645445
        },
```        
 It's a pretty simple app
  #### Launch app -> view a list of all vehicles( horizontal orientation) -> click view all to see all markers of the vehiclles available -> click a single item to isolate the selected vehicle on the map and zoom to its location -> click map to display the steet name. 
  
### Screenshots and recordings

<img src="https://user-images.githubusercontent.com/44951692/148917345-187683df-eb22-4efc-b70b-83af7eb95d36.jpg" width=30% height=30%> <img src="https://user-images.githubusercontent.com/44951692/148917384-7396ba9d-091c-4c65-9fb9-77a5b2f66fa5.jpg" width=30% height=30%> <img src="https://user-images.githubusercontent.com/44951692/148917360-f7dd6c6e-42cc-4ae8-ad59-f146f9b9269b.jpg" width=30% height=30%>



https://user-images.githubusercontent.com/44951692/148918420-09fc7ea0-6564-421a-8d70-74c4b83b4719.mp4



### A few gists that I would like to single out in this project;

#### - To zoom map to given bounds
```kotlin
val builder = LatLngBounds.Builder()
        val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
        builder.include(locBounds.southwest)
        builder.include(locBounds.northeast)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
```
#### - Show markers of all vehicles on the map
 I saved the list of vehicles in case I would need it in another fragment which I didn't so ;-|
 In the viewmodel, 
 
 ``` kotlin
 val vehicles = MutableLiveData<List<Poi>>()

    fun setVehicles(poi: List<Poi>){
        vehicles.value = poi
    }
```

#### - Retrieve the list, I used forEach in place of for loop make the code more concise and add markers at the given locations


```kotlin
 mainViewModel.vehicles.observe(viewLifecycleOwner, {
            val data = it
            Log.d("TAG", "startMapNewList:$data ")
            data.forEach { poi ->
                val latitude = poi.coordinate.latitude
                val longitude = poi.coordinate.longitude
                val location = LatLng(latitude, longitude)
                Log.d("TAG", "Location: $location")
                listLocation.add(location)
            }
            Log.d("TAG", "LocationArray:$listLocation ")
            listLocation.forEach { place ->
               locationMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place)
                        .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
                )!!
                allMarkers.add(locationMarker)

            }

        })
```

