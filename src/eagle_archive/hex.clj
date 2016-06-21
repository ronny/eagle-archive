(ns eagle-archive.hex
  "Utility functions to deal with hex strings and hex values"
  (:require [clojure.string :as string]))

(defn without-0x
  "Remove the `0x` prefix. e.g. `0xff` → `ff`"
  [hex-string]

  (if (some? hex-string)
    (string/replace hex-string #"^0[xX]" "")))

(defn to-int
  "e.g. '0xff' → 255"
  [hex-string]

  (if (some? hex-string)
    (let [trimmed-hex-string (without-0x hex-string)]
      (try (Integer/parseInt trimmed-hex-string 16)
        (catch NumberFormatException e nil)))))

(defn- pad-if-odd-length [hex-string]
  (if (odd? (count hex-string))
    (str "0" hex-string)
    hex-string))

; TODO: this doesn't belong in this ns
(defn- present? [thing]
  (and
    (some? thing)
    (< 0 (count thing))))

(defn format-as-mac-addr
  "Formats given hex string as a normalised MAC address.
  e.g. `0xab12cd34ef56` → `AB:12:CD:34:EF:56`"
  [hex-string]

  (if (present? hex-string)
    (let [mac-id-string (-> hex-string
                            .toUpperCase
                            without-0x
                            pad-if-odd-length)]
      (string/join ":"
                   (map string/join
                        (partition 2 mac-id-string))))))
