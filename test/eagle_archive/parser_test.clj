(ns eagle-archive.parser-test
  (:require [midje.sweet :refer :all]
            [eagle-archive.parser :as parser]))

(def valid-instantaneous-demand-xml-string
  "<?xml version=\"1.0\"?><rainforest macId=\"0x1234567890ab\" version=\"undefined\" timestamp=\"1466517985s\">
<InstantaneousDemand>
  <DeviceMacId>0x1234567890abcdef</DeviceMacId>
  <MeterMacId>0xaa11223344556677</MeterMacId>
  <TimeStamp>0x1efbf248</TimeStamp>
  <Demand>0x00056a</Demand>
  <Multiplier>0x00000001</Multiplier>
  <Divisor>0x000003e8</Divisor>
  <DigitsRight>0x03</DigitsRight>
  <DigitsLeft>0x0f</DigitsLeft>
  <SuppressLeadingZero>Y</SuppressLeadingZero>
  <Port>/dev/ttySP0</Port>
</InstantaneousDemand>

</rainforest>
")

(def valid-device-info-xml-string
  "<?xml version=\"1.0\"?><rainforest macId=\"0x1234567890ab\" version=\"undefined\" timestamp=\"1466517983s\">
<DeviceInfo>
  <DeviceMacId>0x1234567890abcdef</DeviceMacId>
  <InstallCode>0x5511111111111111</InstallCode>
  <LinkKey>0x12312399999999999999999999999999</LinkKey>
  <FWVersion>1.4.48 (6952)</FWVersion>
  <HWVersion>1.2.6</HWVersion>
  <ImageType>0x1301</ImageType>
  <Manufacturer>Rainforest Automation, Inc.</Manufacturer>
  <ModelId>Z109-EAGLE</ModelId>
  <DateCode>2015040124620816</DateCode>
  <Port>/dev/ttySP0</Port>
  <Port>/dev/ttySP0</Port>
</DeviceInfo>

</rainforest>
")

(facts "parser/parse"
  (fact "parses instantaneous demand correctly"
        (parser/parse valid-instantaneous-demand-xml-string) =>
        {:event-type :InstantaneousDemand
         :timestamp "2016-06-21T14:06:25Z"
         :device-mac-id "12:34:56:78:90:AB:CD:EF"
         :attribute-map {:Demand '("0x00056a")
                         :DeviceMacId '("0x1234567890abcdef")
                         :DigitsLeft '("0x0f")
                         :DigitsRight '("0x03")
                         :Divisor '("0x000003e8")
                         :MeterMacId '("0xaa11223344556677")
                         :Multiplier '("0x00000001")
                         :Port '("/dev/ttySP0")
                         :SuppressLeadingZero '("Y")
                         :TimeStamp '("0x1efbf248")}
         :meter-mac-id "AA:11:22:33:44:55:66:77"
         :demand-in-kw 1.386M})
  (fact "parses device info correctly"
        (parser/parse valid-device-info-xml-string) =>
        {:event-type :DeviceInfo
         :timestamp "2016-06-21T14:06:23Z"
         :device-mac-id "12:34:56:78:90:AB:CD:EF"
         :attribute-map {:DateCode '("2015040124620816")
                         :DeviceMacId '("0x1234567890abcdef")
                         :FWVersion '("1.4.48 (6952)")
                         :HWVersion '("1.2.6")
                         :ImageType '("0x1301")
                         :InstallCode '("0x5511111111111111")
                         :LinkKey '("0x12312399999999999999999999999999")
                         :Manufacturer '("Rainforest Automation, Inc.")
                         :ModelId '("Z109-EAGLE")
                         :Port '("/dev/ttySP0")}}))


