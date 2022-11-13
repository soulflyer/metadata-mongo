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
         (get (sut/meta-with-keywords-vector file) "Keywords"))))

(deftest selected-meta-test
  (testing "selected-meta returns correct meta-data with cleaned up keys")
  (is (= {"Image-Height" "2316 pixels", "Software" "Aperture 3.6", "Exposure-Time" "1/60 sec", "Camera-Owner-Name" "Iain Wood", "Color-Space" "sRGB", "F-Number" "f/4.5", "Aperture-Value" "f/4.5", "Reference-Date" "20151106T215756+07", "Exposure-Bias-Value" "-4/3 EV", "Make" "Canon", "White-Balance-Mode" "Auto white balance", "Image-Width" "3474 pixels", "ISO-Speed-Ratings" "640", "Exposure-Program" "Manual control", "Metering-Mode" "Multi-segment", "Date-Time-Digitized" "2015:09:01 13:53:57", "Exposure-Mode" "Manual exposure", "Special-Instructions" "BL20S15X1Y1", "Flash" "Flash did not fire, auto", "Model" "Canon PowerShot G15", "File-Modified-Date" "Fri Nov 06 21:57:16 +07:00 2015", "Focal-Length" "6.1 mm", "Copyright-Notice" "©2015 Iain Wood, All rights reserved.", "Rating" "4", "Keywords" "Dragonflies", "Date-Time-Original" "2015:09:01 13:53:57", "Data-Precision" "8 bits", "Lens" " 6.1-30.5mm"}
         (sut/selected-meta file)))
  (testing "Only returns fields that are specified in the field-list")
  (is (subset?
        (set (keys (sut/selected-meta file)))
        (set (utils/cleanup field-list)))))
