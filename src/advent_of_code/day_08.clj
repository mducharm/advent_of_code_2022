(ns advent-of-code.day-08
  (:require [clojure.string :as str]))
(def input (slurp (clojure.java.io/resource "day-08-example.txt")))
(def input (slurp (clojure.java.io/resource "day-08.txt")))
(str/split-lines input)

(defn parse-tree-grid
  "Returns list of list containing tree heights."
  [input]
  (letfn [(convert-to-nums [line]
            (->> line
                 (partition 1)
                 (reduce concat)
                 (map #(Character/digit % 10))))]

    (->> input
         (str/split-lines)
         (map convert-to-nums))))

(defn get-sightlines [idx coll]
  (let [split (split-at idx coll)]
    (list (first split) (drop 1 (second split)))))
(get-sightlines 0 (list 0 5 5 3 5))

(defn tree-visible? [[x y] grid]
  (let [row (nth grid y)
        column (map #(nth % x) grid)
        current-tree (-> grid (nth y) (nth x))
        sight-lines (concat (get-sightlines y column) (get-sightlines x row))]
;; (println "is edge tree" (some empty? sight-lines))
    (or (some empty? sight-lines)
        (some (partial every? #(> current-tree %)) sight-lines))
    ;; (println "current " current-tree)
    ;; (println "row " row)
    ;; (println "col " column)
    ;; (println "x y " x y)
    ;; (println "sightlines " sight-lines)
    ;; sight-lines

;; (some empty? sight-lines)
;; (some (partial every? #(> current-tree %)) sight-lines)

    ;; (println "col " column)
    ))
(defn calculate-scenic-score [[x y] grid])


(defn get-all-coords [size]
  (for [x (range size) y (range size)] (vector x y)))

(defn get-tree [x y grid] (-> grid (nth y) (nth x)))

(defn part-1
  "Day 08 Part 1"
  [input]
  (let [grid (parse-tree-grid input)
        forest-size (count grid)
        coords (get-all-coords forest-size)]
    (->> (for [pair coords]
           (tree-visible? pair grid))
         (filter true?)
         count)))

(defn part-2
  "Day 08 Part 2"
  [input]
  input)
