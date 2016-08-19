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
import com.google.android.gms.location.Geofence;
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
    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

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
                URL file = new URL("http://nicolas.raoul.free.fr/lab/wikishootme-japan.csv");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(file.openStream()));

                boolean firstLine = true;
                String line;
                while ((line = in.readLine()) != null) {

                    // Skip CSV header.
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    System.out.println(line);
                    String[] fields = line.split(",");
                    String name = fields[0];
                    double latitude = Double.parseDouble(fields[1]);
                    double longitude = Double.parseDouble(fields[2]);
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

    /**
     * All photos used with permission under the Creative Commons Attribution-ShareAlike License.
     */
    /*public static final HashMap<String, List<Attraction>> ATTRACTIONS =
            new HashMap<String, List<Attraction>>() {{

        put(CITY_SYDNEY, new ArrayList<Attraction>() {{
            add(new Attraction(
                    "軽井沢スキーバス転落事故",
                    "event", // list
                    "event", // details
                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/c/ca/Anarchist_attack_on_the_King_of_Spain_Alfonso_XIII_%281906%29.jpg"),
                    null,
                    new LatLng(36.32686111, 138.64547222),
                    CITY_SYDNEY
            ));

            add(new Attraction(
                    "Sydney Harbour Bridge",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ut nulla neque. Morbi nec felis vel neque rhoncus malesuada.",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ut nulla neque. Morbi nec felis vel neque rhoncus malesuada. Mauris non nisi est. Nunc in ipsum euismod, suscipit dolor eget, efficitur nisi. Integer venenatis mauris mauris, quis luctus risus pellentesque a. Duis tempus est at ligula vehicula fermentum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.\n\nNam ut sodales nibh, euismod aliquet lectus. Curabitur ornare dictum nisi, at faucibus magna. Morbi tempus nibh sed sodales volutpat. Etiam sodales, turpis sit amet porttitor tristique, libero libero faucibus est, viverra dictum risus ipsum vel augue. Nulla dolor magna, iaculis ac ornare id, fermentum eget massa. Sed mattis, odio nec sodales vehicula, neque metus ullamcorper nulla, sit amet ullamcorper risus lectus a ipsum. Curabitur venenatis feugiat quam nec elementum. Curabitur a interdum urna. Curabitur tincidunt tortor eget neque condimentum blandit. Etiam imperdiet, enim nec blandit convallis, nunc augue.",
                    Uri.parse("https://lh6.googleusercontent.com/-ORRJtfLQlaw/VGLmQPv3n8I/AAAAAAAAAD8/2TzSCCPzl9k/w600-no/DSC04114.JPG"),
                    Uri.parse("https://lh4.googleusercontent.com/-ch9Kk-7pD68/VGLkCNh5niI/AAAAAAAAADc/ztxkRHWX-po/w600-no/DSC_2739.JPG"),
                    new LatLng(-33.852222, 151.210556),
                    CITY_SYDNEY
            ));

            add(new Attraction(
                    "Darling Harbour",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ut dui in ipsum suscipit aliquet pretium aliquet odio.",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ut dui in ipsum suscipit aliquet pretium aliquet odio. Nam posuere nunc sed risus molestie varius. Suspendisse posuere faucibus urna, id vestibulum ante iaculis et. Vivamus placerat suscipit sem, a tempor nunc vehicula ac. Ut libero velit, dapibus sit amet euismod vel, dignissim a nisl.\n\nDonec non dui non felis laoreet malesuada. Fusce ac metus ultrices, fermentum felis quis, varius velit. Donec ac felis semper, scelerisque diam sed, dignissim risus. Maecenas vel semper sapien. Fusce euismod justo posuere, efficitur risus tincidunt, congue tellus. In hac habitasse platea dictumst. Sed lobortis risus consequat vehicula facilisis.\n\nIn hendrerit, neque in gravida rutrum, purus enim aliquet lectus, sit amet vulputate tortor lacus at sem. Aenean lorem metus, finibus rhoncus eros at, ullamcorper fringilla velit. Nulla vitae porttitor metus, quis gravida lectus. In rhoncus, diam a elementum luctus, erat nisi tempus ex, in porta est.",
                    Uri.parse("https://lh5.googleusercontent.com/-qX43g6s92LY/VGLaTT3N35I/AAAAAAAAAC8/BbueQmch0Rw/w600-no/68001.jpg"),
                    Uri.parse("https://lh6.googleusercontent.com/-SQ6T1Ure6l8/VGLaTg2iGuI/AAAAAAAAACo/m6_RkTW2G1o/w600-no/IMG_20140201_082851.jpg"),
                    new LatLng(-33.8723, 151.19896),
                    CITY_SYDNEY
            ));

            add(new Attraction(
                    "Bondi Beach",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam et nunc in leo laoreet placerat. Interdum et malesuada fames ac ante ipsum primis in faucibus.",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam et nunc in leo laoreet placerat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Proin vestibulum laoreet odio nec posuere. Quisque ante arcu, malesuada vitae velit a, auctor tincidunt ante. Mauris varius eros eros, eget scelerisque mi scelerisque ut. Donec vehicula vitae urna ac hendrerit. Phasellus egestas risus nec euismod auctor.\n\nInteger fermentum velit et dolor varius sagittis. Proin et viverra sapien. Nulla aliquet ante et hendrerit egestas. Duis vulputate libero in nisi gravida cursus. Praesent laoreet nec dolor non iaculis. Aliquam eleifend ultricies ipsum, eu pellentesque libero rutrum non. Mauris et purus erat. Nullam semper mi id tincidunt viverra. Ut porta sem congue lectus luctus ultricies. Suspendisse iaculis lacinia nibh, eu accumsan magna volutpat vel. Sed id interdum mi, vel sollicitudin elit. Fusce facilisis elementum gravida. Duis at volutpat odio. Integer porta convallis tincidunt. Donec aliquam, leo ut.",
                    Uri.parse("https://lh4.googleusercontent.com/-wbNgVdUkBiE/VHe99hGVtNI/AAAAAAAAAFY/fAHfhchNLJw/w600-no/IMG_20141124_143747.jpg"),
                    Uri.parse("https://lh6.googleusercontent.com/-sjY_xlEOic4/VHe9-I4DD9I/AAAAAAAAAFI/Mt0VnjU7SxQ/w600-no/IMG_20141124_144008.jpg"),
                    new LatLng(-33.89102, 151.277726),
                    CITY_SYDNEY
            ));

            add(new Attraction(
                    "Taronga Zoo",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eros velit, faucibus in mi in, accumsan eleifend magna. Fusce efficitur volutpat leo nec finibus. Vivamus luctus quis dolor ac interdum.",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eros velit, faucibus in mi in, accumsan eleifend magna. Fusce efficitur volutpat leo nec finibus. Vivamus luctus quis dolor ac interdum. Donec iaculis, orci quis semper vulputate, tortor nisi porttitor tortor, at pretium ante quam ut odio. Donec fringilla sapien et dolor pharetra ultrices. Aenean faucibus felis non vulputate iaculis.\n\nEtiam eget dapibus ligula. Nunc facilisis dignissim tortor et elementum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam condimentum pellentesque mollis. Aliquam finibus urna ipsum, sed accumsan ante blandit quis. Vestibulum vel lacinia ligula. Nunc justo ex, volutpat nec justo ut, efficitur gravida lectus. Mauris cursus dui libero, vel tristique purus laoreet non.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse ultrices ullamcorper est, at consequat massa. Nam egestas at urna at pellentesque. Quisque lacus quam, efficitur vel erat eget, placerat feugiat eros. Mauris.",
                    Uri.parse("https://lh6.googleusercontent.com/-kypwDfnk674/VGLWpQPm4VI/AAAAAAAAAB0/SrfL0fE9DnE/w500-no/OI000020_2.jpg"),
                    Uri.parse("https://lh3.googleusercontent.com/-6_Ioko2ysgU/VHva2PjmRCI/AAAAAAAAAGM/cHjJC7ney4Q/w500-no/PC190054.JPG"),
                    new LatLng(-33.843333, 151.241111),
                    CITY_SYDNEY
            ));
        }});

    }};*/

    /**
     * Creates a list of geofences based on the city locations
     */
    public static List<Geofence> getGeofenceList() {
        List<Geofence> geofenceList = new ArrayList<Geofence>();
        for (String city : CITY_LOCATIONS.keySet()) {
            LatLng cityLatLng = CITY_LOCATIONS.get(city);
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(cityLatLng.latitude, cityLatLng.longitude, TRIGGER_RADIUS)
                    .setRequestId(city)
                    .setTransitionTypes(TRIGGER_TRANSITION)
                    .setExpirationDuration(EXPIRATION_DURATION)
                    .build());
        }
        return geofenceList;
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
