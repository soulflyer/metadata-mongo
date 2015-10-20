(ns metadata-mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [metadata.core :as metadata]
            [clojure.string :as str])
  (:import [com.mongodb MongoOptions ServerAddress]
           org.bson.types.ObjectId))

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
                         "White-Balance Mode")]
    (metadata/getmeta file metadatafields)))

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
  ())

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
                       :_id (str year month project (basename filename))}]
     (merge filedetails (selectedmeta file)))))

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")]
  (mc/save db "documents" (image-entry "/Users/iain/Pictures/Published/fullsize" "2015" "09" "01-Dragon" "IMG_6666.jpg"))
  (mc/save db "documents" (image-entry "/Users/iain/Pictures/Published/fullsize/2015/09/01-Lui-DSD/DIW_5490.jpg")))

(defn save-meta
  "Saves the metadata from a specified picture, or from all the jpeg pictures
  in a directory to the database.
  [pathname database document]"
  ([pathname database document]
   (let [connection (mg/connect)
         db (mg/get-db connection database)
         file (java.io.File. pathname)]
     (if (.isFile file)
       (let [imagemeta (image-entry pathname)]
         (mc/save db document imagemeta))
       (for [filename (.list file)]
         (mc/save db document (image-entry (str pathname "/" filename))))
       ))))

(save-meta "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5634.jpg" "monger-test" "documents")
(save-meta "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle" "monger-test" "documents")

;; (selectedmeta file)
;; (image-entry "/Users/iain/Pictures/Published/fullsize" 2015 "09" "01-Dragon" "IMG_6666.jpg")

;; (def filename "/Users/iain/Pictures/Published/fullsize/2015/09/01-Lui-DSD/DIW_5490.jpg")
;; (def beetlefile "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5633.jpg")
;; (def file (java.io.File. filename))
;; (def beetle (java.io.File. beetlefile))
;; (metadata/getmeta file)
;; (metadata/getmeta file metadatafields)
