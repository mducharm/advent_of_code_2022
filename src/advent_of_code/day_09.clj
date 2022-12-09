(ns advent-of-code.day-09
  (:require [clojure.string :as str]))


(def input (slurp (clojure.java.io/resource "day-09-example.txt")))
(def input (slurp (clojure.java.io/resource "day-09.txt")))


(defn parse-input [input]
  (->> input
       (str/split-lines)
       (map #(str/split % #" "))
       (map (fn [[direction steps]]
              [(keyword direction) (read-string steps)]))
       (reduce (fn [acc [direction steps]]
                 (concat acc (repeat steps direction))) [])))

(defn next-h [[x y] direction]
  (case direction
    :R [(+ x 1) y]
    :L [(- x 1) y]
    :U [x (+ y 1)]
    :D [x (- y 1)]))

(defn point-diff [[x y] [a b]]
  [(+ x a) (+ y b)])

(defn are-neighors?
  [t h]
  (let [relative-neighbor-coords
        (for [x (range -1 2) y (range -1 2)] (vector x y))]
    (->> relative-neighbor-coords
         (map (partial point-diff t))
         (map (partial = h))
         (some true?))))

(defn next-t [t old-h new-h]
  (cond
    ;; if touching, no change
    (are-neighors? t new-h) t
    ;; otherwise, let tail take head's old space
    :else old-h))

(defn next-state [data direction]
  (let [h (:h data)
        t (:t data)
        positions (:positions data)
        new-h (next-h h direction)
        new-t (next-t t h new-h)
        new-positions (conj positions new-t)]
    {:h new-h
     :t new-t
     :positions new-positions}))


(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       (parse-input)
       (reduce next-state {:h [0 0] :t [0 0] :positions #{[0 0]}})
       (:positions)
       (count)))

(defn part-2
  "Day 09 Part 2"
  [input]
  input)
