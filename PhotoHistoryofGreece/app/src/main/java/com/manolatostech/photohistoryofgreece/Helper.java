package com.manolatostech.photohistoryofgreece;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by kmanolatos on 20/2/2018.
 */

public class Helper {

    public void deleteDCIMFile(File sourceFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        source = new FileInputStream(sourceFile).getChannel();
        if (source != null) {
            source.close();
        }
        File file = new File(String.valueOf(sourceFile));
        file.delete();
    }

    public String getLastDCIMPic(MainActivity mainActivity) {
        String picPath = "";
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = mainActivity.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?
                picPath = imageFile.getAbsolutePath();
            }
        }
        return picPath;
    }

    public void deleteDCIMThumbnail(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

    public String SaveImage(int dataModelSize, String fname, Bitmap finalBitmap, File storageDir) {
        File file = new File(storageDir, fname);
        while (file.exists()) {
            dataModelSize++;
            file = new File(storageDir, dataModelSize + ".jpg");
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataModelSize + ".jpg";
    }


    public boolean isOnline(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager;
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {

        }
        return connected;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public ArrayList<InformationModel> getInformation() {
        ArrayList<InformationModel> model = new ArrayList<InformationModel>();
        InformationModel temp = new InformationModel();
        temp.id = 1;
        temp.information = "The Acropolis Museum (Greek: Μουσείο Ακρόπολης, Mouseio Akropolis) is an archaeological museum focused on the findings of the archaeological site of the Acropolis of Athens. The museum was built to house every artifact found on the rock and on the surrounding slopes, from the Greek Bronze Age to Roman and Byzantine Greece. It also lies over the ruins of a part of Roman and early Byzantine Athens.\n" +
                "\n" +
                "The museum was founded in 2003, while the Organization of the Museum was established in 2008. It opened to the public on 20 June 2009. Nearly 4,000 objects are exhibited over an area of 14,000 square metres. The Organization for the Construction of the new museum is chaired by Aristotle University of Thessaloniki Professor Emeritus of Archaeology, Dimitrios Pandermalis.";
        temp.latitude = 37.971421;
        temp.longitude = 23.726166;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 2;
        temp.information = "The Archaeological Museum of Chora is a museum in Chora, Messenia, in southern Greece, whose collections focus on the Mycenaean civilization, particularly from the excavations at the Palace of Nestor and other regions of Messenia. The museum was founded in 1969 by the Greek Archaeological Service under the auspices of the Ephorate of Antiquities of Olympia. At the time, the latter included in its jurisdiction the larger part of Messenia.";
        temp.latitude = 37.0536;
        temp.longitude = 21.7207;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 3;
        temp.information = "Kerameikos (Greek: Κεραμεικός, pronounced [ceramikos]) also known by its Latinized form Ceramicus, is an area of Athens, Greece, located to the northwest of the Acropolis, which includes an extensive area both within and outside the ancient city walls, on both sides of the Dipylon (Δίπυλον) Gate and by the banks of the Eridanos River. It was the potters' quarter of the city, from which the English word \"ceramic\" is derived, and was also the site of an important cemetery and numerous funerary sculptures erected along the road out of the city towards Eleusis.";
        temp.latitude = 37.973662772;
        temp.longitude = 23.717830462;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 4;
        temp.information = "The Nicholas P. Goulandris Foundation - Museum of Cycladic Art is a museum of Athens. It houses a notable collection of artifacts of Cycladic art.\n" +
                "\n" +
                "The museum was founded in 1986 in order to house the collection of Cycladic and Ancient Greek art belonging to Nicholas and Dolly Goulandris. Starting in the early 1960s, the couple collected Greek antiquities, with special interest in the prehistoric art from the Cyclades islands of the Aegean Sea. The Museum's main building, erected in the centre of Athens in 1985, was designed by the Greek architect Ioannis Vikelas. In 1991, the Museum acquired a new wing, the neo-classical Stathatos Mansion at the corner of Vassilissis Sofias Avenue and Herodotou Street.";
        temp.latitude = 37.9722644443;
        temp.longitude = 23.7386387121;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 5;
        temp.information = "The Stoa of Attalos (also spelled Attalus) was a stoa (covered walkway or portico) in the Agora of Athens, Greece. It was built by and named after King Attalos II of Pergamon, who ruled between 159 BC and 138 BC. The current building was reconstructed in 1952–1956 by American architects along with the Greek architect Ioannis Travlos and the Greek Civil Engineer Yeoryios Biris.";
        temp.latitude = 37.97166278;
        temp.longitude = 23.721163782;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 6;
        temp.information = "The Museum of the Center for the Acropolis Studies (Greek: Κέντρο Μελετών Ακροπόλεως) is a museum in Athens, Greece, a part of the new Acropolis Museum and its research workshops. It is housed in the Weiler Building, named after the Bavarian engineer who designed it in 1834 and constructed it in 1836. \n" +
                "\n" +
                "After serving as a military hospital and a gendarmes barracks, Weiler Building was remodelled from 1985 to 1987 and was converted to a museum. Its collections include casts of the Parthenon sculptures, plaster models of the Acropolis illustrating the architectural development of the monuments from the neolithic to present times, and a permanent exhibition on the works of conservation and restoration and exhibits concerning the Erechtheion and other Acropolis monuments.";
        temp.latitude = 37.9679994613;
        temp.longitude = 23.723992104;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 7;
        temp.information = "The National Archaeological Museum (Greek: Εθνικό Αρχαιολογικό Μουσείο) in Athens houses some of the most important artifacts from a variety of archaeological locations around Greece from prehistory to late antiquity. It is considered one of the greatest museums in the world and contains the richest collection of artifacts from Greek antiquity worldwide. It is situated in the Exarcheia area in central Athens between Epirus Street, Bouboulinas Street and Tositsas Street while its entrance is on the Patission Street adjacent to the historical building of the Athens Polytechnic university.";
        temp.latitude = 37.989170;
        temp.longitude = 23.731827;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 8;
        temp.information = "The Syntagma Metro Station Archeological Collection is a museum in Athens, Greece. It is located at the Syntagma station of the Athens metro and it features a variety of historical items unearthed during the process of building the metro.";
        temp.latitude = 37.9719544455;
        temp.longitude = 23.7344053957;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 9;
        temp.information = "The Byzantine and Christian Museum (Greek: Βυζαντινό και Χριστιανικό Μουσείο) is situated at Vassilissis Sofias Avenue in Athens, Greece. It was founded in 1914, and houses more than 25,000 exhibits with rare collections of pictures, scriptures, frescoes, pottery, fabrics, manuscripts, and copies of artifacts from the 3rd century AD to the late medieval era. It is one of the most important museums in the world in Byzantine Art. In June 2004, in time for its 90th anniversary and the 2004 Athens Olympics, the museum reopened to the public after an extensive renovation and the addition of another wing.";
        temp.latitude = 37.971496114;
        temp.longitude = 23.73999704;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 10;
        temp.information = "The Benaki Museum, established and endowed in 1930 by Antonis Benakis in memory of his father Emmanuel Benakis, is housed in the Benakis family mansion in downtown Athens, Greece. The museum houses Greek works of art from the prehistorical to the modern times, an extensive collection of Asian art, hosts periodic exhibitions and maintains a state-of-the-art restoration and conservation workshop. Although the museum initially housed a collection that included Islamic art, Chinese porcelain and exhibits on toys, its 2000 re-opening led to the creation of satellite museums that focused on specific collections, allowing the main museum to focus on Greek culture over the span of the country's history.";
        temp.latitude = 37.9722261111;
        temp.longitude = 23.737575383;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 11;
        temp.information = "The Jewish Museum of Greece (Greek: Εβραϊκό Μουσείο της Ελλάδος) is a museum in Athens, Greece. It was established by Nicholas Stavroulakis in 1977 to preserve the material culture of the Greek Jews.";
        temp.latitude = 37.9704577848;
        temp.longitude = 23.7264770941;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 12;
        temp.information = "The Museum of Pavlos and Alexandra Kanellopoulou is a museum of antiquities in Athens, Greece. Founded in 1976, it comprises the private collection of Paul and Alexandra Kanellopoulos which was donated to the Greek state. It is housed in the neoclassical mansion of the Michalea family, situated on the north slope of the Acropolis and built in 1864. The mansion was purchased by the Greek state in the 1960s-1970s and was restored to permanently house the collection.\n" +
                "\n" +
                "The items of the collection include clay and stone vases and figurines, busts, jewellery, weapons, coins and inscriptions, ranging from 3000-1200 BC to the 18th and 19th centuries AD.";
        temp.latitude = 37.9703794518;
        temp.longitude = 23.7221971112;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 13;
        temp.information = "The Old Parliament House (Greek: Παλαιά Βουλή, Paleá Voulí) at Stadiou Street in Athens, housed the Greek Parliament between 1875 and 1935. It now houses the country's National Historical Museum.";
        temp.latitude = 37.973162774;
        temp.longitude = 23.726330428;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 14;
        temp.information = "The Museum of Greek Folk Art is a museum in Athens, Greece. The museum was founded in 1918 as the Museum of Greek Handicrafts in the Tzistarakis Mosque in Monastiraki, which later became the National Museum of Decorative Arts and in 1959 it obtained its current name. In 1973 the greater part of the collection and the main functions of the museum were moved to 17 Kydathinaion Str. in Plaka and the mosque was annexed to it. Other annexes are the old \"Public Baths\" at Kyrristou 8 and one at Thespidos 8, both also in Plaka.";
        temp.latitude = 37.9701827859;
        temp.longitude = 23.7260487625;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 15;
        temp.information = "Museum of the History of the Greek Costume is a special interest museum in Athens, Greece. It was inaugurated by the former Minister of Culture, Melina Mercouri, in 1988 and is part of the Lyceum of Hellenic women, a non profit society founded in 1910.\n" +
                "\n" +
                "The museum's collections include Greek traditional costumes, jewellery, reproductions of minoan, classic and Byzantine clothes, as well as porcelain dolls with Greek costumes.";
        temp.latitude = 37.9739627708;
        temp.longitude = 23.7359470562;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 16;
        temp.information = "Frissiras Museum is a contemporary painting museum in Plaka Athens, Greece. It was founded and endowed by Vlassis Frissiras, an art-collecting lawyer. Its permanent collection consists of 3000 paintings and sculptures by Greek and other European artists on the subject of the human form.";
        temp.latitude = 37.9700294532;
        temp.longitude = 23.7259804294;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 17;
        temp.information = "Gounaropoulos Museum is located in Athens, Greece. Founded in 1979, it belongs to the municipality of Zografou and aims to present and promote the work of the painter Giorgos Gounaropoulos. The museum is housed in the artist's home and atelier, and contains 40 oil paintings and drawings, the artist's personal belongings and archive. Guided tours and educational programs are offered, and a variety of art exhibitions, lectures and other cultural events are hosted.";
        temp.latitude = 37.97166278;
        temp.longitude = 23.756913639;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 18;
        temp.information = "The Athens War Museum (Greek: Πολεμικό Μουσείο), established on July 18, 1975, is the museum of the Greek Armed Forces. Its purpose is the exhibition of weapon artifacts and the relevant research in the history of war. It covers the history of war in all ages. The museums' collections include the collection of the Greek Army, with artifacts from other civilizations such as Ancient China and Ancient Japan.In 1964, the Hellenic State decided to found the War Museum, wishing to honor all those who fought for Greece and its freedom. The design of the museum was undertaken by a team of distinguished scientists, headed by Professor Thoukidides Valentis of the National Technical university of Athens (N.T.U.A). On July 18, 1975, the President of the Hellenic Republic H.E. Constantine Tsatsos and the Minister of National Defense Evangelos Averoff-Tositsas inaugurated the Museum. Its various activities include the publication of books, the establishment and maintenance of monuments and memorials and the aid to services and agencies all over Greece. The Museum’s exhibition areas are distributed over four levels (floors) and present images of Greek history from antiquity to the present.The museum's centerpieces are weaponry from wars in which Greece was involved.\n" +
                "\n" +
                "The War Museum has established Museum Branches at the cities of Nauplion (Peloponnese) (1988), Chania (Crete) (1995), Tripoli (Peloponnese) (1997) and Thessaloniki (2000). After building the Hellenic Air Force Museum, some airplanes were brought to this museum.";
        temp.latitude = 37.971829446;
        temp.longitude = 23.740497038;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 19;
        temp.information = "The Hellenic Air Force Museum was founded in 1986 and since 1992 has been located on Dekelia Air Base in Acharnes north of Athens. In opposition to the War Museum of Athens it displays air force history and is active in restoring and presenting old aircraft. Most aircraft in the collection come from the Hellenic Air Force; some were exchanged with other European aircraft museums.\n" +
                "\n" +
                "The HAF Underwater Operations Team (KOSYTHE) helped recover some rare aircraft from underwater for the museum: a Bristol Blenheim, a Junkers Ju 52/3m and a Junkers Ju 87.";
        temp.latitude = 38.1029495882;
        temp.longitude = 23.7737302384;
        model.add(temp);

        temp = new InformationModel();
        temp.id = 20;
        temp.information = "The Athens University Museum is a museum in Plaka, Athens, Greece.\n" +
                "\n" +
                "The building was a structure of the Ottoman period but fundamentally restructured between 1831 and 1833 by Stamatios Kleanthis and Eduard Schaubert for their architectural office. From 1837 to 1841 it housed the newly founded University of Athens.";
        temp.latitude = 37.9705211179;
        temp.longitude = 23.7227387757;
        model.add(temp);

        return model;
    }
}
