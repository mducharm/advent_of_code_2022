(ns advent-of-code.day-01-test
  (:require [advent-of-code.day-01 :refer [part-1 part-2]]
            [clojure.java.io :refer [resource]]
            [clojure.test :refer [deftest is run-tests]]))

(deftest part1
  (let [expected 24000]
    (is (= expected (part-1 (slurp (resource "day-01-example.txt")))))))

(deftest part2
  (let [expected 45000]
    (is (= expected (part-2 (slurp (resource "day-01-example.txt")))))))