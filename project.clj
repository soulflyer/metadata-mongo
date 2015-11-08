(defproject clojure-mongo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :repositories [["The Buzz Media Maven Repository"
                  {:url "http://maven.thebuzzmedia.com" :checksum :warn}]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.novemberain/monger "3.0.1"]
                 [org.clojure/tools.cli "0.3.3"]
                 [metadata "0.1.1-SNAPSHOT"]]
  :main metadata-mongo.core
  :bin {:name "runme"
        :bin-path "~/bin"})
