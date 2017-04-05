(ns uniq-files.core
  (:use [clojure.java.io]
        [clojure.string]))

(defn
  lines
  [path]
  (with-open [rdr (reader path)]
    (doall (line-seq rdr))))

(defn
  tokenize
  [line]
  (let [[filename hash] (split line #" ")]
    {:hash hash :filename filename}))

(defn group-by-hash
  [xs]
  (reduce (fn
            [acc ele]
            (let [{hash :hash filename :filename} ele
                  current (get acc hash [])]
              (assoc acc hash (conj current filename)))) {} xs))

(defn to-script
  [[hash filenames]]
  (let [filenames (sort filenames)]
    ;(println (str "#HASH = " hash))
    (concat
      (list (str "# keep " (last filenames)))
      (doall (map #(str "rm " %) (butlast filenames))))))

(defn
  create-script
  [lines]
  (letfn []
    (->>
      lines
      (map tokenize)
      group-by-hash
      (map to-script)
      flatten
      )))


(defn
  bash-script-action
  [lines]
  (->>
    lines
    create-script
    (map println))
  )
