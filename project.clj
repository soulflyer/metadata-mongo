(defproject clojure-mongo "0.1.0"
  :description "Save image metadata to a mongo database"
  :url "https://github.com/soulflyer/metadata-mongo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; :repositories [["The Buzz Media Maven Repository"
  ;;                 {:url "http://maven.thebuzzmedia.com" :checksum :warn}]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.novemberain/monger "3.6.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [com.drewnoakes/metadata-extractor "2.9.0"]
                 [yogthos/config "1.2.0"]]
  :main metadata-mongo.core
  :bin {:name "save-meta"
        :bin-path "~/bin"})
