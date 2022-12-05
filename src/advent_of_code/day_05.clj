(ns advent-of-code.day-05
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(defn get-stacks-and-instructions [input]
  (let [lines (->> input
                   (str/split-lines)
                   (partition-by str/blank?))
        column-section (first lines)
        column-ids (str/split (str/trim (last column-section)) #"   ")
        columns (->> column-section
                     (drop-last 1)
                     (map #(take-nth 4 (drop 1 %))))
        stacks (->> column-ids
                    (count)
                    (range)
                    (map #(for [col columns] (nth col %)))
                    (map (partial filter #(Character/isLetter %))))
        instructions (->> lines (last)
                          (map (partial re-seq #"\d+"))
                          (map (partial map read-string)))]
    {:stacks stacks
     :instructions instructions}))

(defn run-instructions [stacks [num-to-move from-col to-col]]
  (let [from-idx (- from-col 1)
        to-idx (- to-col 1)
        new-to-col
        (concat
         (reverse (take num-to-move
                        (nth stacks from-idx)))
         (nth stacks to-idx))
        new-from-col
        (drop num-to-move (nth stacks from-idx))]
    (map-indexed (fn [idx col]
                   (cond
                     (= idx from-idx) new-from-col
                     (= idx to-idx) new-to-col
                     :else col)) stacks)))


(defn run-instructions-pt2 [stacks [num-to-move from-col to-col]]
  (let [from-idx (- from-col 1)
        to-idx (- to-col 1)
        new-to-col
        (concat
         (take num-to-move
               (nth stacks from-idx))
         (nth stacks to-idx))
        new-from-col
        (drop num-to-move (nth stacks from-idx))]
    (map-indexed (fn [idx col]
                   (cond
                     (= idx from-idx) new-from-col
                     (= idx to-idx) new-to-col
                     :else col)) stacks)))

(defn process-stacks [run-inst {stacks :stacks instructions :instructions}]
  (let [remaining-instructions (atom instructions)
        current-stack (atom stacks)]
    (while (not-empty @remaining-instructions)
      (let [new-stack (run-inst @current-stack (first @remaining-instructions))]
        (reset! current-stack new-stack))
      (reset! remaining-instructions (drop 1 @remaining-instructions)))
    @current-stack))


(defn part-1
  "Day 05 Part 1"
  [input]
  (->> input
       (get-stacks-and-instructions)
       (process-stacks run-instructions)
       (map first)
       (reduce str)))

(defn part-2
  "Day 05 Part 2"
  [input]
  (->> input
       (get-stacks-and-instructions)
       (process-stacks run-instructions-pt2)
       (map first)
       (reduce str)))