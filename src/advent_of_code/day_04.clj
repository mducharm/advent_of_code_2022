(ns advent-of-code.day-04
  (:require [clojure.string :as str]))

(defn is-overlapping?
  "Returns true if one pair is completely overlapped by the other."
  [[[a b] [x y]]]
  (or
   (and (>= a x) (<= b y))
   (and (>= x a) (<= y b))))

(defn is-partially-overlapping?
  "Returns true if one pair is at all overlapped by the other."
  [[[a b] [x y]]]
  (or (<= a x b) (<= a y b)
      (<= x a y) (<= x b y)))

(defn get-pairs
  "Prepares input so that each line looks like this:
   [[2 4] [6 8]]"
  [line]
  (->> line
       (map #(str/split % #","))
       (map (partial map #(str/split % #"-")))
       (map (partial map (partial map read-string)))))

(defn part-1
  "Day 04 Part 1"
  [input]
  (->> input
       (str/split-lines)
       (get-pairs)
       (filter is-overlapping?)
       (count)))

(defn part-2
  "Day 04 Part 2"
  [input]
  (->> input
       (str/split-lines)
       (get-pairs)
       (filter is-partially-overlapping?)
       (count)))
