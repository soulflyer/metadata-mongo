(ns metadata-mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [metadata.core :as metadata])
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

(selectedmeta file)

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

(defn image-id
  "Creates an image id for the _id field based on the year, month, folder name and filename"
  ([year month project filename]
   (str year month project (basename filename))))

(image-id "2015" "09" "01-Lui-DSD" "IMG_6666.jpg")

(defn image-entry
  "Creates a map describing the given image for inclusion in the database"
  ([rootdir year month project filename]
   (let [path (str rootdir "/" year "/" month "/" project "/" filename)
         file (java.io.File. path)
         filedetails { "Year" year "Month" month
                       "Project" project "Version" (basename filename)
                       :_id (str year month project (basename filename))}]
     (merge filedetails (selectedmeta file)))))

(image-entry "/Users/iain/Pictures/Published/fullsize" "2015" "09" "01-Dragon" "IMG_6666.jpg")

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")]
  (mc/insert db "documents" (metadata/getmeta file metadatafields)))


;; (def filename "/Users/iain/Pictures/Published/fullsize/2015/09/01-Lui-DSD/DIW_5490.jpg")
;; (def beetlefile "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5633.jpg")
;; (def file (java.io.File. filename))
;; (def beetle (java.io.File. beetlefile))
;; (def metadatafields '(
;;                       "Aperture Value"
;;                       "Artist"
;;                       "By-Line"
;;                       "Camera Owner Name"
;;                       "Caption/Abstract"
;;                       "Color Space"
;;                       "Copyright"
;;                       "Copyright Notice"
;;                       "Data Precision"
;;                       "Date/Time Digitized"
;;                       "Date/Time Original"
;;                       "Exposure Bias Value"
;;                       "Exposure Mode"
;;                       "Exposure Program"
;;                       "Exposure Time"
;;                       "F-Number"
;;                       "File Modified Date"
;;                       "Flash"
;;                       "Focal Length"
;;                       "Focal Length 35"
;;                       "ISO Speed Ratings"
;;                       "Image Height"
;;                       "Image Width"
;;                       "Lens"
;;                       "Make"
;;                       "Metering Mode"
;;                       "Model"
;;                       "Object Name"
;;                       "Rating"
;;                       "Reference Date"
;;                       "Software"
;;                       "Special Instructions"
;;                       "User Comment"
;;                       "White Balance"
;;                       "White-Balance Mode"))
;; (metadata/getmeta file)
;; (metadata/getmeta file metadatafields)
