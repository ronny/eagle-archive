(defproject eagle-archive "0.1.0"
  :description "Cloud endpoint web app for archiving data from Rainforest Automation Eagle devices"
  :url "https://github.com/ronny/eagle-archive"
  :license {:name "Eclipse Public License 1.0"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [com.taoensso/timbre "4.10.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [cc.qbits/alia-all "4.0.2"]
                 [cc.qbits/hayt "4.0.0"]
                 [clj-time "0.14.0"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-ring "0.12.1"]]}}
  :ring {:handler eagle-archive.web/app})
