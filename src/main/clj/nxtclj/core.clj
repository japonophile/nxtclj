(ns
  ^{:doc "Command a LEGO Mindstorms NXT robot with Clojure"
    :author "Antoine Choppin"}
  nxtclj.core
  (:require [nxtclj.bluetooth :as bt])
  (:require [nxtclj.transcode :as xc]))

(def bt-connect  bt/connect)
(def bt-send-cmd bt/send-cmd)

(defmacro clj->nxt
  "Convert Clojure-like commands to integer arrays to be sent to the NXT robot"
  [& body]
  (vec (mapcat xc/clj-to-nxt body)))

