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
  (letfn [(upsert-by-hash
            [acc ele]
            (let [{hash :hash filename :filename} ele
                  current (get acc hash [])]
              (->>
                filename
                (conj current)
                (assoc acc hash))))]
    (reduce upsert-by-hash {} xs)))

(defn to-script
  [[hash filenames]]
  (let [filenames (sort filenames)]
    (letfn [(object [filename command] {:filename filename :command command})
            (to-keep [filenames] (list (object (last filenames)
                                               (fn [record] (str "# keep " (:filename record))))))
            (to-remove [filenames] (map #(object %
                                                 (fn [record] (str "rm " (:filename record))))
                                        (butlast filenames)))]
      ;(println (str "#HASH = " hash))
      (->>
        (concat
          (to-keep filenames)
          (to-remove filenames))
        (map #((:command %) %)))
      )))

(defn
  create-script
  [lines]
  (->>
    lines
    (map tokenize)
    group-by-hash
    (map to-script)
    flatten))


(defn
  bash-script-action
  [lines]
  (->>
    lines
    create-script
    (map println))
  )
