(ns advent-of-code.day-09-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code.day-09 :refer [part-1 part-2]]
            [clojure.java.io :refer [resource]]))

(deftest part1
  (let [expected 13]
    (is (= expected (part-1 (slurp (resource "day-09-example.txt")))))))

(deftest part2-a
  (let [expected 1]
    (is (= expected (part-2 (slurp (resource "day-09-example.txt")))))))

(deftest part2-b
  (let [expected 36]
    (is (= expected (part-2 (slurp (resource "day-09-example2.txt")))))))
