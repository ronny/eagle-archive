(ns eagle-archive.web
  (:require [ring.util.request      :as request]
            [clojure.data.xml       :as xml]
            [clojure.pprint         :as pprint]
            [eagle-archive.recorder :as recorder]
            [eagle-archive.parser   :as parser]
            [eagle-archive.demand   :as demand]))

(def ^:private session
  (recorder/connect))

(defn- valid-request? [req event-type]
  (and
    (= :post (:request-method req))
    (some? event-type)))

(defn- success-response [event-type]
  (println "200 " event-type)
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "OK"})

(defn- unprocessable-response []
  {:status  422
   :headers {"Content-Type" "text/plain"}
   :body    "Unprocessable Entity"})

(defn- process [{:keys [:event-type] :as parsed-data}]
  ; (println "Processing..." event-type)
  ; (pprint/pprint parsed-data)
  (if (= event-type demand/event-type)
    (do
      (pprint/pprint (select-keys parsed-data [:demand-in-kw
                                             :meter-mac-id]))
      (recorder/record-demand session parsed-data)))
  (recorder/record-raw session parsed-data)
  (success-response event-type))

(defn app [req]
  (let [request-body (request/body-string req)
        {:keys [:event-type] :as parsed-data} (parser/parse request-body)]
    (if (valid-request? req event-type)
      (process parsed-data)
      (unprocessable-response req request-body))))
