(defproject eagle-archive "0.1.0-SNAPSHOT"
  :description "Cloud endpoint web app for archiving data from Rainforest Automation Eagle devices"
  :url "http://example.com/FIXME"
  :license {:name "UNLICENSED"
            :url ""}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-jetty-adapter "1.5.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler eagle-archive.web/app})
