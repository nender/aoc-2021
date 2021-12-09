(ns aoc.09
    (:require [clojure.string :as str]))

(defn parse-line [line]
    (mapv #(-> % str Integer/parseInt) line))

(defn read-input [f]
    (->> (slurp f)
         str/split-lines
         (mapv parse-line)))

(defn neighbors [x y v]
    (->> [[(inc x) y] [(dec x) y]
          [x (inc y)] [x (dec y)]]
         (map #(get-in v %))
         (filter (complement nil?))))

(defn values-and-neighbors [input]
    (for [x (range (count input)) y (range (count (input 0)))]
        [(get-in input [x y])
         (neighbors x y input)]))

(defn less-than-neighbors [[x nbr]]
    (every? #(< x %) nbr))

(defn part-one [input]
    (->> (values-and-neighbors input)
         (filter less-than-neighbors)
         (map #(-> % first inc))
         (reduce +)))

(println "Part one solution"
    (part-one (read-input "input.txt")))