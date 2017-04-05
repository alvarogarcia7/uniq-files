(ns uniq-files.core-test
  (:use [clojure.string]
        [uniq-files.core])
  (:require [midje.sweet :refer :all]))

(defn
  group-by-hash
  [lines]
  (letfn [(tokenize
            [line]
            (let [[filename hash] (split line #" ")]
              {:hash hash :filename filename}))]
    (->>
      lines
      (map tokenize)
      (reduce (fn
                [acc ele]
                (let [{hash :hash filename :filename} ele
                      current (get acc hash [])]
                  (assoc acc hash (conj current filename)))) {})
      (map (fn [x] (print "# ") (println  x) x))
      (map (fn
             [[hash filenames]]
             (let [filenames (sort filenames)]
               ;(println (str "#HASH = " hash))
               (concat
                 (list (str "# keep " (last filenames)))
                 (doall (map #(str "rm " %) (butlast filenames)))))))
      flatten)))


(facts
  "reading a file"
  (fact
    "happy path"
    (lines "resources/files/lines.txt") => ["1" "a" "b"]))

(defn-
  same-in-any-order
  [actual expected]
  (clojure.set/difference (into #{} actual) (into #{} expected)) => #{})

(facts
  "grouping by contents"
  (fact
    "acceptance test"
    (let [actual (group-by-hash (lines "resources/example-1/md5.txt"))
          expected (list
             "rm 2016-1.txt"                                ;60b725f10c9c85c70d97880dfe8191b3
             "rm 2016-2.txt"                                ;60b725f10c9c85c70d97880dfe8191b3
             "# keep 2016-3.txt"                            ;e29311f6f1bf1af907f9ef9f44b8328b
             "# keep 2017-1.txt"                            ;60b725f10c9c85c70d97880dfe8191b3
             "# keep 2017-2.txt"                            ;bfcc9da4f2e1d313c63cd0a4ee7604e9
             )]
      (same-in-any-order actual expected)
      )))
