(ns metadata-mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [metadata.core :as metadata])
  (:import [com.mongodb MongoOptions ServerAddress]
           org.bson.types.ObjectId))

(def filename "/Users/iain/Pictures/Published/fullsize/2015/09/01-Lui-DSD/DIW_5490.jpg")
(def beetlefile "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5633.jpg")
(def file (java.io.File. filename))
(def beetle (java.io.File. beetlefile))
(def metadatafields '(
                      "Aperture Value"
                      "Artist"
                      "By-Line"
                      "Camera Owner Name"
                      "Caption/Abstract"
                      "Color Space"
                      "Copyright"
                      "Copyright Notice"
                      "Data Precision"
                      "Date/Time Digitized"
                      "Date/Time Original"
                      "Exposure Bias Value"
                      "Exposure Mode"
                      "Exposure Program"
                      "Exposure Time"
                      "F-Number"
                      "File Modified Date"
                      "Flash"
                      "Focal Length"
                      "Focal Length 35"
                      "ISO Speed Ratings"
                      "Image Height"
                      "Image Width"
                      "Lens"
                      "Make"
                      "Metering Mode"
                      "Model"
                      "Object Name"
                      "Rating"
                      "Reference Date"
                      "Software"
                      "Special Instructions"
                      "User Comment"
                      "White Balance"
                      "White-Balance Mode"))
(metadata/getmeta file)
(metadata/getmeta file metadatafields)
(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")]
  (mc/insert db "documents" (metadata/getmeta file metadatafields)))
