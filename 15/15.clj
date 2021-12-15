(ns aoc.15
  (:use clojure.pprint)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn parse-line [line]
   (mapv #(Character/getNumericValue %) line))

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (mapv parse-line)))

(defn neighbors [graph [y x]]
  (->> [[(inc y) x] [(dec y) x]
        [y (inc x)] [y (dec x)]]
       (filter #(get-in graph %))
       set))

(defn all-coords [graph]
  (let [height (count graph)
        width (count (first graph))]
    (for [y (range height) x (range width)]
      [y x])))

(defn min-dist [unexplored dist]
  (->> dist
    (filter #(contains? unexplored (first %)))
    (sort-by second)
    first))

(defn unexplored-neighbors [graph unexplored coord]
  (set/intersection
    (neighbors graph coord)
    unexplored))

(defn check-neighbors [graph neighbors dist prev coord]
  (loop [neighbors neighbors dist dist prev prev]
    (if (empty? neighbors)
      [dist prev]
      (let [cur (first neighbors)
            alt (+ (get dist coord Integer/MAX_VALUE)
                   (get-in graph cur))]
        (if (< alt (get dist cur Integer/MAX_VALUE))
          (recur (rest neighbors)
            (assoc dist cur alt)
            (assoc prev cur coord))
          (recur (rest neighbors) dist prev))))))

(defn dijkstra [graph source target]
  (loop [unexplored (set (all-coords graph))
         dist {source 0}
         prev {}]
    (let [[curr _] (min-dist unexplored dist)
          unexplored (disj unexplored curr)]
      (if (= target curr)
        (dist target)
        (let [neighbors (unexplored-neighbors graph unexplored curr)
              [dist prev] (check-neighbors graph neighbors dist prev curr)]
          (recur unexplored dist prev))))))

(defn part-one [graph]
  (let [max (dec (count graph))]
    (dijkstra graph [0 0] [max max])))

(println "Part one:" (part-one (read-input "input.txt")))