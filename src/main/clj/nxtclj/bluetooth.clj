(ns
  ^{:doc "Bluetooth interface to send commands to the NXT robot"
    :author "Antoine Choppin"}
  nxtclj.bluetooth
  (:import (java.lang.reflect Array)
           (java.io DataOutputStream)
           (lejos.pc.comm NXTComm NXTConnector NXTCommFactory)))

(def bt-agent (agent [:disconnected]))

(defn- call-after
  [ms f & args]
  (future
    (do
      (Thread/sleep ms)
      (apply f args))))

(defn- write-cmd
  [status cmd]
  (case (first status)
    :connected (let [dos (DataOutputStream. (.getOutputStream (second status)))]
      (try
        (do
          (doseq [c cmd] (.writeInt dos c))
          (.flush dos)
          status)
        (catch Exception e
          (into [:connecting] cmd))))
    :connecting (into status cmd)
    :disconnected (into [:connecting] cmd)))

(defn- make-connection
  [ag key oldstatus newstatus]
  (if (and (not (= :connecting (first oldstatus)))
           (= :connecting (first newstatus)))
    (let [conn (NXTConnector.)
          devices (.search conn nil nil NXTCommFactory/BLUETOOTH)]
      (if (< 0 (Array/getLength devices))
        (do
          (println "connecting...")
          (if (.connectTo conn (Array/get devices 0) NXTComm/PACKET)
            (do
              (println "connected")
              (send bt-agent
                    (fn [status]
                      (if (= :connecting (first status))
                        (if (= [] (rest status))
                          [:connected conn]
                          (write-cmd [:connected conn] (rest status)))
                        ;; should not happen
                        status))))
            ;; retry
            (do
              (println "connection failed")
              (call-after 5000 make-connection ag key oldstatus newstatus))))
        (do
          (println "no bluetooth device found")
          (call-after 5000 make-connection ag key oldstatus newstatus))))))

(add-watch bt-agent :make-connection make-connection)

(defn connect
  "Connect to the NXT robot through bluetooth"
  []
  (send bt-agent
        (fn [status]
          (if (= :disconnected (first status))
            [:connecting]
            status))))

(defn send-cmd
  "Send one or more commands (an array of integers) to the NXT robot through bluetooth"
  [cmd]
  (send bt-agent write-cmd cmd))

;; for test only
(defn reset
  []
  (send bt-agent
        (fn [status]
          [:disconnected])))

