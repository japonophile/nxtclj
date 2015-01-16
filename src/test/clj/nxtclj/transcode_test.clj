(ns nxtclj.transcode-test
  (:require [clojure.test :refer :all]
            [nxtclj.transcode :refer :all])
  (:import (nxtclj Action Condition Flow Sensor)))

(deftest nxtclj-parse-test
  (testing "should parse simple commands"
    (is (= [Action/MOTORA Action/FORWARD 50]
           (nxtclj-parse "(motor! :a :forward 50)")))
    (is (= [Action/MOTORA Action/STOP]
           (nxtclj-parse "(motor! :a :stop)")))
    (is (= [Action/SLEEP 100]
           (nxtclj-parse "(sleep 100)")))
    (is (= [Action/LAMP Sensor/SENSOR3 1]
           (nxtclj-parse "(lamp! :s3 :green)")))
    ))

