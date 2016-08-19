/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.xyztouristattractions.provider;

import android.net.Uri;
import android.os.StrictMode;

import com.example.android.xyztouristattractions.common.Attraction;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static data content provider.
 */
public class TouristAttractions {

    public static final String CITY_SYDNEY = "Sydney";

    public static final String TEST_CITY = CITY_SYDNEY;

    private static final float TRIGGER_RADIUS = 2000; // 2KM


    public static final Map<String, LatLng> CITY_LOCATIONS = new HashMap<String, LatLng>() {{
        put(CITY_SYDNEY, new LatLng(-33.873651, 151.2068896));
    }};

    static List<Attraction> attractions = null;

    public static synchronized List<Attraction> get() {
        if(attractions != null) {
            return attractions;
        }
        else {
            try {
                attractions = new ArrayList<Attraction>();
                // TODO Load in a different thread and show wait dialog
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);


                //URL file = new URL("http://nicolas.raoul.free.fr/lab/wikishootme-test.csv");
                URL file = new URL("https://tools.wmflabs.org/wiki-needs-pictures/data/data.csv");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(file.openStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    String[] fields = line.split(",");
                    String name = fields[0];

                    double latitude;
                    double longitude;
                    try {
                        latitude = Double.parseDouble(fields[1]);
                    } catch (NumberFormatException e) {
                        latitude = 0;
                    }
                    try {
                        longitude = Double.parseDouble(fields[2]);
                    } catch (NumberFormatException e) {
                        longitude = 0;
                    }

                    String type = fields[3];
                    String image;

                    switch(type) {
                        case "event":
                            image = "https://upload.wikimedia.org/wikipedia/commons/c/ca/Anarchist_attack_on_the_King_of_Spain_Alfonso_XIII_%281906%29.jpg";
                            break;
                        case "edu":
                            image = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Vrt%2C_pogled_na_glavni_ulaz.JPG";
                            break;
                        case "landmark":
                            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/20150902GrenspaalElst_03.JPG/767px-20150902GrenspaalElst_03.JPG";
                            break;
                        default:
                            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Point_d_interrogation.jpg/120px-Point_d_interrogation.jpg";
                    }

                    attractions.add(new Attraction(
                            name,
                            type, // list
                            type, // details
                            Uri.parse(image),
                            null,
                            new LatLng(latitude, longitude),
                            CITY_SYDNEY
                    ));
                }
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return attractions;
    }

    public static String getClosestCity(LatLng curLatLng) {
        if (curLatLng == null) {
            // If location is unknown return test city so some data is shown
            return TEST_CITY;
        }

        double minDistance = 0;
        String closestCity = null;
        for (Map.Entry<String, LatLng> entry: CITY_LOCATIONS.entrySet()) {
            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue());
            if (minDistance == 0 || distance < minDistance) {
                minDistance = distance;
                closestCity = entry.getKey();
            }
        }
        return closestCity;
    }
}
