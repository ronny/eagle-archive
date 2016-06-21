(ns eagle-archive.hex-test
  (:require [midje.sweet :refer :all]
            [eagle-archive.hex :as hex]))

(facts "hex/without-0x"
  (fact "removes the 0x prefix"
        (hex/without-0x "0x123abc") => "123abc"
        (hex/without-0x "0X123ABC") => "123ABC"
        (hex/without-0x "123") => "123"
        (hex/without-0x nil) => nil))

(facts "hex/to-int"
  (fact "parses input correctly as integer"
        (hex/to-int "0x0") => 0
        (hex/to-int "0x123") => 291
        (hex/to-int "ff") => 255
        (hex/to-int "0X123ABF") => 1194687
        (hex/to-int "0x00000001") => 1)
  (fact "returns nil when unable to parse input as int"
        (hex/to-int "0x0x") => nil
        (hex/to-int "omnomnom") => nil
        (hex/to-int nil) => nil
        (hex/to-int "0x9874987398127319283719237182") => nil))

(facts "hex/format-as-mac-addr"
  (fact "formats as MAC address when length is even"
        (hex/format-as-mac-addr "0xab12cd34ef56") => "AB:12:CD:34:EF:56"
        (hex/format-as-mac-addr "0xAB12CD34EF56") => "AB:12:CD:34:EF:56"
        (hex/format-as-mac-addr "ab12cd34ef56") => "AB:12:CD:34:EF:56"
        (hex/format-as-mac-addr "AB12CD34EF56") => "AB:12:CD:34:EF:56"
        (hex/format-as-mac-addr "0xab12cd34ef561234567890") => "AB:12:CD:34:EF:56:12:34:56:78:90")
  (fact "pads with 0 when length is odd"
        (hex/format-as-mac-addr "abc") => "0A:BC")
  (fact "returns nil when given nil or empty string"
        (hex/format-as-mac-addr "") => nil
        (hex/format-as-mac-addr nil) => nil))
