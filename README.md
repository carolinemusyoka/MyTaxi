# MyTaxi
App consuming myTaxi api to show taxi vehicles list on google maps. The list should show all the vehicle data in the bounds of Hamburg (53.694865,
9.757589 & 53.394655, 10.099891).

Endpont to get the vehicles: 
https://fake-poi-api.mytaxi.com/?p1Lat={Latitude1}&p1Lon={Longitude1}&p2Lat={Latitude2}&p2Lon={Longitude2}

in this case, https://fake-poi-api.mytaxi.com/?p1Lat=53.694865&p1Lon=9.757589&p2Lat=53.394655&p2Lon=10.099891


Example response: 

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
        
        
