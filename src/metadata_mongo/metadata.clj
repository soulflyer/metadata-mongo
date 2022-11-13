(ns metadata-mongo.metadata
  (:require
   [clojure.string :as str]
   [metadata-mongo.fields :refer [field-list]]
   [metadata-mongo.utils :refer [cleanup-keys]])
  (:import
   (com.drew.imaging ImageMetadataReader)))

(defn gettags [taglist]
  (let [acc {}]
    (reduce
     (fn [a b]
       (assoc a (.getTagName b) (.getDescription b)))
     acc (.getTags taglist))))

(defn getmeta
  "Returns a hash-map containing the metadata from an image file.
  Expects a java.io.File and an optional sequence of field names."
  ([file]
   (let [imagedata (ImageMetadataReader/readMetadata file)
         acc {}]
     (reduce (fn [list tags]
               (merge list (gettags tags)))
             acc (.getDirectories imagedata))))
  ([file fields]
   (select-keys (getmeta file) fields)))

(defn selected-meta
  "Returns a map of selected metadata fields from a java.io.File"
  [file]
  (cleanup-keys (getmeta file field-list)))

(defn keywords-string-to-vector
  "takes a map of metadata fields and converts the keywords from a string
  to a list of strings"
  [metadatamap]
  (if (contains? metadatamap "Keywords")
    (let [othermeta (dissoc metadatamap "Keywords")
          keywords (str/split (metadatamap "Keywords") #";")]
      (merge othermeta {"Keywords" keywords}))
    metadatamap))

(defn meta-with-keywords-vector
  [file]
  (keywords-string-to-vector (selected-meta file)))
