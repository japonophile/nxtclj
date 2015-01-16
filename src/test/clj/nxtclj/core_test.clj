(ns nxtclj.core-test
  (:require [clojure.test :refer :all]
            [nxtclj.core :refer :all])
  (:import (nxtclj Action Condition Flow Sensor)))

(deftest clj->nxt-test
  (testing "should parse motor calibration procedure"
    (is (=
         [Flow/IF Sensor/PRESSED Sensor/SENSOR1
            Flow/DO 3
              Action/MOTORB 100 Action/FORWARD
              Flow/WHILE Sensor/PRESSED Sensor/SENSOR1
                Action/SLEEP 100
              Action/MOTORB 0 Action/STOP
            Flow/DO 3
              Action/MOTORB 100 Action/FORWARD
              Flow/WHILE Condition/NOT Sensor/PRESSED Sensor/SENSOR1
                Action/SLEEP 100
              Action/MOTORB 0 Action/STOP])
        (clj->nxt 
          (if (pressed? :s1)
            (do
              (motor! :b 50 :forward)
              (while (pressed? :s1)
                (sleep 300))
              (motor! :b 0 :stop)))
          (do
            (motor! :b 50 :forward)
            (while (not (pressed? :s1))
              (sleep 300))
            (motor! :b 0 :stop))))))

(comment

(bt-connect)

(bt-send-cmd [0])

; red light for 3 sec if object detected within 100
(defn light-if-obstacle-detected []
  (bt-send-cmd
    (clj->nxt
      (if (obstacle? :s4 :within 100)
        (do
          (lamp! :s3 :red)
          (sleep 3000)
          (lamp! :s3 :none))))))

(defn light-off []
  (bt-send-cmd
    (clj->nxt
      (lamp! :s3 :none))))

(defn reset-right-leg []
  (bt-send-cmd
    (clj->nxt 
      (do
        (if (pressed? :s1)
          (do
            (motor! :b :forward 50)
            (while (pressed? :s1)
              (sleep 300))
            (motor! :b :stop)))
        (do
          (motor! :b :forward 50)
          (while (not (pressed? :s1))
            (sleep 300))
          (motor! :b :stop))))))

(defn reset-left-leg []
  (bt-send-cmd
    (clj->nxt 
      (do
        (if (pressed? :s2)
          (do
            (motor! :c :forward 50)
            (while (pressed? :s2)
              (sleep 300))
            (motor! :c :stop)))
        (do
          (motor! :c :forward 50)
          (while (not (pressed? :s2))
            (sleep 300))
          (motor! :c :stop))))))

(defn right-leg-down []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :b :rotate 45)
        (motor! :b :stop)))))

(defn left-leg-up []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :c :backward 70)
        (motor! :c :rotate 150)
        (motor! :c :stop)))))

(defn reset-legs []
  (reset-right-leg)
  (reset-left-leg)
  (right-leg-down)
  (left-leg-up))

(defn walk-forward []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :b :forward 90)
        (motor! :c :forward 90)
        (sleep 20000)
        (motor! :b :stop)
        (motor! :c :stop)))))

(defn walk-backward []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :b :backward 90)
        (motor! :c :backward 90)
        (sleep 20000)
        (motor! :b :stop)
        (motor! :c :stop)))))

(defn walk-inplace-right []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :b :forward 90)
        (motor! :c :backward 90)
        (sleep 20000)
        (motor! :b :stop)
        (motor! :c :stop)))))

(defn walk-inplace-left []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :b :backward 90)
        (motor! :c :forward 90)
        (sleep 20000)
        (motor! :b :stop)
        (motor! :c :stop)))))

(defn turn-neck []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :a :forward 70)
        (motor! :a :rotate 90)
        (motor! :a :stop)))))

(defn turn-neck []
  (bt-send-cmd
    (clj->nxt
      (do
        (motor! :a :forward 70)
        (motor! :a :rotate 90)
        (motor! :a :stop)))))

(turn-neck)

(reset-legs)

(walk-forward)

(walk-backward)

(walk-inplace-right)

(walk-inplace-left)

(light-if-obstacle-detected)

(light-off)

)


