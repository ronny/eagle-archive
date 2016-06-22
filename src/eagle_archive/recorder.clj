(ns eagle-archive.recorder
  "Interacting with the database for the main purpose of recording events."

  (:require [qbits.alia     :as alia]
            [qbits.hayt     :as h]
            [clojure.pprint :as pprint]))

(def ^:private cluster
  (alia/cluster {:contact-points ["localhost"]}))

(defn- create-demand-events-table [session]
  (alia/execute
    session
    (h/create-table :demand_events
                    (h/if-not-exists)
                    (h/column-definitions {:meter_mac_id :text
                                           :event_time   :timestamp
                                           :demand_in_kw :decimal
                                           :primary-key [:meter_mac_id :event_time]})
                    (h/with {:clustering-order [[:event_time :desc]]}))))

(defn- create-raw-events-table [session]
  (alia/execute
    session
    (h/create-table :raw_events
                    (h/if-not-exists)
                    (h/column-definitions {:event_time    :timestamp
                                           :type          :text
                                           :device_mac_id :text
                                           :data          :text
                                           :primary-key [:device_mac_id :event_time]})
                    (h/with {:clustering-order [[:event_time :desc]]}))))

(defn connect
  "Establishes a database session and ensure we're using the right keyspace, and all the tables are there. Returns the session handle."
  []

  (let [session (alia/connect cluster)]
    (alia/execute
      session
      (h/create-keyspace "eagle_archive"
                         (h/if-exists false)
                         (h/with {:replication {:class "SimpleStrategy"
                                                :replication_factor 3}})))
    (alia/execute
      session
      (h/use-keyspace "eagle_archive"))
    (create-raw-events-table session)
    (create-demand-events-table session)
    session))

(defn record-demand [session {:keys [:meter-mac-id
                                     :timestamp
                                     :demand-in-kw]}]
  (let [values [[:meter_mac_id meter-mac-id]
                [:event_time   timestamp]
                [:demand_in_kw demand-in-kw]]
        query (h/insert :demand_events
                (h/values values))]
    ; (println "record-demand: values=")
    ; (pprint/pprint values)
    ; (pprint/pprint (h/->raw query))
    (alia/execute session query)))

(defn record-raw [session {:keys [:timestamp
                                  :event-type
                                  :device-mac-id
                                  :attribute-map]}]
  (let [query (h/insert :raw_events
                (h/values [[:event_time    timestamp]
                           [:type          (pr-str event-type)]
                           [:device_mac_id device-mac-id]
                           [:data          (pr-str attribute-map)]]))]
    ; (pprint/pprint (h/->raw query))
    (alia/execute session query)))
