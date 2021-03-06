(ns uniq-files.core
  (:gen-class)
  (:use [clojure.java.io])
  (:require [clojure.string :as str]))

(defn
  lines
  [path]
  (with-open [rdr (reader path)]
    (doall (line-seq rdr))))

(defn
  tokenize
  [line]
  (let [[filename hash] (str/split line #" ")]
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

(def
  actions
  (letfn [(object [filename command action-name hash] {:filename    filename
                                                       :command     command
                                                       :action-name action-name
                                                       :hash        hash})
          (to-keep [[hash filenames]] (list (object (last filenames)
                                                    (fn [record] (str "# keep " (:filename record)))
                                                    :keep
                                                    hash)))
          (to-remove [[hash filenames]] (map #(object %
                                                      (fn [record] (str "rm " (:filename record)))
                                                      :remove
                                                      hash)
                                             (butlast filenames)))]
    (list to-keep to-remove)))

(defn decide-command
  [hash-group]
  {:hash      hash
   :filenames (concat (map #(% hash-group) actions))})

(defn apply-command
  [action]
  ((:command action) action))

(defn
  ungroup-by-hash
  [coll]
  (->>
    coll
    (map :filenames)
    flatten))

(defn
  create-script
  [lines]
  (->>
    lines
    (map tokenize)
    group-by-hash
    (map decide-command)
    ungroup-by-hash
    (map apply-command)
    ))


(defn
  bash-script-action
  [file-lines]
  (->>
    file-lines
    create-script
    (map println)
    doall)
  )

(defn
  -main
  [& args]
  (->>
    args
    first
    lines
    bash-script-action))
