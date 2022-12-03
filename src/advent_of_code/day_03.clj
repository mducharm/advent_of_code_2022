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

(def get-intersection-priority
  "Gets the intersecting item from the provided sets, and calculates the item's priority."
  (comp
   (map (partial apply set/intersection))
   (map first) ;; only one intersecting item expected, so just grab the first
   (map calculate-priority)))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> input
       (str/split-lines)
       (map get-compartments)
       (map (partial map set)) ;; make compartments into sets
       (transduce get-intersection-priority +)))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> input
       (str/split-lines)
       (map set)
       (partition 3) ;; split into groups
       (transduce get-intersection-priority +)))
