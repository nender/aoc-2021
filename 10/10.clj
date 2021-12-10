(ns aoc.10
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn read-input [f]
  (->> (slurp f)
     str/split-lines
     (map seq)))

(def match {\( \) \[ \] \{ \} \< \>})
; not a literal to dodge paren coloring issue lol
(def score (zipmap ")]}>" [3 57 1197 25137])) 

(defn score-line [line]
  (loop [line line state []]
    (if-let [chunk (first line)]
      (if (contains? (set "({[<") chunk)
            (recur (rest line) (conj state chunk))
            (let [top (peek state)]
              (if (not= (match top) chunk)
                (score chunk)
                (recur (rest line) (pop state)))))
      (if (empty? state)
        0
        nil))))

(defn part-one [input]
    (->> input
        (map score-line)
        (filter int?)
        (reduce +)))

(part-one (read-input "input.txt"))