(ns eagle-archive.web
  (:require [ring.util.request :as request]
            [clojure.data.xml  :as xml]
            [clojure.pprint    :as pprint]
            [eagle-archive.parser :as parser]))

(defn- understable-request? [req type]
  (and
    (= :post (:request-method req))
    (not (nil? type))))

(defn- respond-with-success [type content-map]
  (println "200 " type)
  (pprint/pprint content-map)
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "OK"})

(defn- respond-with-sadness [req request-body]
  (println "422 " request-body)
  {:status  422
   :headers {"Content-Type" "text/plain"}
   :body    "Unprocessable Entity"})

(defn app [req]
  (let [request-body (request/body-string req)
        {:keys [:type :content-map]} (parser/parse request-body)]

    (if (understable-request? req type)
      (respond-with-success type content-map)
      (respond-with-sadness req request-body))))
