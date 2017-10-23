(ns eagle-archive.parser
  "Parsing raw XML strings and extracting values from the resulting data structure."

  (:require [clojure.data.xml        :as xml]
            [clojure.string          :as string]
            [clojure.pprint          :as pprint]
            [eagle-archive.demand    :as demand]
            [eagle-archive.hex       :as hex]
            [eagle-archive.timestamp :as timestamp]))

(defn- parse-as-xml [xml-string]
  (try (xml/parse-str xml-string)
    (catch javax.xml.stream.XMLStreamException e
      (println "Ignored exception while parsing xml-string:")
      (pprint/pprint e)
      {})))

(defn- extract-event-node [parsed-xml-string]
  (-> parsed-xml-string :content first))

(defn- ^Long extract-unix-timestamp
  "e.g. `1355292588s` → 1355292588"
  [parsed-xml-string]

  ; (pprint/pprint parsed-xml-string)

  (let [raw-string (-> parsed-xml-string :attrs :timestamp)]
    (try
      (Long/parseLong (string/replace raw-string #"s$" ""))
     (catch NumberFormatException e
       (println "WTF unable to parse unix timestamp?" raw-string)
       nil))))

(defn- extract-type [event-node]
  (:tag event-node))

(defn- extract-attribute-map [event-node]
  (let [attribute-nodes (:content event-node)]
    (reduce into {}
            (map (fn [node]
                   {(:tag node) (:content node)})
                 attribute-nodes))))

(defn- extract-additional [event-type attribute-map]
  (if (= event-type demand/event-type)
    (demand/parse attribute-map)
    {}))

(defn parse
  "Parses a raw XML string then extracts the event data and returns it as a hash map."
  [xml-string]

  ; (pprint/pprint xml-string)

  (let [parsed-xml-string (parse-as-xml xml-string)
        unix-timestamp    (extract-unix-timestamp parsed-xml-string)
        event-node        (extract-event-node parsed-xml-string)
        event-type        (extract-type event-node)
        attribute-map     (extract-attribute-map event-node)
        common            {:event-type    event-type
                           :timestamp     (timestamp/unix->iso8601 unix-timestamp)
                           :device-mac-id (hex/format-as-mac-addr (-> attribute-map :DeviceMacId first))
                           :attribute-map attribute-map}
        additional        (extract-additional event-type attribute-map)]
    (merge common additional)))
