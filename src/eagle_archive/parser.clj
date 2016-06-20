(ns eagle-archive.parser
  (:require [clojure.data.xml :as xml]
            [clojure.pprint   :as pprint]))

(defn- extract-event-node [parsed-body]
  (first (:content parsed-body)))

(defn- extract-type [event-node]
  (:tag event-node))

(defn- extract-content-map [event-node]
  (let [attribute-nodes (:content event-node)]
    (reduce into {}
            (map (fn [node]
                   {(:tag node) (:content node)})
                 attribute-nodes))))

(defn- parse-as-xml [body]
  (try (xml/parse-str body)
    (catch javax.xml.stream.XMLStreamException e
      (println "Ignored exception while parsing body:")
      (pprint/pprint e)
      {})))

(defn parse [body]
  (let [event-node (extract-event-node (parse-as-xml body))]
    {:type (extract-type event-node)
     :content-map (extract-content-map event-node)}))
