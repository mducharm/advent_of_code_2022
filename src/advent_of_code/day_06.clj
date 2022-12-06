(ns advent-of-code.day-06)

(defn get-answer [input window-size]
  (->> input
       (partition window-size 1)
       (map distinct)
       (map count)
       (take-while (partial not= window-size))
       (count)
       (+ window-size)))

(defn part-1
  "Day 06 Part 1"
  [input]
  (get-answer input 4))

(defn part-2
  "Day 06 Part 2"
  [input]
  (get-answer input 14))
