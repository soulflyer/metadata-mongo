(ns metadata-mongo.core
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.operators :refer :all]
            [metadata.core :as metadata]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]])
  (:import [com.mongodb MongoOptions ServerAddress]
           org.bson.types.ObjectId)
  (:gen-class))

(def cli-options
  [["-h" "--help"]])

;; (defn -main [& args]
;;   (save-meta args))

;; (defn -main [& args]
;;   (println "My CLI received arguments:" args))

(defn selectedmeta
  "returns a map of selected metdata fields from file"
  [file]
  (let [metadatafields '("Aperture Value"
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
                         "Keywords"
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
                         "White Balance Mode")]
    (metadata/getmeta file metadatafields)))

(defn meta-and-keywords-array
  "takes a map of metadata fields and converts the keywords from a string
  to a list of strings"
  [metadatamap]
  (if (contains? metadatamap "Keywords")
    (let [othermeta (dissoc metadatamap "Keywords")
         keywords (str/split (metadatamap "Keywords") #";")]
      (merge othermeta {"Keywords" keywords}))
    metadatamap))

(defn basename
  "Cuts the extension off the end of a string
  (basename \"file.jpg\")
  =>  file
  (basename \"file.name.jpg\")
  =>  file.name
  (basename \"file\")
  =>  file"
  [filename]
  (let [ind (.lastIndexOf filename ".")]
    (if (< 0 ind)
      (subs filename 0 ind)
      filename)))

(defn path-items
  "returns a list of all sections of a / seperated path
  (path-items \"/usr/local/bin\")
  =>  (\"\" \"usr\" \"local\" \"bin\")
  (path-items \"usr/local/bin\")
  =>  (\"usr\" \"local\" \"bin\")"

  [path]
  ;; use this line for a version that doesn't have the leading empty string that
  ;; denotes a path with a leading /
  ;;  (filter (complement str/blank?) (str/split path #"/"))
  (str/split path #"/"))

(defn is-image?
  "Checks if a string ends in jpg"
  [filename]
  (if (re-find #".jpg$" filename)
    true
    false))

(defn image-entry
  "Creates a map describing the given image for inclusion in the database
  [\"/path/to/image/year/month/project/filename\"]
  [\"rootdir\" \"year\" \"month\" \"project\" \"filename\"]"
  ([fullpath]
   (let [pathlist (vec (path-items fullpath))
         filename (last pathlist)
         items    (count pathlist)
         project  (nth pathlist (- items 2))
         month    (nth pathlist (- items 3))
         year     (nth pathlist (- items 4))
         rootdir  (str/join "/" (subvec pathlist 0 (- items 4)))]
     (image-entry rootdir year month project filename)))
  ([rootdir year month project filename]
   (let [path (str rootdir "/" year "/" month "/" project "/" filename)
         file (java.io.File. path)
         filedetails { "Year" year "Month" month
                       "Project" project "Version" (basename filename)
                       "_id" (str year month project (basename filename))}
         metaarray   (meta-and-keywords-array (selectedmeta file))]
     (merge filedetails metaarray))))

(defn save-meta
  "Saves the metadata from a specified picture, or from all the jpeg pictures
  in a directory to the database.
  [database document pathname]"
  ([database document pathname]
   (let [connection (mg/connect)
         db (mg/get-db connection database)
         file (java.io.File. pathname)]
     (if (.isFile file)
       (let [imagemeta (image-entry pathname)]
         (mc/save db document imagemeta))
       (doall (for [filename (filter is-image? (.list file))]
                (mc/save db document (image-entry (str pathname "/" filename)))))
       ))))

(defn -main [& args]
  (save-meta (first args) "monger-test" "documents"))


;; (save-meta "monger-test" "documents" "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5634.jpg")

;; (save-meta "monger-test" "documents" "/Users/iain/Pictures/Published/fullsize/2015/09/23-Frog")
