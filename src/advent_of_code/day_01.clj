(ns advent-of-code.day-01
  (:require
   [clojure.string :as str]))

(defn sum [nums]
  (reduce + (map #(Integer/parseInt %) nums)))

(defn get-sums [input]
  (->> input
       (str/split-lines)
       (partition-by str/blank?)
       (filter (partial not= '("")))
       (map sum)))

(defn part-1
  "Day 01 Part 1"
  [input]
  (->> input
       (get-sums)
       (apply max)))

(defn part-2
  "Day 01 Part 2"
  [input]
  (->> input
       (get-sums)
       (sort)
       (take-last 3)
       (reduce +)))
