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

(defn decide-action
  [[hash filenames]]
  (let [filenames (sort filenames)]
    (letfn [(object [filename command action-name] {:filename filename :command command :action-name action-name })
            (to-keep [filenames] (list (object (last filenames)
                                               (fn [record] (str "# keep " (:filename record)))
                                               :keep)))
            (to-remove [filenames] (map #(object %
                                                 (fn [record] (str "rm " (:filename record)))
                                                 :remove)
                                        (butlast filenames)))]
      ;(println (str "#HASH = " hash))
      (concat
        (to-keep filenames)
        (to-remove filenames)))))

(defn apply-action
  [action]
  ((:command action) action))

(defn
  create-script
  [lines]
  (->>
    lines
    (map tokenize)
    group-by-hash
    (map decide-action)
    flatten
    (map apply-action)
    ))


(defn
  bash-script-action
  [lines]
  (->>
    lines
    create-script
    (map println))
  )
