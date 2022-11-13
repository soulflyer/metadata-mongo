(ns metadata-mongo.metadata-test
  (:require
    [clojure.set :refer [subset?]]
    [clojure.test :refer [deftest is testing]]
    [metadata-mongo.fields :refer [field-list]]
    [metadata-mongo.metadata :as sut]
    [metadata-mongo.utils :as utils]))

(def filename "/Users/iain/Pictures/Published/fullsize/2015/09/01-Dragon/IMG_6666.jpg")
(def file (java.io.File. filename))

(deftest keywords-string-to-vector-test
  (testing "keywords-string-to-vector should return a map with the keyword entry converted to a vector of strings")
  (is (= {"F-Number" "f/4.5"
          "Aperture Value" "f/4.5"
          "Keywords" ["Dragonflies"]
          "Exposure Time" "1/60 sec"}

         (sut/keywords-string-to-vector {"F-Number" "f/4.5"
                                         "Aperture Value" "f/4.5"
                                         "Keywords" "Dragonflies"
                                         "Exposure Time" "1/60 sec"}))))

(deftest meta-with-keywords-vector-test
  (testing "meta-with-keywords-vector returns a map")
  (is (= clojure.lang.PersistentHashMap (type (sut/meta-with-keywords-vector file))))
  (testing "meta-with-keywords-vector returns a map of metadata with keywords converted to a vector of strings")
  (is (= ["Dragonflies"]
         (get (sut/meta-with-keywords-vector file) "Keywords")))
  ;; (is (subset?
  ;;       (sort (keys (sut/meta-with-keywords-vector file)))
  ;;       (sort (set field-list))))
  )

(deftest selected-meta-test
  (testing "selected-meta returns correct meta-data with cleaned up keys")
  (is (= {"Image-Height" "2316 pixels", "Software" "Aperture 3.6", "Exposure-Time" "1/60 sec", "Camera-Owner-Name" "Iain Wood", "Color-Space" "sRGB", "F-Number" "f/4.5", "Aperture-Value" "f/4.5", "Reference-Date" "20151106T215756+07", "Exposure-Bias-Value" "-4/3 EV", "Make" "Canon", "White-Balance-Mode" "Auto white balance", "Image-Width" "3474 pixels", "ISO-Speed-Ratings" "640", "Exposure-Program" "Manual control", "Metering-Mode" "Multi-segment", "Date-Time-Digitized" "2015:09:01 13:53:57", "Exposure-Mode" "Manual exposure", "Special-Instructions" "BL20S15X1Y1", "Flash" "Flash did not fire, auto", "Model" "Canon PowerShot G15", "File-Modified-Date" "Fri Nov 06 21:57:16 +07:00 2015", "Focal-Length" "6.1 mm", "Copyright-Notice" "©2015 Iain Wood, All rights reserved.", "Rating" "4.0", "Keywords" "Dragonflies", "Date-Time-Original" "2015:09:01 13:53:57", "Data-Precision" "8 bits", "Lens" " 6.1-30.5mm"}
         (sut/selected-meta file)))
  (testing "Only returns fields that are specified in the field-list")
  (is (subset?
        (set (keys (sut/selected-meta file)))
        (set (utils/cleanup field-list)))))

(comment
  (sut/getmeta file)
  {"Aperture Value" "f/4.5",
   "Application Record Version" "2",
   "By-line" "Iain Wood",
   "Camera Owner Name" "Iain Wood",
   "Caption Digest" "144 200 251 61 243 188 207 156 211 3 111 61 188 111 26 218",
   "Coded Character Set" "UTF-8",
   "Color Space" "sRGB",
   "Component 1" "Y component: Quantization table 0, Sampling factors 2 horiz/2 vert",
   "Component 2" "Cb component: Quantization table 1, Sampling factors 1 horiz/1 vert",
   "Component 3" "Cr component: Quantization table 1, Sampling factors 1 horiz/1 vert",
   "Components Configuration" "YCbCr",
   "Compressed Bits Per Pixel" "5 bits/pixel",
   "Copyright Notice" "©2015 Iain Wood, All rights reserved.",
   "Custom Rendered" "Normal process",
   "Data Precision" "8 bits",
   "Date/Time Digitized" "2015:09:01 13:53:57",
   "Date/Time Original" "2015:09:01 13:53:57",
   "Date/Time" "2015:09:01 13:53:57",
   "Detected File Type Long Name" "Joint Photographic Experts Group",
   "Detected File Type Name" "JPEG",
   "Detected MIME Type" "image/jpeg",
   "Digital Zoom Ratio" "1",
   "Exif Image Height" "2316 pixels",
   "Exif Image Width" "3474 pixels",
   "Exif Version" "2.30",
   "Expected File Name Extension" "jpg",
   "Exposure Bias Value" "-4/3 EV",
   "Exposure Mode" "Manual exposure",
   "Exposure Program" "Manual control",
   "Exposure Time" "1/60 sec",
   "F-Number" "f/4.5",
   "File Modified Date" "Fri Nov 06 21:57:16 +07:00 2015",
   "File Name" "IMG_6666.jpg",
   "File Size" "1267892 bytes",
   "File Source" "Digital Still Camera (DSC)",
   "Flash" "Flash did not fire",
   "FlashPix Version" "1.00",
   "Focal Length" "6.1 mm",
   "Focal Plane Resolution Unit" "Inches",
   "Focal Plane X Resolution" "57/778157 inches",
   "Focal Plane Y Resolution" "11/150000 inches",
   "ISO Speed Ratings" "640",
   "Image Height" "2316 pixels",
   "Image Width" "3474 pixels",
   "Keywords" "Dragonflies",
   "Make" "Canon",
   "Max Aperture Value" "f/1.8"
   "Metering Mode" "Multi-segment",
   "Model" "Canon PowerShot G15",
   "Number of Components" "3",
   "Number of Tables" "4 Huffman tables",
   "Orientation" "Top, left side (Horizontal / normal)",
   "Reference Date" "20151106T215756+07",
   "Resolution Units" "none",
   "Scene Capture Type" "Standard",
   "Sensing Method" "One-chip color area sensor",
   "Sensitivity Type" "Standard Output Sensitivity and Recommended Exposure Index",
   "Shutter Speed Value" "1/59 sec",
   "Software" "Aperture 3.6",
   "Special Instructions" "BL20S15X1Y1",
   "Thumbnail Height Pixels" "0",
   "Thumbnail Width Pixels" "0",
   "Version" "1.1",
   "White Balance Mode" "Auto white balance",
   "X Resolution" "72 dots per unit",
   "XMP Value Count" "16",
   "Y Resolution" "72 dots per unit",
   "Compression Type" "Baseline"})
