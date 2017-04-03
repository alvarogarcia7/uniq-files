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



(defn
  publish-message
  [ch qname message-type payload]
  (lb/publish
    ch
    default-exchange-name
    qname
    payload
    {:content-type "text/plain" :type "greetings.hi"}))

(defn test-send-messages
  []
  (let [mq (connect-to-mq)
        {ch :channel} mq
        qname "langohr.examples.hello-world"]
    (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
    (configure-handler ch qname message-handler)
    (doall
      (for [i (range 10)]
             (lb/publish ch default-exchange-name qname (str "Hello! " i) {:content-type "text/plain" :type
                                                                                         "greetings.hi"})))
    (Thread/sleep 2000)
    (println "[main] Disconnecting...")
    (disconnect-from-mq mq)))
