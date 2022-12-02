(ns advent-of-code.day-02
  (:require [clojure.set :as set]
            [clojure.string :as str]))

;; (def input (slurp (clojure.java.io/resource "day-02.txt")))
;; (str/split-lines input)

(defn convert-to-choice [choice]
  (case choice
    "A" :rock
    "X" :rock
    "B" :paper
    "Y" :paper
    "C" :scissors
    "Z" :scissors))

(defn convert-to-outcome [choice]
  (case choice
    "A" :rock
    "B" :paper
    "C" :scissors
    "X" :lose
    "Y" :draw
    "Z" :win))

(def win-map (hash-map
              :rock :paper
              :paper :scissors
              :scissors :rock))

(def lose-map (set/map-invert win-map))

(defn outcome-to-choice [[choice outcome]]
  (let [expected (case outcome
                       :win (win-map choice)
                       :lose (lose-map choice)
                       :draw choice
                   )]
 
    (list choice expected)))

(defn get-rounds [input]
  (->> input
       (str/split-lines)
       (map #(str/split % #" "))))

(defn determine-score [[opponent-choice your-choice]]
  (def shape-score (case your-choice
                     :rock 1
                     :paper 2
                     :scissors 3))
  (def round-score (cond
                     (= opponent-choice your-choice) 3
                     (and (= opponent-choice :rock) (= your-choice :paper)) 6
                     (and (= opponent-choice :paper) (= your-choice :scissors)) 6
                     (and (= opponent-choice :scissors) (= your-choice :rock)) 6
                     :else 0))
  (+ shape-score round-score))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> input
       (get-rounds)
       (map #(map convert-to-choice %))
       (map determine-score)
       (reduce +))
  )

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> input
       (get-rounds)
       (map #(map convert-to-outcome %))
       (map outcome-to-choice)
       (map determine-score)
       (reduce +)))
