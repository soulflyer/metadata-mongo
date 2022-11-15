(ns metadata-mongo.utils-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [metadata-mongo.fields :refer [field-list]]
    [metadata-mongo.utils :as sut]))

(deftest cleanup-test
  (testing "all spaces and slashes are removed from entries in a list")
  (is (= '("Aperture-Value"
            "Artist"
            "By-Line"
            "Camera-Owner-Name"
            "Caption-Abstract"
            "Color-Space"
            "Copyright"
            "Copyright-Notice"
            "Data-Precision"
            "Date-Time-Digitized"
            "Date-Time-Original")
         (sut/cleanup
           '("Aperture Value"
              "Artist"
              "By-Line"
              "Camera Owner Name"
              "Caption/Abstract"
              "Color Space"
              "Copyright"
              "Copyright Notice"
              "Data Precision"
              "Date/Time Digitized"
              "Date/Time Original")))))
