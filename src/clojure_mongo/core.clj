(ns metadata-mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [metadata.core :as metadata])
  (:import [com.mongodb MongoOptions ServerAddress]
           org.bson.types.ObjectId))

(def filename "/Users/iain/Pictures/Published/fullsize/2015/09/01-Dragon/IMG_6666.jpg")
(def file (java.io.File. filename))
(def metadatafields '("Rating" "Model"))
(metadata/getmeta file)
(metadata/getmeta file metadatafields)
(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")]
  (mc/insert db "documents" (metadata/getmeta file metadatafields)))
