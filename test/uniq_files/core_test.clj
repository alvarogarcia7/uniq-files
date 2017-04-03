(ns uniq-files.core-test
  (:require [clojure.string :require :split]))
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
               (println (str "#HASH = " hash))
               (println (str "# keep " (last filenames)))
               (doall (map #(println "rm " %) (butlast filenames)))))))))


(defn
  lines
  [path]
  (with-open [rdr (reader path)]
    (doall (line-seq rdr))))
