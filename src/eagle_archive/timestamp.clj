(ns eagle-archive.timestamp
  (:require [clj-time.core     :as t]
            [clj-time.format   :as f]
            [clj-time.coerce   :as c]
            [eagle-archive.hex :as hex]))

; do (clj-time.format/show-formatters) to see all formats
(def iso8601-formatter (f/formatters :date-time-no-ms))

(defn unix->iso8601
  "Converts a unix timestamp integer to an ISO8601 formatted string.
  e.g. 1466517985 → `2016-06-21T14:06:25Z`"
  [^Long unix-timestamp]

  (f/unparse iso8601-formatter
             (c/from-long (* 1000 unix-timestamp))))

(defn hour
  "Returns a non-ambiguous string that uniquely identifies a particular hour,
  suitable for paritioning key (one 'row' per hour).

  e.g. `2016-06-21T12:40:40Z` → `2016-06-21T12"
  [timestamp]

  (f/unparse (f/formatters :date-hour)
             (f/parse iso8601-formatter timestamp)))

; -------------------------------------------------------------------------
; TODO: everything below can probably be deleted once we determine we don't
; need to interpret <TimeStamp> values.

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
  (f/unparse iso8601-formatter
             (with-epoch-offset seconds)))

(defn timestamp->iso8601
  "Converts rainforest/eagle <TimeStamp> value to an ISO8601 formatted string.
  e.g. `0x1efbf248` → `2016-06-21T12:40:40Z`"
  [parsed-timestamp]

  ; Seconds since 1 Jan 2000 UTC when demand data was received from meter
  (let [seconds (hex/to-int parsed-timestamp)]
    (seconds->iso8601 seconds)))
