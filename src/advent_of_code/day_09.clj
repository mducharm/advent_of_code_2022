(ns advent-of-code.day-09
  (:require [clojure.string :as str]))

(def input (slurp (clojure.java.io/resource "day-09-example.txt")))
(def input (slurp (clojure.java.io/resource "day-09-example2.txt")))
(def input (slurp (clojure.java.io/resource "day-09.txt")))

(defn parse-input [input]
  (->> input
       (str/split-lines)
       (map #(str/split % #" "))
       (map (fn [[direction steps]]
              [(keyword direction) (read-string steps)]))
       (reduce (fn [acc [direction steps]]
                 (concat acc (repeat steps direction))) [])))

(defn next-state-of-point [direction [x y]]
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
  (if (are-neighors? t new-h)
    t ;; if touching, no change
    old-h ;; otherwise, let tail take head's old space
    ))

(defn next-state [data direction]
  (let [h (:h data)
        t (:t data)
        positions (:positions data)
        new-h (next-state-of-point direction h)
        new-t (next-t t h new-h)
        new-positions (conj positions new-t)]
    {:h new-h
     :t new-t
     :positions new-positions}))

(def initial-coords
  (into [] (repeat 10 [0 0 0])))

(initial-coords)
(partition 2 1 (initial-coords))

(def dirs  (parse-input input))

(defn next-point
  "Given a point, find it's next state based on it's current direction index."
  [[x y idx] directions]
  (let [direction (nth directions idx)
        [new-x new-y] (next-state-of-point direction [x y])]
    [new-x new-y (+ 1 idx)]))

(next-point [1 0 5] dirs)

(defn advance-segment-state
  "Advances the given point b until it is neighbors with a."
  [directions [a b]]
  (loop [a a
         b b]
    (if (are-neighors? (take 2 a) (take 2 b))
      b
      (recur a (next-point b directions)))))

(defn segment-reducer
  "Produces a new list of points in their next states; uses the accumulator to hold onto the next segment's new state."
  [directions]
  (fn [acc p]
    (let [[previous-point points] acc
          new-p (advance-segment-state directions [previous-point p])]
      [new-p
       (conj points new-p)])))

(defn process-rope
  "Advances the state of the head and all following segments, once."
  [directions points]
  (let [new-h (next-point (first points) directions)]
    (->> (rest points)
         (reduce (segment-reducer directions) [new-h []])
         (second)
         (concat [new-h]))))

(defn rope-reducer
  "Returns a reducer that collects all states of the rope."
  [directions]
  (fn [acc x]
    (let [[previous-rope ropes] acc
          next-rope (process-rope directions previous-rope)]
      [next-rope (conj ropes next-rope)])))

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
  (let [directions (parse-input input)]
    (->> directions
         (reduce (rope-reducer directions) [initial-coords []])
         (second)
         (map last)
         (into #{})
         (count)
         (+ -1) ;; remove origin
         )
    ))
