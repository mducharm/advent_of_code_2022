(ns advent-of-code.day-03 (:require [clojure.set :as set]
                                    [clojure.string :as str]))

(defn get-compartments
  "Partitions line into 2 lists"
  [line]
  (->> line
       (partition (/ (count line) 2))))

(let [upper-case (map char (range 65 91))
      lower-case (map char (range 97 123))]

  (def priority-map
    "Builds a map where the letter is the key, and the value is the score calculated from the letter's ASCII code."
    (merge
     (zipmap lower-case (map #(- (int %) 96) lower-case))
     (zipmap upper-case (map #(- (int %) 38) upper-case)))))

(def calculate-priority (partial get priority-map))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> input
       (str/split-lines)
       (map get-compartments)
       (map (partial map set)) ;; make compartments into sets
       (map (partial apply set/intersection)) ;; find intersection of 2 compartments
       (map first) ;; get duplicate item
       (map calculate-priority)
       (reduce +)))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> input
       (str/split-lines)
       (map set)
       (partition 3) ;; split into groups
       (map (partial apply set/intersection)) ;; find intersection of all 3
       (map first) ;; get badge
       (map calculate-priority)
       (reduce +)))
