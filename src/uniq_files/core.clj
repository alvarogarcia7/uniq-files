(ns uniq-files.core
  (:use [clojure.java.io]))

(defn
  lines
  [path]
  (with-open [rdr (reader path)]
    (doall (line-seq rdr))))
