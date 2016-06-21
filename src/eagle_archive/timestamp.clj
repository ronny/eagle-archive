(ns eagle-archive.timestamp
  (:require [clj-time.core     :as t]
            [clj-time.format   :as f]
            [clj-time.coerce   :as c]
            [eagle-archive.hex :as hex]))

(def ^:private epoch-offset
  "This is what the Eagle Z109 considers as its epoch."
  (c/to-long (t/date-time 2000 1 1)))

(defn- with-epoch-offset
  "`seconds` is since `2000-01-01 00:00:00`"
  [seconds]

  (if (some? seconds)
    (c/from-long (+ epoch-offset (* 1000 seconds)))))

; seconds since 1 Jan 2000 (!!!) UTC to ISO8601 formatted string
; e.g. 519721538 → "2016-06-20T07:05:38Z"
(defn- seconds->iso8601 [seconds]
  (f/unparse (f/formatters :date-time-no-ms) ; do (clj-time.format/show-formatters) to see all formats
             (with-epoch-offset seconds)))

(defn timestamp->iso8601
  "Converts rainforest/eagle <TimeStamp> value to an ISO8601 formatted string.
  e.g. `0x1efbf248` → `2016-06-21T12:40:40Z`"
  [parsed-timestamp]

  ; Seconds since 1 Jan 2000 UTC when demand data was received from meter
  (let [seconds (hex/to-int parsed-timestamp)]
    (seconds->iso8601 seconds)))

(defn unix->iso8601
  "Converts a unix timestamp integer to an ISO8601 formatted string.
  e.g. 1466517985 → `2016-06-21T14:06:25Z`"
  [^Long unix-timestamp]

  (f/unparse (f/formatters :date-time-no-ms)
             (c/from-long (* 1000 unix-timestamp))))
