(ns uniq-files.core-test
  (:use [uniq-files.core])
  (:require [midje.sweet :refer :all]))



(facts
  "reading a file"
  (fact
    "happy path"
    (lines "resources/files/lines.txt") => ["1" "a" "b"]))

(defn
  print-if-different
  [actual expected]
  (if (not= #{} (clojure.set/difference actual expected))
    (do (println (str "expected: " expected))
        (println (str "actual: " actual)))
    ))

(facts
  "grouping by contents"
  (fact :acceptance
    "simple, order of md5_file is order of date"
    (let [actual (create-script (lines "resources/example-1/md5.txt"))
          expected (list
             "rm 2016-1.txt"                                ;60b725f10c9c85c70d97880dfe8191b3
             "rm 2016-2.txt"                                ;60b725f10c9c85c70d97880dfe8191b3
             "# keep 2016-3.txt"                            ;e29311f6f1bf1af907f9ef9f44b8328b
             "# keep 2017-1.txt"                            ;60b725f10c9c85c70d97880dfe8191b3
             "# keep 2017-2.txt"                            ;bfcc9da4f2e1d313c63cd0a4ee7604e9
             )
          [actual expected] (map #(into #{} %) (list actual expected))]
      (print-if-different actual expected)
      actual => expected))

  (fact :acceptance
    "order of the file does matter. Files are sorted by occurrence, not by filename"
        ; do not trust name of file in the contents of the md5_file,
        ; use the order in which they appear on the file"
    (let [actual (create-script (lines "resources/example-unsorted/md5.txt"))
          expected (list
                     "rm 2017-1.txt"                        ;60b725f10c9c85c70d97880dfe8191b3
                     "rm 2016-1.txt"                        ;60b725f10c9c85c70d97880dfe8191b3
                     "# keep 2016-2.txt"                    ;60b725f10c9c85c70d97880dfe8191b3
                     "# keep 2016-3.txt"                    ;e29311f6f1bf1af907f9ef9f44b8328b
                     "# keep 2017-2.txt"                    ;bfcc9da4f2e1d313c63cd0a4ee7604e9
                     )
          [actual expected] (map #(into #{} %) (list actual expected))]
      (print-if-different actual expected)
      actual => expected)))
