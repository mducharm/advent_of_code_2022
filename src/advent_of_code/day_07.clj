(ns advent-of-code.day-07
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def input (slurp (clojure.java.io/resource "day-07-example.txt")))
(def input (slurp (clojure.java.io/resource "day-07.txt")))
(str/split-lines input)

(defn determine-cmd-type [cmd]
  (cond
    (str/includes? cmd "$ cd ..") :up-dir
    (str/includes? cmd "$ cd") :cd
    (not (nil? (re-seq #"\d+" cmd))) :file
    :else :no-op))

(defn cmd-is? [cmd-type cmd]
  (= cmd-type (determine-cmd-type cmd)))

(defn cmd-is-not? [cmd-type cmd]
  (not= cmd-type (determine-cmd-type cmd)))

(defn chunk-by-file [coll]
  (let [last-path (atom ())]
    (partition-by #(if (or (string? %) (= :up-dir %))
                     (reset! last-path %)
                     @last-path) coll)))

(defn parse-input [input]
  (->> input
       (str/split-lines)
       (filter (partial cmd-is-not? :no-op))
       (map #(case (determine-cmd-type %)
               :cd (subs % 5)
               :file (read-string (re-find #"\d+" %))
               :up-dir :up-dir
               :else :no-op))))

(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       (parse-input)))


(defn part-2
  "Day 07 Part 2"
  [input]
  input)
