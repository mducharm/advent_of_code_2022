(ns advent-of-code.day-08
  (:require [clojure.string :as str]))

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
    (list (reverse (first split)) (drop 1 (second split)))))

(defn get-current-tree [[x y] grid]
  (-> grid (nth y) (nth x)))

(defn prepare-tree-sightlines [[x y] grid]
  (let [row (nth grid y)
        column (map #(nth % x) grid)]
    (concat (get-sightlines y column) (get-sightlines x row))))

(defn tree-visible? [pair grid]
  (let [current-tree (get-current-tree pair grid)
        sight-lines (prepare-tree-sightlines pair grid)]
    (or (some empty? sight-lines)
        (some (partial every? #(> current-tree %)) sight-lines))))

(defn calculate-scenic-score [pair grid]
  (let [current-tree (get-current-tree pair grid)
        sight-lines (prepare-tree-sightlines pair grid)]
    (->> sight-lines
         (map (partial split-with #(> current-tree %)))
         (map #(concat (first %) (take 1 (second %))))
         (map count)
         (reduce *))))

(defn get-all-coords [size]
  (for [x (range size) y (range size)] (vector x y)))

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
  (let [grid (parse-tree-grid input)
        forest-size (count grid)
        coords (get-all-coords forest-size)]

    (->> (for [pair coords]
           (calculate-scenic-score pair grid))
         (apply max))))


