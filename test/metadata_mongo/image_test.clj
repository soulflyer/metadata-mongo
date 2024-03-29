(ns metadata-mongo.image-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [metadata-mongo.image :as sut]))

(deftest image-entry-test
  (testing "creates the expected metadata map for an image given a full path to the image")
  (is (= {"Image-Height" "4912 pixels", "Focal-Length-35" "105 mm", "Software" "Aperture 3.6", "Exposure-Time" "1/160 sec", "Color-Space" "sRGB", "F-Number" "f/36.0", "Reference-Date" "20151019T145002+07", "Exposure-Bias-Value" "0 EV", "Make" "NIKON CORPORATION", "White-Balance-Mode" "Manual white balance", "Image-Width" "7360 pixels", "ISO-Speed-Ratings" "400", "Exposure-Program" "Manual control", "Metering-Mode" "Spot", "Date-Time-Digitized" "2015:09:19 22:31:22", "Exposure-Mode" "Manual exposure", "Special-Instructions" "BL20S15X1Y1", "Flash" "Flash did not fire, auto", "Model" "NIKON D800", "File-Modified-Date" "Mon Oct 19 14:57:23 +07:00 2015", "Focal-Length" "105 mm", "Copyright-Notice" "©2015 Iain Wood, All rights reserved.", "Rating" "4", "Project" "19-Beetle", "Keywords" ["coleoptera (beetles)"], "_id" "20150919-BeetleDIW_5634", "Year" "2015", "White-Balance" "Shade", "Version" "DIW_5634", "User-Comment" "", "Date-Time-Original" "2015:09:19 22:31:22", "Month" "09", "Data-Precision" "8 bits", "Lens" "AF-S VR Micro-Nikkor 105mm f/2.8G IF-ED"}
         (sut/image-entry "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5634.jpg")))
  (testing "creates the expected metadata map for an image given the separate params describing the image")
  (is (= {"Image-Height" "4912 pixels",
          "Focal-Length-35" "105 mm",
          "Software" "Aperture 3.6",
          "Exposure-Time" "1/160 sec",
          "Color-Space" "sRGB",
          "F-Number" "f/36.0",
          "Reference-Date" "20151019T145002+07",
          "Exposure-Bias-Value" "0 EV",
          "Make" "NIKON CORPORATION",
          "White-Balance-Mode" "Manual white balance",
          "Image-Width" "7360 pixels",
          "ISO-Speed-Ratings" "400",
          "Exposure-Program" "Manual control",
          "Metering-Mode" "Spot",
          "Date-Time-Digitized" "2015:09:19 22:31:22",
          "Exposure-Mode" "Manual exposure",
          "Special-Instructions" "BL20S15X1Y1",
          "Flash" "Flash did not fire, auto",
          "Model" "NIKON D800",
          "File-Modified-Date" "Mon Oct 19 14:57:23 +07:00 2015",
          "Focal-Length" "105 mm",
          "Copyright-Notice" "©2015 Iain Wood, All rights reserved.",
          "Rating" "4",
          "Project" "19-Beetle",
          "Keywords" ["coleoptera (beetles)"],
          "_id" "20150919-BeetleDIW_5634",
          "Year" "2015",
          "White-Balance" "Shade",
          "Version" "DIW_5634",
          "User-Comment" "",
          "Date-Time-Original" "2015:09:19 22:31:22",
          "Month" "09",
          "Data-Precision" "8 bits",
          "Lens" "AF-S VR Micro-Nikkor 105mm f/2.8G IF-ED"}
         (sut/image-entry "/Users/iain/Pictures/Published/fullsize" "2015" "09" "19-Beetle" "DIW_5634.jpg"))))
