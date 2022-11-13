(ns metadata-mongo.utils
  (:require
   [clojure.string :as str]))

(defn cleanup
  [alist]
  (map #(str/replace % #"[ /]" "-") alist))

(defn cleanup-keys
  "Remove spaces and slashes from keys in a metadata map. Replace them with -"
  [metadata-map]
  (zipmap (cleanup (keys metadata-map)) (vals metadata-map)))

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
