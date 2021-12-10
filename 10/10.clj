(ns aoc.10
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn read-input [f]
  (->> (slurp f)
     str/split-lines
     (map seq)))

(def closing-bracket {\( \) \[ \] \{ \} \< \>})
; not a literal to dodge paren coloring issue lol
(def score-bad (zipmap ")]}>" [3 57 1197 25137]))
(def incomplete-lookup (zipmap ")]}>" (range 1 5)))

(defn score-incomplete [stack]
    (loop [stack (rseq (mapv closing-bracket stack))
           sum 0]
      (if (empty? stack)
        sum
        (recur
          (rest stack)
          (+ (* sum 5)
             (incomplete-lookup (first stack)))))))

(defn score-line [line]
  (loop [line line stack []]
    (if-let [chunk (first line)]
      (if (contains? (set "({[<") chunk)
        (recur (rest line) (conj stack chunk))
          (if (not= (closing-bracket (peek stack)) chunk)
            [:invalid (score-bad chunk)]
            (recur (rest line) (pop stack))))
      (if (empty? stack)
        [:valid 0]
        [:incomplete (score-incomplete stack)]))))

(defn score-for [target [state score]]
 (if (= target state)
    score))

(defn part-one [input]
  (->> input
    (map score-line)
    (keep #(score-for :invalid %))
    (reduce +)))

(defn get-middle [v]
    (get v (-> v count (/ 2) int)))

(defn part-two [input]
  (->> input
    (map score-line)
    (keep #(score-for :incomplete %))
    sort
    vec
    get-middle))

(println "Part one:" (part-one (read-input "input.txt")))
(println "Part two:" (part-two (read-input "input.txt")))