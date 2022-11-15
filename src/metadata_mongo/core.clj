(ns metadata-mongo.core
  (:gen-class)
  (:require
   [clojure.string :as str]
   [clojure.tools.cli :refer [parse-opts]]
   [metadata-mongo.utils :refer [path-items is-image?]]
   [metadata-mongo.image :refer [image-entry]] 
   [monger.collection :as mc]
   [monger.core :as mg]))

(def cli-options
  [["-d" "--database DATABASE" "specifies database to use"
    :default "photos"]
   ["-i" "--image-collection IMAGE-COLLECTION" "specifies the image collection"
    :default "images"]
   ["-h" "--help"]])

(defn save-meta
  "Saves the metadata from a specified picture, or from all the jpeg pictures
  in a directory to the database.
  [database document pathname]"
  ([database document pathname]
   (let [connection (mg/connect)
         db (mg/get-db connection database)
         file (java.io.File. pathname)
         fullpath (.getAbsolutePath file)]
     (if (.isFile file)
       (let [imagemeta (image-entry fullpath)]
         ;; (println (str "*********** _id: " (imagemeta "_id")))
         (mc/save db document imagemeta))
       (doall
         (for [filename (filter is-image? (.list file))]
           (let [fp (str/replace fullpath #"[/.]+$" "")
                filepath (str fp "/" (last (path-items filename)))
                imagemeta (image-entry filepath)]
            ;; (println (str "********** _id: " (imagemeta "_id")))
            (mc/save db document imagemeta))))))))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]

    (cond
     (:help options)
     (println (str "Usage:\nbin/save-meta [options] path/to/image/or/directory\n\noptions:\n" summary))

     :else
     (save-meta (:database options) (:image-collection options) (first args)))))


(comment
  (save-meta
    "monger-test"
    "documents"
    "/Users/iain/Pictures/Published/fullsize/2015/09/19-Beetle/DIW_5634.jpg")

  (save-meta
    "monger-test"
    "documents"
    "/Users/iain/Pictures/Published/fullsize/2015/09/23-Frog")
)
