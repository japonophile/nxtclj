(ns
  ^{:doc "Transcode Clojure-like code to commands (integer array) for the NXT robot"
    :author "Antoine Choppin"}
  nxtclj.transcode
  (:require [instaparse.core :as insta])
  (:import (nxtclj Action Condition Flow Sensor)))


(def nxtclj-parser
  (insta/parser
    "
     <statements>    = statement | statement <' '> statements
     <statement>     = action | flow
     <condition>     = sensor-cond | not-cond
     not-cond        = <'(not '> condition <')'>
     sensor          = ':s1' | ':s2' | ':s3' | ':s4'
     <sensor-cond>   = pressed-cond | distance-cond
     pressed-cond    = <'(pressed? '> sensor <')'>
     <distance-cond> = <'(obstacle? '> distance <')'>
     <distance>      = within-cond | between-cond | none-cond
     within-cond     = sensor <' :within '> integer
     between-cond    = sensor <' :between '> integer <' '> integer
     none-cond       = sensor <' :none'>
     <flow>          = if-stmt | while-stmt | do-stmt
     if-stmt         = <'(if '> condition <' '> statement <')'>
     while-stmt      = <'(while '> condition <' '> statement <')'>
     do-stmt         = <'(do '> statements <')'>
     <action>        = motor-cmd | sleep-cmd | lamp-cmd
     motor-cmd       = <'(motor! '> motor <' '> motor-instr <')'>
     <motor>         = ':a' | ':b' | ':c'
     <motor-instr>   = motor-on | motor-off
     motor-on        = ( ':forward' | ':backward' | ':rotate' ) <' '> integer
     motor-off       = ':stop' | ':float'
     sleep-cmd       = <'(sleep '> integer <')'>
     lamp-cmd        = <'(lamp! '> sensor <' '> color <')'>
     <color>         = ':red' | ':green' | ':blue' | ':none'
     integer         = #'[-]?[0-9]+'
    "))

(defn nxtclj-parse
  [clj]
  (flatten
    (insta/transform {:if-stmt      (fn [c s] [Flow/IF c s])
                      :while-stmt   (fn [c s] [Flow/WHILE c s])
                      :do-stmt      (fn [& cmds]
                                      (concat [Flow/DO (count cmds)] cmds))
                      :motor-cmd    (fn [m i]
                                      [({":a" Action/MOTORA
                                         ":b" Action/MOTORB
                                         ":c" Action/MOTORC} m) i])
                      :motor-on     (fn [c a]
                                      [({":forward"  Action/FORWARD
                                         ":backward" Action/BACKWARD
                                         ":rotate"   Action/ROTATE} c) a])
                      :motor-off    (fn [c]
                                      [({":stop"     Action/STOP
                                         ":float"    Action/FLOAT} c)])
                      :sleep-cmd    (fn [d] [Action/SLEEP d])
                      :lamp-cmd     (fn [s c]
                                      [Action/LAMP s 
                                       ({":red"   0
                                         ":green" 1
                                         ":blue"  2
                                         ":none"  -1} c)])
                      :not-cond     (fn [c] [Condition/NOT c])
                      :pressed-cond (fn [s] [Sensor/PRESSED s])
                      :within-cond  (fn [s d]     [Sensor/OBSTACLE_WITHIN s d])
                      :between-cond (fn [s d1 d2] [Sensor/OBSTACLE_BETWEEN s d1 d2])
                      :none-cond    (fn [s]       [Sensor/OBSTACLE_NONE s])
                      :sensor       (fn [s]
                                      ({":s1" Sensor/SENSOR1
                                        ":s2" Sensor/SENSOR2
                                        ":s3" Sensor/SENSOR3
                                        ":s4" Sensor/SENSOR4} s))
                      :integer      (fn [i] (Integer. i))}
                     (nxtclj-parser clj))))

(defn clj-to-nxt
  "Convert Clojure-like code to an integer sequence understandable by the NXT robot"
  [clj]
  (nxtclj-parse (pr-str clj)))

;
;(if (pressed? :s1)
;  (while (pressed? :s1)
;    (do
;      (motor! :b 50 :forward)
;      (sleep 300))))
;(while (not (pressed? :s1))
;  (do
;    (motor! :b 50 :forward)
;    (sleep 300)))
;
;              |
;              V
;
;[Flow/IF Sensor/PRESSED Sensor/SENSOR1
;   Flow/DO 2
;     Action/MOTORB 50 Action/FORWARD
;     Flow/WHILE Sensor/PRESSED Sensor/SENSOR1 
;       Action/SLEEP 300
; Flow/DO 2
;   Action/MOTORB 50 Action/FORWARD
;   Flow/WHILE Condition/NOT Sensor/PRESSED Sensor/SENSOR1
;     Action/SLEEP 300]
;

