(ns metadata-mongo.image
  (:require
   [clojure.string :as str]
   [metadata-mongo.utils :refer [path-items basename]]
   [metadata-mongo.metadata :refer [meta-with-keywords-vector]]))

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
         metaarray   (meta-with-keywords-vector file)]
     (merge filedetails metaarray))))
