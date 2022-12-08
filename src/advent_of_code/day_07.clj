(ns advent-of-code.day-07
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def input (slurp (clojure.java.io/resource "day-07-example.txt")))
(def input (slurp (clojure.java.io/resource "day-07.txt")))
(str/split-lines input)

(defn contains-cd? [cmd] (str/includes? cmd "$ cd"))

(defn determine-path [current-path cmds]
  (letfn [(update [current-path cd]
            (if (str/includes? cd "cd ..")
              (into [] (drop-last current-path))
              (conj current-path (subs cd 5))))]
    (reduce update current-path cmds)))

;; (determine-path (list) (list "$ cd /"))
;; (determine-path ["/" "a"] (list "$ cd e"))
;; (determine-path ["/" "a" "e"] (list "$ cd .." "$ cd .." "$ cd d"))

(defn process-paths [commands]
  (letfn [(update [[current-path acc] current-cmd]
            (if (some contains-cd? current-cmd)
              ;; cd, convert to path
              (let [new-path (determine-path current-path current-cmd)]
                [new-path (conj acc new-path)])
              ;; ls, leave for now
              [current-path (conj acc current-cmd)]))]
    (reduce update [[] []] commands)))

(defn calc-immediate-folder-sum [files]
  (->> files
       (map (partial re-seq #"\d+"))
       (reduce concat)
       (map read-string)
       (reduce +)))


(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       (str/split-lines)
       (partition-by contains-cd?)
       (process-paths)
       (second)
       (partition 2)
       ;; join path into string
       (map #(cons (str/join (first %)) (rest %)))
       ;; find initial sums of each dir
       (map #(list (first %) (calc-immediate-folder-sum (second %))))
       (reduce concat)
       (apply hash-map))
)


(defn part-2
  "Day 07 Part 2"
  [input]
  input)
