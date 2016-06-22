(ns eagle-archive.timestamp-test
  (:require [midje.sweet :refer :all]
            [eagle-archive.timestamp :as timestamp]))

(facts "timestamp/timestamp->iso8601"
       (fact "returns the correct date time in iso8601 format"
             (timestamp/timestamp->iso8601 "0x1efbf248") => "2016-06-21T12:40:40Z"))

(facts "timestamp/hour"
       (fact "reformats iso8601 string as yyyy-mm-ddThh"
             (timestamp/hour "2016-06-21T12:40:40Z") => "2016-06-21T12"))

(facts "timestamp/unix->iso8601"
       (fact "returns the correct date time in iso8601 format"
             (timestamp/unix->iso8601 1466517985) => "2016-06-21T14:06:25Z"))
