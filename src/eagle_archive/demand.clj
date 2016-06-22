(ns eagle-archive.demand
  "Parsing and interpreting values specific to `InstantaneousDemand` events.

  See Eagle Uploader API Manual PDF for more info about the raw data format
  and how to interpret values. This is the latest at time of writing:
  http://rainforestautomation.com/wp-content/uploads/2014/07/EAGLE-Uploader-API_06.pdf
  "
  (:require [clojure.string          :as string]
            [clojure.pprint          :as pprint]
            [eagle-archive.hex       :as hex]))

(def event-type :InstantaneousDemand)

(defn demand-in-kw
  "Returns `demand * multiplier / divisor` as a big decimal"
  [demand multiplier divisor]

  (if (every? some? [demand multiplier divisor])
    (bigdec (/ (* demand multiplier)
               divisor))))

(defn parse
  "Parses and interprets the values in attribute-map into more natural/normalised/understandable values.
  Returns a hash map, e.g.

  ```
  {:meter-mac-id \"00:11:22:33:44:55:aa:bb:cc\"
   :demand-in-kw 3.1456M} ; the M is a notation for BigDecimal, not million or mega
  ```
  "
  [attribute-map]

  (let [meter-mac-id  (-> attribute-map :MeterMacId first)
        demand        (hex/to-int (-> attribute-map :Demand first))
        multiplier    (hex/to-int (-> attribute-map :Multiplier first))
        divisor       (hex/to-int (-> attribute-map :Divisor first))]
    {:meter-mac-id (hex/format-as-mac-addr meter-mac-id)
     :demand-in-kw (demand-in-kw demand multiplier divisor)}))
