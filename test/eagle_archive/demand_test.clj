(ns eagle-archive.demand-test
  (:require [midje.sweet :refer :all]
            [eagle-archive.demand :as demand]))

(def valid-attribute-map
  {:Port '("/dev/ttySP0"),
   :TimeStamp '("0x1efbf248"),           ; 519828040
   :MeterMacId '("0x0011223344556677"),
   :DigitsRight '("0x03"),
   :Divisor '("0x000003e8"),             ; 1000
   :DigitsLeft '("0x0f"),
   :Demand '("0x00056a"),                ; 1386
   :Multiplier '("0x00000001"),
   :SuppressLeadingZero '("Y"),
   :DeviceMacId '("0x1234567890abcdef")})

(facts "demand/parse"
  (fact "returns the right hash map"
        (demand/parse valid-attribute-map) =>
          {:meter-mac-id "00:11:22:33:44:55:66:77"
           :demand-in-kw 1.386M})) ; the M is a notation for big decimal, not million or mega
